package com.hamhuo.massey;

import java.awt.*;
import java.util.ArrayList;

/**
 * The Snake class represents a snake in a game, with properties like the snake's body, head,
 * color, movement direction, and initial length. It provides methods for initializing the snake
 * and retrieving its body and color.
 */
public class Snake {

    // List to store the points representing the snake's body
    private ArrayList<Point> snakeBody;

    // The direction the snake is moving in (can be an enum or class that defines the direction)
    private Direction direction;

    // The point representing the snake's head
    private Point snakeHead;
    // The initial length of the snake (default is 3)
    private int initLength = 3;
    /**
     * The speed of the snake, in seconds per grid unit.
     * A value of 0.1 means the snake moves two cells per second.
     */
    private double speed = 0.1;
    private static int MAX_SIZE = 20;


    /**
     * Constructs a new Snake object.
     */
    public Snake() {
    }

    public static int getMaxSize() {
        return MAX_SIZE;
    }

    public static void setMaxSize(int maxSize) {
        MAX_SIZE = maxSize;
    }

    /**
     * Initializes the snake with a given length and color.
     *
     * @param length the initial length of the snake
     */
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


    /**
     * Accumulated time since the last movement, in seconds.
     */
    double accumulatedTime = 0;

    /**
     * Moves the snake based on the elapsed time.
     * <p>
     * The snake only moves if the accumulated time exceeds the speed threshold,
     * and the current direction is valid (i.e., not READY).
     * </p>
     *
     * @param time the elapsed time since the last frame or update call, in seconds
     */
    public void move(double time) {
        // Accumulate time since the last movement
        accumulatedTime += time;

        // Move the snake if enough time has passed and direction is set
        if (accumulatedTime >= speed && direction != Direction.READY) {
            // Get the current head position
            int x = snakeHead.x;
            int y = snakeHead.y;

            // Update position based on the current direction
            switch (direction) {
                case UP:
                    y--;
                    break;
                case DOWN:
                    y++;
                    break;
                case LEFT:
                    x--;
                    break;
                case RIGHT:
                    x++;
                    break;
            }

            // Set new head position
            snakeHead = new Point(x, y);

            if (grow && snakeBody.size() < MAX_SIZE) {
                snakeBody.add(snakeBody.get(snakeBody.size() - 1));
                grow = false;
            }
            // Move body segments forward
            for (int i = snakeBody.size() - 1; i > 0; i--) {
                snakeBody.set(i, snakeBody.get(i - 1));
            }
            snakeBody.set(0, snakeHead);

            // Reset the accumulated time after movement
            accumulatedTime = 0;
        }
    }

    private boolean grow = false;

    public void grow() {
        grow = true;
    }

    public boolean isOccupied(int x, int y) {
        return snakeBody.contains(new Point(x, y));
    }

    /**
     * Returns the body of the snake as an ArrayList of Points.
     *
     * @return an ArrayList containing the Points representing the snake's body
     */
    public ArrayList<Point> getSnakeBody() {
        return snakeBody;
    }


    public void setDirection(Direction d) {
        // todo 非凡的构想，无敌的异或，我太强了
        if ((direction.getValue() ^ d.getValue()) != 2) {
            this.direction = d;
        }

    }

    public Point getSnakeHead() {
        return snakeHead;
    }

    public Direction getDirection() {
        return direction;
    }

}
