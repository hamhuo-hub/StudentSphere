package com.hamhuo.massey.slapocalypse.entity.enemy;

import com.hamhuo.massey.slapocalypse.core.*;
import com.hamhuo.massey.slapocalypse.entity.Entity;
import com.hamhuo.massey.slapocalypse.entity.GameMap;
import com.hamhuo.massey.slapocalypse.enums.Direction;
import com.hamhuo.massey.slapocalypse.enums.State;


import java.awt.event.KeyEvent;
import java.util.*;

/**
 * 蓝色史莱姆：每 moveBeatInterval 拍移动或攻击一次，使用 A* 算法追逐玩家。
 */
public class BlueSlime extends Entity {
    private final Entity player;
    private final GameMap map;
    private boolean hasActedThisBeat; // Prevents multiple actions in the same rhythm window
    private int beatCount; // Tracks number of beats passed
    private final int moveBeatInterval; // Number of beats between moves (e.g., 2 for every two beats)

    // Constructor initializes position, animation, and beat interval
    public BlueSlime(int startX, int startY, Animation anim, Entity player, GameMap map) {
        this.x = startX;
        this.y = startY;
        this.animation = anim;
        this.player = player;
        this.map = map;
        this.hasActedThisBeat = false;
        this.beatCount = 0;
        this.moveBeatInterval = 1; // Default: move every 2 beats
    }

    @Override
    public void action(boolean inRhythmWindow) {
        if (inRhythmWindow && !hasActedThisBeat) {
            System.out.println("Beat count: " + beatCount);
            beatCount++;
            if (beatCount >= moveBeatInterval) {
                beatStepAStar(); // Move or attack only when beatCount reaches interval
                beatCount = 0; // Reset after action
            } else {
                state = State.IDLE; // Stay idle on non-move beats
            }
            hasActedThisBeat = true;
        } else if (!inRhythmWindow) {
            hasActedThisBeat = false;
            state = State.IDLE; // Reset to idle outside rhythm window
        }
    }

    @Override
    public void handleInput(KeyEvent e) {
        // BlueSlime does not handle keyboard input
    }

    // Performs A* pathfinding to move toward player or attack if adjacent
    private void beatStepAStar() {
        int cols = map.getWidth();
        int rows = map.getHeight();
        int sx = x / 32, sy = y / 32;
        int px = player.getX() / 32, py = player.getY() / 32;

        int dx = px - sx, dy = py - sy;
        int dist = Math.abs(dx) + Math.abs(dy);

        // Handle adjacent player: attack if facing, otherwise turn
        if (dist == 1) {
            Direction toPlayer;
            if (dx == 1) toPlayer = Direction.RIGHT;
            else if (dx == -1) toPlayer = Direction.LEFT;
            else if (dy == 1) toPlayer = Direction.DOWN;
            else toPlayer = Direction.UP;

            if (direction == toPlayer) {
                state = State.ATTACK;
            } else {
                direction = toPlayer;
                state = State.IDLE;
            }
            return;
        }

        // A* pathfinding setup
        class Node {
            int x, y;
            double g, f;
            Node parent;

            Node(int x, int y, double g, double h, Node p) {
                this.x = x;
                this.y = y;
                this.g = g;
                this.f = g + h;
                this.parent = p;
            }
        }
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));
        boolean[][] closed = new boolean[cols][rows];
        Node[][] bestNode = new Node[cols][rows];

        // Initialize starting node
        double h0 = Math.abs(sx - px) + Math.abs(sy - py);
        Node start = new Node(sx, sy, 0, h0, null);
        open.add(start);
        bestNode[sx][sy] = start;

        Node goal = null;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        // A* search
        while (!open.isEmpty()) {
            Node cur = open.poll();
            if (closed[cur.x][cur.y]) continue;
            if (cur.x == px && cur.y == py) {
                goal = cur;
                break;
            }
            closed[cur.x][cur.y] = true;

            for (int[] d : dirs) {
                int nx = cur.x + d[0], ny = cur.y + d[1];
                if (nx < 0 || nx >= cols || ny < 0 || ny >= rows) continue;
                if (!map.isValidMove(nx, ny) || closed[nx][ny]) continue;

                double ng = cur.g + 1;
                double nh = Math.abs(nx - px) + Math.abs(ny - py);
                Node prev = bestNode[nx][ny];

                if (prev == null || ng < prev.g) {
                    Node next = new Node(nx, ny, ng, nh, cur);
                    bestNode[nx][ny] = next;
                    open.add(next);
                }
            }
        }

        // If no path found, stay idle
        if (goal == null) {
            state = State.IDLE;
            return;
        }

        // Trace back to first step
        Node step = goal;
        while (step.parent != null && !(step.parent.x == sx && step.parent.y == sy)) {
            step = step.parent;
        }

        // Set direction and move
        if (step.x > sx) direction = Direction.RIGHT;
        else if (step.x < sx) direction = Direction.LEFT;
        else if (step.y > sy) direction = Direction.DOWN;
        else direction = Direction.UP;

        x = step.x * 32;
        y = step.y * 32;
        state = State.RUN;
    }
}