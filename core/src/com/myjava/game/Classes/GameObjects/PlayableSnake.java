package com.myjava.game.Classes.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.myjava.game.Classes.Abstract.Snake;
import com.myjava.game.Classes.SnakePlayer;
import com.myjava.game.Interfaces.Playable;
import com.myjava.game.MyGame;

import java.util.LinkedList;

public class PlayableSnake extends Snake implements Playable {
    protected Vector2 direction;
    protected SnakePlayer player;
    protected float sprint_speed;
    public boolean is_sprinting;
    private int stamina;
    private static final int MAX_STAMINA = 10;

    public PlayableSnake(MyGame game, SnakeMap map_, float speed, Vector2 ... V) {
        super.segments = new LinkedList<>();
        super.map_ = map_;
        super.game = game;
        super.dt_counter = 0f;
        super.speed = speed;
        super.strength = 0;
        super.is_alive = true;

        this.direction = new Vector2();

        this.sprint_speed = speed + 5f;
        this.stamina = MAX_STAMINA;
        this.is_sprinting = false;

        for (Vector2 v: V ) {
            super.segments.push(v);
            super.map_.set_cell('S', (int) v.x, (int) v.y);
        }
    }

    public boolean collides_with_terrain() {
        Vector2 head = super.segments.getFirst();
        char c = super.map_.get_cell((int) head.x, (int) head.y);
        return  (c == 'B' || c == 'S');
    }
    @Override
    public boolean collides_with_snake() {
        Vector2 head = super.segments.getFirst();
        char c = super.map_.get_cell((int) head.x, (int) head.y);
        return  (c == 'E');
    }

    public boolean enters_door() {
        Vector2 head = super.segments.getFirst();
        char c = super.map_.get_cell((int) head.x, (int) head.y);
        return  (c == 'O');
    }

    @Override
    public void draw() {
        this.game.batch.begin();
        TextureAtlas.AtlasRegion body_texture = this.game.atlas.findRegion("body");
        TextureAtlas.AtlasRegion head_texture = this.game.atlas.findRegion("head_up");

        if (this.direction.equals(new Vector2(1, 0))) head_texture = this.game.atlas.findRegion("head_right");
        else if (this.direction.equals(new Vector2(-1, 0))) head_texture = this.game.atlas.findRegion("head_left");
        else if (this.direction.equals(new Vector2(0, 1))) head_texture = this.game.atlas.findRegion("head_up");
        else if (this.direction.equals(new Vector2(0,-1))) head_texture = this.game.atlas.findRegion("head_down");

        for (Vector2 v : this.segments)
            this.game.batch.draw(body_texture, v.x, v.y, 1, 1);

        Vector2 head = this.segments.getFirst();
        this.game.batch.draw(head_texture, head.x, head.y, 1, 1);
        this.game.batch.end();
    }

    @Override
    public void move(float delta) {
        if (is_sprinting)
            super.dt_counter += delta * this.sprint_speed;
        else
            super.dt_counter += delta * super.speed;

        if (super.dt_counter >= 1) {
            if (is_sprinting) {
                this.stamina--;
                if (this.stamina <= 0) {
                    this.stamina = 0;
                    this.is_sprinting = false;
                }
            } else if (this.stamina < MAX_STAMINA)
                this.stamina = Math.min(this.stamina + 2, MAX_STAMINA);

            Vector2 head = super.segments.getFirst();
            Vector2 tail = super.segments.removeLast();
            super.map_.set_cell('\0', (int) tail.x, (int) tail.y);
            super.map_.set_cell('S', (int) head.x, (int) head.y);

            Vector2 new_head = head.cpy().add(this.direction);
            new_head.x = new_head.x >= super.map_.get_width() ? 0 : new_head.x;
            new_head.x = new_head.x < 0 ? super.map_.get_width() - 1: new_head.x;
            new_head.y = new_head.y >= super.map_.get_height() ? 0 : new_head.y;
            new_head.y = new_head.y < 0 ? super.map_.get_height() - 1: new_head.y;

            super.segments.addFirst(new_head);

            if (super.collides_with_food()) {
                super.segments.addLast(tail.cpy());
                super.strength += 10;
                this.player.add_score(1);
            }

            if (this.collides_with_terrain()) {
                super.is_alive = false;
                super.segments.addLast(tail.cpy());
                super.segments.removeFirst();
            }

            super.dt_counter--;
        }
    }

    @Override
    public void set_direction(int x, int y) {
        Vector2 forehead = super.segments.get(1);
        Vector2 head = super.segments.getFirst();
        if (new Vector2(x, y).add(head).equals(forehead)) return;

        this.direction.set(x, y);
    }
    @Override
    public void set_player(SnakePlayer player) {
        this.player = player;
    }

    public int get_stamina() {
        return this.stamina;
    }

    @Override
    public void set_sprinting(boolean is_sprinting) {
        this.is_sprinting = is_sprinting;
    }
}
