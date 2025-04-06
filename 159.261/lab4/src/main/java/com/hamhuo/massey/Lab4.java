package com.hamhuo.massey;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Lab4 extends GameEngine {
    // Main Function
    public static void main(String[] args) {
        // Warning: Only call createGame in this function
        // Create a new Lab4
        createGame(new Lab4());
    }

    //-------------------------------------------------------
    // Game
    //-------------------------------------------------------

    // Booleans to keep track of whether a key is pressed or not
    boolean left, right, up;
    boolean gameOver;

    //-------------------------------------------------------
    // Spaceship
    //-------------------------------------------------------

    // Spaceship position & Velocity
    double spaceshipPositionX, spaceshipPositionY;
    double spaceshipVelocityX, spaceshipVelocityY;
    double screenX = 500;
    double screenY = 500;
    // Spaceship angle
    double spaceshipAngle;

    // Code to update 'move' the spaceship
    public void updateSpaceship(double dt) {
        if (up) {
            // Increase the velocity of the spaceship
            // as determined by the angle
            // change angle to x-velocity and y-velocity
            spaceshipVelocityX += sin(spaceshipAngle) * 250 * dt;
            spaceshipVelocityY += -cos(spaceshipAngle) * 250 * dt;
        }

        // If the user is holding down the left arrow key
        // Make the spaceship rotate anti-clockwise
        if (left) {
            spaceshipAngle -= 180 * dt;
        }

        // If the user is holding down the right arrow key
        // Make the spaceship rotate clockwise
        if (right) {
            spaceshipAngle += 180 * dt;
        }


        // Make the spaceship move forward
        // x = x + vt, speed to distance
        spaceshipPositionX += spaceshipVelocityX * dt;
        spaceshipPositionY += spaceshipVelocityY * dt;

        if (spaceshipPositionX > screenX) {
            spaceshipPositionX %= screenX;
        }

        if (spaceshipPositionY > screenY) {
            spaceshipPositionY %= screenY;
        }

        if (spaceshipPositionY < 0) {
            spaceshipPositionY = screenY + spaceshipPositionY;
        }

        if (spaceshipPositionX < 0) {
            spaceshipPositionX = screenX + spaceshipPositionX;
        }

        // for testing, simulate damping
        spaceshipVelocityX *= 0.99;
        spaceshipVelocityY *= 0.99;

    }

    // Function to draw the spaceship
    public void drawSpaceship() {
        // Set the color to white
        changeColor(white);

        // Save the current transform
        saveCurrentTransform();

        // translate to the position of the asteroid
        translate(spaceshipPositionX, spaceshipPositionY);

        // Rotate the drawing context around the angle of the asteroid
        rotate(spaceshipAngle);

        // Draw the actual spaceship
        // 画一个三角形
        drawLine(-10, 0, 0, 20);  // 左侧边
        drawLine(0, 20, 10, 0);  // 右侧边
        drawLine(-10, 0, 10, 0);    // 底边

// 画一个圆形
        drawCircle(0, 10, 10);  // 圆心(10, 10)，半径10

        // Restore last transform to undo the rotate and translate transforms
        restoreLastTransform();
    }

    //-------------------------------------------------------
    // Laser
    //-------------------------------------------------------

    // Laser position & Velocity
    double laserPositionX, laserPositionY;
    double laserVelocityX, laserVelocityY;

    // Laser Angle
    double laserAngle;

    // Laser active
    boolean laserActive = false;

    // Function to shoot a new laser
    public void fireLaser() {
        if (!laserActive) {
            // Set the laser position as the current spaceship position
            laserPositionX = spaceshipPositionX;
            laserPositionY = spaceshipPositionY;

            // And make it move in the same direction as the spaceship is facing
            // speed fixed
            laserVelocityX = sin(spaceshipAngle) * 250;
            laserVelocityY = -cos(spaceshipAngle) * 250;

            // And face the same direction as the spaceship
            laserAngle = spaceshipAngle;

            // Set it to active
            laserActive = true;
        }
    }

    // Function to update 'move' the laser
    public void updateLaser(double dt) {
        // Update the laser
        laserPositionX += laserVelocityX * dt;
        laserPositionY += laserVelocityY * dt;
        if(laserPositionX < 0 || laserPositionY < 0 || laserPositionX > screenX || laserPositionY > screenY) {
            laserActive = false;
        }
    }

    // Function to draw the laser
    public void drawLaser() {
        if (laserActive) {
            changeColor(red);
            saveCurrentTransform();
            translate(laserPositionX, laserPositionY);
            rotate(laserAngle);
            drawLine(0, 0, 0, 10);
            restoreLastTransform();
        }
    }

    //-------------------------------------------------------
    // Asteroid
    //-------------------------------------------------------

    // Asteroid Position & Velocity
    double asteroidPositionX, asteroidPositionY;
    double asteroidVelocityX, asteroidVelocityY;

    // Asteroid Radius
    double asteroidRadius;

    public void randomAsteroid() {

        // Generate a random asteroid
        asteroidPositionX = rand(500);
        asteroidPositionY = rand(500);

        asteroidVelocityX = -50 + rand(100);
        asteroidVelocityY = -50 + rand(100);
    }

    // Function to update 'move' the asteroid
    public void updateAsteroid(double dt) {
        // Update the asteroid
        asteroidPositionX += asteroidVelocityX * dt;
        asteroidPositionY += asteroidVelocityY * dt;


        // 包裹逻辑
        if (asteroidPositionY > screenY) {
            asteroidPositionY = 0;  // 直接重置到顶部
        }
        if (asteroidPositionX > screenX) {
            asteroidPositionX = 0;  // 直接重置到左边
        }

        if (asteroidPositionY < 0) {
            asteroidPositionY = screenY;  // 直接重置到底部
        }

        if (asteroidPositionX < 0) {
            asteroidPositionX = screenX;  // 直接重置到右边
        }

    }

    // Function to draw the asteroid
    public void drawAsteroid() {
        // Draw the asteroid
        changeColor(yellow);
        // Draw the actual spaceship
        drawCircle(asteroidPositionX , asteroidPositionY, asteroidRadius);
    }

    //-------------------------------------------------------
    // Game
    //-------------------------------------------------------

    public void init() {
        // Initialise game boolean
        gameOver = false;

        // Initialise key booleans
        left = false;
        right = false;
        up = false;

        // Initialise Spaceship
        spaceshipPositionX = 250;
        spaceshipPositionY = 250;
        spaceshipVelocityX = 0;
        spaceshipVelocityY = 0;
        spaceshipAngle = 0;

        // Initialise Laser
        laserPositionX = 0;
        laserPositionY = 0;
        laserVelocityX = 0;
        laserVelocityY = 0;
        laserAngle = 0;
        laserActive = false;

        // Initialise Asteroid
        asteroidPositionX = 0;
        asteroidPositionY = 0;
        asteroidVelocityX = 0;
        asteroidVelocityY = 0;
        asteroidRadius = 20;

        randomAsteroid();
    }

    int starts = 0;
    // Updates the display
    public void update(double dt){
        // If the game is over
        // Update the spaceship
        updateSpaceship(dt);

        // Update the laser
        updateLaser(dt);

        // Update Asteroid
        updateAsteroid(dt);

        //-------------------------------------------------------
        // Add code to check for collisions
        //-------------------------------------------------------
        double distance = distance(laserPositionX, laserPositionY, asteroidPositionX, asteroidPositionY);
        double risk = distance(spaceshipPositionX, spaceshipPositionY, asteroidPositionX, asteroidPositionY);
        if (distance < asteroidRadius) {
            randomAsteroid();
            starts++;
        }

        if(risk <= asteroidRadius){
            gameOver = true;
        }
    }

    int colorDirection = 1;
    int[] colors = new int[]{255,255,255};
    public void flashAndShakeSpeed(double dt){
            colors[0] += (int) (colorDirection * 200 * dt);
            colors[1] += (int) (colorDirection * 200 * dt);
            colors[2] += (int) (colorDirection * 200 * dt);

            if (colors[0] >= 255) {
                colorDirection = -1;
            }
    }
    // This gets called any time the Operating System
    // tells the program to paint itself
    public void paintComponent() {
        // Clear the background to black
            changeBackgroundColor(black);
            clearBackground((int) screenX, (int) screenY);


        // If the game is not over yet
        if (!gameOver) {
            // Paint the Spaceship
            drawSpaceship();

            // Paint the Asteroid
            drawAsteroid();

            // Paint the laser (if it's active)
            drawLaser();
        } else {
            // If the game is over
            // Display GameOver text
            changeColor(white);
            drawText(85, 250, "GAME OVER!", "Arial", 50);
            drawText(85, 350, "hit: " + "  " + starts, "Arial", 25);
        }
    }

    // Called whenever a key is pressed
    public void keyPressed(KeyEvent e) {
        // The user pressed left arrow
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
        // The user pressed right arrow
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
        // The user pressed up arrow
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            fireLaser();
        }
    }

    // Called whenever a key is released
    public void keyReleased(KeyEvent e) {
        // The user released left arrow
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }
        // The user released right arrow
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
        // The user released up arrow
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = false;
        }
    }
}