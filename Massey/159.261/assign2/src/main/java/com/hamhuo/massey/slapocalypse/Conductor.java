package com.hamhuo.massey.slapocalypse;


public class Conductor {
    private double bpm;          // 每分钟节拍数
    private double crotchet;     // 四分音符持续时间（秒）
    private double offset;       // 歌曲偏移（秒）
    private long startTime;      // 歌曲开始时间（毫秒）
    private double songPosition; // 当前歌曲位置（秒）
    private boolean isPlaying;   // 是否正在播放
    private GameEngine.AudioClip audioClip; // 音频剪辑
    private GameEngine gameEngine; // 游戏引擎实例

    public Conductor(GameEngine gameEngine, double bpm, double offset) {
        this.gameEngine = gameEngine;
        this.bpm = bpm;
        this.crotchet = 60.0 / bpm;
        this.offset = offset;
        this.isPlaying = false;
        this.songPosition = 0;
    }

    public void setAudioClip(GameEngine.AudioClip audioClip) {
        this.audioClip = audioClip;
    }

    public void startSong() {
        if (audioClip != null) {
            gameEngine.startAudioLoop(audioClip);
            startTime = gameEngine.getTime();
            isPlaying = true;
        }
    }

    public void update() {
        if (isPlaying) {
            long currentTime = gameEngine.getTime();
            songPosition = (currentTime - startTime) / 1000.0 - offset;
        }
    }

    public double getSongPosition() {
        return songPosition;
    }

    public double getCrotchet() {
        return crotchet;
    }

    public boolean isInRhythmWindow(double tolerance) {
        double beatTime = songPosition % crotchet;
        return beatTime < tolerance || beatTime > crotchet - tolerance;
    }
}