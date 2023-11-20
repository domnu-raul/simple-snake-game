package com.myjava.game.Classes;

import com.badlogic.gdx.files.FileHandle;
import com.myjava.game.Classes.Exceptions.InvalidNameException;
import com.myjava.game.GameFiles;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveHandler implements Serializable {
    public String get_name() {
        return name;
    }

    public int get_level() {
        return level;
    }

    public int get_score() {
        return score;
    }

    private String name;
    private int level;
    private int score;

    private boolean saved = false;
    public SaveHandler(int level, int score) {
        this.level = level;
        this.score = score;
    }

    public SaveHandler() {

    }


    public void setName(String name) throws InvalidNameException {
        if (!name.isEmpty()) {
            if (!name.replaceAll("[0-9a-zA-Z_]", "").isEmpty())
                throw  new InvalidNameException("Name should only contain alphanumerical characters and underscores.");
            this.name = name;
        }
    }

    public void save() {this.saved = true;}
    public boolean is_saved() { return this.saved; }

    public void print() {
        System.out.println(this.name + "\n" + this.level + "\n" + this.score + "\n");
    }
}