package com.hamhuo.massey.slapocalypse.core;

import com.hamhuo.massey.slapocalypse.state.Direction;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

public class AnimationManager {
    private GameEngine engine;
    private Map<String, Map<String, Image[][]>> animations; // [EntityType][State][Direction][Frame]

    public AnimationManager(GameEngine engine) {
        this.engine = engine;
        this.animations = new HashMap<>();
    }

    public void loadSpritesheet(String entityType, String stateName, String path, int framesPerRow) {
        Image sheet = engine.loadImage(path);
        int w = sheet.getWidth(null), h = sheet.getHeight(null);
        int frameW = w / framesPerRow, frameH = h / 4;
        Image[][] stateFrames = new Image[Direction.values().length][];
        for (int di = 0; di < 4; di++) {
            Image[] arr = new Image[framesPerRow];
            for (int fi = 0; fi < framesPerRow; fi++) {
                arr[fi] = engine.subImage(sheet, fi * frameW, di * frameH, frameW, frameH);
            }
            stateFrames[di] = arr;
        }
        animations.computeIfAbsent(entityType, k -> new HashMap<>()).put(stateName, stateFrames);
    }

    public int getFrameCount(String entityType, String stateName, Direction dir) {
        Map<String, Image[][]> entityAnims = animations.get(entityType);
        if (entityAnims == null) return 0;
        Image[][] stateFrames = entityAnims.get(stateName);
        if (stateFrames == null) return 0;
        Image[] arr = stateFrames[dir.ordinal()];
        return arr == null ? 0 : arr.length;
    }

    public Image getFrame(String entityType, String stateName, Direction dir, int idx) {
        Map<String, Image[][]> entityAnims = animations.get(entityType);
        if (entityAnims == null) return null;
        Image[][] stateFrames = entityAnims.get(stateName);
        if (stateFrames == null) return null;
        Image[] arr = stateFrames[dir.ordinal()];
        if (arr == null || arr.length == 0) return null;
        return arr[idx % arr.length];
    }
}