package com.hamhuo.massey.slapocalypse.entity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private final int[][] grid;
    private final int width, height;
    private final Image wallTexture;
    private final List<Entity> enemies = new ArrayList<>();
    private Entity player;

    public GameMap(int width, int height, Image wallTexture) {
        this.width = width;
        this.height = height;
        this.wallTexture = wallTexture;
        this.grid = new int[width][height];
        for (int x = 0; x < width; x++) {
            grid[x][0] = grid[x][height - 1] = 1; // Top and bottom borders
        }
        for (int y = 0; y < height; y++) {
            grid[0][y] = grid[width - 1][y] = 1; // Left and right borders
        }
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public Entity getPlayer() {
        return player;
    }

    public void addEnemy(Entity e) {
        enemies.add(e);
    }

    public void update(double dt, boolean inRhythmWindow) {
        player.action(inRhythmWindow);
        player.updateAnimation(dt);
        for (Entity e : enemies) {
            e.action(inRhythmWindow);
            e.updateAnimation(dt);
        }
    }

    public void render(Graphics2D g) {
        g.drawImage(player.getCurrentFrame(), player.getX(), player.getY(), null);
        for (Entity e : enemies) {
            g.drawImage(e.getCurrentFrame(), e.getX(), e.getY(), null);
        }
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

    public Image getTileTexture(int x, int y) {
        return wallTexture;
    }
}