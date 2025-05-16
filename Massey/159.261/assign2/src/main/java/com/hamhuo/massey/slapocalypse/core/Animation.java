package com.hamhuo.massey.slapocalypse.core;

import com.hamhuo.massey.slapocalypse.enums.Direction;
import com.hamhuo.massey.slapocalypse.enums.State;

import java.awt.Image;

public class Animation {
    private final GameEngine engine;
    private final Image[][][] frames; // [State][Direction][Frame]

    public Animation(GameEngine engine) {
        this.engine = engine;
        frames = new Image[State.values().length][Direction.values().length][];
    }

    public void loadStateSpritesheet(State state, String path, int framesPerRow) {
        Image sheet = engine.loadImage(path);
        int w = sheet.getWidth(null), h = sheet.getHeight(null);
        int frameW = w / framesPerRow, frameH = h / 4;
        for (int di = 0; di < 4; di++) {
            Image[] arr = new Image[framesPerRow];
            for (int fi = 0; fi < framesPerRow; fi++) {
                arr[fi] = engine.subImage(sheet, fi * frameW, di * frameH, frameW, frameH);
            }
            frames[state.ordinal()][di] = arr;
        }
    }

    public int getFrameCount(State state, Direction dir) {
        Image[] arr = frames[state.ordinal()][dir.ordinal()];
        return arr == null ? 0 : arr.length;
    }

    public Image getFrame(State state, Direction dir, int idx) {
        Image[] arr = frames[state.ordinal()][dir.ordinal()];
        if (arr == null || arr.length == 0) return null;
        return arr[idx % arr.length];
    }
}