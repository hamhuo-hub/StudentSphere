import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Demo extends GameEngine {

    private int gridSize = 20; // Each cell size in the grid
    private int numRows = 20, numCols = 20; // Grid dimensions (adjustable)
    private Snake snake;
    private Point apple;
    private boolean gameOver = false;

    //游戏入口
    public static void main(String[] args) {
        //新建游戏实例
        Demo game = new Demo();
        // call engine
        createGame(game,15);
    }

    public Demo() {
        super();
        snake = new Snake();
        spawnApple();
        this.setupWindow(numCols * gridSize, numRows * gridSize); // Adjust window size to grid
    }

    @Override
    public void init() {
        // Initialize any game setup here
        // 全局设置初始化
    }

    @Override
    public void update(double dt) throws InterruptedException {
        if (gameOver) {
            return;
        }

        snake.move();
        checkCollisions();

        if (snake.head.equals(apple)) {
            snake.grow();
            spawnApple();
        }
    }

    @Override
    public void paintComponent() {
        clearBackground(numCols * gridSize, numRows * gridSize);
        drawSnake();
        drawApple();
        if (gameOver) {
            drawText(numCols / 3 * gridSize, numRows / 2 * gridSize, "Game Over! Press R to Restart");
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (gameOver && event.getKeyCode() == KeyEvent.VK_R) {
            gameOver = false;
            snake.reset();
            spawnApple();
        } else if (!gameOver) {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_W: snake.setDirection(Direction.UP); break;
                case KeyEvent.VK_S: snake.setDirection(Direction.DOWN); break;
                case KeyEvent.VK_A: snake.setDirection(Direction.LEFT); break;
                case KeyEvent.VK_D: snake.setDirection(Direction.RIGHT); break;
                case KeyEvent.VK_UP: snake.setDirection(Direction.UP); break;
                case KeyEvent.VK_DOWN: snake.setDirection(Direction.DOWN); break;
                case KeyEvent.VK_LEFT: snake.setDirection(Direction.LEFT); break;
                case KeyEvent.VK_RIGHT: snake.setDirection(Direction.RIGHT); break;
            }
        }
    }

    private void spawnApple() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(numCols);
            y = rand.nextInt(numRows);
        } while (snake.isOccupied(x, y));
        apple = new Point(x, y);
    }

    private void drawSnake() {
        for (Point p : snake.body) {
            drawSolidRectangle(p.x * gridSize, p.y * gridSize, gridSize, gridSize);
        }
    }

    private void drawApple() {
        drawSolidCircle(apple.x * gridSize + gridSize / 2, apple.y * gridSize + gridSize / 2, gridSize / 2);
    }

    private void checkCollisions() {
        // Check border collision
        if (snake.head.x < 0 || snake.head.x >= numCols || snake.head.y < 0 || snake.head.y >= numRows) {
            gameOver = true;
        }

        // Check self collision
        for (int i = 1; i < snake.body.size(); i++) {
            if (snake.body.get(i).equals(snake.head)) {
                gameOver = true;
                break;
            }
        }
    }

    private class Snake {
        private LinkedList<Point> body;
        private Direction direction;
        private Point head;

        public Snake() {
            body = new LinkedList<>();
            body.add(new Point(5, 5));
            body.add(new Point(4, 5));
            body.add(new Point(3, 5));
            head = body.getFirst();
            direction = Direction.RIGHT;
        }

        public void move() {
            Point newHead = new Point(head);
            switch (direction) {
                case UP: newHead.y--; break;
                case DOWN: newHead.y++; break;
                case LEFT: newHead.x--; break;
                case RIGHT: newHead.x++; break;
            }

            body.addFirst(newHead);
            head = newHead;

            // Remove the tail if not growing
            if (body.size() > 3) {
                body.removeLast();
            }
        }

        public void grow() {
            // Do nothing to remove the tail, just grow the snake
        }

        public void reset() {
            body.clear();
            body.add(new Point(5, 5));
            body.add(new Point(4, 5));
            body.add(new Point(3, 5));
            head = body.getFirst();
            direction = Direction.RIGHT;
        }

        public void setDirection(Direction newDirection) {
            if ((direction == Direction.UP && newDirection != Direction.DOWN) ||
                    (direction == Direction.DOWN && newDirection != Direction.UP) ||
                    (direction == Direction.LEFT && newDirection != Direction.RIGHT) ||
                    (direction == Direction.RIGHT && newDirection != Direction.LEFT)) {
                direction = newDirection;
            }
        }

        public boolean isOccupied(int x, int y) {
            return body.contains(new Point(x, y));
        }
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
