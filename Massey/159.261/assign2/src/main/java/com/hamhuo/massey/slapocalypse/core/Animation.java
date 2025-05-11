package com.hamhuo.massey.slapocalypse.core;

import java.awt.*;

public class Animation {
    private Image[] frames;
    private double frameTime;    // 每帧持续时间 (秒)
    private double elapsed = 0;
    private int current = 0;

    public Animation(Image[] frames, double frameTime) {
        this.frames = frames;
        this.frameTime = frameTime;
    }

    /** 更新动画，dt 为秒 */
    public void update(double dt) {
        elapsed += dt;
        if (elapsed >= frameTime) {
            int steps = (int)(elapsed / frameTime);
            current = (current + steps) % frames.length;
            elapsed -= steps * frameTime;
        }
    }

    public Image getFrame() {
        return frames[current];
    }

    public boolean isLastFrame() {
        return current == frames.length - 1;
    }

    public void reset() {
        current = 0;
        elapsed = 0;
    }

    // 添加setFrames方法
    public void setFrames(Image[] frames) {
        this.frames = frames;
        reset(); // 设置新帧集时重置动画
    }
}