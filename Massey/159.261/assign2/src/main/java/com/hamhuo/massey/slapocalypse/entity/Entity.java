package com.hamhuo.massey.slapocalypse.entity;

import com.hamhuo.massey.slapocalypse.core.AnimationManager;
import com.hamhuo.massey.slapocalypse.core.AudioManager;
import com.hamhuo.massey.slapocalypse.state.*;

import java.awt.Image;
import java.awt.event.KeyEvent;

public abstract class Entity {
    protected int x, y;
    protected int previousX, previousY; // For move rollback
    protected int hp = 100;
    protected int attack = 10;
    protected State state;
    protected Direction direction = Direction.DOWN;
    protected AnimationManager animationManager;
    public AudioManager audioManager;
    protected String entityType;
    protected int frameIndex = 0;
    protected double animTimer = 0;
    protected boolean hasActedThisBeat = false;
    protected boolean hasAttackedThisBeat = false;
    protected boolean updatedThisBeat = false;
    protected int priority;
    protected int attackX, attackY; // Danger zone coordinates

    public Entity(int x, int y, String entityType, AnimationManager anim, AudioManager audio, int priority) {
        this.x = x;
        this.y = y;
        this.previousX = x;
        this.previousY = y;
        this.entityType = entityType;
        this.animationManager = anim;
        this.audioManager = audio;
        this.state = new IdleState(this);
        this.priority = priority;
        this.attackX = -1;
        this.attackY = -1;
    }

    public void setState(State state) {
        this.state.exit();
        this.state = state;
        this.state.entry();
    }

    public State getState() {
        return state;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setPositionAndDirection(int x, int y, Direction direction) {
        this.previousX = this.x;
        this.previousY = this.y;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void revertPosition() {
        this.x = previousX;
        this.y = previousY;
    }

    public int getHP() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public void takeDamage(int damage) {
        hp -= damage;
    }

    public int getPriority() {
        return priority;
    }

    public void setAttackPosition(int x, int y) {
        this.attackX = x;
        this.attackY = y;
    }

    public int getAttackX() {
        return attackX;
    }

    public int getAttackY() {
        return attackY;
    }

    public void clearAttackPosition() {
        this.attackX = -1;
        this.attackY = -1;
    }

    public boolean hasAttackedThisBeat() {
        return hasAttackedThisBeat;
    }

    public void setAttackedThisBeat(boolean attacked) {
        this.hasAttackedThisBeat = attacked;
    }

    public void updateAnimation(double dt) {
        double dur = state instanceof RunState ? 0.055 : (state instanceof AttackState ? 0.05 : 0.15);
        animTimer += dt;
        int count = animationManager.getFrameCount(entityType, state.getName(), direction);
        if (count > 0 && animTimer >= dur) {
            animTimer -= dur;
            frameIndex = (frameIndex + 1) % count;
        }
    }

    public Image getCurrentFrame() {
        return animationManager.getFrame(entityType, state.getName(), direction, frameIndex);
    }

    public abstract void action(boolean inRhythmWindow);

    public abstract void handleInput(KeyEvent e);

    public void markUpdated() {
        updatedThisBeat = true;
    }

    public boolean isUpdatedThisBeat() {
        return updatedThisBeat;
    }

    public void resetUpdateFlag() {
        updatedThisBeat = false;
        hasAttackedThisBeat = false;
    }
}