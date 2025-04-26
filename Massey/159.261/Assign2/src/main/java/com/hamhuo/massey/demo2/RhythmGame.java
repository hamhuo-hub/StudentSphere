package com.hamhuo.massey.demo2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Scanner;

public class RhythmGame extends JPanel implements ActionListener, KeyListener {

    // 游戏配置常量
    private static final int GRID_SIZE = 16;
    private static final int CELL_SIZE = 40; // 每个格子的像素大小
    private static final int WINDOW_SIZE = GRID_SIZE * CELL_SIZE;
    private static final Double BEAT_INTERVAL = 1000/(110.0/60); // 每秒一个节拍（1秒1拍）
    private static final int TOLERANCE = 300; // 容忍度
    private static int moveLock = 0; // 容忍度

    private Timer timer;
    private long lastBeatTime; // 上一个节拍的时间戳
    private int playerX, playerY; // 主角的位置
    private int enemyX, enemyY; // 敌人的位置

    public RhythmGame() {
        this.setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        // 初始化主角和敌人的位置
        playerX = GRID_SIZE / 2;
        playerY = GRID_SIZE - 2;
        enemyX = GRID_SIZE / 2;
        enemyY = 1;

        // 初始化节拍计时器
        lastBeatTime = System.currentTimeMillis();

        // 启动定时器，每50毫秒更新一次
        timer = new Timer(50, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long currentTime = System.currentTimeMillis();

        // 检查节拍时间，BPM
        if (currentTime - lastBeatTime >= BEAT_INTERVAL) {
            // 节拍到了，更新敌人的位置
            moveEnemy();
            lastBeatTime = currentTime;
            moveLock = 0;
        }
        // 渲染游戏
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 绘制网格
        g.setColor(Color.WHITE);
        for (int i = 0; i <= GRID_SIZE; i++) {
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, WINDOW_SIZE);
            g.drawLine(0, i * CELL_SIZE, WINDOW_SIZE, i * CELL_SIZE);
        }

        // 绘制主角
        g.setColor(Color.BLUE);
        g.fillRect(playerX * CELL_SIZE, playerY * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // 绘制敌人
        g.setColor(Color.RED);
        g.fillRect(enemyX * CELL_SIZE, enemyY * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        long currentTime = System.currentTimeMillis();

        // 检查主角是否能移动：只能在节拍整数倍时移动，±20ms的容忍度
        if (Math.abs(currentTime - lastBeatTime) <= TOLERANCE && moveLock <= 2) {
            moveLock++;
            //System.out.println(currentTime - lastBeatTime);
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (playerX > 0) playerX--;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (playerX < GRID_SIZE - 1) playerX++;
                    break;
                case KeyEvent.VK_UP:
                    if (playerY > 0) playerY--;
                    break;
                case KeyEvent.VK_DOWN:
                    if (playerY < GRID_SIZE - 1) playerY++;
                    break;
            }
        }else{
            System.out.println("miss hit");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    // 敌人根据节拍移动
    private void moveEnemy() {
        Random rand = new Random();
        int direction = rand.nextInt(4); // 随机选择一个方向

        switch (direction) {
            case 0: // 向上
                if (enemyY > 0) enemyY--;
                break;
            case 1: // 向下
                if (enemyY < GRID_SIZE - 1) enemyY++;
                break;
            case 2: // 向左
                if (enemyX > 0) enemyX--;
                break;
            case 3: // 向右
                if (enemyX < GRID_SIZE - 1) enemyX++;
                break;
        }
    }

    public static void main(String[] args) {
        int flag = 0;
        Scanner scan = new Scanner(System.in);
        flag = scan.nextInt();
        JFrame frame = new JFrame("Rhythm Game");
        while (flag == 0) {};
            RhythmGame gamePanel = new RhythmGame();
            frame.add(gamePanel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
    }
}
