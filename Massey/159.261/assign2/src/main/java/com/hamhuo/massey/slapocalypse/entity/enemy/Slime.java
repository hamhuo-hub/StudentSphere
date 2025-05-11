package com.hamhuo.massey.slapocalypse.entity.enemy;

import com.hamhuo.massey.slapocalypse.core.*;
import com.hamhuo.massey.slapocalypse.role.GameMap;
import com.hamhuo.massey.slapocalypse.role.Player;
import com.hamhuo.massey.slapocalypse.role.Enemy;

import java.awt.*;

public class Slime extends Enemy {
    protected Direction currentDirection;

    // 动画帧集合
    protected Image[][] idleFrames = new Image[4][7];
    protected Image[][] movingFrames = new Image[4][8];
    protected Image[][] attackFrames = new Image[4][10];
    protected Image[][] hurtFrames = new Image[4][5];
    protected Image[][] deathFrames = new Image[4][10];

    public Slime() {
        currentDirection = Direction.STEADY;
        // 初始化帧
        loadFrames(idleFrames, "./src/main/resources/Slime1/Idle/Slime1_Idle_full.png", 384, 256, 7);
        loadFrames(movingFrames, "./src/main/resources/Slime1/Run/Slime1_Run_full.png", 512, 256, 8);
        loadFrames(attackFrames, "./src/main/resources/Slime1/Attack/Slime1_Attack_full.png", 640, 256, 10);
        loadFrames(hurtFrames, "./src/main/resources/Slime1/Hurt/Slime1_Hurt_full.png", 320, 256, 5);
        loadFrames(deathFrames, "./src/main/resources/Slime1/Death/Slime1_Death_full.png", 640, 256, 10);

        // 初始化动画
        anime.put(EnemyState.IDLE, new Animation(idleFrames[currentDirection.ordinal()], 0.1));
        anime.put(EnemyState.MOVING, new Animation(movingFrames[currentDirection.ordinal()], 0.1));
        anime.put(EnemyState.ATTACK, new Animation(attackFrames[currentDirection.ordinal()], 0.1));
        anime.put(EnemyState.HURT, new Animation(hurtFrames[currentDirection.ordinal()], 0.1));
        anime.put(EnemyState.DEAD, new Animation(deathFrames[currentDirection.ordinal()], 0.1));
    }


    protected void setMovingFrames(Image movingSheet) {
        for (int dir = 0; dir < 4; dir++) {
            int yOffset = dir * 64; // Each direction is on a different row (64 pixels per row)
            for (int i = 0; i < 8; i++) {
                movingFrames[dir][i] = engine.subImage(movingSheet, i * 64, yOffset, 64, 64);
            }
        }
    }

    // fixme 这里不需要改写
    @Override
    public void update(Player player, GameMap map, double dt) throws InterruptedException {
        // 动画更新
        Animation a = anime.get(state);
        if (a != null) a.update(dt);

        // 如果节奏到达并且不是在受伤/死亡中
        double songPos = conductor.getSongPosition();
        if (state != EnemyState.HURT && state != EnemyState.DEAD &&
                songPos > lastBeat + conductor.getCrotchet() && conductor.isInRhythmWindow(0.1)) {

            // 简单AI与方向判定
            int dx = player.getX() - x;
            int dy = player.getY() - y;
            int nx = x, ny = y;
            if (Math.abs(dx) > Math.abs(dy)) {
                nx += dx > 0 ? 1 : -1;
                currentDirection = dx > 0 ? Direction.RIGHT : Direction.LEFT;
            } else {
                ny += dy > 0 ? 1 : -1;
                currentDirection = dy > 0 ? Direction.DOWN : Direction.UP;
            }

            if (map.isValidMove(nx, ny)) {
                x = nx; y = ny;
                changeState(EnemyState.MOVING);
                // Update MOVING animation based on current direction
                Animation movingAnim = anime.get(EnemyState.MOVING);
                if (movingAnim != null) {
                    movingAnim = new Animation(movingFrames[currentDirection.ordinal()], 0.1);
                    anime.put(EnemyState.MOVING, movingAnim);
                }
            }
            // 攻击判定
            if (Math.abs(x - player.getX()) + Math.abs(y - player.getY()) <= 1) {
                player.takeDamage(attack);
                changeState(EnemyState.ATTACK);
            }
            lastBeat += conductor.getCrotchet();
        }
    }

    @Override
    public void drawEnemy() {
        Animation a = anime.get(state);
        if (a != null) {
            Image frame = a.getFrame();
            // Draw at tile position (50x50 tiles), scaling from 32x32 to 50x50
            engine.drawImage(frame, x * 50, y * 50, 180, 180);
        }
    }
}