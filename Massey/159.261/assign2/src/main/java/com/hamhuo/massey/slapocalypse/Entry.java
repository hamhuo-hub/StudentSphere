package com.hamhuo.massey.slapocalypse;

import com.hamhuo.massey.slapocalypse.core.GameController;

import static com.hamhuo.massey.slapocalypse.core.GameEngine.createGame;

public class Entry {
    public static void main(String[] args) {
        GameController game = new GameController();
        createGame(game, 180);
    }
}
