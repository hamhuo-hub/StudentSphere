package com.hamhuo.massey.slapocalypse.state;

import com.hamhuo.massey.slapocalypse.core.GameEngine;
import com.hamhuo.massey.slapocalypse.Menu;
import com.hamhuo.massey.slapocalypse.core.ResourceManager;

import java.awt.event.*;

public class StateManager extends GameEngine {
    private GameState currentState;
    private final ResourceManager resourceManager;

    public StateManager() {
        super();
        this.resourceManager = new ResourceManager(this);
        setupWindow(0, 0); // 全屏窗口
    }

    public void setState(GameState state) {
        if (currentState != null) {
            currentState.cleanup();
        }
        currentState = state;
        currentState.init();
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    @Override
    public void init() {
        setState(new Menu(this));
    }

    @Override
    public void update(double dt) throws InterruptedException {
        if (currentState != null) {
            currentState.update(dt);
        }
    }

    @Override
    public void paintComponent() {
        if (currentState != null) {
            currentState.paintComponent();
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (currentState != null) {
            currentState.keyPressed(event);
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (currentState != null) {
            currentState.mousePressed(event);
        }
    }
}