package com.hamhuo.massey.stage2;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stage 2 (10 marks)
 * - Click inside a balloon to pop it.
 * - Popping plays animation and disappears, with 0.2s expanding circle from balloon radius.
 * - Play "pop.wav" when popped.
 * - Click inside multiple balloons, all pop.
 * - Touching balloons pop recursively.
 * - Subsequent pops can start immediately or after animation.
 * fixme: author: HUO ZIHANG DO NOT COPY
 */
class Balloon {
    public double x, y;
    public int size;
    public double velocityY;
    public Image image;
    public Image[] popImages;
    public boolean isPopping;
    public double popStartTime;
    public int popFrame;

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
    public double x, y;
    public double radius;
    public double startRadius;
    public double startTime;

    public ExpandingCircle(double x, double y, double startRadius, double startTime) {
        this.x = x;
        this.y = y;
        this.startRadius = startRadius;
        this.radius = startRadius;
        this.startTime = startTime;
    }
}

public class Stage2 extends GameEngine {
    Boolean gameOver = false;
    List<Balloon> balloons = new ArrayList<>();
    List<ExpandingCircle> circles = new ArrayList<>();
    double startTime = getTime();
    double speed = 1.0;
    double lastSpawnTime = getTime();
    double spawnInterval = 0;
    Image[] balloonImages = new Image[2];
    Image[] popRedImages = new Image[5];
    Image[] popGreenImages = new Image[5];
    AudioClip popSound;

    @Override
    public void init() {
        // Load balloon and popping sprites
        balloonImages[0] = loadImage("./labtest/src/resources/greenballoon/1.png");
        balloonImages[1] = loadImage("./labtest/src/resources/redballoon/1.png");
        for (int i = 0; i < 5; i++) {
            popRedImages[i] = loadImage("./labtest/src/resources/redballoon/" + (i + 2) + ".png");
            popGreenImages[i] = loadImage("./labtest/src/resources/greenballoon/" + (i + 2) + ".png");
        }
        // Load sound
        popSound = loadAudio("./labtest/src/resources/pop.wav");
        spawnInterval = rand(2.) / speed;
    }

    public static void main(String[] args) {
        GameEngine.createGame(new Stage2());
    }

    @Override
    public void update(double dt) {
        speed = ((getTime() - startTime) / 1000) / 10 + 1.;

        // Spawn new balloons
        if ((getTime() - lastSpawnTime) / 1000 >= spawnInterval) {
            int size = rand(2) == 0 ? 40 : 80;
            double x = rand(width() - size) + size / 2.;
            double y = -(size / 2.);
            double velocityY = rand(101) + (speed * 50);
            // stage 2
            Image[] popImages;
            Image image;


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

        // Update balloons
        for (int i = balloons.size() - 1; i >= 0; i--) {
            Balloon b = balloons.get(i);
            if (!b.isPopping) {
                b.y += b.velocityY * dt; // Move normal balloons
            } else {
                // Update popping animation
                double elapsed = (getTime() - b.popStartTime) / 1000.;
                b.popFrame = (int) (elapsed / 0.04); // 0.04s per frame
                if (elapsed >= 0.2) {
                    balloons.remove(i); // Remove after animation
                }
            }
            if (!b.isPopping && b.y > height() + b.size / 2.) {
                balloons.remove(i); // Remove balloons off-screen
            }
        }

        // Update expanding circles
        for (int i = circles.size() - 1; i >= 0; i--) {
            ExpandingCircle c = circles.get(i);
            double elapsed = (getTime() - c.startTime) / 1000.;
            c.radius = c.startRadius + (elapsed / 0.2) * (c.startRadius * 2); // Expand to 2x radius
            if (elapsed >= 0.2) {
                circles.remove(i); // Remove after animation
            }
        }
    }

    @Override
    public void paintComponent() {
        changeBackgroundColor(Color.BLACK);
        clearBackground(width(), height());

        if (!gameOver) {
            // Draw balloons
            for (Balloon b : balloons) {
                saveCurrentTransform();
                if (!b.isPopping) {
                    drawImage(b.image, b.x - b.size / 2., b.y - b.size / 2., b.size, b.size);
                } else {
                    int frame = Math.min(b.popFrame, 4); // Cap at 5 frames
                    drawImage(b.popImages[frame], b.x - b.size / 2., b.y - b.size / 2., b.size, b.size);
                }
                restoreLastTransform();
            }

            // Draw expanding circles
            for (ExpandingCircle c : circles) {
                saveCurrentTransform();
                changeColor(Color.WHITE);
                drawCircle(c.x, c.y, c.radius);
                restoreLastTransform();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        double clickX = event.getX();
        double clickY = event.getY();
        Set<Balloon> toPop = new HashSet<>();

        // Detect clicked balloons
        for (Balloon b : balloons) {
            if (!b.isPopping && distance(clickX, clickY, b.x, b.y) < b.size / 2.) {
                toPop.add(b);
            }
        }

        // Find all touching balloons recursively
        Set<Balloon> allToPop = new HashSet<>();
        for (Balloon b : toPop) {
            findTouchingBalloons(b, allToPop);
        }

        // Trigger popping
        for (Balloon b : allToPop) {
            b.isPopping = true;
            b.popStartTime = getTime();
            b.popFrame = 0;
            circles.add(new ExpandingCircle(b.x, b.y, b.size / 2., getTime()));
            playAudio(popSound);
        }
    }

    // Recursively find touching balloons
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