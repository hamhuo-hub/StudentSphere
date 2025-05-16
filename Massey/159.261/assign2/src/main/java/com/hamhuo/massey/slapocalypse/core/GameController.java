package com.hamhuo.massey.slapocalypse.core;

import com.hamhuo.massey.slapocalypse.entity.GameMap;
import com.hamhuo.massey.slapocalypse.entity.Hero;
import com.hamhuo.massey.slapocalypse.entity.enemy.BlueSlime;
import com.hamhuo.massey.slapocalypse.entity.enemy.PinkSlime;
import com.hamhuo.massey.slapocalypse.entity.enemy.YellowSlime;
import com.hamhuo.massey.slapocalypse.enums.State;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 游戏控制器：负责初始化、更新和渲染整个游戏场景。
 */
public class GameController extends GameEngine {
    private GameMap map;
    private Image floorTile1;
    private Image floorTile2;
    private boolean useAlternateFloor = false;

    // Rhythm-related fields
    private double bpm = 120;
    private double crotchet = 60.0 / bpm;
    private double offset = 0;
    private long startTime;
    private double songPos = 0;
    private boolean isPlaying = false;
    private AudioClip clip;

    public GameController() {
        super();
        setupWindow(900, 900); // Adjusted to 8x8 grid with 32x32 tiles
        floorTile1 = loadImage("src/main/resources/map1.png");
        floorTile2 = loadImage("src/main/resources/map2.png");

        // Initialize map with 8x8 grid
        map = new GameMap(12, 12, floorTile1);
        Hero player = new Hero();
        Animation heroAnim = new Animation(this);
        heroAnim.loadStateSpritesheet(State.IDLE, "src/main/resources/player1/Idle/Vampires3_Idle_full.png", 4);
        heroAnim.loadStateSpritesheet(State.RUN, "src/main/resources/player1/Run/Vampires3_Run_full.png", 8);
        player.setAnimation(heroAnim);
        player.setPosition(0, 0);
        map.setPlayer(player);

        // BlueSlime
        Animation blueSlimeAnim = new Animation(this);
        blueSlimeAnim.loadStateSpritesheet(State.RUN, "src/main/resources/Slime1/Run/Slime1_Run_full.png", 8);
        blueSlimeAnim.loadStateSpritesheet(State.IDLE, "src/main/resources/Slime1/Idle/Slime1_Idle_full.png", 6);
        blueSlimeAnim.loadStateSpritesheet(State.HURT, "src/main/resources/Slime1/Hurt/Slime1_Hurt_full.png", 5);
        blueSlimeAnim.loadStateSpritesheet(State.DEATH, "src/main/resources/Slime1/Death/Slime1_Death_full.png", 10);
        blueSlimeAnim.loadStateSpritesheet(State.ATTACK, "src/main/resources/Slime1/Attack/Slime1_Attack_full.png", 10);
        BlueSlime blueSlime = new BlueSlime(7 * 32, 7 * 32, blueSlimeAnim, player, map);
        map.addEnemy(blueSlime);

        // YellowSlime
        Animation yellowSlimeAnim = new Animation(this);
        yellowSlimeAnim.loadStateSpritesheet(State.RUN, "src/main/resources/Slime2/Run/Slime2_Run_full.png", 8);
        yellowSlimeAnim.loadStateSpritesheet(State.IDLE, "src/main/resources/Slime2/Idle/Slime2_Idle_full.png", 6);
        yellowSlimeAnim.loadStateSpritesheet(State.HURT, "src/main/resources/Slime2/Hurt/Slime2_Hurt_full.png", 5);
        yellowSlimeAnim.loadStateSpritesheet(State.DEATH, "src/main/resources/Slime2/Death/Slime2_Death_full.png", 10);
        yellowSlimeAnim.loadStateSpritesheet(State.ATTACK, "src/main/resources/Slime2/Attack/Slime2_Attack_full.png", 10);
        YellowSlime yellowSlime = new YellowSlime(1 * 32, 1 * 32, yellowSlimeAnim, player, map);
        map.addEnemy(yellowSlime);

        // PinkSlime
        Animation pinkSlimeAnim = new Animation(this);
        pinkSlimeAnim.loadStateSpritesheet(State.RUN, "src/main/resources/Slime3/Run/Slime3_Run_full.png", 8);
        pinkSlimeAnim.loadStateSpritesheet(State.IDLE, "src/main/resources/Slime3/Idle/Slime3_Idle_full.png", 6);
        pinkSlimeAnim.loadStateSpritesheet(State.HURT, "src/main/resources/Slime3/Hurt/Slime3_Hurt_full.png", 5);
        pinkSlimeAnim.loadStateSpritesheet(State.DEATH, "src/main/resources/Slime3/Death/Slime3_Death_full.png", 10);
        pinkSlimeAnim.loadStateSpritesheet(State.ATTACK, "src/main/resources/Slime3/Attack/Slime3_Attack_full.png", 10);
        PinkSlime pinkSlime = new PinkSlime(6 * 32, 6 * 32, pinkSlimeAnim, player, map);
        map.addEnemy(pinkSlime);
    }

    @Override
    public void init() {
        clip = loadAudio("src/main/resources/song.wav");
        startSong();
    }

    private void startSong() {
        if (clip != null) {
            startAudioLoop(clip);
            startTime = getTime();
            isPlaying = true;
        }
    }

    private void updateRhythm() {
        if (isPlaying) {
            long now = getTime();
            songPos = (now - startTime) / 1000.0 - offset;
        }
    }

    private boolean isInRhythmWindow(double tol) {
        double t = songPos % crotchet;
        return t < tol || t > crotchet - tol;
    }

    @Override
    public void update(double dt) throws InterruptedException {
        updateRhythm();
        if (isInRhythmWindow(0.1)) {
            useAlternateFloor = !useAlternateFloor; // Switch floor texture every beat
        }
        map.update(dt, isInRhythmWindow(0.1));
    }

    @Override
    public void paintComponent() {
        clearBackground(width(), height());
        render(mGraphics);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0); // Exit game on ESC
        }
        map.getPlayer().handleInput(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void render(Graphics2D g) {
        int cols = map.getWidth();
        int rows = map.getHeight();

        // Draw floor with alternating textures
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Image tex = map.isFloorTile(x, y) ? (useAlternateFloor ? floorTile2 : floorTile1) : map.getTileTexture(x, y);
                drawImage(tex, 0, 0, width(), height()); // Draw each tile
            }
        }

        // Draw entities
        map.render(g);
    }
}