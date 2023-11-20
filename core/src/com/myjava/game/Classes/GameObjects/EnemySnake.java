package com.myjava.game.Classes.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.myjava.game.Classes.Abstract.Snake;
import com.myjava.game.Classes.Pathfinder;
import com.myjava.game.MyGame;

import java.util.Iterator;
import java.util.LinkedList;

public class EnemySnake extends Snake {
    private Pathfinder pathfinder;
    private Iterator<Vector2> path_iterator;

    public EnemySnake(MyGame game, SnakeMap map_, float speed, Vector2 ... V) {
        super.segments = new LinkedList<>();
        super.map_ = map_;
        super.game = game;
        super.dt_counter = 0f;
        super.speed = speed;
        super.strength = 50;
        super.is_alive = true;

        this.pathfinder = null;
        this.path_iterator = new Iterator<Vector2>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Vector2 next() {
                return null;
            }

            @Override
            public void remove() {
                return;
            }
        };

        for (Vector2 v: V ) {
            this.segments.push(v);
            this.map_.set_cell('E', (int) v.x, (int) v.y);
        }
    }

    @Override
    public boolean collides_with_snake() {
        Vector2 head = super.segments.getFirst();
        char c = super.map_.get_cell((int) head.x, (int) head.y);
        return  (c == 'S');
    }

    @Override
    public void draw() {
        super.game.batch.begin();
        TextureAtlas.AtlasRegion body_texture = super.game.atlas.findRegion("enemy_body");
        TextureAtlas.AtlasRegion head_texture = super.game.atlas.findRegion("enemy_head");

        for (Vector2 v : super.segments)
            super.game.batch.draw(body_texture, v.x, v.y, 1, 1);

        Vector2 head = this.segments.getFirst();
        super.game.batch.draw(head_texture, head.x, head.y, 1, 1);
        super.game.batch.end();
    }

    @Override
    public void move(float delta) {
        if (this.pathfinder == null || !this.path_iterator.hasNext()) {
            this.pathfinder = new Pathfinder(map_, super.segments.getFirst(), super.map_.food.get_pos());
            while (!pathfinder.compute()) {
                super.map_.food.spawn();
                this.pathfinder = new Pathfinder(super.map_, super.segments.getFirst(), super.map_.food.get_pos());
            }
            this.path_iterator = this.pathfinder.get_iterator();
            this.path_iterator.next();
        }

        this.dt_counter += delta * this.speed;
        if (this.dt_counter >= 1) {

            Vector2 tail = this.segments.removeLast();
            Vector2 head = this.segments.getFirst();
            super.map_.set_cell('\0', (int) tail.x, (int) tail.y);
            super.map_.set_cell('E', (int) head.x, (int) head.y);

            Vector2 new_head = this.path_iterator.next();
            super.segments.addFirst(new_head);

            super.collides_with_food();
            super.dt_counter--;
        }
    }

    public void clear() {
        for (Vector2 v: super.segments)
            super.map_.set_cell('\0', (int) v.x, (int) v.y);
    }
}
