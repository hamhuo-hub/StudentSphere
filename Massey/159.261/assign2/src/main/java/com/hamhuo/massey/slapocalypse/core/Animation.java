package com.hamhuo.massey.slapocalypse.core;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Animation {
    // 存储每种状态的动画帧，键是状态，值是Image[][]（方向 × 帧）
    private final Map<State, Image[][]> framesMap;
    // 存储每种状态的帧率
    private final Map<State, Double> frameRates;
    // 当前状态和方向
    private State currentState;
    private Direction currentDirection;
    // 自动画开始以来的累计时间
    private double elapsedTime;
    // 动画是否在播放
    private boolean isPlaying;

    public Animation() {
        framesMap = new HashMap<>();
        frameRates = new HashMap<>();
        elapsedTime = 0;
        isPlaying = false;
    }

    // 设置某状态的动画帧
    public void setFrames(State state, Image[][] frames) {
        framesMap.put(state, frames);
    }

    // 设置某状态的帧率
    public void setFrameRate(State state, double frameRate) {
        frameRates.put(state, frameRate);
    }

    // 设置当前状态，切换时重置动画时间
    public void setState(State state) {
        if (currentState != state) {
            currentState = state;
            elapsedTime = 0;
        }
    }

    // 设置当前方向
    public void setDirection(Direction direction) {
        currentDirection = direction;
    }

    // 更新动画进度，deltaTime是时间增量（单位：秒）
    public void update(double deltaTime) {
        if (isPlaying) {
            elapsedTime += deltaTime;
        }
    }

    // 获取当前帧
    public Image getCurrentFrame() {
        if (currentState == null || currentDirection == null) {
            return null;
        }

        Image[][] frames = framesMap.get(currentState);
        if (frames == null) {
            return null;
        }

        Image[] directionFrames = frames[currentDirection.ordinal()];
        if (directionFrames == null || directionFrames.length == 0) {
            return null;
        }

        double frameRate = frameRates.getOrDefault(currentState, 0.1);
        int frameIndex = (int) (elapsedTime / frameRate) % directionFrames.length;
        return directionFrames[frameIndex];
    }

    // 开始播放动画
    public void play() {
        isPlaying = true;
    }

    // 停止播放动画
    public void stop() {
        isPlaying = false;
    }
}