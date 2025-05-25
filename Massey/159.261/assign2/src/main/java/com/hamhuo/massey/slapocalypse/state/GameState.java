package com.hamhuo.massey.slapocalypse.state;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface GameState {
    void init();
    void update(double dt) throws InterruptedException;
    void paintComponent();
    void keyPressed(KeyEvent event);
    void mousePressed(MouseEvent event);
    void cleanup();
}