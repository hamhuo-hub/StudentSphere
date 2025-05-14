package com.hamhuo.massey.slapocalypse.role;

import java.awt.event.KeyEvent;

public abstract class Hero extends Role {
    protected int pendingMoveX, pendingMoveY; // 缓存的移动输入
    private boolean hasPendingAction; // 是否有待执行动作

    Hero() {
        hasPendingAction = false;
    }

    public void handleInput(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pendingMoveX = 0;
        pendingMoveY = 0;
        if (keyCode == KeyEvent.VK_W) {
            pendingMoveY = -1;
        } else if (keyCode == KeyEvent.VK_S) {
            pendingMoveY = 1;
        } else if (keyCode == KeyEvent.VK_A) {
            pendingMoveX = -1;
        } else if (keyCode == KeyEvent.VK_D) {
            pendingMoveX = 1;
        }
        hasPendingAction = pendingMoveX != 0 || pendingMoveY != 0;
    }

    public void update(GameMap map, Enemy[] enemies) {
        if (hasPendingAction && conductor.isInRhythmWindow(0.1)) {
            int newX = x + pendingMoveX;
            int newY = y + pendingMoveY;
            // 检查是否可以移动
            if (map.isValidMove(newX, newY)) {
                // 检查是否撞到敌人
                for (Enemy enemy : enemies) {
                    if (enemy.getX() == newX && enemy.getY() == newY) {
                        enemy.takeDamage(attack); // 攻击敌人
                        hasPendingAction = false;
                        return;
                    }
                }
                x = newX;
                y = newY;
            }
            hasPendingAction = false;
        }
    }

    public void draw() {
        gameEngine.changeColor(gameEngine.blue);
        gameEngine.drawSolidRectangle(x * 50, y * 50, 50, 50);
        gameEngine.changeColor(gameEngine.white);
        gameEngine.drawText(x * 50, y * 50 + 30, "HP: " + hp);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void takeDamage(int damage) {
        hp -= damage;
    }
}