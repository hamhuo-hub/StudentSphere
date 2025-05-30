package com.hamhuo.massey.slapocalypse.state;

import com.hamhuo.massey.slapocalypse.entity.Entity;

public class RunState implements State {
    private final Entity entity;

    public RunState(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void entry() {
        entity.audioManager.playSoundEffect("RUN");
        System.out.println("Play RUN sound for " + entity.getEntityType());
    }

    @Override
    public void update(boolean inRhythmWindow) {}

    @Override
    public void exit() {}

    @Override
    public String getName() {
        return "RUN";
    }
}