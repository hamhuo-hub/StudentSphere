package com.hamhuo.massey.slapocalypse.core;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class GameMap {
    private final int[][] grid;
    private final int width, height;
    private int tileSize;
    private final Image[] floorTextures;
    private final int[][] floorTextureIndices;
    private final Random random;

    public GameMap(int width, int height, int windowSize, Image[] floorTextures, String mapFilePath) throws IOException {
        this.width = width;
        this.height = height;
        this.tileSize = windowSize / 9;
        this.floorTextures = floorTextures;
        this.grid = new int[width][height];
        this.floorTextureIndices = new int[width][height];
        this.random = new Random();

        if (!loadMapFromFile(mapFilePath)) {
            for (int x = 0; x < width; x++) {
                grid[x][0] = grid[x][height - 1] = 1;
            }
            for (int y = 0; y < height; y++) {
                grid[0][y] = grid[width - 1][y] = 1;
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y] == 0) {
                    floorTextureIndices[x][y] = random.nextInt(floorTextures.length);
                } else {
                    floorTextureIndices[x][y] = -1;
                }
            }
        }

        System.out.println("GameMap initialized: width=" + width + ", height=" + height + ", tileSize=" + tileSize);
    }

    private boolean loadMapFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int[][] tempGrid = new int[width][height];
        for (int y = 0; y < height; y++) {
            String line = reader.readLine();
            if (line == null) return false;
            String[] tokens = line.trim().split("\\s+");
            if (tokens.length != width) {
                return false;
            }
            for (int x = 0; x < width; x++) {
                int value = Integer.parseInt(tokens[x]);
                tempGrid[x][y] = value;
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[x][y] = tempGrid[x][y];
            }
        }
        reader.close();
        return true;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    public int getTileSize() {
        return tileSize;
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
        return x >= 0 && x < width && y >= 0 && y < height && grid[x][y] == 0;
    }

    public int getTileType(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[x][y];
        }
        return 1;
    }

    public Image getTileTexture(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height && grid[x][y] == 0) {
            int index = floorTextureIndices[x][y];
            if (index >= 0 && index < floorTextures.length) {
                return floorTextures[index];
            }
        }
        return null;
    }
}