package com.hamhuo.massey;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;


/**
 * Main game class for the Snake game, extending {@link GameEngine}.
 * Manages the game grid, snake, apple, game state, and rendering.
 */
public class MainGame extends GameEngine {

    /**
     * <p>Represents the basic configuration of the grid, which is used to render
     * the grass area in the game. The grid is made up of individual cells, each
     * with a size of 20x20 pixels, and the total grid size is 500px x 500px,
     * representing a 25x25 area.</p>
     *
     * <p>Parameters:</p>
     * <ul>
     *     <li><b>numCols</b>: The number of columns in the grid. Set to 25,
     *     making the grid 25 cells wide. Each column represents a vertical slice
     *     of the grid, with each slice containing cells that are 20px wide.</li>
     *     <li><b>numRows</b>: The number of rows in the grid. Set to 25, which
     *     results in a grid that is 25 cells tall. Each row contains cells that
     *     are 20px high.</li>
     *     <li><b>gridSize</b>: The size of each individual grid cell in pixels.
     *     Set to 20px, meaning each cell is 20 pixels in both width and height.
     *     This value determines the size of each grid cell in the rendered layout.</li>
     *     <li><b>grassColor</b>: The color used to fill the grid cells that
     *     represent grass. By default, it is set to <code>Color.GREEN</code>,
     *     which mimics the appearance of grass, but can be changed to simulate
     *     different environmental effects or themes.</li>
     * </ul>
     *
     * <p>This configuration is ideal for rendering a simple 2D tiled grid, where
     * each cell can represent a part of the environment, such as grass, water,
     * obstacles, or other terrain features. The parameters can be adjusted to
     * customize the grid size, appearance, and environmental representation.</p>
     *
     * <p>Note: This grid configuration is designed for visual simplicity and
     * flexibility in rendering various grid-based environments in 2D games.</p>
     */
    private int numCols = 25; // Number of columns in the grid
    private int numRows = 25; // Number of rows in the grid
    private int gridSize = 20; // Size of each grid cell in pixels
    Color grassColor; // Color of the grid background

    private final Snake snake; // The snake object
    private Point apple; // Position of the apple

    private Boolean gameOver = false; // Indicates if the game is over
    private int score = 0; // Current score

    private Image appleImage; // Image for the apple
    private Image bodyImage; // Image for the snake's body
    private Image headImage; // Image for the snake's head

    // private static final Color APPLE_COLOR = new Color(144, 198, 124); // Commented out: Apple color
    private static final Color TEXT_COLOR = new Color(103, 174, 110); // Color for text
    private static final Color HIGH_TEXT_COLOR = new Color(225, 238, 188); // Color for highlighted text
    // private static final Color SNAKE_HEAD_COLOR = new Color(103, 174, 110); // Commented out: Snake head color
    // private static final Color SNAKE_BODY_COLOR = new Color(225, 238, 188); // Commented out: Snake body color

    // Stores settings menu values
    private int initialLength; // Initial length of the snake
    private double snakeSpeed; // Speed of the snake

    /**
     * Constructs a new {@code MainGame} instance.
     * Initializes the snake object.
     */
    public MainGame() {
        snake = new Snake();
    }

    /**
     * Entry point for the game.
     * Creates a new game instance, shows the settings dialog, and starts the game if settings are confirmed.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        MainGame game = new MainGame();
        game.showSettingsDialog(); // Show settings dialog
        if (game.isSettingsConfirmed()) {
            createGame(game, 180); // Start game with 180 FPS
        } else {
            System.exit(0); // Exit if user cancels settings
        }
    }

    /**
     * Checks if the settings dialog was confirmed.
     *
     * @return {@code true} if settings were confirmed, {@code false} otherwise
     */
    private boolean isSettingsConfirmed() {
        return initialLength > 0; // Simple check for confirmation
    }

    /**
     * Displays the settings dialog and retrieves user inputs.
     * Stores the initial length, grass color, and snake speed if confirmed.
     */
    private void showSettingsDialog() {
        SettingsDialog dialog = new SettingsDialog(null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            initialLength = dialog.getInitialLength();
            grassColor = Color.decode(dialog.getGrassColor());
            snakeSpeed = dialog.getSnakeSpeed();
        }
    }

    /**
     * Initializes the game state.
     * Loads images, initializes the snake with user settings, resets the score, and spawns an apple.
     */
    @Override
    public void init() {
        try {
            appleImage = loadImage("src/main/resources/apple.png"); // Load apple image
            bodyImage = loadImage("src/main/resources/dot.png"); // Load snake body image
            headImage = loadImage("src/main/resources/head.png"); // Load snake head image
        } catch (Exception e) {
            System.err.println("Failed to load images: " + e.getMessage());
        }
        //todo level setting (option)
        snake.init(initialLength, Direction.READY, snakeSpeed); // Initialize snake with user settings
        score = 0; // Reset score
        spawnApple(); // Spawn initial apple
    }

