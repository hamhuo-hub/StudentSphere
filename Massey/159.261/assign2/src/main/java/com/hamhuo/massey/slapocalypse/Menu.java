package com.hamhuo.massey.slapocalypse;

import com.hamhuo.massey.slapocalypse.core.GameController;
import com.hamhuo.massey.slapocalypse.core.ResourceManager;
import com.hamhuo.massey.slapocalypse.core.ResourcePaths;
import com.hamhuo.massey.slapocalypse.state.GameState;
import com.hamhuo.massey.slapocalypse.state.StateManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Menu implements GameState {
    private final StateManager stateManager;
    private final ResourceManager resourceManager;
    private BufferedImage backgroundImg;
    private BufferedImage titleImg;
    private BufferedImage uiImg;
    private final int buttonWidth = 512;
    private final int buttonHeight = 256;
    private Rectangle playButtonRect = new Rectangle();
    private Rectangle exitButtonRect = new Rectangle();

    public Menu(StateManager stateManager) {
        this.stateManager = stateManager;
        this.resourceManager = stateManager.getResourceManager();
    }

    @Override
    public void init() {
        backgroundImg = (BufferedImage) resourceManager.loadImage(ResourcePaths.getImagePath("menu", "Background.png"));
        titleImg = (BufferedImage) resourceManager.loadImage(ResourcePaths.getImagePath("menu", "GameName.png"));
        uiImg = (BufferedImage) resourceManager.loadImage(ResourcePaths.getImagePath("menu", "UI.png"));
    }

    @Override
    public void paintComponent() {
        Graphics2D g = stateManager.mGraphics;
        g.setTransform(new java.awt.geom.AffineTransform());

        stateManager.drawImage(backgroundImg, 0, 0, stateManager.width(), stateManager.height());

        int titleW = (int)(titleImg.getWidth() * 1.5);
        int titleH = (int)(titleImg.getHeight() * 1.5);
        int titleX = (stateManager.width() - titleW) / 2;
        stateManager.drawImage(titleImg.getScaledInstance(titleW, titleH, Image.SCALE_SMOOTH), titleX, 100);

        int centerX = (stateManager.width() - buttonWidth) / 2;
        int playY = (int)(stateManager.height() * 0.4);
        int exitY = playY + buttonHeight;

        playButtonRect.setBounds(centerX, playY, buttonWidth, buttonHeight);
        exitButtonRect.setBounds(centerX, exitY, buttonWidth, buttonHeight);

        BufferedImage playImg = uiImg.getSubimage(0, 128, 64, 32);
        BufferedImage exitImg = uiImg.getSubimage(0, 192, 64, 32);
        Image scaledPlay = playImg.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        Image scaledExit = exitImg.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);

        stateManager.drawImage(scaledPlay, playButtonRect.x, playButtonRect.y);
        stateManager.drawImage(scaledExit, exitButtonRect.x, exitButtonRect.y);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (playButtonRect.contains(e.getPoint())) {
            try {
                stateManager.setState(new GameController(stateManager));
            } catch (IOException ex) {
                throw new RuntimeException();
            }
        } else if (exitButtonRect.contains(e.getPoint())) {
            System.exit(0);
        }
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void keyPressed(KeyEvent event) {

    }

    @Override
    public void cleanup() {
        backgroundImg = null;
        titleImg = null;
        uiImg = null;
    }
}