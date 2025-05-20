package com.hamhuo.massey.slapocalypse.state;

import com.hamhuo.massey.slapocalypse.entity.Entity;
import com.hamhuo.massey.slapocalypse.entity.Player;
import com.hamhuo.massey.slapocalypse.entity.Enemy;

public class IdleState implements State {
    private Entity entity;

    public IdleState(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void entry() {
        if (entity instanceof Enemy) {
            Entity player = ((Enemy) entity).getPlayer();
            if (player != null) {
                int dx = player.getX() - entity.getX();
                int dy = player.getY() - entity.getY();
                int dist = Math.abs(dx) + Math.abs(dy);
                if (dist == 1) {
                    if (dx == 1) entity.setDirection(Direction.RIGHT);
                    else if (dx == -1) entity.setDirection(Direction.LEFT);
                    else if (dy == 1) entity.setDirection(Direction.DOWN);
                    else if (dy == -1) entity.setDirection(Direction.UP);
                }
            }
        }
        entity.audioManager.playSoundEffect("IDLE");
    }

    @Override
    public void update(boolean inRhythmWindow) {}

    @Override
    public void exit() {}

    @Override
    public String getName() {
        return "IDLE";
    }
}