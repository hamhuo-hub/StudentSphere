package com.hamhuo.massey.slapocalypse.state;

import com.hamhuo.massey.slapocalypse.entity.Entity;

public class DeathState implements State {
    private Entity entity;

    public DeathState(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void entry() {
        entity.audioManager.playSoundEffect("DEATH");
        System.out.println(entity.getClass().getSimpleName() + " Died");
    }

    @Override
    public void update(boolean inRhythmWindow) {}

    @Override
    public void exit() {}

    @Override
    public String getName() {
        return "DEATH";
    }
}