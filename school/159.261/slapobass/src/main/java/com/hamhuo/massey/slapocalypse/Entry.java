package com.hamhuo.massey.slapocalypse;

import com.hamhuo.massey.slapocalypse.core.GameRenderer;
import com.hamhuo.massey.slapocalypse.core.GameEngine;
import com.hamhuo.massey.slapocalypse.core.Menu;

import java.io.IOException;

import static com.hamhuo.massey.slapocalypse.core.GameEngine.createGame;

public class Entry {
    public static void main(String[] args) {
        GameEngine menu = new Menu();
        createGame(menu, 180);
    }

    public static void startGame() throws IOException {
        GameEngine game = new GameRenderer();
        createGame(game, 180);
    }
}
