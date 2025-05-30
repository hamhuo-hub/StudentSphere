package com.hamhuo.massey.slapocalypse.state;

import com.hamhuo.massey.slapocalypse.entity.Entity;

public class DeathState implements State {
    private final Entity entity;

    public DeathState(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void entry() {
        if (entity instanceof com.hamhuo.massey.slapocalypse.entity.Player) {
            entity.audioManager.playSoundEffect("PlayDeath");
        } else {
            String key = entity.getEntityType() + "DEATH";
            entity.audioManager.playSoundEffect(key);
        }
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