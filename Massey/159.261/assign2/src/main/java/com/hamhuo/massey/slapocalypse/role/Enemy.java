package com.hamhuo.massey.slapocalypse.role;

import com.hamhuo.massey.slapocalypse.core.EnemyState;
import com.hamhuo.massey.slapocalypse.core.Animation;
import com.hamhuo.massey.slapocalypse.core.Conductor;

import java.util.Map;

public abstract class Enemy{

    //全局的节拍器
    protected Conductor conductor;

    //当前坐标
    protected int x, y;

    // 血量，攻击力
    protected int hp, attack;

    //状态
    protected EnemyState state;

    // 状态动画
    protected Map<EnemyState, Animation> anime;

    //打击反馈
    protected Map<EnemyState, Animation> audio;

    public Enemy(){
        // 拿到节拍器
        conductor = Conductor.getInstance();
        // 初始化状态
        state = EnemyState.IDLE;
    }

//    public Enemy(GameEngine engine, Conductor conductor, int x, int y) {
//        this.engine = engine;
//        this.conductor = conductor;
//        this.x = x;
//        this.y = y;
//        this.hp = 50;
//        this.attack = 5;
//        this.lastBeat = 0;
//    }

    // 切换状态
    protected void changeState(EnemyState newState) {
        if (state == newState) return;
        state = newState;
        // 重置动画
        Animation a = anime.get(state);
        if (a != null) {
            a.reset(); // Reset animation timer and frame
        }
    }

    public abstract void action();

//    public void update(Player player, GameMap map, double dt) throws InterruptedException {
//        // 动画更新
//        Animation a = anime.get(state);
//        if (a != null) a.update(dt);
//
//        // 如果节奏到达并且不是在受伤/死亡中
//        double songPos = conductor.getSongPosition();
//        if (state != EnemyState.HURT && state != EnemyState.DEAD &&
//                songPos > lastBeat + conductor.getCrotchet() && conductor.isInRhythmWindow(0.1)) {
//
//            // 简单AI
//            int dx = player.getX() - x;
//            int dy = player.getY() - y;
//            int nx = x, ny = y;
//            if (Math.abs(dx) > Math.abs(dy)) {
//                nx += dx > 0 ? 1 : -1;
//            } else {
//                ny += dy > 0 ? 1 : -1;
//            }
//
//            if (map.isValidMove(nx, ny)) {
//                x = nx; y = ny;
//                changeState(EnemyState.MOVING);
//            }
//            // 攻击判定
//            if (Math.abs(x - player.getX()) + Math.abs(y - player.getY()) <= 1) {
//                player.takeDamage(attack);
//                changeState(EnemyState.ATTACK);
//            }
//            lastBeat += conductor.getCrotchet();
//        }
//    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp <= 0) changeState(EnemyState.DEAD);
        else changeState(EnemyState.HURT);
    }

    public boolean isDeadFinished() {
        Animation a = anime.get(EnemyState.DEAD);
        return state == EnemyState.DEAD && a != null && a.isLastFrame();
    }
}