# Slapocalypse v2.0

Welcome to **Slapocalypse**, an enhanced version of the rhythm-based turn-based grid game! Control a character on an expanded 20x20 grid map, battling three types of slime enemies (Blue, Yellow, Pink) with actions synchronized to the beat of the background music. New features like a camera system, HUD, full-screen mode, and preparation for music segmentation deepen the strategic and rhythmic experience.

## Game Features

- **Rhythm-Driven Combat**: Movements and attacks must occur within a 0.1-second beat window, testing your rhythm skills.
- **Movement-Based Attacks**: Players attack by moving into enemy-occupied grid cells, replacing key-based attacks.
- **Diverse Enemy AI**:
  - Blue Slime: A* pathfinding, moves every beat, priority 3, tied to drum track.
  - Yellow Slime: BFS pathfinding, moves every two beats, priority 2, tied to bass track.
  - Pink Slime: Greedy Best-First pathfinding, moves every three beats, priority 1, tied to keyboard track.
- **Priority System**: Player (priority 100) takes precedence, resolving movement and attack overlaps fairly.
- **Expanded Map**: Now a 20x20 grid, with a 9x9 camera view following the player.
- **Camera System**: Dynamically tracks the player, displaying a 9x9 portion of the map for focused gameplay.
- **HUD**: Displays player HP and other information, enhancing gameplay feedback.
- **Full-Screen Mode**: Toggle with F11 to play in full-screen, with the game area centered.
- **Dynamic Map**: Textures (`map1.png`, `map2.png`) switch with the beat for visual rhythm feedback.
- **Immersive Audio**: Background music with multiple tracks (base, drum, bass, keyboard) prepared for dynamic activation; includes state-specific sound effects (idle, move, attack, hurt, death).
- **Rich Animations**: Multi-directional animations (up, down, left, right) for idle, run, attack, hurt, and death states.

  ![Game Screenshot](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250520133844662.png)

## Installation Instructions

### System Requirements
- **Operating System**: Windows, macOS, or Linux
- **Java**: JDK 8 or higher
- **Memory**: At least 512 MB available RAM
- **Storage**: Approximately 100 MB for game and resource files

### Installation Steps
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/slapocalypse.git
   cd slapocalypse
   ```
2. **Prepare Resource Files**:
   - Ensure `src/main/resources/` contains:
     - Map textures: `map1.png`, `map2.png`
     - Background music: `base.wav`, `drum.wav`, `bass.wav`, `keyboard.wav`
     - Sound effects: `sounds/idle.wav`, `sounds/move.wav`, `sounds/attack.wav`, `sounds/hurt.wav`, `sounds/death.wav`
     - Spritesheets: Animation files under `player1/`, `Slime1/`, `Slime2/`, `Slime3/`
   - If resources are missing, contact developers or use placeholders.
3. **Compile and Run**:
   ```bash
   javac com/hamhuo/massey/slapocalypse/core/*.java com/hamhuo/massey/slapocalypse/entity/*.java com/hamhuo/massey/slapocalypse/state/*.java
   java com.hamhuo.massey.slapocalypse.core.GameController
   ```

## Gameplay

- **Objective**: Defeat all enemies (Blue, Yellow, Pink Slimes) on the 20x20 grid map while avoiding defeat (player HP ≤ 0 triggers game over).
- **Controls**:
  - **Arrow Keys**: Move to an empty tile (`RunState`) or enemy tile (`AttackState`).
  - **F11**: Toggle full-screen mode.
  - **ESC**: Exit the game.
- **Rhythm Mechanics**:
  - Runs at 100 BPM (0.6 seconds per beat).
  - Actions must occur within a 0.1-second beat window; missing it resets to `IdleState`.
- **Combat**:
  - Attacks deal 10 damage (player and enemy HP start at 100).
  - Player has highest priority, ensuring moves and attacks resolve first.
  - Enemies use pathfinding (A*, BFS, Greedy) to pursue and attack when adjacent.
- **Music Segmentation**:
  - Background music includes multiple tracks (base, drum, bass, keyboard).
  - Defeating an enemy type (e.g., Blue Slime) activates its track (e.g., drum), stacking music for immersion.
- **States**:
  - `Idle`: Waiting for input or beat.
  - `Run`: Moving to a new tile.
  - `Attack`: Attacking a target tile, rolling back if no target.
  - `Hurt`: Taking damage, showing hurt animation.
  - `Death`: HP ≤ 0, ending the game for the player.

## Contribution

We welcome contributions to **Slapocalypse** via code, bug fixes, or suggestions! Follow these steps:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/YourFeature`).
3. Commit changes (`git commit -m 'Add YourFeature'`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Submit a Pull Request.

### Suggested Improvements
- Implement a win condition (e.g., defeating all enemies).
- Enhance HUD with beat indicators or enemy HP.
- Support adjustable BPM or difficulty settings.
- Add map obstacles for varied pathfinding challenges.
- Fully integrate music segmentation with dynamic track activation.

## Acknowledgments

Thank you to all testers and contributors! Special thanks to:
- [YingHuan](https://gitee.com/ddd020622)
- [ShiDouJia](https://gitee.com/shi-dou-jia)
- [ShangJiaPeng](https://gitee.com/shang-jiapeng)

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

**Slapocalypse v2.0** - Rhythm, Strategy, and Immersive Music Combined!
背景
https://jonik9i.itch.io/free-pixel-dungeon-game-asset#google_vignette