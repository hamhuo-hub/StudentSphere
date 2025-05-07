package com.hamhuo.massey.demo3;

import java.awt.event.KeyEvent;

public class RhythmRPG extends GameEngine {
    private Conductor conductor;
    private Player player;
    private Enemy[] enemies;
    private Map map;
    private AudioClip song;

    public RhythmRPG() {
        super();
        setupWindow(800, 600);
    }

    @Override
    public void init() {
        song = loadAudio("Assign2/src/main/resources/song.wav");
        conductor = new Conductor(this, 120, 0.05);
        conductor.setAudioClip(song);
        conductor.startSong();

        map = new Map(16, 12);
        player = new Player(this, conductor, 5, 5);
        enemies = new Enemy[] {
                new Enemy(this, conductor, 8, 8)
        };
    }

    @Override
    public void update(double dt) throws InterruptedException {
        conductor.update();
        player.update(map, enemies);
        for (Enemy enemy : enemies) {
            enemy.update(player, map);
        }
    }

    @Override
    public void paintComponent() {
        clearBackground(width(), height());
        map.draw(this);
        player.draw();
        for (Enemy enemy : enemies) {
            enemy.draw();
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

    public static void main(String[] args) {
        RhythmRPG game = new RhythmRPG();
        createGame(game, 60);
    }
}