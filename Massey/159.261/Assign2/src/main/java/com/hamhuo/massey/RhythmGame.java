package com.hamhuo.massey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class RhythmGame extends JFrame {
    private GamePanel panel;

    public RhythmGame() {
        setTitle("Rhythm Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new GamePanel();
        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new RhythmGame();
    }
}

