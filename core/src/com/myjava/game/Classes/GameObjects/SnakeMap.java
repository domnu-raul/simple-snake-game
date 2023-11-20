package com.myjava.game.Classes.GameObjects;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.myjava.game.MyGame;
import com.myjava.game.GameFiles;

public class SnakeMap extends com.myjava.game.Classes.Abstract.GridMap {
    private MyGame game;
    public Food food;
    public Door door;
    public SnakeMap(int level, MyGame game) {

        Pixmap map_grid = new Pixmap(GameFiles.get_level_grid(level));
        super.height = map_grid.getHeight();
        super.width = map_grid.getWidth();
        super.grid = new char[super.height][super.width];

        for (int x = 0; x < super.width; x++) {
            for (int y = 0; y < super.height; y++) {
                int pixel_color = map_grid.getPixel(x, super.height - y - 1);
                if (pixel_color == 255)
                    super.grid[y][x] = 'B';
                else
                    super.grid[y][x] = '\0';
            }
        }

        this.game = game;

        this.game.load_textures(
                GameFiles.get_level_graphics(level),
                16,
                "body",
                "head_right",
                "head_up",
                "head_down",
                "head_left",
                "food",
                "background",
                "brick",
                "door-0",
                "door-1",
                "open-door-0",
                "open-door-1",
                "enemy_body",
                "enemy_head"
                );

        this.food = new Food(this.game, this);
        JsonValue json_door_pos = new JsonReader().parse(GameFiles.get_level_data(level)).get("door_pos");
        this.door = new Door(this.game, this,
                new Vector2(json_door_pos.get(0).getFloat("x"), json_door_pos.get(0).getFloat("y")),
                new Vector2(json_door_pos.get(1).getFloat("x"), json_door_pos.get(1).getFloat("y"))
        );
    }

    @Override
    public void set_cell(char c, int x, int y) {
        if (super.grid[y][x] != 'O')
            super.grid[y][x] = c;
    }
    @Override
    public void draw() {
        TextureAtlas.AtlasRegion block = this.game.atlas.findRegion("brick");
        TextureAtlas.AtlasRegion bg = this.game.atlas.findRegion("background");
        this.game.batch.begin();
        for (int x = 0; x < super.width; x++) {
            for (int y = 0; y < super.height; y++) {
                if (super.grid[y][x] == 'B')
                    this.game.batch.draw(block, x, y, 1, 1);
                else
                    this.game.batch.draw(bg, x, y, 1, 1);
            }
        }
        this.game.batch.end();
        this.food.draw();
        this.door.draw();
    }
}
