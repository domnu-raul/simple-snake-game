package com.myjava.game.Classes.Abstract;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.myjava.game.Interfaces.Playable;

public abstract class Player implements InputProcessor {
    protected int score;
    protected Playable character;
    protected Vector2 touch_start = new Vector2();
}
