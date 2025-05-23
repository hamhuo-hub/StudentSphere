package com.hamhuo.massey.slapocalypse.entity;

import com.hamhuo.massey.slapocalypse.core.AudioManager;
import com.hamhuo.massey.slapocalypse.core.GameMap;
import com.hamhuo.massey.slapocalypse.state.*;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EntityManager {
    private Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private final GameMap map;
    private final AudioManager audioManager;

    public EntityManager(GameMap map, AudioManager audioManager) {
        this.map = map;
        this.audioManager = audioManager;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void handleInput(KeyEvent e) {
        player.handleInput(e);
    }

    public void update(double dt, boolean inRhythmWindow) {
        // Sort entities by priority (descending)
        List<Entity> allEntities = new ArrayList<>();
        allEntities.add(player);
        allEntities.addAll(enemies);
        allEntities.sort(Comparator.comparingInt(Entity::getPriority).reversed());

        // Update entities in priority order
        for (Entity entity : allEntities) {
            entity.action(inRhythmWindow);
        }

        // Resolve movement conflicts
        resolveMovementConflicts(allEntities);

        // Process attacks and collisions
        if (inRhythmWindow) {
            checkCollisions(allEntities);
        }

        // Update animations
        for (Entity entity : allEntities) {
            entity.updateAnimation(dt);
        }

        // Check for game over
        if (player.getState() instanceof DeathState) {
            System.out.println("Game Over: Player died");
            System.exit(0);
        }
    }

    private void resolveMovementConflicts(List<Entity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e1 = entities.get(i);
            if (e1.getState() instanceof DeathState) continue;
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e2 = entities.get(j);
                if (e2.getState() instanceof DeathState) continue;
                if (e1.getX() == e2.getX() && e1.getY() == e2.getY()) {
                    // Lower-priority entity reverts position
                    if (e1.getPriority() > e2.getPriority()) {
                        e2.revertPosition();
                        e2.setState(new IdleState(e2));
                    } else {
                        e1.revertPosition();
                        e1.setState(new IdleState(e1));
                    }
                }
            }
        }
    }

    private void checkCollisions(List<Entity> entities) {
        for (Entity attacker : entities) {
            if (attacker.getState() instanceof AttackState && !attacker.hasAttackedThisBeat()) {
                Entity target = findTarget(attacker);
                if (target != null) {
                    applyDamage(attacker, target);
                    attacker.setAttackedThisBeat(true);
                } else {
                    // Attack rollback if no target
                    attacker.setState(new IdleState(attacker));
                    attacker.clearAttackPosition();
                }
            }
        }
    }

    private Entity findTarget(Entity attacker) {
        int tx = attacker.getAttackX();
        int ty = attacker.getAttackY();
        if (tx == -1 || ty == -1) return null;
        if (!(attacker instanceof Player) && player.getX() == tx && player.getY() == ty) {
            return player;
        }
        for (Enemy enemy : enemies) {
            if (enemy != attacker && enemy.getX() == tx && enemy.getY() == ty) {
                return enemy;
            }
        }
        return null;
    }

    private void applyDamage(Entity attacker, Entity target) {
        int damage = attacker.getAttack();
        target.takeDamage(damage);
        if (target.getHP() <= 0) {
            target.setState(new DeathState(target));
        } else {
            target.setState(new HurtState(target));
        }
        audioManager.playSoundEffect("HURT");
    }

    public void render(Graphics2D g) {
        int tileSize = map.getTileSize();
        List<Entity> allEntities = new ArrayList<>();
        allEntities.add(player);
        allEntities.addAll(enemies);
        for (Entity entity : allEntities) {
            if (!(entity.getState() instanceof DeathState)) {
                g.drawImage(entity.getCurrentFrame(), entity.getX() * tileSize, entity.getY() * tileSize, tileSize, tileSize, null);
            }
        }
    }
}