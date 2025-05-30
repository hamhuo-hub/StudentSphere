package com.hamhuo.massey.slapocalypse.core;

public class Camera {
    private double x, y; // Camera position in world coordinates
    private final int viewTilesX, viewTilesY; // Number of tiles visible
    private final GameMap map;
    private final int screenWidth, screenHeight;
    private final int tileSize;

    public Camera(GameMap map, int viewTilesX, int viewTilesY, int screenWidth, int screenHeight) {
        this.map = map;
        this.viewTilesX = viewTilesX;
        this.viewTilesY = viewTilesY;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.tileSize = screenWidth / viewTilesX; // Tile size based on screen width
        this.x = 0;
        this.y = 0;
    }

    public void update(int playerX, int playerY) {
        // Center camera on player
        x = playerX * tileSize - (double) (viewTilesX * tileSize) /2;
        y = playerY * tileSize - (double) (viewTilesY * tileSize) /2 + 0.5 * tileSize;

        // Clamp camera to map boundaries
        int maxX = map.getWidth() * tileSize - screenWidth;
        int maxY = map.getHeight() * tileSize - screenHeight;
        x = Math.max(0, Math.min(x, maxX));
        y = Math.max(0, Math.min(y, maxY));
    }

    public int getOffsetX() {
        return (int) -x;
    }

    public int getOffsetY() {
        return (int) -y;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getViewTilesX() {
        return viewTilesX;
    }

    public int getViewTilesY() {
        return viewTilesY;
    }
}