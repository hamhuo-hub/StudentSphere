package com.hamhuo.massey.slapocalypse.core;

import com.hamhuo.massey.slapocalypse.entity.*;
import com.hamhuo.massey.slapocalypse.state.Direction;
import com.hamhuo.massey.slapocalypse.state.GameState;
import com.hamhuo.massey.slapocalypse.state.StateManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameController implements GameState {
    private final StateManager stateManager;
    private final ResourceManager resourceManager;
    private final EntityManager entityManager;
    private final AudioManager audioManager;
    private final AnimationManager animationManager;
    private final GameMap map;
    private final Camera camera;
    private Image[] floorTiles;
    private Image[] obstacleTiles;
    private final double bpm = 86;
    private final double crotchet = 60.0 / bpm;
    private double offset = 0;
    private long startTime;
    private double songPos = 0;
    private boolean inRhythmWindow = false;

    private Image[] heartFrames;
    private Image[] flameFrames;
    private int flameFrameIndex = 0;
    private boolean isFlamePlaying = false;
    private double flameAnimTimer = 0;
    private double flameFrameDuration;
    private Image[] lifeBarFrames;
    private int lifeBarFrameIndex;
    private final int HEART_FRAME_COUNT = 8;
    private int heartFrameIndex = 0;
    private double heartAnimTimer = 0;
    private double heartFrameDuration;
    private boolean heartPlaying = false;

    private final SmokeParticleEmitter leftSmokeEmitter;
    private final SmokeParticleEmitter rightSmokeEmitter;
    private double smokeTimer = 0;
    private boolean smokeActive = false;
    private final int FLAME_FRAME_COUNT = 38;
    private int bw;
    private int bh;

    private Image[] leftSideFlameFrames;
    private Image[] rightSideFlameFrames;
    private final int SIDE_FLAME_FRAME_COUNT = 84;
    private int sideFlameFrameIndex = 0;
    private boolean isSideFlamePlaying = false;
    private double sideFlameAnimTimer = 0;
    private double sideFlameFrameDuration;
    private boolean lowHPAnimationsActive = false;
    private double sideFlameScaleFactor = 4.0;

    public GameController(StateManager stateManager) throws IOException {
        this.stateManager = stateManager;
        this.resourceManager = stateManager.getResourceManager();
        int screenWidth = stateManager.width();
        int screenHeight = stateManager.height();
        bw = screenWidth / 5;
        bh = screenWidth / 16;
        leftSmokeEmitter = new SmokeParticleEmitter();
        leftSmokeEmitter.setPosition(0, screenHeight);
        leftSmokeEmitter.angle = (float)(3 * Math.PI / 4);
        rightSmokeEmitter = new SmokeParticleEmitter();
        rightSmokeEmitter.setPosition(screenWidth - 200, screenHeight);
        rightSmokeEmitter.angle = (float)(5 * Math.PI / 4);


        floorTiles = new Image[5];
        for (int i = 0; i < 5; i++) {
            floorTiles[i] = resourceManager.loadImage(ResourcePaths.getImagePath("map", "map" + (i + 1) + ".png"));
        }


        obstacleTiles = new Image[13];
        for (int i = 0; i < 5; i++) {
            obstacleTiles[i] = resourceManager.loadImage(ResourcePaths.getImagePath("map", "obstacle" + (i + 1) + ".png"));
        }
        obstacleTiles[5] = stateManager.loadRotatedImage(ResourcePaths.getImagePath("map", "obstacle1.png"), 90);
        obstacleTiles[6] = stateManager.loadRotatedImage(ResourcePaths.getImagePath("map", "obstacle4.png"), 180);
        obstacleTiles[7] = stateManager.loadRotatedImage(ResourcePaths.getImagePath("map", "obstacle5.png"), 90);
        obstacleTiles[8] = stateManager.loadRotatedImage(ResourcePaths.getImagePath("map", "obstacle5.png"), 180);
        obstacleTiles[9] = stateManager.loadRotatedImage(ResourcePaths.getImagePath("map", "obstacle5.png"), 270);
        obstacleTiles[10] = stateManager.loadRotatedImage(ResourcePaths.getImagePath("map", "obstacle4.png"), 270);
        obstacleTiles[11] = stateManager.loadRotatedImage(ResourcePaths.getImagePath("map", "obstacle3.png"), 180);
        obstacleTiles[12] = resourceManager.loadImage(ResourcePaths.getImagePath("map", "obstacle6.png"));

        for (int i = 0; i < floorTiles.length; i++) {
            if (floorTiles[i] == null) {
                floorTiles[i] = createFallbackImage();
            }
        }
        for (int i = 0; i < obstacleTiles.length; i++) {
            if (obstacleTiles[i] == null) {
                obstacleTiles[i] = createFallbackImage();
            }
        }

        map = new GameMap(16, 24, screenWidth, floorTiles, ResourcePaths.MAP_PATH);
        camera = new Camera(map, 16, 16, screenWidth, screenHeight);
        map.setTileSize(camera.getTileSize());

        audioManager = new AudioManager(stateManager);
        animationManager = new AnimationManager(stateManager);
        loadResources();

        entityManager = new EntityManager(map, audioManager);
        initializeEntities();
    }

    private Image createFallbackImage() {
        return getImage();
    }

    static Image getImage() {
        BufferedImage fallback = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = fallback.createGraphics();
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, 100, 100);
        g.dispose();
        return fallback;
    }

    private void loadResources() {

        audioManager.loadBackgroundMusic(ResourcePaths.getSoundPath("song.wav"));
        audioManager.loadSoundEffect("ATTACK", ResourcePaths.getSoundPath("SlimeAttack.wav"));
        audioManager.loadSoundEffect("Excellent", ResourcePaths.getSoundPath("excellent.wav"));
        audioManager.loadSoundEffect("PlayerAttack", ResourcePaths.getSoundPath("SlimeAttack.wav"));

        animationManager.loadSpritesheet("Player", "IDLE", ResourcePaths.getImagePath("player", "Vampires3_Idle_full.png"), 4);
        animationManager.loadSpritesheet("Player", "RUN", ResourcePaths.getImagePath("player", "Vampires3_Run_full.png"), 8);
        animationManager.loadSpritesheet("Player", "ATTACK", ResourcePaths.getImagePath("player", "Vampires3_Attack_full.png"), 12);
        animationManager.loadSpritesheet("Player", "HURT", ResourcePaths.getImagePath("player", "Vampires3_Death_full.png"), 11);
        animationManager.loadSpritesheet("Player", "DEATH", ResourcePaths.getImagePath("player", "Vampires3_Death_full.png"), 11);

        Image barSheet = resourceManager.loadImage(ResourcePaths.getImagePath("ui", "lifeBar.png"));
        int cols = 3, rows = 4;
        int frameW = barSheet.getWidth(null) / cols;
        int frameH = barSheet.getHeight(null) / rows;

        lifeBarFrames = new Image[rows * cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int index = row * cols + col;
                lifeBarFrames[index] = stateManager.subImage(barSheet, col * frameW, row * frameH, frameW, frameH);
            }
        }
        lifeBarFrameIndex = 0;


        String[] slimeTypes = {"slime1", "slime2", "slime3"};
        int[] attackFrameCounts = {10, 11, 9};
        for (int i = 0; i < slimeTypes.length; i++) {
            String type = slimeTypes[i];
            animationManager.loadSpritesheet(type, "IDLE", ResourcePaths.getImagePath(type, type + "_Idle_full.png"), 6);
            animationManager.loadSpritesheet(type, "RUN", ResourcePaths.getImagePath(type, type + "_Run_full.png"), 8);
            animationManager.loadSpritesheet(type, "ATTACK", ResourcePaths.getImagePath(type, type + "_Attack_full.png"), attackFrameCounts[i]);
            animationManager.loadSpritesheet(type, "HURT", ResourcePaths.getImagePath(type, type + "_Hurt_full.png"), 5);
            animationManager.loadSpritesheet(type, "DEATH", ResourcePaths.getImagePath(type, type + "_Death_full.png"), 10);
            audioManager.loadSoundEffect(type + "DEATH", ResourcePaths.getSoundPath(type + "Death.wav"));
        }

        Image heartSheet = resourceManager.loadImage(ResourcePaths.getImagePath("ui", "heart.png"));
        int w = heartSheet.getWidth(null);
        int h = heartSheet.getHeight(null);
        heartFrameDuration = crotchet / HEART_FRAME_COUNT;
        int frameWI = w / HEART_FRAME_COUNT;
        int frameHE = h;
        heartFrames = new Image[HEART_FRAME_COUNT];
        for (int i = 0; i < HEART_FRAME_COUNT; i++) {
            heartFrames[i] = stateManager.subImage(heartSheet, i * frameWI, 0, frameWI, frameHE);
        }


        flameFrames = new Image[FLAME_FRAME_COUNT];
        for (int i = 0; i < FLAME_FRAME_COUNT; i++) {
            flameFrames[i] = resourceManager.loadImage(ResourcePaths.getImagePath("fire/Fire8", "pshik_" + (i + 1) + ".png"));
        }
        flameFrameDuration = crotchet / FLAME_FRAME_COUNT;


        rightSideFlameFrames = new Image[SIDE_FLAME_FRAME_COUNT];
        for (int i = 0; i < SIDE_FLAME_FRAME_COUNT; i++) {
            rightSideFlameFrames[i] = resourceManager.loadImage(ResourcePaths.getImagePath("fire/Fire5_leftright", "png_" + i + ".png"));
        }

        leftSideFlameFrames = new Image[SIDE_FLAME_FRAME_COUNT];
        for (int i = 0; i < SIDE_FLAME_FRAME_COUNT; i++) {
            leftSideFlameFrames[i] = stateManager.flipImage(rightSideFlameFrames[i], true);
        }
        sideFlameFrameDuration = 4 * crotchet / SIDE_FLAME_FRAME_COUNT;
    }

    private void initializeEntities() {
        Player player = new Player(4, 4, "Player", animationManager, audioManager, map, entityManager, 100, this);
        entityManager.setPlayer(player);
        Enemy greenSlime = new Enemy(14, 20, "slime1", animationManager, audioManager, map, player, new PathfindingStrategy.AStarPathfinding(), 4, 1);
        Enemy greenSlime1 = new Enemy(10, 20, "slime1", animationManager, audioManager, map, player, new PathfindingStrategy.AStarPathfinding(), 3, 2);
        Enemy greenSlime2 = new Enemy(4, 20, "slime1", animationManager, audioManager, map, player, new PathfindingStrategy.AStarPathfinding(), 1, 3);
        Enemy greenSlime3 = new Enemy(6, 20, "slime1", animationManager, audioManager, map, player, new PathfindingStrategy.AStarPathfinding(), 2, 4);
        Enemy blueSlime = new Enemy(5, 9, "slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 1, 5);
        Enemy blueSlime1 = new Enemy(14, 4, "slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 2, 6);
        Enemy blueSlime2 = new Enemy(9, 9, "slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 3, 7);
        Enemy blueSlime3 = new Enemy(13, 20, "slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 4, 7);
        Enemy blueSlime4 = new Enemy(12, 20, "slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 1, 7);
        Enemy blueSlime5 = new Enemy(11, 20, "slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 1, 7);
        Enemy yellowSlime = new Enemy(2, 10, "slime3", animationManager, audioManager, map, player, new PathfindingStrategy.GreedyBestFirstPathfinding(), 2, 8);
        Enemy yellowSlime1 = new Enemy(12, 6, "slime3", animationManager, audioManager, map, player, new PathfindingStrategy.GreedyBestFirstPathfinding(), 1, 9);
        Enemy yellowSlime2 = new Enemy(5, 7, "slime3", animationManager, audioManager, map, player, new PathfindingStrategy.GreedyBestFirstPathfinding(), 3, 10);
        entityManager.addEnemy(greenSlime);
        entityManager.addEnemy(greenSlime1);
        entityManager.addEnemy(greenSlime2);
        entityManager.addEnemy(greenSlime3);
        entityManager.addEnemy(blueSlime);
        entityManager.addEnemy(blueSlime1);
        entityManager.addEnemy(blueSlime2);
        entityManager.addEnemy(blueSlime3);
        entityManager.addEnemy(blueSlime4);
        entityManager.addEnemy(blueSlime5);
        entityManager.addEnemy(yellowSlime);
        entityManager.addEnemy(yellowSlime1);
        entityManager.addEnemy(yellowSlime2);
    }

    public void setLifeBarFrameIndex(int idx) {
        this.lifeBarFrameIndex = idx;
    }

    @Override
    public void init() {
        audioManager.playBackgroundMusic();
        startTime = stateManager.getTime();
    }

    @Override
    public void update(double dt) throws InterruptedException {
        Player p = entityManager.getPlayer();
        if (p.getHP() <= 20 && !lowHPAnimationsActive) {
            lowHPAnimationsActive = true;
            isSideFlamePlaying = true;
            sideFlameFrameIndex = 0;
            sideFlameAnimTimer = 0;
            if (!isFlamePlaying) {
                isFlamePlaying = true;
                flameFrameIndex = 0;
                flameAnimTimer = 0;
            }
            leftSmokeEmitter.setEmitting(true);
            rightSmokeEmitter.setEmitting(true);
        }

        updateRhythm();
        audioManager.updateSongPosition(songPos);
        camera.update(entityManager.getPlayer().getX(), entityManager.getPlayer().getY());
        entityManager.update(dt, inRhythmWindow);
        updateHeartAnimation(dt);
        updateFlameAnimation(dt);
        updateSideFlameAnimation(dt);
        leftSmokeEmitter.update(dt);
        rightSmokeEmitter.update(dt);

        if (smokeActive) {
            smokeTimer -= dt;
            if (smokeTimer <= 0) {
                if (lowHPAnimationsActive) {
                    smokeTimer += crotchet;
                    leftSmokeEmitter.emitBurst(38);
                    rightSmokeEmitter.emitBurst(38);
                } else {
                    smokeActive = false;
                    leftSmokeEmitter.setEmitting(false);
                    rightSmokeEmitter.setEmitting(false);
                }
            }
        }
    }

    private void updateFlameAnimation(double dt) {
        if (!isFlamePlaying) return;
        flameAnimTimer += dt;
        while (flameAnimTimer >= flameFrameDuration) {
            flameAnimTimer -= flameFrameDuration;
            flameFrameIndex++;
            if (flameFrameIndex >= FLAME_FRAME_COUNT) {
                if (lowHPAnimationsActive) {
                    flameFrameIndex = 0;
                } else {
                    isFlamePlaying = false;
                }
            }
        }
    }

    private void updateSideFlameAnimation(double dt) {
        if (!isSideFlamePlaying) return;
        sideFlameAnimTimer += dt;
        while (sideFlameAnimTimer >= sideFlameFrameDuration) {
            sideFlameAnimTimer -= sideFlameFrameDuration;
            sideFlameFrameIndex++;
            if (sideFlameFrameIndex >= SIDE_FLAME_FRAME_COUNT) {
                sideFlameFrameIndex = 0;
            }
        }
    }

    private void updateHeartAnimation(double dt) {
        if (!heartPlaying) return;
        heartAnimTimer += dt;
        if (heartAnimTimer >= heartFrameDuration) {
            heartAnimTimer -= heartFrameDuration;
            heartFrameIndex++;
            if (heartFrameIndex >= HEART_FRAME_COUNT) {
                heartFrameIndex = 0;
                heartPlaying = false;
            }
        }
    }

    private void updateRhythm() {
        long now = stateManager.getTime();
        songPos = (now - startTime) / 1000.0 - offset;
        double t = songPos % crotchet;
        double tol = 0.15;
        inRhythmWindow = t < tol || t > crotchet - tol;
        if (!inRhythmWindow) {
            heartPlaying = true;
            heartFrameIndex = 0;
            heartAnimTimer = 0;
        }
    }

    public void triggerSmoke() {
        smokeActive = true;
        smokeTimer = crotchet;
        leftSmokeEmitter.setEmitting(true);
        rightSmokeEmitter.setEmitting(true);
        leftSmokeEmitter.emitBurst(38);
        rightSmokeEmitter.emitBurst(38);
    }

    public void startFlameAnimation() {
        isFlamePlaying = true;
        flameFrameIndex = 0;
        flameAnimTimer = 0;
    }

    public double getSongPos() {
        return songPos;
    }

    public double getCrotchet() {
        return crotchet;
    }

    @Override
    public void paintComponent() {
        stateManager.clearBackground(stateManager.width(), stateManager.height());
        render();
    }

    private void render() {
        Graphics2D g = stateManager.mGraphics;
        g.setTransform(new java.awt.geom.AffineTransform());
        int screenWidth = stateManager.width();
        int screenHeight = stateManager.height();
        int mapSize = screenWidth;
        int topPadding = (screenHeight - mapSize) / 2;
        int bottomPadding = screenHeight - mapSize - topPadding;

        stateManager.changeColor(stateManager.black);
        stateManager.drawSolidRectangle(0, 0, screenWidth, topPadding);
        stateManager.drawSolidRectangle(0, screenHeight - bottomPadding, screenWidth, bottomPadding);

        stateManager.translate(0, topPadding);
        int tileSize = camera.getTileSize();
        int offsetX = camera.getOffsetX();
        int offsetY = camera.getOffsetY();

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                int drawX = x * tileSize + offsetX;
                int drawY = y * tileSize + offsetY;
                if (drawX >= -tileSize && drawX < mapSize && drawY >= -tileSize && drawY < mapSize) {
                    Image tileTexture;
                    int tileType = map.getTileType(x, y);
                    if (tileType == 0) {
                        tileTexture = map.getTileTexture(x, y);
                    } else {
                        tileTexture = obstacleTiles[Math.min(tileType - 1, obstacleTiles.length - 1)];
                    }
                    if (tileTexture != null) {
                        stateManager.drawImage(tileTexture, drawX, drawY, tileSize, tileSize);
                    } else {
                        stateManager.changeColor(128, 128, 128);
                        stateManager.drawSolidRectangle(drawX, drawY, tileSize, tileSize);
                    }
                }
            }
        }

        entityManager.render(g, offsetX, offsetY, tileSize);

        g.setTransform(new java.awt.geom.AffineTransform());

        if (heartFrames != null) {
            Image heartFrame = heartFrames[heartPlaying ? heartFrameIndex : 0];
            int heartSize = 64;
            int heartX = (screenWidth - heartSize) / 2;
            int heartY = screenHeight - heartSize - 50;
            stateManager.drawImage(heartFrame, heartX, heartY, heartSize, heartSize);

            if (isSideFlamePlaying && leftSideFlameFrames != null && rightSideFlameFrames != null) {
                Image leftFrame = leftSideFlameFrames[sideFlameFrameIndex];
                Image rightFrame = rightSideFlameFrames[sideFlameFrameIndex];
                int someGap = 10;
                int scaledFlameWidth = (int)(leftFrame.getWidth(null) * sideFlameScaleFactor);
                int scaledFlameHeight = (int)(leftFrame.getHeight(null) * sideFlameScaleFactor);
                int leftX = heartX - scaledFlameWidth - someGap + 600;
                int leftY = heartY + (heartSize - scaledFlameHeight) / 2 - 100;
                stateManager.drawImage(leftFrame, leftX, leftY, scaledFlameWidth, scaledFlameHeight);


                int rightX = heartX + heartSize + someGap - 600;
                int rightY = leftY;
                stateManager.drawImage(rightFrame, rightX, rightY, scaledFlameWidth, scaledFlameHeight);

            }
        }

        Player p = entityManager.getPlayer();
        int maxHP = 60;
        int hp = p.getHP();
        int frameIndex = (maxHP - hp) / 5;
        lifeBarFrameIndex = Math.min(11, Math.max(0, frameIndex));
        if (hp > 0 && lifeBarFrames != null) {
            Image barImg = lifeBarFrames[lifeBarFrameIndex];
            int x = screenWidth / 100;
            int y = screenWidth / 100;
            stateManager.drawImage(barImg, x, y, bw, bh);
            stateManager.changeColor(stateManager.white);
            stateManager.drawText(x + bw, y + bh / 2, "HP: " + hp, "Arial", 30);
        }

        drawRhythmIndicator();

        if (isFlamePlaying) {
            Image frame = flameFrames[flameFrameIndex];
            int flameSize = screenWidth / 10;
            int leftX = screenWidth / 20 - 120;
            int leftY = screenHeight - flameSize - screenWidth / 20 + 120;
            stateManager.drawImage(frame, leftX, leftY, flameSize, flameSize);
            int rightX = screenWidth - flameSize - screenWidth / 20 + 120;
            int rightY = screenHeight - flameSize - screenWidth / 20 + 120;
            stateManager.drawImage(frame, rightX, rightY, flameSize, flameSize);
        }

        if (!leftSmokeEmitter.isEmpty() || !rightSmokeEmitter.isEmpty()) {
            stateManager.saveCurrentTransform();
            leftSmokeEmitter.draw(stateManager);
            rightSmokeEmitter.draw(stateManager);
            stateManager.restoreLastTransform();
        }
    }

    private void drawRhythmIndicator() {
        if (heartFrames != null) {
            Image frame = heartFrames[heartPlaying ? heartFrameIndex : 0];
            int heartSize = stateManager.width() / 8;
            int x = stateManager.width() / 2 - heartSize / 2;
            int y = (stateManager.height() / 4) * 3;
            stateManager.drawImage(frame, x, y, heartSize, heartSize);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        entityManager.handleInput(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void cleanup() {
        heartFrames = null;
        flameFrames = null;
        lifeBarFrames = null;
        leftSideFlameFrames = null;
        rightSideFlameFrames = null;
        floorTiles = null;
        obstacleTiles = null;
    }
}