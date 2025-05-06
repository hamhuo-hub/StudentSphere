from dinov2.data.datasets import ImageNet

for split in ImageNet.Split:
    dataset = ImageNet(split=split, root="imagenet", extra="imagenet")
    dataset.dump_extra()
