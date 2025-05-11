package com.hamhuo.massey.slapocalypse.core;

import com.hamhuo.massey.slapocalypse.role.GameMap;
import com.hamhuo.massey.slapocalypse.role.Enemy;
import com.hamhuo.massey.slapocalypse.entity.enemy.Slime;
import com.hamhuo.massey.slapocalypse.role.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameController extends GameEngine {

    private List<Enemy> enemies; // Changed to List for dynamic removal
    private List<Player> players; // Changed to List for dynamic removal
    private GameMap map;


    public GameController() {
        super();
        setupWindow(800, 600);
    }

    @Override
    public void init() {
        System.out.println(System.getProperty("user.dir"));
        song = loadAudio("./src/main/resources/song.wav");
//        conductor = new Conductor(this, 120, 0.05);
        conductor.setAudioClip(song);
        conductor.startSong();

        map = new GameMap(16, 12);
        player = new Player(this, conductor, 5, 5);
        enemies = new ArrayList<>();
        enemies.add(new Slime(this, conductor, 8, 8));
    }

    @Override
    public void update(double dt) throws InterruptedException {
        conductor.update();
        player.update(map, enemies.toArray(new Slime[0])); // Pass array to player

        // Update enemies and collect dead ones
        List<Slime> toRemove = new ArrayList<>();
        for (Slime slime : enemies) {
            slime.update(player, map, dt); // Fixed: instance method with dt
            if (slime.isDeadFinished()) {
                toRemove.add(slime);
            }
        }
        enemies.removeAll(toRemove); // Remove dead enemies
    }

    // 增强器，用于自动切割图片，放入数组中
    public void loadFrames(Image[][] frameArray, String path, int sheetWidth, int sheetHeight, int framesPerRow) {
        Image sheet = loadImage(path);
        int frameWidth = sheetWidth / framesPerRow;
        int frameHeight = sheetHeight / 4;
        for (int dir = 0; dir < 4; dir++) {
            for (int i = 0; i < framesPerRow; i++) {
                frameArray[dir][i] = subImage(sheet, i * frameWidth, dir * frameHeight, frameWidth, frameHeight);
            }
        }
    }

    @Override
    public void paintComponent() {
        clearBackground(width(), height());
        map.draw(this);
        player.draw();
        for (Slime slime : enemies) {
            slime.drawEnemy();
        }
        // 节奏窗口提示
        if (conductor.isInRhythmWindow(0.1)) {
            changeColor(yellow);
            drawSolidCircle(50, 50, 20);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.handleInput(e);
    }


}