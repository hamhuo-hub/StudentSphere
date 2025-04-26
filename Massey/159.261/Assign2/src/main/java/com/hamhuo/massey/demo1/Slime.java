package com.hamhuo.massey.demo1;

import java.awt.*;

public class Slime {
    private int x, y;
    private int speed = 2;

    public Slime(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        x -= speed;  // Move left
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, 30, 30);  // Draw slime as a square
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 30);
    }
}
