package com.myjava.game.Classes.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.myjava.game.Interfaces.Drawable;
import com.myjava.game.MyGame;

import java.util.ArrayList;
import java.util.Arrays;

public class Door implements Drawable {
    private ArrayList<Vector2> pos;
    private MyGame game;
    private SnakeMap map_;

    private int next_level;
    public int get_next_level() {
        return next_level;
    }

    private boolean is_open;

    public Door(MyGame game, SnakeMap map_, Vector2 ... V) {
        this.game = game;
        this.map_ = map_;
        this.is_open = false;
        this.next_level = 2;
        this.pos = new ArrayList<Vector2>();
        this.pos.addAll(Arrays.asList(V));
    }

    @Override public void draw() {
        this.game.batch.begin();
            for (int i = 0; i < this.pos.size(); i++) {
                TextureAtlas.AtlasRegion t = null;
                if (is_open)
                    t = this.game.atlas.findRegion(String.format("open-door-%d", i));
                else
                    t = this.game.atlas.findRegion(String.format("door-%d", i));
                this.game.batch.draw(t, this.pos.get(i).x, this.pos.get(i).y, 1, 1);
            }
        this.game.batch.end();
    }

    public void open() {
        this.is_open = true;
        for (Vector2 v : this.pos)
            this.map_.set_cell('O', (int) v.x, (int) v.y);
    }
}
