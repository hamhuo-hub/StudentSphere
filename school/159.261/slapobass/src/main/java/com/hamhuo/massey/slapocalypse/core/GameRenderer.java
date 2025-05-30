package com.hamhuo.massey.slapocalypse.core;

import com.hamhuo.massey.slapocalypse.entity.EntityManager;
import com.hamhuo.massey.slapocalypse.entity.Player;
import com.hamhuo.massey.slapocalypse.entity.Enemy;
import com.hamhuo.massey.slapocalypse.entity.SmokeParticleEmitter;
import com.hamhuo.massey.slapocalypse.state.DeathState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameRenderer extends GameEngine {
    private EntityManager entityManager;
    private final AudioManager audioManager;
    private final AnimationManager animationManager;
    private final GameMap map;
    private final Camera camera;
    private Image floorTile1, floorTile2, obstacleTile1, obstacleTile2, obstacleTile3, obstacleTile4, obstacleTile5,
            obstacleTile6, obstacleTile7, obstacleTile8, obstacleTile9, obstacleTile10, obstacleTile11, obstacleTile12, obstacleTile13;
    private final double bpm = 120;
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
    private Image backgroundImage;

    private final SmokeParticleEmitter leftSmokeEmitter;
    private final SmokeParticleEmitter rightSmokeEmitter;
    private double smokeTimer = 0;
    private boolean smokeActive = false;
    private final int FLAME_FRAME_COUNT = 38;
    int bw = width()/5;
    int bh = width()/16;

    private boolean isGameOver= false;

    // 侧边火焰动画变量
    private Image[] leftSideFlameFrames;
    private Image[] rightSideFlameFrames;
    private final int SIDE_FLAME_FRAME_COUNT = 84;
    private int sideFlameFrameIndex = 0;
    private boolean isSideFlamePlaying = false;
    private double sideFlameAnimTimer = 0;
    private double sideFlameFrameDuration;
    private boolean lowHPAnimationsActive = false;
    private double sideFlameScaleFactor = 4.0; // 新增：控制侧边火焰缩放比例

    public GameRenderer() throws IOException {
        super();
        setupWindow(0, 0);
        int screenWidth = width();
        int screenHeight = height();
        bw = width()/5;
        bh = width()/16;
        leftSmokeEmitter = new SmokeParticleEmitter();
        leftSmokeEmitter.setPosition(0, height() );
        leftSmokeEmitter.angle = (float)(3 * Math.PI / 4);
        rightSmokeEmitter = new SmokeParticleEmitter();
        rightSmokeEmitter.setPosition(width()-200, height() );
        rightSmokeEmitter.angle = (float)(5 * Math.PI / 4);

        Image[] floorTiles = new Image[5];
        floorTiles[0] = loadImage("src/main/resources/ui/map1.png");
        floorTiles[1] = loadImage("src/main/resources/ui/map2.png");
        floorTiles[2] = loadImage("src/main/resources/ui/map3.png");
        floorTiles[3] = loadImage("src/main/resources/ui/map4.png");
        floorTiles[4] = loadImage("src/main/resources/ui/map5.png");

        obstacleTile1 = loadImage("src/main/resources/ui/obstacle1.png");
        obstacleTile2 = loadImage("src/main/resources/ui/obstacle2.png");
        obstacleTile3 = loadImage("src/main/resources/ui/obstacle3.png");
        obstacleTile4 = loadImage("src/main/resources/ui/obstacle4.png");
        obstacleTile5 = loadImage("src/main/resources/ui/obstacle5.png");
        obstacleTile6 = loadRotatedImage("src/main/resources/ui/obstacle1.png", 90);
        obstacleTile7 = loadRotatedImage("src/main/resources/ui/obstacle4.png", 180);
        obstacleTile8 = loadRotatedImage("src/main/resources/ui/obstacle5.png", 90);
        obstacleTile9 = loadRotatedImage("src/main/resources/ui/obstacle5.png", 180);
        obstacleTile10 = loadRotatedImage("src/main/resources/ui/obstacle5.png", 270);
        obstacleTile11 = loadRotatedImage("src/main/resources/ui/obstacle4.png", 270);
        obstacleTile12 = loadRotatedImage("src/main/resources/ui/obstacle3.png", 180);
        obstacleTile13 = loadImage("src/main/resources/ui/obstacle6.png");

        for (int i = 0; i < floorTiles.length; i++) {
            if (floorTiles[i] == null) {
                floorTiles[i] = new java.awt.image.BufferedImage(100, 100, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) floorTiles[i].getGraphics();
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, 100, 100);
                g.dispose();
            }
        }

        Image[] obstacleTiles = {obstacleTile1, obstacleTile2, obstacleTile3, obstacleTile4, obstacleTile5,
                obstacleTile6, obstacleTile7, obstacleTile8, obstacleTile9, obstacleTile10, obstacleTile11,
                obstacleTile12, obstacleTile13};
        for (int i = 0; i < obstacleTiles.length; i++) {
            if (obstacleTiles[i] == null) {
                obstacleTiles[i] = new java.awt.image.BufferedImage(100, 100, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) obstacleTiles[i].getGraphics();
                g.setColor(new Color(50 + i * 20, 50 + i * 20, 50 + i * 20));
                g.fillRect(0, 0, 100, 100);
                g.dispose();
            }
        }
        obstacleTile1 = obstacleTiles[0];
        obstacleTile2 = obstacleTiles[1];
        obstacleTile3 = obstacleTiles[2];
        obstacleTile4 = obstacleTiles[3];
        obstacleTile5 = obstacleTiles[4];
        obstacleTile6 = obstacleTiles[5];
        obstacleTile7 = obstacleTiles[6];
        obstacleTile8 = obstacleTiles[7];
        obstacleTile9 = obstacleTiles[8];
        obstacleTile10 = obstacleTiles[9];
        obstacleTile11 = obstacleTiles[10];
        obstacleTile12 = obstacleTiles[11];
        obstacleTile13 = obstacleTiles[12];

        map = new GameMap(16, 24, screenWidth, floorTiles, "src/main/resources/map.txt");
        camera = new Camera(map, 10, 10, screenWidth, screenHeight);
        map.setTileSize(camera.getTileSize());

        audioManager = new AudioManager(this);
        animationManager = new AnimationManager(this);
        loadResources();

        entityManager = new EntityManager(map, audioManager);
        initializeEntities();
    }

    private void loadResources() {
        backgroundImage = loadImage("src/main/resources/Background.png");
        audioManager.loadBackgroundMusic("src/main/resources/sound/song.wav");
        audioManager.loadSoundEffect("ATTACK", "src/main/resources/sound/SlimeAttack.wav");
        audioManager.loadSoundEffect("Excellent", "src/main/resources/sound/excellent.wav");
        audioManager.loadSoundEffect("PlayerAttack", "src/main/resources/sound/SlimeAttack.wav");

        // 加载玩家动画
        animationManager.loadSpritesheet("Player", "IDLE", "src/main/resources/ui/player1/Vampires3_Idle_full.png", 4);
        animationManager.loadSpritesheet("Player", "RUN", "src/main/resources/ui/player1/Vampires3_Run_full.png", 8);
        animationManager.loadSpritesheet("Player", "ATTACK", "src/main/resources/ui/player1/Vampires3_Attack_full.png", 12);
        animationManager.loadSpritesheet("Player", "HURT", "src/main/resources/ui/player1/Vampires3_Death_full.png", 11);
        animationManager.loadSpritesheet("Player", "DEATH", "src/main/resources/ui/player1/Vampires3_Death_full.png", 11);

        Image barSheet = loadImage("src/main/resources/ui/lifeBar.png");
        int cols = 3, rows = 4;
        int frameW = barSheet.getWidth(null) / cols;
        int frameH = barSheet.getHeight(null) / rows;

        lifeBarFrames = new Image[rows * cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int index = row * cols + col;
                lifeBarFrames[index] = subImage(
                        barSheet,
                        col * frameW,
                        row * frameH,
                        frameW, frameH
                );
            }
        }
        lifeBarFrameIndex = 0;

        String[] slimeTypes = {"Slime1", "Slime2", "Slime3"};
        for (String type : slimeTypes) {
            animationManager.loadSpritesheet(type, "IDLE", "src/main/resources/ui/" + type + "/" + type + "_Idle_full.png", 6);
            animationManager.loadSpritesheet(type, "RUN", "src/main/resources/ui/" + type + "/" + type + "_Run_full.png", 8);
            animationManager.loadSpritesheet(type, "ATTACK", "src/main/resources/ui/" + type + "/" + type + "_Attack_full.png", type.equals("Slime1") ? 10 : type.equals("Slime2") ? 11 : 9);
            animationManager.loadSpritesheet(type, "HURT", "src/main/resources/ui/" + type + "/" + type + "_Hurt_full.png", 5);
            animationManager.loadSpritesheet(type, "DEATH", "src/main/resources/ui/" + type + "/" + type + "_Death_full.png", 10);
            audioManager.loadSoundEffect(type + "DEATH", "src/main/resources/sound/" + type + "Death.wav");
        }

        Image heartSheet = loadImage("src/main/resources/ui/heart.png");
        int w = heartSheet.getWidth(null);
        int h = heartSheet.getHeight(null);
        heartFrameDuration = crotchet / HEART_FRAME_COUNT;
        int frameWI = w / HEART_FRAME_COUNT;
        int frameHE = h;
        heartFrames = new Image[HEART_FRAME_COUNT];
        for (int i = 0; i < HEART_FRAME_COUNT; i++) {
            heartFrames[i] = subImage(heartSheet, i * frameWI, 0, frameWI, frameHE);
        }

        // 新增：加载火焰动画帧
        flameFrames = new Image[FLAME_FRAME_COUNT];
        for (int i = 0; i < FLAME_FRAME_COUNT; i++) {
            flameFrames[i] = loadImage("src/main/resources/Fire8/pshik_" + (i + 1) + ".png");
        }
        flameFrameDuration = crotchet / FLAME_FRAME_COUNT;

        // 加载侧边火焰帧
        rightSideFlameFrames = new Image[SIDE_FLAME_FRAME_COUNT];
        for (int i = 0; i < SIDE_FLAME_FRAME_COUNT; i++) {
            rightSideFlameFrames[i] = loadImage("src/main/resources/Fire5_leftright/png_" + i + ".png");

        }

        // 通过镜像创建左侧火焰帧
        leftSideFlameFrames = new Image[SIDE_FLAME_FRAME_COUNT];
        for (int i = 0; i < SIDE_FLAME_FRAME_COUNT; i++) {
            leftSideFlameFrames[i] = flipImage(rightSideFlameFrames[i], true);
        }

        // 设置侧边火焰帧持续时间
        sideFlameFrameDuration = 4 * crotchet / SIDE_FLAME_FRAME_COUNT;
    }

    public void setLifeBarFrameIndex(int idx) {
        this.lifeBarFrameIndex = idx;
    }

    private void initializeEntities() {
        Player player = new Player(4, 4, "Player", animationManager, audioManager, map, entityManager, 100,this);
        entityManager.setPlayer(player);
        Enemy greenSlime = new Enemy(14, 20, "Slime1", animationManager, audioManager, map, player, new PathfindingStrategy.AStarPathfinding(), 4, 1);
        Enemy greenSlime1 = new Enemy(10, 20, "Slime1", animationManager, audioManager, map, player, new PathfindingStrategy.AStarPathfinding(), 3, 2);
        Enemy greenSlime2 = new Enemy(4, 20, "Slime1", animationManager, audioManager, map, player, new PathfindingStrategy.AStarPathfinding(), 1, 3);
        Enemy greenSlime3 = new Enemy(6, 20, "Slime1", animationManager, audioManager, map, player, new PathfindingStrategy.AStarPathfinding(), 2, 4);
        Enemy blueSlime = new Enemy(5, 9, "Slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 1, 5);
        Enemy blueSlime1 = new Enemy(14, 4, "Slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 2, 6);
        Enemy blueSlime2 = new Enemy(9, 9, "Slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 3, 7);
        Enemy blueSlime3 = new Enemy(13, 20, "Slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 4, 7);
        Enemy blueSlime4 = new Enemy(12, 20, "Slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 1, 7);
        Enemy blueSlime5 = new Enemy(11, 20, "Slime2", animationManager, audioManager, map, player, new PathfindingStrategy.BFSPathfinding(), 1, 7);
        Enemy yellowSlime = new Enemy(2, 10, "Slime3", animationManager, audioManager, map, player, new PathfindingStrategy.GreedyBestFirstPathfinding(), 2, 8);
        Enemy yellowSlime1 = new Enemy(12, 6, "Slime3", animationManager, audioManager, map, player, new PathfindingStrategy.GreedyBestFirstPathfinding(), 1, 9);
        Enemy yellowSlime2 = new Enemy(5, 7, "Slime3", animationManager, audioManager, map, player, new PathfindingStrategy.GreedyBestFirstPathfinding(), 3, 10);
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

    @Override
    public void init() {
        audioManager.playBackgroundMusic();
        startTime = getTime();
    }

    @Override
    // 在 GameController.java 的 update 方法中修改
    public void update(double dt) throws InterruptedException {
        if (!isGameOver) {
            entityManager.update(dt, inRhythmWindow);
            if (entityManager.getEnemies().isEmpty() || entityManager.getPlayer().getState() instanceof DeathState) {
                isGameOver = true;
            }
        }
        // 其余代码保持不变
        // 检查低血量以启动动画
        Player p = entityManager.getPlayer();
        if (p.getHP() <= 20 && !lowHPAnimationsActive) {
            System.out.println("低血量检测到！启动动画。");
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
        long now = getTime();
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
        if (isGameOver) {
            int screenW = width();
            int screenH = height();
            drawImage(backgroundImage, 0, 0, screenW, screenH);
            changeColor(white);
            drawText(100, 100, "Game Over!");
            drawText(100, 150, "Press R to Restart");
            drawText(100, 200, "Press ESC to Exit");

        } else {
            clearBackground(width(), height());
            render();
        }

    }

    private void render() {
        mGraphics.setTransform(new java.awt.geom.AffineTransform());
        int screenWidth = width();
        int screenHeight = height();
        int mapSize = screenWidth;
        int topPadding = (screenHeight - mapSize) / 2;
        int bottomPadding = screenHeight - mapSize - topPadding;

        changeColor(black);
        drawSolidRectangle(0, 0, screenWidth, topPadding);
        drawSolidRectangle(0, screenHeight - bottomPadding, screenWidth, bottomPadding);

        translate(0, topPadding);
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
                        switch (tileType) {
                            case 2:
                                tileTexture = obstacleTile2;
                                break;
                            case 3:
                                tileTexture = obstacleTile3;
                                break;
                            case 4:
                                tileTexture = obstacleTile4;
                                break;
                            case 5:
                                tileTexture = obstacleTile5;
                                break;
                            case 6:
                                tileTexture = obstacleTile6;
                                break;
                            case 7:
                                tileTexture = obstacleTile7;
                                break;
                            case 8:
                                tileTexture = obstacleTile8;
                                break;
                            case 9:
                                tileTexture = obstacleTile9;
                                break;
                            case 10:
                                tileTexture = obstacleTile10;
                                break;
                            case 11:
                                tileTexture = obstacleTile11;
                                break;
                            case 12:
                                tileTexture = obstacleTile12;
                                break;
                            case 13:
                                tileTexture = obstacleTile13;
                                break;
                            default:
                                tileTexture = obstacleTile1;
                        }
                    }
                    if (tileTexture != null) {
                        drawImage(tileTexture, drawX, drawY, tileSize, tileSize);
                    } else {
                        changeColor(128, 128, 128);
                        drawSolidRectangle(drawX, drawY, tileSize, tileSize);
                    }
                }
            }
        }

        entityManager.render(mGraphics, offsetX, offsetY, tileSize);


        mGraphics.setTransform(new java.awt.geom.AffineTransform());


        if (heartFrames != null) {
            Image heartFrame = heartFrames[heartPlaying ? heartFrameIndex : 0];
            int heartSize = 64;
            int heartX = (screenWidth - heartSize) / 2;
            int heartY = screenHeight - heartSize - 50;
            drawImage(heartFrame, heartX, heartY, heartSize, heartSize);


            if (isSideFlamePlaying && leftSideFlameFrames != null && rightSideFlameFrames != null) {
                Image leftFrame = leftSideFlameFrames[sideFlameFrameIndex];
                Image rightFrame = rightSideFlameFrames[sideFlameFrameIndex];
                int someGap = 10;
                int scaledFlameWidth = (int)(leftFrame.getWidth(null) * sideFlameScaleFactor);
                int scaledFlameHeight = (int)(leftFrame.getHeight(null) * sideFlameScaleFactor);
                int leftX = heartX - scaledFlameWidth - someGap+600;
                int leftY = heartY + (heartSize - scaledFlameHeight) / 2-100;
                drawImage(leftFrame, leftX, leftY, scaledFlameWidth, scaledFlameHeight);


                int rightX = heartX + heartSize + someGap-600;
                int rightY = leftY;
                drawImage(rightFrame, rightX, rightY, scaledFlameWidth, scaledFlameHeight);

            }
        }

        Player p = entityManager.getPlayer();
        int maxHP = 60;
        int hp = p.getHP();
        int frameIndex = (maxHP - hp) / 5;
        lifeBarFrameIndex = Math.min(11, Math.max(0, frameIndex));
        if (hp > 0 && lifeBarFrames != null) {
            Image barImg = lifeBarFrames[lifeBarFrameIndex];
            int x = width() / 100;
            int y = width() / 100;
            drawImage(barImg, x, y, bw, bh);
            changeColor(white);
            drawText(x + bw, y + bh/2, "HP: " + hp, "Arial", 30);
        }

        drawRhythmIndicator();


        if (isFlamePlaying) {
            Image frame = flameFrames[flameFrameIndex];
            int flameSize = width() / 10;
            int leftX = width() / 20 - 120;
            int leftY = height() - flameSize - width() / 20 + 120;
            drawImage(frame, leftX, leftY, flameSize, flameSize);
            int rightX = width() - flameSize - width() / 20 + 120;
            int rightY = height() - flameSize - width() / 20 + 120;
            drawImage(frame, rightX, rightY, flameSize, flameSize);
        }

        if (!leftSmokeEmitter.isEmpty() || !rightSmokeEmitter.isEmpty()) {
            saveCurrentTransform();
            leftSmokeEmitter.draw(this);
            rightSmokeEmitter.draw(this);
            restoreLastTransform();
        }
    }

    private void drawRhythmIndicator() {
        if (heartFrames != null) {
            Image frame = heartFrames[heartPlaying ? heartFrameIndex : 0];
            int heartSize = width() / 8;
            int x = width() / 2 - heartSize / 2;
            int y = (height() / 4) * 3;
            drawImage(frame, x, y, heartSize, heartSize);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isGameOver) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                // 重新开始逻辑：重置游戏状态，isGameOver = false
                resetGame();
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        entityManager.handleInput(e);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (isGameOver) {
            Rectangle restartButton = new Rectangle(100, 250, 130, 70); // 替换为实际坐标和大小
            Rectangle exitButton = new Rectangle(250, 250, 130, 70); // 替换为实际坐标和大小
            if (restartButton.contains(event.getPoint())) {
                resetGame(); // 重新开始逻辑
                isGameOver = false;
            } else if (exitButton.contains(event.getPoint())) {
                System.exit(0); // 退出游戏
            }
        }

    }
    private void resetGame() {
        // 1. 重建实体管理器
        entityManager = new EntityManager(map, audioManager);
        initializeEntities();

        // 2. 重置各种动画／计时器状态
        isGameOver = false;
        lowHPAnimationsActive = false;
        isFlamePlaying        = false;
        isSideFlamePlaying    = false;
        smokeActive           = false;
        heartPlaying          = false;
        sideFlameFrameIndex   = 0;
        flameFrameIndex       = 0;
        heartFrameIndex       = 0;
        smokeTimer            = 0;
        // … 如果有更多动画状态，也一并重置

        // 3. 重置音乐与节奏计时
        audioManager.playBackgroundMusic();
        startTime = getTime();

    }

    public void renderEntityManager(int offsetX, int offsetY, int tileSize) {
        entityManager.render(mGraphics, offsetX, offsetY, tileSize);
    }
}

