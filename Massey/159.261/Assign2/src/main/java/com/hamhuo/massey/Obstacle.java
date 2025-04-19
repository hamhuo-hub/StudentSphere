package com.hamhuo.massey;

import java.awt.*;

public class Obstacle {
    private int x, y;
    private int width = 50;
    private int height = 50;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        x -= 3;  // Move left at a slower speed
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);  // Draw obstacle as a red square
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
