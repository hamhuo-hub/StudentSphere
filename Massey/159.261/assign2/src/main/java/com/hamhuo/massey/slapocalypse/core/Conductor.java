// src/main/java/com/hamhuo/massey/slapocalypse/core/Conductor.java
package com.hamhuo.massey.slapocalypse.core;

/**
 * 单例节拍器：记录 startTime, BPM, offset，
 * 并提供落拍窗口判断。
 */
public class Conductor {
    private static final Conductor CON = new Conductor();
    private GameEngine engine;
    private double bpm=10, crotchet=0.5, offset=0;
    private long startTime;
    private double songPos=0;
    private boolean isPlaying=false;
    private GameEngine.AudioClip clip;

    private Conductor(){}

    public static Conductor getInstance(){return CON;}

    public void init(GameEngine eng, double bpm, double offset){
        this.engine=eng;
        this.bpm=bpm;
        this.crotchet=60.0/bpm;
        this.offset=offset;
    }
    public void setAudioClip(GameEngine.AudioClip c){this.clip=c;}
    public void startSong(){
        if (clip!=null && engine!=null){
            engine.startAudioLoop(clip);
            startTime=engine.getTime();
            isPlaying=true;
        }
    }
    public void update(){
        if(isPlaying&&engine!=null){
            long now=engine.getTime();
            songPos=(now-startTime)/1000.0 - offset;
        }
    }
    public boolean isInRhythmWindow(double tol){
        double t = songPos % crotchet;
        return t<tol || t>crotchet-tol;
    }
}
