package com.hamhuo.massey.slapocalypse.entity;

import com.hamhuo.massey.slapocalypse.core.AnimationManager;
import com.hamhuo.massey.slapocalypse.core.AudioManager;
import com.hamhuo.massey.slapocalypse.core.GameMap;
import com.hamhuo.massey.slapocalypse.state.*;

import java.awt.event.KeyEvent;

public class Player extends Entity {
    private final GameMap map;
    private final EntityManager entityManager;
    private int pendingDx, pendingDy;
    private Direction pendingDirection;
    private boolean hasPendingMove;


    public Player(int x, int y, String entityType, AnimationManager anim, AudioManager audio, GameMap map, EntityManager entityManager, int priority) {
        super(x, y, entityType, anim, audio, priority);
        this.map = map;
        this.entityManager = entityManager;
        this.pendingDx = 0;
        this.pendingDy = 0;
        this.pendingDirection = direction;
        this.hasPendingMove = false;
        this.attack = 100;
    }

    @Override
    public void action(boolean inRhythmWindow) {
        if (!inRhythmWindow) {
            hasActedThisBeat = false;
            resetUpdateFlag();
            if (!(state instanceof IdleState) && !(state instanceof DeathState)) {
                setState(new IdleState(this));
            }
            return;
        }

        if (inRhythmWindow && !hasActedThisBeat && hasPendingMove && state instanceof IdleState) {
            int newX = x + pendingDx;
            int newY = y + pendingDy;
            // Check if target tile has an enemy
            Entity target = findEntityAt(newX, newY);
            if (target != null && !(target.getState() instanceof DeathState)) {
                // Attack instead of moving
                setDirection(pendingDirection);
                setAttackPosition(newX, newY);
                setState(new AttackState(this));
            } else if (map.isValidMove(newX, newY)) {
                setPositionAndDirection(newX, newY, pendingDirection);
                setState(new RunState(this));
            } else {
                setState(new IdleState(this));
            }
            hasActedThisBeat = true;
            markUpdated();
            hasPendingMove = false;
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