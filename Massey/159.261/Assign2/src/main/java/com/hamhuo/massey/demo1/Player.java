package com.hamhuo.massey.demo1;

import java.awt.*;

public class Player {
    private int x, y;
    private int speed = 5;
    private boolean isAttacking = false;
    private boolean isSliding = false;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 50, 50);  // Draw player as a square
    }

    public void setAttacking(boolean attacking) {
        this.isAttacking = attacking;
    }

    public void setSliding(boolean sliding) {
        this.isSliding = sliding;
    }

    public void slide(int mouseX, int mouseY) {
        // Update player position based on mouse drag
        this.x = mouseX;
        this.y = mouseY;
    }

    public void dodge() {
        // Dodge action (player moves quickly in a direction)
        this.x += 50;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isSliding() {
        return isSliding;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50);
    }
}
