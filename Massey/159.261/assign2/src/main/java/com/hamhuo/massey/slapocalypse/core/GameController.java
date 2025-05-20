package com.hamhuo.massey.slapocalypse.core;

import com.hamhuo.massey.slapocalypse.entity.EntityManager;
import com.hamhuo.massey.slapocalypse.entity.Player;
import com.hamhuo.massey.slapocalypse.entity.Enemy;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameController extends GameEngine {
    private EntityManager entityManager;
    private AudioManager audioManager;
    private AnimationManager animationManager;
    private GameMap map;
    private Image floorTile1, floorTile2;
    private double bpm = 120;
    private double crotchet = 60.0 / bpm;
    private double offset = 0;
    private long startTime;
    private double songPos = 0;
    private boolean inRhythmWindow = false;

    public GameController() {
        super();
        int windowSize = 900;
        setupWindow(windowSize, windowSize);
        Insets insets = mFrame.getInsets();
        int drawableWidth = windowSize - insets.left - insets.right;

        // Load map textures with error handling
        floorTile1 = loadImage("src/main/resources/map1.png");
        floorTile2 = loadImage("src/main/resources/map2.png");
        if (floorTile1 == null || floorTile2 == null) {
            floorTile1 = new java.awt.image.BufferedImage(100, 100, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            floorTile2 = floorTile1;
            Graphics2D g = (Graphics2D) floorTile1.getGraphics();
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, 100, 100);
            g.dispose();
        }
        Image[] frames = {floorTile1, floorTile2};
        map = new GameMap(9, 9, drawableWidth, frames);

        audioManager = new AudioManager(this);
        animationManager = new AnimationManager(this);
        loadResources();

        entityManager = new EntityManager(map, audioManager);
        initializeEntities();
    }

    private void loadResources() {
        // Load background music
        audioManager.loadBackgroundMusic("src/main/resources/song.wav");
        // Load sound effects
        audioManager.loadSoundEffect("IDLE", "src/main/resources/sounds/idle.wav");
        audioManager.loadSoundEffect("RUN", "src/main/resources/sounds/move.wav");
        audioManager.loadSoundEffect("ATTACK", "src/main/resources/sounds/attack.wav");
        audioManager.loadSoundEffect("HURT", "src/main/resources/sounds/hurt.wav");
        audioManager.loadSoundEffect("DEATH", "src/main/resources/sounds/death.wav");

        // Load player animations
        animationManager.loadSpritesheet("Player", "IDLE", "src/main/resources/player1/Idle/Vampires3_Idle_full.png", 4);
        animationManager.loadSpritesheet("Player", "RUN", "src/main/resources/player1/Run/Vampires3_Run_full.png", 8);
        animationManager.loadSpritesheet("Player", "ATTACK", "src/main/resources/player1/Attack/Vampires3_Attack_full.png", 12);
        animationManager.loadSpritesheet("Player", "HURT", "src/main/resources/player1/Hurt/Vampires3_Hurt_full.png", 4);
        animationManager.loadSpritesheet("Player", "DEATH", "src/main/resources/player1/Death/Vampires3_Death_full.png", 10);

        // Load enemy animations (BlueSlime, YellowSlime, PinkSlime)
        String[] slimeTypes = {"Slime1", "Slime2", "Slime3"};
        for (String type : slimeTypes) {
            animationManager.loadSpritesheet(type, "IDLE", "src/main/resources/" + type + "/Idle/" + type + "_Idle_full.png", 6);
            animationManager.loadSpritesheet(type, "RUN", "src/main/resources/" + type + "/Run/" + type + "_Run_full.png", 8);
            if (type == "Slime1") {
                animationManager.loadSpritesheet(type, "ATTACK", "src/main/resources/" + type + "/Attack/" + type + "_Attack_full.png", 10);
            }
            if (type == "Slime2") {
                animationManager.loadSpritesheet(type, "ATTACK", "src/main/resources/" + type + "/Attack/" + type + "_Attack_full.png", 11);
            }
            if (type == "Slime3") {
                animationManager.loadSpritesheet(type, "ATTACK", "src/main/resources/" + type + "/Attack/" + type + "_Attack_full.png", 9);
            }
            animationManager.loadSpritesheet(type, "HURT", "src/main/resources/" + type + "/Hurt/" + type + "_Hurt_full.png", 5);
            animationManager.loadSpritesheet(type, "DEATH", "src/main/resources/" + type + "/Death/" + type + "_Death_full.png", 10);
        }
    }

    private void initializeEntities() {
        // Player: highest priority (100)
        Player player = new Player(1, 1, "Player", animationManager, audioManager, map, entityManager, 100);
        entityManager.setPlayer(player);

        // BlueSlime: A* pathfinding, move every beat, priority 3
        Enemy blueSlime = new Enemy(7, 7, "Slime1", animationManager, audioManager, map, player, new PathfindingStrategy.AStarPathfinding(), 2, 3);
        // YellowSlime: BFS pathfinding, move every 2 beats, priority 2, spawn at (2,2)
        Enemy yellowSlime = new Enemy(2, 2, "Slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 2, 2);
        // PinkSlime: Greedy Best-First pathfinding, move every 3 beats, priority 1
        Enemy pinkSlime = new Enemy(6, 6, "Slime3", animationManager, audioManager, map, player, new PathfindingStrategy.GreedyBestFirstPathfinding(), 3, 1);
        entityManager.addEnemy(blueSlime);
        entityManager.addEnemy(yellowSlime);
        entityManager.addEnemy(pinkSlime);
    }

    @Override
    public void init() {
        audioManager.playBackgroundMusic();
        startTime = getTime();
    }

    @Override
    public void update(double dt) throws InterruptedException {
        updateRhythm();
        map.update(dt, inRhythmWindow); // 调用 GameMap 的 update 方法
        audioManager.updateSongPosition(songPos);
        entityManager.update(dt, inRhythmWindow);
    }

    private void updateRhythm() {
        long now = getTime();
        songPos = (now - startTime) / 1000.0 - offset;
        double t = songPos % crotchet;
        double tol = 0.15;
        inRhythmWindow = t < tol || t > crotchet - tol;
    }

    @Override
    public void paintComponent() {
        clearBackground(width(), height());
        render(mGraphics);
    }

    private void render(Graphics2D g) {
        g.setTransform(new java.awt.geom.AffineTransform());
        Image background = map.getTileTexture();
        int tileSize = map.getTileSize();
        // Ensure background is not null
        if (background == null) {
            System.err.println("Empty map");
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, width(), height());
        } else {
            // Tile the map texture across the 9x9 grid
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    if (map.isFloorTile(x, y)) {
                        g.drawImage(background, x * tileSize, y * tileSize, tileSize, tileSize, null);
                    }
                }
            }
        }
        entityManager.render(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        entityManager.handleInput(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}