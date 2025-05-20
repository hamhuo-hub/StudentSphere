package com.hamhuo.massey.slapocalypse.state;

import com.hamhuo.massey.slapocalypse.entity.Entity;

public class AttackState implements State {
    private Entity entity;

    public AttackState(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void entry() {
        entity.audioManager.playSoundEffect("ATTACK");
        System.out.println(entity.getClass().getSimpleName() + " Attacking, direction: " + entity.getDirection());
    }

    @Override
    public void update(boolean inRhythmWindow) {
        // Rollback to Idle if no target (handled in EntityManager)
    }

    @Override
    public void exit() {
        entity.clearAttackPosition();
    }

    @Override
    public String getName() {
        return "ATTACK";
    }
}