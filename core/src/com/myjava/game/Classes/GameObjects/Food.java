package com.myjava.game.Classes.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.myjava.game.Classes.Abstract.GridMap;
import com.myjava.game.Interfaces.Drawable;
import com.myjava.game.MyGame;

public class Food implements Drawable {
    private GridMap map_;
    private Vector2 pos;
    private MyGame game;

    public Food (MyGame game, GridMap map_) {
        this.map_ = map_;
        this.pos = new Vector2(-1, -1);
        this.game = game;
        this.spawn();
    }

    public void spawn() {
        int width = this.map_.get_width(), height = this.map_.get_height();
        do {
            this.pos.x = (int) (Math.random() * width);
            this.pos.y = (int) (Math.random() * height);
        } while (this.map_.get_cell((int) this.pos.x, (int) this.pos.y) != '\0');
    }

    public Vector2 get_pos() {
        return this.pos;
    }

    @Override
    public void draw() {
        TextureAtlas.AtlasRegion texture = this.game.atlas.findRegion("food");
        this.game.batch.begin();
        this.game.batch.draw(texture, this.pos.x, this.pos.y, 1, 1);
        this.game.batch.end();
    }
}
