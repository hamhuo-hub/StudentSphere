package com.hamhuo.massey.stage3;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stage 3 (10 marks)
 * - Display time (2 decimal places) and score in top left.
 * - Score starts at 100.
 * - Green balloon pop: +10; red balloon pop: -50; green balloon off-screen: -10.
 * - Game over if score < 0.
 * - Game over shows time, "GAME OVER!", and "Press Space to Restart".
 * - Restart game with space bar.
 */
class Balloon {
    public double x, y;
    public int size;
    public double velocityY;
    public Image image;
    public Image[] popImages;
    public boolean isPopping;
    public double popStartTime;
    public int popFrame; // Current frame of popping animation

    public Balloon(double x, double y, int size, double velocityY, Image image, Image[] popImages) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.velocityY = velocityY;
        this.image = image;
        this.popImages = popImages;
        this.isPopping = false;
        this.popStartTime = 0;
        this.popFrame = 0;
    }
}

class ExpandingCircle {
    public double x, y; // Center
    public double radius; // Current radius
    public double startRadius; // Initial radius
    public double startTime; // Animation start time

    public ExpandingCircle(double x, double y, double startRadius, double startTime) {
        this.x = x;
        this.y = y;
        this.startRadius = startRadius;
        this.radius = startRadius;
        this.startTime = startTime;
    }
}

public class Stage3 extends GameEngine {
    boolean gameOver = false;
    List<Balloon> balloons = new ArrayList<>();
    List<ExpandingCircle> circles = new ArrayList<>();
    double startTime = getTime();
    double passTime = 0;
    double speed = 1.0;
    double lastSpawnTime = getTime();
    double spawnInterval = 0;
    int score = 100; // Initial score
    Image[] balloonImages = new Image[2];
    Image[] popRedImages = new Image[5];
    Image[] popGreenImages = new Image[5];
    AudioClip popSound;

    @Override
    public void init() {
        // Load balloon and popping sprites
        balloonImages[0] = loadImage("./labtest/src/main/resources/greenballoon/1.png");
        balloonImages[1] = loadImage("./labtest/src/main/resources/redballoon/1.png");
        for (int i = 0; i < 5; i++) {
            popRedImages[i] = loadImage("./labtest/src/main/resources/redballoon/" + (i + 2) + ".png");
            popGreenImages[i] = loadImage("./labtest/src/main/resources/greenballoon/" + (i + 2) + ".png");
        }
        // Load sound
        popSound = loadAudio("./labtest/src/main/resources/pop.wav");
        spawnInterval = rand(2.0) / speed;
    }

    public static void main(String[] args) {
        GameEngine.createGame(new Stage3());
    }

    @Override
    public void update(double dt) {
        if (!gameOver) {
            passTime = (getTime() - startTime) / 1000;
            speed = passTime / 10 + 1.0;

            // Spawn new balloons
            if ((getTime() - lastSpawnTime) / 1000 >= spawnInterval) {
                int size = rand(2) == 0 ? 40 : 80;
                double x = rand(width() - size) + size / 2.0;
                double y = -(size / 2.0);
                double velocityY = rand(101) + (speed * 50);
                Image image;
                Image[] popImages;
                if (rand(2) == 0) {
                    image = balloonImages[0];
                    popImages = popGreenImages;
                } else {
                    image = balloonImages[1];
                    popImages = popRedImages;
                }

                balloons.add(new Balloon(x, y, size, velocityY, image, popImages));
                lastSpawnTime = getTime();
                spawnInterval = rand(2.) / speed;
            }


            for (int i = balloons.size() - 1; i >= 0; i--) {
                Balloon b = balloons.get(i);
                if (!b.isPopping) {
                    b.y += b.velocityY * dt;
                } else {

                    double elapsed = (getTime() - b.popStartTime) / 1000.0;
                    b.popFrame = (int) (elapsed / 0.04);
                    if (elapsed >= 0.2) {
                        balloons.remove(i);
                    }
                }
                if (!b.isPopping && b.y > height() + b.size / 2.) {
                    if (b.image == balloonImages[0]) {
                        score -= 10;
                    }
                    balloons.remove(i);
                }
            }

            for (int i = circles.size() - 1; i >= 0; i--) {
                ExpandingCircle c = circles.get(i);
                double elapsed = (getTime() - c.startTime) / 1000.0;
                c.radius = c.startRadius + (elapsed / 0.2) * (c.startRadius * 2);
                if (elapsed >= 0.2) {
                    circles.remove(i);
                }
            }

            // Check game over
            if (score < 0) {
                gameOver = true;
            }
        }
    }

    @Override
    public void paintComponent() {
        changeBackgroundColor(Color.BLACK);
        clearBackground(width(), height());

        // Display time and score
        changeColor(Color.WHITE);
        drawText(10, 20, String.format("Time: %.2f", passTime), "Arial", 20);
        drawText(10, 40, "Score: " + score, "Arial", 20);

        if (!gameOver) {

            for (Balloon b : balloons) {
                saveCurrentTransform();
                if (!b.isPopping) {
                    drawImage(b.image, b.x - b.size / 2., b.y - b.size / 2., b.size, b.size);
                } else {
                    int frame = Math.min(b.popFrame, 4);
                    drawImage(b.popImages[frame], b.x - b.size / 2., b.y - b.size / 2., b.size, b.size);
                }
                restoreLastTransform();
            }

            for (ExpandingCircle c : circles) {
                saveCurrentTransform();
                changeColor(Color.WHITE);
                drawCircle(c.x, c.y, c.radius);
                restoreLastTransform();
            }
        } else {
            changeColor(Color.RED);
            drawBoldText(150, 200, "GAME OVER!", "Arial", 40);
            drawText(150, 250, String.format("Time: %.2f", passTime), "Arial", 30);
            drawText(150, 300, "Press Space to Restart", "Arial", 30);
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (!gameOver) {
            double clickX = event.getX();
            double clickY = event.getY();
            Set<Balloon> toPop = new HashSet<>();

            for (Balloon b : balloons) {
                if (!b.isPopping && distance(clickX, clickY, b.x, b.y) < b.size / 2.) {
                    toPop.add(b);
                }
            }

            Set<Balloon> allToPop = new HashSet<>();
            for (Balloon b : toPop) {
                findTouchingBalloons(b, allToPop);
            }

            for (Balloon b : allToPop) {
                b.isPopping = true;
                b.popStartTime = getTime();
                b.popFrame = 0;
                circles.add(new ExpandingCircle(b.x, b.y, b.size / 2., getTime()));
                playAudio(popSound);
                if (b.image == balloonImages[0]) {
                    score += 10;
                } else {
                    score -= 50;
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (gameOver && event.getKeyCode() == KeyEvent.VK_SPACE) {
            balloons.clear();
            circles.clear();
            startTime = getTime();
            passTime = 0;
            speed = 1.0;
            lastSpawnTime = getTime();
            spawnInterval = rand(2.0) / speed;
            score = 100;
            gameOver = false;
        }
    }
    
    private void findTouchingBalloons(Balloon b, Set<Balloon> toPop) {
        if (toPop.contains(b)) return;
        toPop.add(b);
        for (Balloon other : balloons) {
            if (!other.isPopping && other != b && distance(b.x, b.y, other.x, other.y) < (b.size / 2. + other.size / 2.)) {
                findTouchingBalloons(other, toPop);
            }
        }
    }
}