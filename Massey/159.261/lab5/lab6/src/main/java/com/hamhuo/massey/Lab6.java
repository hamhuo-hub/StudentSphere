package com.hamhuo.massey;

import com.hamhuo.massey.GameEngine;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Lab6 extends GameEngine {
    // Main Function
    public static void main(String args[]) {
        // Warning: Only call createGame in this function
        // Create a new Lab6
        createGame(new Lab6());
    }
    //-------------------------------------------------------
    // Spaceship
    //-------------------------------------------------------
    Image spaceshipImage;
    Image spaceshipEngine;
    Image spaceshipLeft;
    Image spaceshipRight;

    double spaceshipPositionX;
    double spaceshipPositionY;
    double spaceshipVelocityX;
    double spaceshipVelocityY;
    double spaceshipAngle;

    boolean left, right, up, down;
    boolean gameOver;

    public void initSpaceship() {
        spaceshipImage = subImage(spritesheet, 0, 0, 240, 240);
        spaceshipEngine = subImage(spritesheet, 0, 240, 240, 240);
        spaceshipLeft = subImage(spritesheet, 240, 240, 240, 240);
        spaceshipRight = subImage(spritesheet, 480, 240, 240, 240);

        spaceshipPositionX = width() / 2;
        spaceshipPositionY = height() / 2;
        spaceshipVelocityX = 0;
        spaceshipVelocityY = 0;
        spaceshipAngle = 0;
    }

    public void drawSpaceship() {
        saveCurrentTransform();
        translate(spaceshipPositionX, spaceshipPositionY);
        rotate(spaceshipAngle);

        drawImage(spaceshipImage, -30, -30, 60, 60);

        if (left) {
            drawImage(spaceshipLeft, -30, -30, 60, 60);
        }
        if (right) {
            drawImage(spaceshipRight, -30, -30, 60, 60);
        }
        if (up) {
            drawImage(spaceshipEngine, -30, -15, 60, 60);
        }

        restoreLastTransform();
    }

    public void updateSpaceship(double dt) {
        if (up) {
            spaceshipVelocityX += sin(spaceshipAngle) * 250 * dt;
            spaceshipVelocityY -= cos(spaceshipAngle) * 250 * dt;
        }

        if (left) {
            spaceshipAngle -= 250 * dt;
        }

        if (right) {
            spaceshipAngle += 250 * dt;
        }

        spaceshipPositionX += spaceshipVelocityX * dt;
        spaceshipPositionY += spaceshipVelocityY * dt;

        if (spaceshipPositionX > width()) spaceshipPositionX -= width();
        if (spaceshipPositionX < 0) spaceshipPositionX += width();
        if (spaceshipPositionY > height()) spaceshipPositionY -= height();
        if (spaceshipPositionY < 0) spaceshipPositionY += height();
    }

    //-------------------------------------------------------
    // Laser
    //-------------------------------------------------------
    Image laserImage;

    class Laser {
        double laserPositionX;
        double laserPositionY;
        double laserVelocityX;
        double laserVelocityY;
        double laserAngle;
        boolean laserActive;
    }

    int maxLasers;
    ArrayList<Laser> lasers;

    public void initLaser() {
        maxLasers = 3;
        lasers = new ArrayList<>();
        for (int i = 0; i < maxLasers; i++) {
            lasers.add(new Laser());
        }
        laserImage = subImage(spritesheet, 240, 0, 240, 240);
    }

    public void fireLaser() {
        for (Laser l : lasers) {
            if (!l.laserActive) {
                l.laserPositionX = spaceshipPositionX;
                l.laserPositionY = spaceshipPositionY;
                l.laserVelocityX = sin(spaceshipAngle) * 250;
                l.laserVelocityY = -cos(spaceshipAngle) * 250;
                l.laserAngle = spaceshipAngle;
                l.laserActive = true;
                break;
            }
        }
    }

    public void drawLaser() {
        for (Laser l : lasers) {
            if (l.laserActive) {
                saveCurrentTransform();
                translate(l.laserPositionX, l.laserPositionY);
                rotate(l.laserAngle);
                drawImage(laserImage, -30, -30, 60, 60);
                restoreLastTransform();
            }
        }
    }

    public void updateLaser(double dt) {
        for (Laser l : lasers) {
            if (l.laserActive) {
                l.laserPositionX += l.laserVelocityX * dt;
                l.laserPositionY += l.laserVelocityY * dt;

                if (l.laserPositionX < 0 || l.laserPositionX >= width() ||
                        l.laserPositionY < 0 || l.laserPositionY >= height()) {
                    l.laserActive = false;
                }
            }
        }
    }

    //-------------------------------------------------------
    // Asteroid
    //-------------------------------------------------------
    Image asteroidImage;

    double asteroidPositionX;
    double asteroidPositionY;
    double asteroidVelocityX;
    double asteroidVelocityY;
    double asteroidAngle;
    double asteroidRadius;

    public void initAsteroid() {
        asteroidImage = subImage(spritesheet, 480, 0, 240, 240);
    }

    public void randomAsteroid() {
        asteroidPositionX = rand(width());
        asteroidPositionY = rand(height());
        while (distance(asteroidPositionX, asteroidPositionY, spaceshipPositionX, spaceshipPositionY) < asteroidRadius * 2) {
            asteroidPositionX = rand(width());
            asteroidPositionY = rand(height());
        }
        asteroidVelocityX = -50 + rand(100);
        asteroidVelocityY = -50 + rand(100);
        asteroidAngle = rand(360);
        asteroidRadius = 30;
    }

    public void updateAsteroid(double dt) {
        asteroidPositionX += asteroidVelocityX * dt;
        asteroidPositionY += asteroidVelocityY * dt;

        if (asteroidPositionX < 0) asteroidPositionX += width();
        if (asteroidPositionX >= width()) asteroidPositionX -= width();
        if (asteroidPositionY < 0) asteroidPositionY += height();
        if (asteroidPositionY >= height()) asteroidPositionY -= height();
    }

    public void drawAsteroid() {
        saveCurrentTransform();
        translate(asteroidPositionX, asteroidPositionY);
        rotate(asteroidAngle);
        drawImage(asteroidImage, -30, -30, 60, 60);
        restoreLastTransform();
    }

    //-------------------------------------------------------
    // Explosion
    //-------------------------------------------------------
    Image[] explosionImages = new Image[32];
    double explosionPositionX;
    double explosionPositionY;
    double explosionTimer;
    double explosionDuration;
    boolean explosionActive;

    public void initExplosion() {
        for (int i = 0; i < 4; i++) {
            for(int j = 0; j < 8; j++) {
                explosionImages[i] = subImage(spritesheet, 0 + j*240,  480 + i * 240, 240, 240);
            }

        }
    }

    public void createExplosion(double x, double y) {
        explosionPositionX = x;
        explosionPositionY = y;
        explosionTimer = 0;
        explosionDuration = 1; // 1 second explosion
        explosionActive = true;
    }

    public void updateExplosion(double dt) {
        if (explosionActive) {
            explosionTimer += dt;
            if (explosionTimer >= explosionDuration) {
                explosionActive = false;
            }
        }
    }

    public void drawExplosion() {
        if (explosionActive) {
            int frame = (int)(explosionTimer / explosionDuration * explosionImages.length);
            drawImage(explosionImages[frame], explosionPositionX, explosionPositionY);
        }
    }

    //-------------------------------------------------------
    // Alien
    //-------------------------------------------------------
    double alienPositionX, alienPositionY;
    double alienVelocityX, alienVelocityY;
    double alienAngle;
    Image alienImage;

    public void initAlien() {
        alienImage = subImage(spritesheet, 960, 0, 240, 240);
    }

    public void randomAlien() {
        alienPositionX = rand(width());
        alienPositionY = rand(height());
        alienVelocityX = -50 + rand(100);
        alienVelocityY = -50 + rand(100);
    }

    public void updateAlien(double dt) {
        double dx = spaceshipPositionX - alienPositionX;
        double dy = spaceshipPositionY - alienPositionY;

        if (dx > width() / 2) dx -= width();
        if (dx < -width() / 2) dx += width();
        if (dy > height() / 2) dy -= height();
        if (dy < -height() / 2) dy += height();

        double length = length(dx, dy);
        alienVelocityX = dx / length * 50; // speed factor of 50
        alienVelocityY = dy / length * 50;

        alienPositionX += alienVelocityX * dt;
        alienPositionY += alienVelocityY * dt;
    }

    public void drawAlien() {
        saveCurrentTransform();
        translate(alienPositionX, alienPositionY);
        rotate(alienAngle);
        drawImage(alienImage, -30, -30, 60, 60);
        restoreLastTransform();
    }

    //-------------------------------------------------------
    // Game Logic
    //-------------------------------------------------------
    Image spritesheet;

    public void init() {
        spritesheet = loadImage("C:\\Users\\HuoZihang\\Documents\\GitHub\\StudentSphere\\Massey\\159.261\\lab5\\lab6\\src\\main\\resources\\spritesheet.png");
        initSpaceship();
        initLaser();
        initAsteroid();
        randomAsteroid();
        initExplosion();
        initAlien();
    }

    public void update(double dt) {
        if (gameOver) return;

        updateSpaceship(dt);
        updateLaser(dt);
        updateAsteroid(dt);
        updateExplosion(dt);
        updateAlien(dt);

        for (Laser l : lasers) {
            if (l.laserActive && distance(l.laserPositionX, l.laserPositionY, asteroidPositionX, asteroidPositionY) < asteroidRadius * 1.2) {
                l.laserActive = false;
                createExplosion(asteroidPositionX, asteroidPositionY);
                randomAsteroid();
            }
            if (l.laserActive && distance(l.laserPositionX, l.laserPositionY, alienPositionX, alienPositionY) < 30) {
                l.laserActive = false;
                createExplosion(alienPositionX, alienPositionY);
                randomAlien();
            }
        }

        if (distance(spaceshipPositionX, spaceshipPositionY, asteroidPositionX, asteroidPositionY) < asteroidRadius + 30) {
            gameOver = true;
        }

        if (distance(spaceshipPositionX, spaceshipPositionY, alienPositionX, alienPositionY) < 60) {
            gameOver = true;
        }
    }

    public void paintComponent() {
        changeBackgroundColor(black);
        clearBackground(width(), height());

        if (!gameOver) {
            drawAsteroid();
            drawLaser();
            drawSpaceship();
            drawExplosion();
            drawAlien();
        } else {
            changeColor(white);
            drawText(width() / 2 - 165, height() / 2, "GAME OVER!", "Arial", 50);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = true;
        if (e.getKeyCode() == KeyEvent.VK_UP) up = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) fireLaser();
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
        if (e.getKeyCode() == KeyEvent.VK_UP) up = false;
    }
}
