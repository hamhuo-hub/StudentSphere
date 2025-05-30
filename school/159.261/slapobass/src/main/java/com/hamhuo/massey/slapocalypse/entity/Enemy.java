package com.hamhuo.massey.slapocalypse.entity;

import com.hamhuo.massey.slapocalypse.core.AnimationManager;
import com.hamhuo.massey.slapocalypse.core.AudioManager;
import com.hamhuo.massey.slapocalypse.core.GameMap;
import com.hamhuo.massey.slapocalypse.core.PathfindingStrategy;
import com.hamhuo.massey.slapocalypse.state.*;

import java.awt.event.KeyEvent;

public class Enemy extends Entity {
    private final Entity player;
    private final GameMap map;
    private final PathfindingStrategy strategy;
    private final int moveBeatInterval;
    private int beatCount = 0;


    public Enemy(int x, int y, String entityType, AnimationManager anim, AudioManager audio, GameMap map, Entity player, PathfindingStrategy strategy, int moveBeatInterval, int priority) {
        super(x, y, entityType, anim, audio, priority);
        this.map = map;
        this.player = player;
        this.strategy = strategy;
        this.moveBeatInterval = moveBeatInterval;
    }

    @Override
    public void action(boolean inRhythmWindow) {
        if (inRhythmWindow && !hasActedThisBeat) {
            beatCount++;
            if (beatCount >= moveBeatInterval && !(state instanceof DeathState)) {
                int dx = player.getX() - x, dy = player.getY() - y;
                int dist = Math.abs(dx) + Math.abs(dy);
                State newState;
                if (dist == 1) {
                    Direction toPlayer = getDirectionToPlayer(dx, dy);
                    setDirection(toPlayer);
                    setAttackPosition(player.getX(), player.getY());
                    newState = new AttackState(this);
                } else {
                    PathfindingStrategy.PathNode next = strategy.findNextStep(x, y, player.getX(), player.getY(), map);
                    if (next != null) {
                        Direction newDirection = getDirectionToNext(next.x, next.y);
                        setPositionAndDirection(next.x, next.y, newDirection);
                        newState = new RunState(this);
                    } else {
                        newState = new IdleState(this);
                    }
                }
                setState(newState);
                beatCount = 0;
                markUpdated();
            }
            hasActedThisBeat = true;
        } else if (!inRhythmWindow) {
            hasActedThisBeat = false;
            resetUpdateFlag();
            if (!(state instanceof IdleState) && !(state instanceof DeathState)) {
                setState(new IdleState(this));
            }
        }
        state.update(inRhythmWindow);
    }

    @Override
    public void handleInput(KeyEvent e) {
        // Enemies do not handle keyboard input
    }

    private Direction getDirectionToPlayer(int dx, int dy) {
        if (dx == 1) return Direction.RIGHT;
        if (dx == -1) return Direction.LEFT;
        if (dy == 1) return Direction.DOWN;
        return Direction.UP;
    }

    private Direction getDirectionToNext(int nx, int ny) {
        if (nx > x) return Direction.RIGHT;
        if (nx < x) return Direction.LEFT;
        if (ny > y) return Direction.DOWN;
        return Direction.UP;
    }

    public Entity getPlayer() {
        return player;
    }
}