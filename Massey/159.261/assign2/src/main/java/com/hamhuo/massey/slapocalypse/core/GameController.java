package com.hamhuo.massey.slapocalypse.core;

import com.hamhuo.massey.slapocalypse.role.GameMap;
import com.hamhuo.massey.slapocalypse.role.Enemy;
import com.hamhuo.massey.slapocalypse.role.Hero;
import com.hamhuo.massey.slapocalypse.role.Role;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class GameController extends GameEngine {

    private List<Role> roles;
    private GameMap map;


    public GameController() {
        super();
        setupWindow(800, 600);
    }

    @Override
    public void init() {
        System.out.println(System.getProperty("user.dir"));
    }

    @Override
    public void update(double dt) throws InterruptedException {

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

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }


}