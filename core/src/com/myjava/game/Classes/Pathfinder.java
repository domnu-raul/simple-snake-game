package com.myjava.game.Classes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.myjava.game.Classes.Abstract.GridMap;

import java.util.*;

public class Pathfinder {

    private final GridMap game_map;
    private final Vector2 source, dest;
    private final Node [][] node_map;
    private final PriorityQueue<Node> open_queue;
    private Node path_node;
    private static final Vector2[] DIRECTIONS = {
            new Vector2(1, 0),
            new Vector2(-1, 0),
            new Vector2(0, 1),
            new Vector2(0, -1)
    };

    private class Node {
        private Vector2 pos;
        private int g_cost, h_cost, f_cost;
        private Node parent;
        private boolean is_closed;

        public Node(){};
        public Node(Vector2 pos) {
            this.pos = pos;
            this.g_cost = 0;
            this.f_cost = this.h_cost = (int) (Math.abs(dest.x - pos.x) + Math.abs(dest.y - pos.y));
            this.parent = null;
            this.is_closed = false;
        }

        public Node(Vector2 pos, Node parent) {
            this.pos = pos;
            this.parent = parent;
            this.g_cost = this.parent.g_cost + 1;
            this.h_cost = (int) (Math.abs(dest.x - this.pos.x) + Math.abs(dest.y - this.pos.y));
            this.f_cost = this.g_cost + this.h_cost;
            this.is_closed = false;
        }

        public void render_neighbours() {
            for (Vector2 v: DIRECTIONS) {
                Vector2 n_pos = this.pos.cpy().add(v);

                n_pos.x = n_pos.x >= game_map.get_width() ? 0 : n_pos.x;
                n_pos.x = n_pos.x < 0 ? game_map.get_width() - 1: n_pos.x;
                n_pos.y = n_pos.y >= game_map.get_height() ? 0 : n_pos.y;
                n_pos.y = n_pos.y < 0 ? game_map.get_height() - 1: n_pos.y;

                char cell = game_map.get_cell((int) n_pos.x, (int) n_pos.y);
                if (cell == 'B' || cell == 'E') continue;

                Node n = node_map[(int) n_pos.y][(int) n_pos.x];
                if (n != null) {
                    if (!n.is_closed)
                        n.update_cost(this);
                    continue;
                }
                n = new Node(n_pos, this);
                node_map[(int) n_pos.y][(int) n_pos.x] = n;
                open_queue.add(n);
            }
        }

        public Vector2 get_pos() {
            return this.pos;
        }

        public void set_closed() {
            this.is_closed = true;
        }

        private void update_cost(Node new_parent) {
            int new_g_cost = new_parent.g_cost + 1;
            int new_f_cost = new_g_cost + this.h_cost;
            if (new_f_cost < this.f_cost) {
                this.g_cost = new_g_cost;
                this.f_cost = new_f_cost;
                this.parent = new_parent;
            }
        }
    }
    private class NodeComparator extends Node implements Comparator<Node> {
        public int compare(Node a, Node b) {
            return -1 * Integer.compare(b.f_cost, a.f_cost);
        }
    }

    public Pathfinder(GridMap game_map, Vector2 source, Vector2 dest) {
        this.game_map = game_map;
        this.source = source;
        this.dest = dest;

        this.node_map = new Node[game_map.get_height()][game_map.get_width()];
        this.open_queue = new PriorityQueue<Node>(new NodeComparator());
    }

    public boolean compute() {
        if (this.path_node != null) return true;

        Node current;
        open_queue.add(new Node(source));

        do {
            current = open_queue.poll();
            if (current == null) return false;
            current.render_neighbours();
            current.set_closed();
        } while (!current.get_pos().equals(dest));

        this.path_node = current;
        return true;
    }

    public LinkedList<Vector2> get_vectors() {
        if (this.path_node == null) return null;

        LinkedList<Vector2> L = new LinkedList<>();
        Node current = this.path_node;

        while (current != null) {
            L.addFirst(current.get_pos());
            current = current.parent;
        }

        return L;
    }

    public Iterator<Vector2> get_iterator() {
        return this.get_vectors().iterator();
    }
}
