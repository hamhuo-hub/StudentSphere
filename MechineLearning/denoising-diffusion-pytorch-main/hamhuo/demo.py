import torch
import torchvision
import torchvision.transforms as T
from denoising_diffusion_pytorch import Unet, GaussianDiffusion, Trainer
import torchvision.utils as utils
import os
from pathlib import Path

# 设置设备
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
print(f"Using device: {device}")

# 创建数据保存文件夹
data_folder = './data/cifar10_images'
os.makedirs(data_folder, exist_ok=True)

# 下载并保存 CIFAR-10 数据集图片
transform = T.Compose([T.ToTensor()])
trainset = torchvision.datasets.CIFAR10(root='./data', train=True,
                                        download=True, transform=transform)

# print("Saving CIFAR-10 training images...")
# for idx, (img, label) in enumerate(trainset):
#     class_folder = Path(data_folder) / str(label)
#     class_folder.mkdir(parents=True, exist_ok=True)
#     img_pil = T.ToPILImage()(img)
#     img_pil.save(class_folder / f"{idx}.png")
#
# print(f"Saved {len(trainset)} images to {data_folder}")


# 定义辅助函数
def num_to_groups(num, divisor):
    groups = num // divisor
    remainder = num % divisor
    arr = [divisor] * groups
    if remainder > 0:
        arr.append(remainder)
    return arr


# 定义模型
model = Unet(
    dim=64,
    dim_mults=(1, 2, 4,)
).to(device)


# 自定义 beta 调度器
def sigmoid_beta_schedule(timesteps, start=-3, end=3, tau=1, clamp_min=1e-5):
    steps = timesteps + 1
    t = torch.linspace(0, timesteps, steps, dtype=torch.float64) / timesteps
    v_start = torch.tensor(start / tau).sigmoid()
    v_end = torch.tensor(end / tau).sigmoid()
    alphas_cumprod = (-((t * (end - start) + start) / tau).sigmoid() + v_end) / (v_end - v_start)
    alphas_cumprod = alphas_cumprod / alphas_cumprod[0]
    betas = 1 - (alphas_cumprod[1:] / alphas_cumprod[:-1])
    return torch.clip(betas, 0, 0.999)


# 定义扩散模型
diffusion = GaussianDiffusion(
    model,
    image_size=32,
    timesteps=200000,
    beta_schedule='sigmoid',
    objective='pred_v',
    schedule_fn_kwargs=dict(),
    ddim_sampling_eta=0.,
    auto_normalize=True,
    offset_noise_strength=0.5,  # https://www.crosslabs.org/blog/diffusion-with-offset-noise
    min_snr_loss_weight=True,  # https://arxiv.org/abs/2303.09556
    min_snr_gamma=5,
    immiscible=False
)

# 创建 results 文件夹
results_folder = './results'
os.makedirs(results_folder, exist_ok=True)

# 定义 Trainer
trainer = Trainer(
    diffusion_model=diffusion,
    folder=data_folder,
    # connect with epoch
    train_batch_size=32,
    gradient_accumulate_every=1,
    augment_horizontal_flip=True,
    train_lr=1e-4,
    train_num_steps=200000,
    ema_update_every=10,
    ema_decay=0.995,
    adam_betas=(0.9, 0.99),
    save_and_sample_every=10000,
    num_samples=25,
    results_folder=results_folder,
    amp=True,
    mixed_precision_type='fp16',
    split_batches=True,
    convert_image_to=None,
    calculate_fid=False,
    inception_block_idx=2048,
    max_grad_norm=1.,
    num_fid_samples=50000,
    save_best_and_latest_only=False
)


# 模型保存与加载函数
def load_model(trainer, milestone):
    try:
        trainer.load(milestone)
        print(f"成功加载模型 model-{milestone}.pt，从步骤 {trainer.step} 继续训练。")
    except FileNotFoundError:
        print(f"未找到 model-{milestone}.pt，将从头开始训练。")


def save_model(trainer, milestone):
    trainer.save(milestone)
    print(f"模型已保存为 model-{milestone}.pt")


# 加载预训练模型（如果有）
load_model(trainer, 'ham')

# 启动训练
print("开始训练...")
trainer.train()

# 保存最终模型
save_model(trainer, 'ham')

# 生成图像样本
if trainer.accelerator.is_main_process:
    print("生成随机样本图像...")
    num_random_samples = 10
    with torch.inference_mode():
        random_batches = num_to_groups(num_random_samples, trainer.batch_size)
        random_images_list = [trainer.ema.ema_model.sample(batch_size=n) for n in random_batches]
        random_images = torch.cat(random_images_list, dim=0)
        utils.save_image(random_images, f'{results_folder}/final_random_samples.png', nrow=num_random_samples)
        print(f"样本图像已保存到 {results_folder}/final_random_samples.png")


