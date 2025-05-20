package com.hamhuo.massey.slapocalypse.state;

import com.hamhuo.massey.slapocalypse.entity.Entity;

public class HurtState implements State {
    private Entity entity;

    public HurtState(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void entry() {
        System.out.println(entity.getClass().getSimpleName() + " Hurt, HP: " + entity.getHP());
    }

    @Override
    public void update(boolean inRhythmWindow) {}

    @Override
    public void exit() {}

    @Override
    public String getName() {
        return "HURT";
    }
}