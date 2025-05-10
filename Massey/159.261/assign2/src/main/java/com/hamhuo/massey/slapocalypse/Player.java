package com.hamhuo.massey.slapocalypse;

import java.awt.event.KeyEvent;

public class Player {
    private int x, y; // 网格坐标
    private int hp;   // 生命值
    private int attack; // 攻击力
    private int pendingMoveX, pendingMoveY; // 缓存的移动输入
    private boolean hasPendingAction; // 是否有待执行动作
    private GameEngine gameEngine;
    private Conductor conductor;

    public Player(GameEngine gameEngine, Conductor conductor, int x, int y) {
        this.gameEngine = gameEngine;
        this.conductor = conductor;
        this.x = x;
        this.y = y;
        this.hp = 100;
        this.attack = 10;
        this.pendingMoveX = 0;
        this.pendingMoveY = 0;
        this.hasPendingAction = false;
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
        // todo 需要计数器，冻结移动
    }

    public void update(Map map, Enemy[] enemies) {
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

    public int getX() { return x; }
    public int getY() { return y; }
    public void takeDamage(int damage) { hp -= damage; }
}