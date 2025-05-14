package com.hamhuo.massey.stage1;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Stage 1 (10 marks)
 * - The game window is 500x500 pixels with a black background.
 * - Balloons have a random size of between 20x20 and 40x40 pixels.
 * - Use provided sprite sheets for red and green balloons.
 * - 50% chance for red or green balloons.
 * - Balloons start with center at random x in [size/2, width - size/2].
 * - Balloons start with center at y = -size/2 (off top of screen).
 * - Speed = (time since start in seconds)/10.0 + 1.0.
 * - Balloons have constant random y velocity in [0, 100] + speed*50 pixels/second.
 * - Time between balloons = (random double in [0, 2])/speed seconds.
 * fixme: author: HUO ZIHANG DO NOT COPY
 */
class Balloon {
    public double x, y;
    public int size;
    public double velocityY;
    public Image image;

    public Balloon(double x, double y, int size, double velocityY, Image image) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.velocityY = velocityY;
        this.image = image;
    }
}

public class Stage1 extends GameEngine {
    Boolean gameOver = false;
    List<Balloon> balloons = new ArrayList<>();
    double startTime = getTime();
    double speed = 1.0;
    double lastSpawnTime = getTime();
    double spawnInterval = 0;
    Image[] balloonImages = new Image[2];

    @Override
    public void init() {
        balloonImages[0] = loadImage("./labtest/src/resources/greenballoon/1.png");
        balloonImages[1] = loadImage("./labtest/src/resources/redballoon/1.png");
        spawnInterval = rand(2.0) / speed;
    }

    public static void main(String[] args) {
        GameEngine.createGame(new Stage1());
    }

    @Override
    public void update(double dt) {

        speed = ((getTime() - startTime) / 1000) / 10 + 1.;


        if ((getTime() - lastSpawnTime) / 1000 >= spawnInterval) {
            int size = rand(2) == 0 ? 20 : 40;
            double x = rand(width() - size) + size / 2.;
            double y = -(size / 2.); // 中心 y 坐标
            double velocityY = rand(101) + (speed * 50);
            Image image = balloonImages[rand(2)];

            balloons.add(new Balloon(x, y, size, velocityY, image));
            lastSpawnTime = getTime();
            spawnInterval = rand(2.) / speed;
        }

        // 更新气球位置并移除超出屏幕的气球
        for (int i = balloons.size() - 1; i >= 0; i--) {
            Balloon b = balloons.get(i);
            b.y += b.velocityY * dt;
            if (b.y > height() + b.size / 2.) {
                balloons.remove(i);
            }
        }
    }

    @Override
    public void paintComponent() {
        changeBackgroundColor(Color.BLACK);
        clearBackground(width(), height());

        if (!gameOver) {
            for (Balloon b : balloons) {
                saveCurrentTransform();
                drawImage(b.image, b.x - b.size / 2., b.y - b.size / 2., b.size, b.size);
                restoreLastTransform();
            }
        }
    }
}