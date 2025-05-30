package com.hamhuo.massey.slapocalypse.entity;

import com.hamhuo.massey.slapocalypse.core.AnimationManager;
import com.hamhuo.massey.slapocalypse.core.AudioManager;
import com.hamhuo.massey.slapocalypse.core.GameRenderer;
import com.hamhuo.massey.slapocalypse.core.GameMap;
import com.hamhuo.massey.slapocalypse.state.*;

import java.awt.event.KeyEvent;

public class Player extends Entity {
    private final GameMap map;
    private final EntityManager entityManager;
    private int pendingDx, pendingDy;
    private Direction pendingDirection;
    private boolean hasPendingMove;
    private int consecutiveOnBeatMoves = 0;
    private int lastOnBeatMove = -1;
    private GameRenderer gameRenderer;

    public Player(int x, int y, String entityType, AnimationManager anim, AudioManager audio, GameMap map, EntityManager entityManager, int priority, GameRenderer gameRenderer) {
        super(x, y, entityType, anim, audio, priority);
        this.map = map;
        this.entityManager = entityManager;
        this.pendingDx = 0;
        this.pendingDy = 0;
        this.pendingDirection = direction;
        this.hasPendingMove = false;
        this.attack = 100;
        this.gameRenderer = gameRenderer;
    }

    @Override
    public void action(boolean inRhythmWindow) {
        // 计算当前节奏编号
        int currentRhythm = (int)(gameRenderer.getSongPos() / gameRenderer.getCrotchet());

        // 如果漏掉了一个节奏，重置计数器
        if (lastOnBeatMove >= 0 && currentRhythm > lastOnBeatMove + 1) {
            consecutiveOnBeatMoves = 0;
        }

        if (inRhythmWindow && !hasActedThisBeat && hasPendingMove && state instanceof IdleState) {
            int newX = x + pendingDx;
            int newY = y + pendingDy;
            // 检查目标位置是否有敌人
            Entity target = findEntityAt(newX, newY);
            if (target != null && !(target.getState() instanceof DeathState)) {
                // 攻击而不是移动
                setDirection(pendingDirection);
                setAttackPosition(newX, newY);
                setState(new AttackState(this));
            } else if (map.isValidMove(newX, newY)) {
                setPositionAndDirection(newX, newY, pendingDirection);
                setState(new RunState(this));
            } else {
                setState(new IdleState(this));
            }
            audioManager.playSoundEffect("PlayerMove");

            // 更新连续移动计数器
            if (currentRhythm == lastOnBeatMove + 1) {
                consecutiveOnBeatMoves++;
            } else {
                consecutiveOnBeatMoves = 1; // 非连续节奏，重置为1
            }
            lastOnBeatMove = currentRhythm;

            // 如果连续移动达到3次，触发音效和烟雾
            if (consecutiveOnBeatMoves >= 3) {
                audioManager.playSoundEffect("Excellent");
                gameRenderer.triggerSmoke();
                gameRenderer.startFlameAnimation();

                consecutiveOnBeatMoves = 0;
                lastOnBeatMove = -1; // 重置节奏编号
            }

            hasActedThisBeat = true;
            markUpdated();
            hasPendingMove = false;
        } else if (!inRhythmWindow) {
            hasActedThisBeat = false;
            resetUpdateFlag();
            if (!(state instanceof IdleState) && !(state instanceof DeathState)) {
                setState(new IdleState(this));
            }
            return;
        }
        state.update(inRhythmWindow);
    }

    @Override
    public void handleInput(KeyEvent e) {
        if (!hasActedThisBeat && state instanceof IdleState) {
            pendingDx = 0;
            pendingDy = 0;
            pendingDirection = direction;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    pendingDy = -1;
                    pendingDirection = Direction.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    pendingDy = 1;
                    pendingDirection = Direction.DOWN;
                    break;
                case KeyEvent.VK_LEFT:
                    pendingDx = -1;
                    pendingDirection = Direction.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    pendingDx = 1;
                    pendingDirection = Direction.RIGHT;
                    break;
                default:
                    return;
            }
            hasPendingMove = true;
        }
    }

    private Entity findEntityAt(int x, int y) {
        for (Enemy enemy : entityManager.getEnemies()) {
            if (enemy.getX() == x && enemy.getY() == y) {
                return enemy;
            }
        }
        return null;
    }
}