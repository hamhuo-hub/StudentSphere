package com.hamhuo.massey.slapocalypse.core;

import java.awt.Image;

public class GameMap {
    private final int[][] grid;
    private final int width, height;
    private final int tileSize;
    private Image wallTexture;
    private Boolean useAlterImage = true;
    private boolean hasSwitchedThisBeat = false;
    private final Image[] frames;

    public GameMap(int width, int height, int windowSize, Image[] frames) {
        this.width = width;
        this.height = height;
        this.tileSize = windowSize / width;
        this.frames = frames;
        this.grid = new int[width][height];
        for (int x = 0; x < width; x++) {
            grid[x][0] = grid[x][height - 1] = 1;
        }
        for (int y = 0; y < height; y++) {
            grid[0][y] = grid[width - 1][y] = 1;
        }
        System.out.println("GameMap initialized: width=" + width + ", height=" + height + ", tileSize=" + tileSize);
    }

    public int getTileSize() {
        return tileSize;
    }

    public void update(double dt, boolean inRhythmWindow) {
        if (inRhythmWindow) {
            if (!hasSwitchedThisBeat) {
                useAlterImage = !useAlterImage;
                hasSwitchedThisBeat = true;
            }
        } else {
            hasSwitchedThisBeat = false;
        }
        wallTexture = useAlterImage ? frames[0] : frames[1];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isFloorTile(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height && grid[x][y] == 0;
    }

    public boolean isValidMove(int x, int y) {
        return x >= 1 && x < width - 1 && y >= 1 && y < height - 1 && isFloorTile(x, y);
    }

    public Image getTileTexture() {
        return wallTexture;
    }
}