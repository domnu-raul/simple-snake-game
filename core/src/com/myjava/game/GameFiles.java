package com.myjava.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public enum GameFiles {
    LEADERBOARD(Gdx.files.local("data/leaderboard.csv")),
    MENU_BG(Gdx.files.internal("menu_bg.png"));
    public final FileHandle path;

    GameFiles(FileHandle path){
        this.path = path;
    };

    private static String get_level_path(int level) {
        return String.format("level%d", level);
    }

    public static FileHandle get_level_data(int level) {
        return Gdx.files.internal(get_level_path(level) + "/data.json");
    }

    public static FileHandle get_level_graphics(int level) {
        return Gdx.files.internal(get_level_path(level) + "/graphics.png");
    }

    public static FileHandle get_level_grid(int level) {
        return Gdx.files.internal(get_level_path(level) + "/map_grid.png");
    }
}
