# Slapocalypse v1.0

欢迎体验 **Slapocalypse**，一款基于节奏的回合制格子游戏！玩家控制一名角色，在 9x9 格子地图上与三种史莱姆敌人（蓝、黄、粉）展开战斗，动作需与背景音乐的节拍同步。游戏结合了策略、节奏和路径规划，带来独特的游戏体验。

## 游戏特色

- **节奏驱动的战斗**：所有移动和攻击必须在节拍窗口（0.1秒容差）内完成，考验你的节奏感。
- **移动即攻击**：玩家通过尝试移动到敌人所在格子触发攻击，取代传统按键攻击。
- **多样化的敌人AI**：
  - 蓝史莱姆：使用 A* 路径规划，每拍移动，优先级 3。
  - 黄史莱姆：使用 BFS 路径规划，每两拍移动，优先级 2。
  - 粉史莱姆：使用贪婪最佳优先路径规划，每三拍移动，优先级 1。
- **优先级系统**：玩家（优先级 100）优先于敌人，解决移动和攻击重叠问题，确保公平结算。
- **动态地图**：地图纹理随节拍切换（`map1.png` 和 `map2.png`），提供视觉节奏反馈。
- **沉浸式音效**：背景音乐和状态音效（闲置、移动、攻击、受伤、死亡）增强游戏氛围。
- **动画支持**：玩家和敌人拥有多方向（上、下、左、右）的动画（闲置、跑动、攻击、受伤、死亡）。

  ![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250520133844662.png)

## 安装说明

### 系统要求
- **操作系统**：Windows、macOS 或 Linux
- **Java**：JDK 8 或更高版本
- **内存**：至少 512 MB 可用 RAM
- **存储**：约 100 MB 用于游戏和资源文件

### 安装步骤
1. **克隆仓库**：
   ```bash  
   git clone https://github.com/your-repo/slapocalypse.git  
   cd slapocalypse  
   ```2. **准备资源文件**：  
   - 确保 `src/main/resources/` 包含以下文件：  
     - 地图纹理：`map1.png`, `map2.png`  
     - 背景音乐：`song.wav`  
     - 音效：`sounds/idle.wav`, `sounds/move.wav`, `sounds/attack.wav`, `sounds/hurt.wav`, `sounds/death.wav`  
     - 精灵表：`player1/`, `Slime1/`, `Slime2/`, `Slime3/` 下的动画文件  
   - 如果缺少资源，请联系开发者或使用占位文件。  
3. **编译和运行**：
   ```bash  
   javac com/hamhuo/massey/slapocalypse/core/*.java com/hamhuo/massey/slapocalypse/entity/*.java com/hamhuo/massey/slapocalypse/state/*.java  
   java com.hamhuo.massey.slapocalypse.core.GameController  
   ```  
## 玩法

- **目标**：在 9x9 格子地图上击败所有敌人（蓝、黄、粉史莱姆），避免被击败（玩家 HP ≤ 0 触发游戏结束）。
- **控制**：
  - **方向键**（上、下、左、右）：选择移动方向，尝试移动到空地板格子（进入 `RunState`）或敌人格子（进入 `AttackState`）。
  - **ESC**：退出游戏。
- **节奏机制**：
  - 游戏以 100 BPM 运行，每拍 0.6 秒。
  - 动作需在节拍窗口（0.1 秒容差）内输入，错过窗口将重置为 `IdleState`。
- **战斗**：
  - 每次攻击造成 10 点伤害（玩家和敌人 HP 初始为 100）。
  - 玩家优先级最高，攻击和移动优先于敌人。
  - 敌人根据路径规划（A*, BFS, 贪婪）追击玩家，靠近时攻击。
- **状态**：
  - `Idle`：等待输入或节拍。
  - `Run`：移动到新格子。
  - `Attack`：攻击目标格子，若无目标则回滚。
  - `Hurt`：受到伤害，显示受伤动画。
  - `Death`：HP ≤ 0，结束（玩家死亡触发游戏结束）。



## 贡献

欢迎为 **Slapocalypse** 贡献代码、修复 bug 或提出建议！请按照以下步骤：

1. Fork 本仓库。
2. 创建特性分支（`git checkout -b feature/YourFeature`）。
3. 提交更改（`git commit -m '添加 YourFeature'`）。
4. 推送到分支（`git push origin feature/YourFeature`）。
5. 提交 Pull Request。


## 致谢

感谢所有测试者和贡献者！特别感谢：  
[颍桓](https://gitee.com/ddd020622)
[十豆加](https://gitee.com/shi-dou-jia)
[商家鹏](https://gitee.com/shang-jiapeng)
## 许可证

本项目采用 MIT 许可证。详情见 [LICENSE](LICENSE) 文件。
  
---  

**Slapocalypse v1.0** - 击弦-正式版 v1.0