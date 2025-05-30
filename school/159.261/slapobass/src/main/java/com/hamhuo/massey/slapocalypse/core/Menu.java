package com.hamhuo.massey.slapocalypse.core;

import com.hamhuo.massey.slapocalypse.Entry;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Menu extends GameEngine {

    private BufferedImage backgroundImg;
    private BufferedImage titleImg;
    private BufferedImage uiImg;

    private Rectangle playButtonRect;
    private Rectangle exitButtonRect;

    @Override
    public void init() {
        try {
            backgroundImg = ImageIO.read(new File("src/main/resources/menu_background.png"));
            titleImg = ImageIO.read(new File("src/main/resources/GameName.png"));
            uiImg = ImageIO.read(new File("src/main/resources/UI.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int buttonWidth = 512;
        int buttonHeight = 256;
        int centerX = 1025;

        playButtonRect = new Rectangle(centerX, 600, buttonWidth, buttonHeight);
        exitButtonRect = new Rectangle(centerX, 750, buttonWidth, buttonHeight);
    }


    @Override
    public void paintComponent() {
        Graphics2D g = mGraphics;

        g.drawImage(backgroundImg, 0, 0, mWidth, mHeight, null);

        int scale = 2;
        int titleW = titleImg.getWidth() * scale;
        int titleH = titleImg.getHeight() * scale;
        int titleX = (mWidth - titleW) / 2;
        g.drawImage(titleImg.getScaledInstance(titleW, titleH, Image.SCALE_SMOOTH), titleX, 100, null);

        BufferedImage playImg = uiImg.getSubimage(0, 128, 64, 32);
        BufferedImage exitImg = uiImg.getSubimage(0, 192, 64, 32);

        Image scaledPlay = playImg.getScaledInstance(playButtonRect.width, playButtonRect.height, Image.SCALE_SMOOTH);
        Image scaledExit = exitImg.getScaledInstance(exitButtonRect.width, exitButtonRect.height, Image.SCALE_SMOOTH);

        g.drawImage(scaledPlay, playButtonRect.x, playButtonRect.y, null);
        g.drawImage(scaledExit, exitButtonRect.x, exitButtonRect.y, null);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (playButtonRect == null || exitButtonRect == null) {
            return;
        }

        Point p = e.getPoint();

        if (playButtonRect.contains(p)) {
            try {
                Entry.startGame();   //Start Game
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (exitButtonRect.contains(p)) {
            System.exit(0);
        }
    }

    @Override
    public void update(double dt) {

    }

}
