package com.hamhuo.massey.slapocalypse;

import com.hamhuo.massey.slapocalypse.core.GameEngine;
import com.hamhuo.massey.slapocalypse.state.StateManager;


public class Entry {
    public static void main(String[] args) {
        GameEngine game = new StateManager();
        GameEngine.createGame(game, 180);
    }
}