package com.hamhuo.massey.slapocalypse.entity;

import java.awt.Image;
import com.hamhuo.massey.slapocalypse.core.GameEngine;

import java.util.ArrayList;
import java.util.List;

import static com.hamhuo.massey.slapocalypse.core.GameEngine.loadImage;

public class SmokeParticleEmitter extends ParticleEmitter {
    private Image[] images;
    private boolean emitting = false;
    private float emissionRate = 0;
    private float x;
    private float y;
    private int globalFrameIndex = 0;
    private List<SmokeParticle> particles = new ArrayList<>();
    private int burstParticlesRemaining = 0;
    private float burstInterval = 0;
    private float timeSinceLastBurst = 0;

    public SmokeParticleEmitter() {
        super(120, 100, 0.04f, 40, 0.1f);
        int numFlames = 17;
        images = new Image[numFlames];
        //images[0]=GameEngine.loadImage("src/main/resources/smoke/smoke1.png");
        //images[1]=GameEngine.loadImage("src/main/resources/smoke/smoke2.png");
        //images[2]=GameEngine.loadImage("src/main/resources/smoke/smoke3.png");
        for (int i = 0; i < numFlames; i++) {
            images[i] = loadImage("src/main/resources/ui/fire/Fire6/png_" + i + ".png");
        }

    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        move(x, y);
    }

    public void setEmitting(boolean emitting) {
        this.emitting = emitting;
        this.emissionRate = emitting ? 100 : 0;
    }

    public boolean isEmpty() {
        return particles.isEmpty();
    }

    public void emitBurst(int numParticles) {
        burstParticlesRemaining = numParticles;
        burstInterval = 0.7f / numParticles; // Spread 38 particles over 0.7 seconds
        timeSinceLastBurst = burstInterval; // Trigger first particle immediately
    }

    @Override
    public Particle newParticle() {
        SmokeParticle p = new SmokeParticle(images[globalFrameIndex]);
        globalFrameIndex = (globalFrameIndex + 1) % images.length;
        return p;
    }

    public void update(double dt) {
        List<SmokeParticle> toRemove = new ArrayList<>();
        for (SmokeParticle particle : particles) {
            particle.update((float) dt);
            if (particle.notvisible()) {
                toRemove.add(particle);
            }
        }
        particles.removeAll(toRemove);

        if (emitting) {
            int numParticles = (int)(emissionRate * dt);
            for (int i = 0; i < numParticles; i++) {
                SmokeParticle particle = (SmokeParticle) newParticle();
                particle.init(x, y, this.angle);
                particles.add(particle);
            }
        }

        if (burstParticlesRemaining > 0) {
            timeSinceLastBurst += (float) dt;
            while (timeSinceLastBurst >= burstInterval && burstParticlesRemaining > 0) {
                SmokeParticle particle = (SmokeParticle) newParticle();
                particle.init(x, y, this.angle);
                particles.add(particle);
                burstParticlesRemaining--;
                timeSinceLastBurst -= burstInterval;
            }
        }
    }

    public void draw(GameEngine game) {
        for (SmokeParticle particle : particles) {
            particle.draw(game);
        }
    }
}