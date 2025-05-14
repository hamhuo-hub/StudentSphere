# Snake Game — Assignment 1
*by Hamhuo*

# NOTICE
**export to zip project is disabled in IDEA unless install Android plugin**
# AI Usage Statement

During the development of this project, AI tools were used in the following ways:

- To assist in generating the initial version of the project README based on the existing code structure and feature highlights.
- To seek advice on color palette combinations for the game's visual design, ensuring appropriate contrast and aesthetic appeal.
- To request suggestions on object sizing (e.g., snake size, grid cell size) for maintaining a balanced and visually consistent game layout.

All AI-assisted content was reviewed, modified, and integrated manually to fit the specific requirements and style of the project.

## 🕹 Introduction
This project is a classic Snake game implemented using Java AWT and a custom stage1.GameEngine.    
The player controls a snake moving on a 25×25 grid, growing longer by eating apples.    
The game ends if the snake hits the wall or collides with itself.

## ✨ Highlights

### 1. Efficient Direction Control
- The snake's movement direction is managed using **numeric encoding combined with XOR operations**.
- Direct reversal (e.g., moving from LEFT to RIGHT immediately) is prevented with a simple and efficient one-line check:

``` java
if ((direction.getValue() ^ d.getValue()) != 2) {  
    this.direction = d;  
}
```

- This approach is cleaner and more efficient than traditional if-else chains.

### 2. Flexible Speed and Length Configuration
Before starting the game, a settings dialog allows players to customize:
- Initial snake length
- Movement speed (cells per second)
- Grass background color


![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250426155544622.png)


### 3. Smooth Time-Step Movement
- Snake movement is controlled by accumulated elapsed time rather than frame count.
- This ensures consistent movement speed across different machines or frame rates.
- The snake moves only when accumulated time exceeds its speed threshold.

``` java
  public void init(int length, Direction d, Double s) {  
  
    speed = 1 / s;  
    // Set the snake's initial length  
    initLength = length;  
  
    // Initialize the snake's body as an ArrayList of Points  
    snakeBody = new ArrayList<>();  
  
    // Add points to the snake's body, starting at position (3, 5)  
    for (int i = 3; i < initLength + 3; i++) {  
        snakeBody.add(new Point(i, 5)); // Add each segment of the snake to the body  
    }  
  
    // Set the snake's head to be the first point in the body  
    snakeHead = snakeBody.get(0);  
  
    direction = d;  
}
```
### 4. Resource Loading and Image Rendering
Separate sprite images are used for:
- Snake head
- Snake body
- Apples
- Proper resource management ensures a polished and visually consistent appearance.

![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250426155640174.png)


![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250426155705822.png)


## 🛠 Technical Features
- **Object-Oriented Design (OOD)** with clear class separation:
    - `Snake` class handles snake logic (movement, growth, shrink).
    - `MainGame` class manages overall game state, rendering, and input.
- **Collision detection**:
    - Wall collision leads to gradual shrinking.
    - Self-collision (snake colliding with its body) triggers immediate game over.
- **Score tracking**:
    - The current score is displayed near the apple’s position.
- **Restart mechanism**:
    - Pressing `R` after game over will reset and restart the game.

![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250426155716473.png)

## 📦 How to Run
1. Open the project with IntelliJ IDEA or any Java IDE.
2. Ensure the resource images (`apple.png`, `dot.png`, `head.png`) are correctly placed under `src/main/resources/`.
3. Run `MainGame.java`.
4. Set your desired snake length, speed, and background color via the settings dialog.
5. Play and enjoy!