    /**
     * Spawns a new apple at a random position not occupied by the snake.
     */
    private void spawnApple() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(numCols); // Random x-coordinate
            y = rand.nextInt(numRows); // Random y-coordinate
        } while (snake.isOccupied(x, y)); // Ensure position is not occupied
        apple = new Point(x, y); // Set apple position
    }

    /**
     * Updates the game state.
     * Moves the snake, checks for collisions, and handles apple consumption.
     *
     * @param dt time elapsed since the last update, in seconds
     * @throws InterruptedException if the update is interrupted
     */
    @Override
    public void update(double dt) throws InterruptedException {
        if (gameOver) {
            return; // Skip update if game is over
        }
        snake.move(dt); // Move the snake
        checkCollisions(); // Check for collisions
        if (snake.getSnakeHead().equals(apple)) {
            score++; // Increment score
            snake.grow(); // Grow the snake
            spawnApple(); // Spawn a new apple
        }
    }

    /**
     * Draws the apple and its score text.
     */
    // TODO ADD some fancy style
    private void drawApple() {
        // 对齐用
        //drawSolidCircle(apple.x * gridSize + (double) gridSize / 2, apple.y * gridSize + (double) gridSize / 2, (double) gridSize / 2);
        drawImage(appleImage, apple.x * gridSize, apple.y * gridSize, 20, 20); // Draw apple image
        changeColor(TEXT_COLOR); // Set text color
        drawText(apple.x * gridSize, apple.y * gridSize, "" + score); // Draw score
    }

    /**
     * Draws the snake, its direction, and body size.
     */
    private void drawSnake() {
        changeColor(HIGH_TEXT_COLOR); // Set highlighted text color
        drawBoldText(300, 400, snake.getDirection().toString()); // Draw snake direction
        drawBoldText(snake.getSnakeHead().x * gridSize, snake.getSnakeHead().y * gridSize, "" + snake.getSnakeBody().size()); // Draw snake body size
        Point p = snake.getSnakeHead(); // Get snake head position
        changeColor(TEXT_COLOR); // Set head color
        //drawSolidRectangle(p.x * gridSize, p.y * gridSize, gridSize, gridSize);
        drawImage(headImage, p.x * gridSize, p.y * gridSize, 20, 20); // Draw head image
        for (int i = 1; i < snake.getSnakeBody().size(); i++) {
            //drawSolidRectangle(snake.getSnakeBody().get(i).x * gridSize, snake.getSnakeBody().get(i).y * gridSize, gridSize, gridSize);
            drawImage(bodyImage, snake.getSnakeBody().get(i).x * gridSize, snake.getSnakeBody().get(i).y * gridSize, 20, 20); // Draw body segment
        }
    }

    /**
     * Renders the game or game-over screen.
     */
    @Override
    public void paintComponent() {
        if (!gameOver) {
            // set color not apply yet
            changeBackgroundColor(grassColor); // Set background color
            // clear background by setting to background color
            clearBackground(500, 500); // Clear canvas
            // drawSnake
            drawSnake(); // Draw snake
            drawApple(); // Draw apple
        } else {
            changeColor(225, 238, 188); // Set text color for game-over screen
            drawBoldText(20, 200, "GAME OVER"); // Draw game-over message
            drawText(20, 250, "Push R"); // Draw restart prompt
            drawText(20, 300, "restart the game"); // Draw restart instruction
        }
    }

    /**
     * Handles keyboard input for snake direction and game restart.
     *
     * @param e the key event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
            gameOver = false; // Reset game state
            init(); // Reinitialize game
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                snake.setDirection(Direction.UP); // Set direction to up
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                snake.setDirection(Direction.DOWN); // Set direction to down
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                snake.setDirection(Direction.LEFT); // Set direction to left
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                snake.setDirection(Direction.RIGHT); // Set direction to right
                break;
        }
    }

    /**
     * Checks for collisions with the grid borders or the snake's own body.
     */
    private void checkCollisions() {
        // Check border collision
        if (snake.getSnakeHead().x < 0 || snake.getSnakeHead().y < 0 ||
                snake.getSnakeHead().x >= numCols || snake.getSnakeHead().y >= numRows) {
                gameOver = true;

        }
        // Check self collision
        for (int i = 1; i < snake.getSnakeBody().size(); i++) {
            if (snake.getSnakeBody().get(i).equals(snake.getSnakeHead())) {
                gameOver = true; // Set game over if snake hits itself
                break;
            }
        }
    }
}