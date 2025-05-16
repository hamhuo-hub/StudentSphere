package com.hamhuo.massey.slapocalypse.entity;

import com.hamhuo.massey.slapocalypse.enums.Direction;
import com.hamhuo.massey.slapocalypse.enums.State;

import java.awt.event.KeyEvent;

public class Hero extends Entity {
    private boolean hasActedThisBeat = false;

    @Override
    public void action(boolean inRhythmWindow) {
        if (!inRhythmWindow) {
            hasActedThisBeat = false;
            state = State.IDLE;
        }
    }

    public void handleInput(KeyEvent e) {
        if (!hasActedThisBeat) {
            int dx = 0, dy = 0;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    dy = -32;
                    direction = Direction.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    dy = 32;
                    direction = Direction.DOWN;
                    break;
                case KeyEvent.VK_LEFT:
                    dx = -32;
                    direction = Direction.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    dx = 32;
                    direction = Direction.RIGHT;
                    break;
                default:
                    return;
            }
            x += dx;
            y += dy;
            state = State.RUN;
            hasActedThisBeat = true;
        }
    }
}