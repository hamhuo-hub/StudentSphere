package com.hamhuo.massey.slapocalypse;

public class Enemy {
    private int x, y; // 网格坐标
    private int hp;   // 生命值
    private int attack; // 攻击力
    private double lastBeat; // 上次行动的节拍
    private GameEngine gameEngine;
    private Conductor conductor;

    public Enemy(GameEngine gameEngine, Conductor conductor, int x, int y) {
        this.gameEngine = gameEngine;
        this.conductor = conductor;
        this.x = x;
        this.y = y;
        this.hp = 50;
        this.attack = 5;
        this.lastBeat = 0;
    }

    public void update(Player player, Map map) {
        double songPosition = conductor.getSongPosition();
        if (songPosition > lastBeat + conductor.getCrotchet() && conductor.isInRhythmWindow(0.1)) {
            // 简单 AI：向玩家移动
            int dx = player.getX() - x;
            int dy = player.getY() - y;
            int newX = x, newY = y;
            if (Math.abs(dx) > Math.abs(dy)) {
                newX += dx > 0 ? 1 : -1;
            } else {
                newY += dy > 0 ? 1 : -1;
            }
            if (map.isValidMove(newX, newY)) {
                x = newX;
                y = newY;
            }
            // 如果靠近玩家，攻击
            if (Math.abs(x - player.getX()) + Math.abs(y - player.getY()) <= 1) {
                player.takeDamage(attack);
            }
            lastBeat += conductor.getCrotchet();
        }
    }

    public void draw() {
        gameEngine.changeColor(gameEngine.red);
        gameEngine.drawSolidRectangle(x * 50, y * 50, 50, 50);
        gameEngine.changeColor(gameEngine.white);
        gameEngine.drawText(x * 50, y * 50 + 30, "HP: " + hp);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void takeDamage(int damage) { hp -= damage; }
}