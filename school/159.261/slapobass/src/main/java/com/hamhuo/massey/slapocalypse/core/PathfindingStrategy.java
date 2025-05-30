package com.hamhuo.massey.slapocalypse.core;



import java.util.*;

public interface PathfindingStrategy {
    class PathNode {
        public int x, y;
        public PathNode(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    PathNode findNextStep(int startX, int startY, int targetX, int targetY, GameMap map);

    class AStarPathfinding implements PathfindingStrategy {
        @Override
        public PathNode findNextStep(int startX, int startY, int targetX, int targetY, GameMap map) {
            int cols = map.getWidth();
            int rows = map.getHeight();

            class Node {
                int x, y;
                double g, f;
                Node parent;

                Node(int x, int y, double g, double h, Node parent) {
                    this.x = x;
                    this.y = y;
                    this.g = g;
                    this.f = g + h;
                    this.parent = parent;
                }
            }
            PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));
            boolean[][] closed = new boolean[cols][rows];
            Node[][] bestNode = new Node[cols][rows];

            double h0 = Math.abs(startX - targetX) + Math.abs(startY - targetY);
            Node start = new Node(startX, startY, 0, h0, null);
            open.add(start);
            bestNode[startX][startY] = start;

            Node goal = null;
            int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

            while (!open.isEmpty()) {
                Node cur = open.poll();
                if (closed[cur.x][cur.y]) continue;
                if (cur.x == targetX && cur.y == targetY) {
                    goal = cur;
                    break;
                }
                closed[cur.x][cur.y] = true;

                for (int[] d : dirs) {
                    int nx = cur.x + d[0], ny = cur.y + d[1];
                    if (nx < 0 || nx >= cols || ny < 0 || ny >= rows || !map.isValidMove(nx, ny) || closed[nx][ny]) {
                        continue;
                    }
                    double ng = cur.g + 1;
                    double nh = Math.abs(nx - targetX) + Math.abs(ny - targetY);
                    Node prev = bestNode[nx][ny];
                    if (prev == null || ng < prev.g) {
                        Node next = new Node(nx, ny, ng, nh, cur);
                        bestNode[nx][ny] = next;
                        open.add(next);
                    }
                }
            }

            if (goal == null) return null;

            Node step = goal;
            while (step.parent != null && !(step.parent.x == startX && step.parent.y == startY)) {
                step = step.parent;
            }
            return new PathNode(step.x, step.y);
        }
    }

    class BFSPathfinding implements PathfindingStrategy {
        @Override
        public PathNode findNextStep(int startX, int startY, int targetX, int targetY, GameMap map) {
            int cols = map.getWidth();
            int rows = map.getHeight();

            class Node {
                int x, y;
                Node parent;

                Node(int x, int y, Node parent) {
                    this.x = x;
                    this.y = y;
                    this.parent = parent;
                }
            }
            Queue<Node> queue = new LinkedList<>();
            boolean[][] visited = new boolean[cols][rows];
            Node[][] parentMap = new Node[cols][rows];

            Node start = new Node(startX, startY, null);
            queue.add(start);
            visited[startX][startY] = true;
            parentMap[startX][startY] = start;

            Node goal = null;
            int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

            while (!queue.isEmpty()) {
                Node cur = queue.poll();
                if (cur.x == targetX && cur.y == targetY) {
                    goal = cur;
                    break;
                }
                for (int[] d : dirs) {
                    int nx = cur.x + d[0], ny = cur.y + d[1];
                    if (nx < 0 || nx >= cols || ny < 0 || ny >= rows || visited[nx][ny] || !map.isValidMove(nx, ny)) {
                        continue;
                    }
                    Node next = new Node(nx, ny, cur);
                    queue.add(next);
                    visited[nx][ny] = true;
                    parentMap[nx][ny] = next;
                }
            }

            if (goal == null) return null;

            Node step = goal;
            while (step.parent != null && !(step.parent.x == startX && step.parent.y == startY)) {
                step = step.parent;
            }
            return new PathNode(step.x, step.y);
        }
    }

    class GreedyBestFirstPathfinding implements PathfindingStrategy {
        @Override
        public PathNode findNextStep(int startX, int startY, int targetX, int targetY, GameMap map) {
            int cols = map.getWidth();
            int rows = map.getHeight();

            class Node {
                int x, y;
                double h;
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

            double h0 = Math.abs(startX - targetX) + Math.abs(startY - targetY);
            Node start = new Node(startX, startY, h0, null);
            open.add(start);
            visited[startX][startY] = true;

            Node bestNode = start;
            int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

            while (!open.isEmpty()) {
                Node cur = open.poll();
                if (cur.x == targetX && cur.y == targetY) {
                    bestNode = cur;
                    break;
                }
                for (int[] d : dirs) {
                    int nx = cur.x + d[0], ny = cur.y + d[1];
                    if (nx < 0 || nx >= cols || ny < 0 || ny >= rows || visited[nx][ny] || !map.isValidMove(nx, ny)) {
                        continue;
                    }
                    double nh = Math.abs(nx - targetX) + Math.abs(ny - targetY);
                    Node next = new Node(nx, ny, nh, cur);
                    open.add(next);
                    visited[nx][ny] = true;
                }
            }

            Node step = bestNode;
            while (step.parent != null && !(step.parent.x == startX && step.parent.y == startY)) {
                step = step.parent;
            }
            return new PathNode(step.x, step.y);
        }
    }
}