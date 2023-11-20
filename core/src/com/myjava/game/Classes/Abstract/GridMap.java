package com.myjava.game.Classes.Abstract;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.myjava.game.Interfaces.Drawable;

public abstract class GridMap implements Drawable {
    public int get_height() {
        return height;
    }

    public int get_width() {
        return width;
    }

    protected int height;
    protected int width;

    protected char[][] grid;

    public char get_cell(int x, int y) {
        return this.grid[y][x];
    }
    public void set_cell(char c, int x, int y) {
        this.grid[y][x] = c;
    }

    public GridMap() {};
    public GridMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
    }
}
