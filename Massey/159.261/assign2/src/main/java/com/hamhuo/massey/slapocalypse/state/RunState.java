package com.hamhuo.massey.slapocalypse.state;

import com.hamhuo.massey.slapocalypse.entity.Entity;

public class RunState implements State {
    private Entity entity;

    public RunState(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void entry() {
        entity.audioManager.playSoundEffect("RUN");
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