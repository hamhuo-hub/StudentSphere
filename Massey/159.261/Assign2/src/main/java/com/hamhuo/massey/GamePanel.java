package com.hamhuo.massey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

class GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
    private Player player;
    private ArrayList<Slime> slimes;
    private ArrayList<Obstacle> obstacles;
    private Timer gameTimer;
    private Random random;

    public GamePanel() {
        player = new Player(100, 500);
        slimes = new ArrayList<>();
        obstacles = new ArrayList<>();
        random = new Random();

        // Set up listeners
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);

        // Timer for game loop (60 FPS)
        gameTimer = new Timer(16, e -> gameLoop());
        gameTimer.start();
    }

    // Main game loop
    private void gameLoop() {
        // Move slimes and check collisions
        for (Slime slime : slimes) {
            slime.move();
            if (slime.getBounds().intersects(player.getBounds()) && player.isAttacking()) {
                slimes.remove(slime);  // Remove slime if attacked
                break;
            }
        }

        for (Obstacle obstacle : obstacles) {
            if (obstacle.getBounds().intersects(player.getBounds()) && player.isSliding()) {
                obstacles.remove(obstacle);  // Remove obstacle if sliding
                break;
            }
        }

        // Randomly spawn slimes and obstacles
        if (random.nextInt(100) < 2) {
            slimes.add(new Slime(800, random.nextInt(450)));
        }
        if (random.nextInt(100) < 2) {
            obstacles.add(new Obstacle(800, random.nextInt(500)));
        }

        // Repaint the panel to update the game state
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);

        // Draw slimes and obstacles
        for (Slime slime : slimes) {
            slime.draw(g);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }
    }

    // Keyboard input for hit
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_F) {
            player.setAttacking(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_F) {
            player.setAttacking(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // Mouse input for slide and dodge
    @Override
    public void mousePressed(MouseEvent e) {
        player.setSliding(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        player.setSliding(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        player.slide(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        player.dodge();
    }
}
