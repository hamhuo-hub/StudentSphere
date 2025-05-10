package com.hamhuo.massey.slapocalypse;

public class Map {
    private int[][] grid; // 0: 可通行, 1: 障碍
    private int width, height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new int[width][height];
        // 示例：设置边界为障碍
        for (int i = 0; i < width; i++) {
            grid[i][0] = 1;
            grid[i][height - 1] = 1;
        }
        for (int j = 0; j < height; j++) {
            grid[0][j] = 1;
            grid[width - 1][j] = 1;
        }
    }

    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height && grid[x][y] == 0;
    }

    public void draw(GameEngine gameEngine) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y] == 1) {
                    gameEngine.changeColor(gameEngine.black);
                    gameEngine.drawSolidRectangle(x * 50, y * 50, 50, 50);
                }
            }
        }
    }
}