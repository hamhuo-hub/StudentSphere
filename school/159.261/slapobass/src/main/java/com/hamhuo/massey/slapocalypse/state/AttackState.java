package com.hamhuo.massey.slapocalypse.state;

import com.hamhuo.massey.slapocalypse.entity.Entity;

public class AttackState implements State {
    private final Entity entity;

    public AttackState(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void entry() {
        if (entity instanceof com.hamhuo.massey.slapocalypse.entity.Player) {
            entity.audioManager.playSoundEffect("PlayerAttack");
        } else {
           entity.audioManager.playSoundEffect("ATTACK");
        }
        System.out.println(entity.getClass().getSimpleName() + " Attacking, direction: " + entity.getDirection());
    }

    @Override
    public void update(boolean inRhythmWindow) {
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