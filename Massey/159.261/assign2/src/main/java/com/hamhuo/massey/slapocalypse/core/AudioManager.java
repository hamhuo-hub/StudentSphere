package com.hamhuo.massey.slapocalypse.core;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private GameEngine engine;
    private Map<String, GameEngine.AudioClip> soundEffects;
    private GameEngine.AudioClip backgroundMusic;
    private double songPosition;

    public AudioManager(GameEngine engine) {
        this.engine = engine;
        this.soundEffects = new HashMap<>();
    }

    public void loadBackgroundMusic(String path) {
        backgroundMusic = engine.loadAudio(path);
    }

    public void loadSoundEffect(String state, String path) {
        soundEffects.put(state, engine.loadAudio(path));
    }

    public void playBackgroundMusic() {
        if (backgroundMusic != null) {
            engine.startAudioLoop(backgroundMusic);
        }
    }

    public void playSoundEffect(String state) {
        GameEngine.AudioClip clip = soundEffects.get(state);
        if (clip != null) {
            engine.playAudio(clip);
        }
    }

    public void updateSongPosition(double position) {
        this.songPosition = position;
    }

    public double getSongPosition() {
        return songPosition;
    }
}