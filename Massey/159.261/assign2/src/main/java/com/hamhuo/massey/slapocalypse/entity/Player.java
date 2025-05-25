package com.hamhuo.massey.slapocalypse.entity;

import com.hamhuo.massey.slapocalypse.core.AnimationManager;
import com.hamhuo.massey.slapocalypse.core.AudioManager;
import com.hamhuo.massey.slapocalypse.core.GameController;
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
    private GameController gameController;

    public Player(int x, int y, String entityType, AnimationManager anim, AudioManager audio, GameMap map, EntityManager entityManager, int priority, GameController gameController) {
        super(x, y, entityType, anim, audio, priority);
        this.map = map;
        this.entityManager = entityManager;
        this.pendingDx = 0;
        this.pendingDy = 0;
        this.pendingDirection = direction;
        this.hasPendingMove = false;
        this.attack = 100;
        this.gameController = gameController;
    }

    @Override
    public void action(boolean inRhythmWindow) {

        int currentRhythm = (int)(gameController.getSongPos() / gameController.getCrotchet());


        if (lastOnBeatMove >= 0 && currentRhythm > lastOnBeatMove + 1) {
            consecutiveOnBeatMoves = 0;
        }

        if (inRhythmWindow && !hasActedThisBeat && hasPendingMove && state instanceof IdleState) {
            int newX = x + pendingDx;
            int newY = y + pendingDy;

            Entity target = findEntityAt(newX, newY);
            if (target != null && !(target.getState() instanceof DeathState)) {

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


            if (currentRhythm == lastOnBeatMove + 1) {
                consecutiveOnBeatMoves++;
            } else {
                consecutiveOnBeatMoves = 1;
            }
            lastOnBeatMove = currentRhythm;


            if (consecutiveOnBeatMoves >= 3) {
                audioManager.playSoundEffect("Excellent");
                gameController.triggerSmoke();
                gameController.startFlameAnimation();

                consecutiveOnBeatMoves = 0;
                lastOnBeatMove = -1;
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