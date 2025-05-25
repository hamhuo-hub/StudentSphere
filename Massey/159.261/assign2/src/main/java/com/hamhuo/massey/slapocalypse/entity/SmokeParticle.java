package com.hamhuo.massey.slapocalypse.entity;

import com.hamhuo.massey.slapocalypse.core.GameEngine;

import java.awt.Image;
import java.awt.AlphaComposite;

public class SmokeParticle extends Particle {
    private Image image;
    private double angle;
    private double rotationspeed;

    public SmokeParticle(Image image) {
        super();
        this.image = image;
    }

    @Override
    public void init(float startx, float starty, float emitterangle) {
        super.init(startx, starty, emitterangle);
        angle = Math.random() * 360.0;
        rotationspeed = Math.random() * 20.0 - 10.0;
    }

    @Override
    public void draw(GameEngine g) {
        if (notvisible()) return;
        g.saveCurrentTransform();
        g.translate(x, y - 20);
        g.rotate(angle);
        g.scale(1.0 * scale, 1.0 * scale);
        g.mGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.drawImage(image, -64, -64);
        g.mGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g.restoreLastTransform();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        time += dt;
        angle = time + 30 * rotationspeed * Math.exp(-time * 2.0);
        scale = (float) (0.1 + 0.10 * Math.log(time * 100.0 + 1));
        alpha = (float)(0.7 * Math.exp(-time * 2.5));
    }
}