package com.hamhuo.massey.slapocalypse.state;

import com.hamhuo.massey.slapocalypse.entity.Entity;

public interface State {
    void entry();
    void update(boolean inRhythmWindow);
    void exit();
    String getName();
}