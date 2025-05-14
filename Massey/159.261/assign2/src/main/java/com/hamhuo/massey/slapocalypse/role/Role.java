package com.hamhuo.massey.slapocalypse.role;

import com.hamhuo.massey.slapocalypse.core.Animation;
import com.hamhuo.massey.slapocalypse.core.Conductor;
import com.hamhuo.massey.slapocalypse.core.State;

public abstract class Role {
    //全局的节拍器
    protected Conductor conductor;

    // 敌人血量，攻击力
    protected int hp, attack;

    // 初始坐标
    protected int x, y;

    // 初始化状态
    protected State state;

    // 动画资源
    protected Animation animation;

}
