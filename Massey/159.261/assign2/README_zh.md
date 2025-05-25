# Slapocalypse v2.0

欢迎体验 **Slapocalypse v2.0**，一款基于节奏的回合制格子游戏的最新版本！在 v1.0 的基础上，v2.0 扩展了地图，新增摄像机系统、HUD、全屏模式和动态音乐分轨，显著提升了策略性和节奏沉浸感。玩家控制角色在 20x20 网格地图上与三种史莱姆敌人（蓝、黄、粉）战斗，动作需与 100 BPM 背景音乐同步。

## v2.0 新功能

- **扩展地图**：从 9x9 升级到 20x20 网格，提供更大的探索和策略空间。
- **摄像机系统**：动态跟踪玩家，显示 9x9 视图，聚焦核心游戏区域。
- **HUD**：屏幕显示玩家生命值（HP），未来可扩展显示更多信息。
- **全屏模式**：按 F11 键切换全屏，游戏区域居中，增强沉浸感。
- **动态音乐分轨**：击败特定敌人激活对应音乐轨道（如鼓、贝斯、键盘），模拟演奏乐器，丰富音乐体验。

## 游戏特色

- **节奏驱动的战斗**：所有移动和攻击需在 0.1 秒节拍窗口内完成，考验节奏感。
- **移动即攻击**：玩家通过移动到敌人格子触发攻击，取代传统按键攻击。
- **多样化的敌人AI**：
  - 蓝史莱姆：A* 路径规划，每拍移动，优先级 3，绑定鼓轨道。
  - 黄史莱姆：BFS 路径规划，每两拍移动，优先级 2，绑定贝斯轨道。
  - 粉史莱姆：贪婪最佳优先路径规划，每三拍移动，优先级 1，绑定键盘轨道。
- **优先级系统**：玩家（优先级 100）优先于敌人，解决移动和攻击冲突，确保公平。
- **动态地图**：地图纹理（`map1.png`, `map2.png`）随节拍切换，提供视觉节奏反馈。
- **沉浸式音效**：多轨道背景音乐和状态音效（闲置、移动、攻击、受伤、死亡）增强氛围。
- **丰富动画**：玩家和敌人拥有多方向（上、下、左、右）动画，支持闲置、跑动、攻击、受伤和死亡状态。

![游戏截图](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250520133844662.png)

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
   ```
2. **准备资源文件**：
   - 确保 `src/main/resources/` 包含以下文件：
     - 地图纹理：`map1.png`, `map2.png`
     - 背景音乐：`base.wav`, `drum.wav`, `bass.wav`, `keyboard.wav`
     - 音效：`sounds/idle.wav`, `sounds/move.wav`, `sounds/attack.wav`, `sounds/hurt.wav`, `sounds/death.wav`
     - 精灵表：`player1/`, `Slime1/`, `Slime2/`, `Slime3/` 下的动画文件
   - 如缺少资源，请联系开发者或使用占位文件。
3. **编译和运行**：
   ```bash
   javac com/hamhuo/massey/slapocalypse/core/*.java com/hamhuo/massey/slapocalypse/entity/*.java com/hamhuo/massey/slapocalypse/state/*.java
   java com.hamhuo.massey.slapocalypse.core.GameController
   ```

## 玩法

- **目标**：在 20x20 网格地图上击败所有敌人（蓝、黄、粉史莱姆），避免 HP 降至 0（触发游戏结束）。
- **控制**：
  - **方向键**：移动到空地板格子（进入 `RunState`）或敌人格子（进入 `AttackState`）。
  - **F11**：切换全屏模式。
  - **ESC**：退出游戏。
- **节奏机制**：
  - 游戏以 100 BPM 运行，每拍 0.6 秒。
  - 动作需在 0.1 秒节拍窗口内输入，错过窗口重置为 `IdleState`。
- **战斗**：
  - 每次攻击造成 10 点伤害（玩家和敌人初始 HP 为 100）。
  - 玩家优先级最高，动作优先处理。
  - 敌人使用路径规划（A*, BFS, 贪婪）追击并在邻近时攻击。
- **音乐分轨**：
  - 背景音乐包含多轨道，击败敌人（如蓝史莱姆）激活对应轨道（如鼓），动态构建音乐。
- **状态**：
  - `Idle`：等待输入或节拍。
  - `Run`：移动到新格子。
  - `Attack`：攻击目标格子，无目标则回滚。
  - `Hurt`：受到伤害，显示受伤动画。
  - `Death`：HP ≤ 0，玩家死亡结束游戏。

## 贡献

欢迎为 **Slapocalypse** 贡献代码、修复 bug 或提出建议！请按以下步骤操作：
1. Fork 仓库。
2. 创建特性分支（`git checkout -b feature/YourFeature`）。
3. 提交更改（`git commit -m '添加 YourFeature'`）。
4. 推送到分支（`git push origin feature/YourFeature`）。
5. 提交 Pull Request。

### 建议改进
- 实现胜利条件（如击败所有敌人）。
- 增强 HUD，显示敌人 HP 或节拍指示器。
- 支持可调 BPM 或难度设置。
- 添加地图障碍物，增加路径规划挑战。
- 完善音乐分轨，加入视觉提示。

## 致谢

衷心感谢所有测试者和贡献者！特别感谢：
- [颍桓](https://gitee.com/ddd020622)
- [十豆加](https://gitee.com/shi-dou-jia)
- [商家鹏](https://gitee.com/shang-jiapeng)

## 许可证

本项目采用 MIT 许可证。详情见 [LICENSE](LICENSE) 文件。

---

**Slapocalypse v2.0** - 节奏与策略的终极融合！