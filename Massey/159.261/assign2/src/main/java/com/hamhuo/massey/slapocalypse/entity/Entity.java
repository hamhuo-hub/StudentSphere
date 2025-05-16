package com.hamhuo.massey.slapocalypse.entity;

import com.hamhuo.massey.slapocalypse.core.Animation;
import com.hamhuo.massey.slapocalypse.enums.Direction;
import com.hamhuo.massey.slapocalypse.enums.State;

import java.awt.Image;
import java.awt.event.KeyEvent;

public abstract class Entity {
    protected int x, y;
    protected int hp = 100;
    protected int attack = 10;
    protected State state = State.IDLE;
    protected Direction direction = Direction.DOWN;
    protected Animation animation;
    protected int frameIndex = 0;
    protected double animTimer = 0;

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setAnimation(Animation anim) {
        this.animation = anim;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Image getCurrentFrame() {
        return animation.getFrame(state, direction, frameIndex);
    }

    public void updateAnimation(double dt) {
        double dur;
        switch (state) {
            case RUN:    dur = 0.055; break;
            case ATTACK: dur = 0.05; break;
            default:     dur = 0.15; break;
        }
        animTimer += dt;
        int count = animation.getFrameCount(state, direction);
        if (count > 0 && animTimer >= dur) {
            animTimer -= dur;
            frameIndex = (frameIndex + 1) % count;
        }
    }

    public abstract void action(boolean inRhythmWindow);

    public abstract void handleInput(KeyEvent e);
}