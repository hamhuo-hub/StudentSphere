package com.hamhuo.massey.slapocalypse.core;


public class Conductor {

    //需要保证全局单例，所以使用工厂模式确保在类内实例化，静态初始化节拍器
    private static final Conductor CON = new Conductor();
    private double bpm;          // 每分钟节拍数
    private double crotchet;     // 四分音符持续时间（秒）
    private double offset;       // 歌曲偏移（秒）
    private long startTime;      // 歌曲开始时间（毫秒）
    private double songPosition; // 当前歌曲位置（秒）
    private boolean isPlaying;   // 是否正在播放
    private GameEngine.AudioClip audioClip; // 音频剪辑
    private GameEngine gameEngine; // 游戏引擎实例

    private Conductor(GameEngine gameEngine, double bpm, double offset) {
        this.gameEngine = gameEngine;
        this.bpm = bpm;
        this.crotchet = 60.0 / bpm;
        this.offset = offset;
        this.isPlaying = false;
        this.songPosition = 0;
    }

    private Conductor() {
        ;
    }

    // 单例，唯一的外部出口
    public static Conductor getInstance() {
        return CON;
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