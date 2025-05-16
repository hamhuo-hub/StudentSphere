package com.hamhuo.massey.slapocalypse.entity.enemy;

import com.hamhuo.massey.slapocalypse.core.*;
import com.hamhuo.massey.slapocalypse.entity.Entity;
import com.hamhuo.massey.slapocalypse.entity.GameMap;
import com.hamhuo.massey.slapocalypse.enums.Direction;
import com.hamhuo.massey.slapocalypse.enums.State;

import java.awt.event.KeyEvent;
import java.util.*;

/**
 * 粉色史莱姆：每 moveBeatInterval 拍使用贪心最佳优先搜索移动或攻击一次。
 */
public class PinkSlime extends Entity {
    private final Entity player;
    private final GameMap map;
    private boolean hasActedThisBeat; // Prevents multiple actions in the same rhythm window
    private int beatCount; // Tracks number of beats passed
    private final int moveBeatInterval; // Number of beats between moves (e.g., 2 for every two beats)

    // Constructor initializes position, animation, and beat interval
    public PinkSlime(int startX, int startY, Animation anim, Entity player, GameMap map) {
        this.x = startX;
        this.y = startY;
        this.animation = anim;
        this.player = player;
        this.map = map;
        this.hasActedThisBeat = false;
        this.beatCount = 0;
        this.moveBeatInterval = 3; // Default: move every 2 beats
    }

    @Override
    public void action(boolean inRhythmWindow) {
        if (inRhythmWindow && !hasActedThisBeat) {
            System.out.println("PinkSlime Beat count: " + beatCount);
            beatCount++;
            if (beatCount >= moveBeatInterval) {
                beatStepGreedy(); // Move or attack using Greedy Best-First
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
        // PinkSlime does not handle keyboard input
    }

    // Performs Greedy Best-First search to move toward player or attack if adjacent
    private void beatStepGreedy() {
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
                System.out.println("PinkSlime Attacking player, direction: " + direction);
            } else {
                direction = toPlayer;
                state = State.IDLE;
                System.out.println("PinkSlime Turning to face player, direction: " + direction);
            }
            return;
        }

        // Greedy Best-First setup
        class Node {
            int x, y;
            double h; // Heuristic: Manhattan distance to player
            Node parent;

            Node(int x, int y, double h, Node parent) {
                this.x = x;
                this.y = y;
                this.h = h;
                this.parent = parent;
            }
        }
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.h));
        boolean[][] visited = new boolean[cols][rows];

        // Initialize starting node
        double h0 = Math.abs(sx - px) + Math.abs(sy - py);
        Node start = new Node(sx, sy, h0, null);
        open.add(start);
        visited[sx][sy] = true;

        Node bestNode = start;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        // Greedy Best-First search
        while (!open.isEmpty()) {
            Node cur = open.poll();
            if (cur.x == px && cur.y == py) {
                bestNode = cur;
                break;
            }

            for (int[] d : dirs) {
                int nx = cur.x + d[0], ny = cur.y + d[1];
                if (nx < 0 || nx >= cols || ny < 0 || ny >= rows || visited[nx][ny] || !map.isValidMove(nx, ny)) {
                    continue;
                }
                double nh = Math.abs(nx - px) + Math.abs(ny - py);
                Node next = new Node(nx, ny, nh, cur);
                open.add(next);
                visited[nx][ny] = true;
            }
        }

        // Trace back to first step
        Node step = bestNode;
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
        System.out.println("PinkSlime Moving to (" + x + ", " + y + "), direction: " + direction);
    }
}