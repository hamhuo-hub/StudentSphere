package com.hamhuo.massey.slapocalypse.core;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import static com.hamhuo.massey.slapocalypse.core.GameController.getImage;

public class ResourceManager {
    private final GameEngine engine;
    private final Map<String, Image> imageCache = new HashMap<>();
    private final Map<String, GameEngine.AudioClip> audioCache = new HashMap<>();

    public ResourceManager(GameEngine engine) {
        this.engine = engine;
    }

    public Image loadImage(String filename) {
        return imageCache.computeIfAbsent(filename, f -> {
            Image img = engine.loadImage(f);
            if (img == null) {
                return createFallbackImage();
            }
            return img;
        });
    }

    public GameEngine.AudioClip loadAudio(String filename) {
        return audioCache.computeIfAbsent(filename, f -> {
            GameEngine.AudioClip clip = engine.loadAudio(f);
            return clip;
        });
    }

    private Image createFallbackImage() {
        return getImage();
    }
}