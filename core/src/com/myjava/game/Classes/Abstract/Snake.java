package com.myjava.game.Classes.Abstract;

import com.badlogic.gdx.math.Vector2;
import com.myjava.game.Classes.GameObjects.Food;
import com.myjava.game.Classes.GameObjects.SnakeMap;
import com.myjava.game.Interfaces.Drawable;
import com.myjava.game.MyGame;

import java.util.LinkedList;

public abstract class Snake implements Drawable, Comparable<Snake> {
    protected LinkedList<Vector2> segments;
    protected SnakeMap map_;
    protected MyGame game;
    protected float dt_counter;
    protected float speed;
    protected int strength;
    public boolean is_alive;

    public abstract void move(float delta);
    public boolean collides_with_food() {
        Vector2 head = this.segments.getFirst();
        Food f = this.map_.food;
        if (head.epsilonEquals(f.get_pos())) {
            this.map_.food.spawn();
            return true;
        }
        return false;
    }

    public int get_strength() {
        return this.strength;
    }

    public abstract boolean collides_with_snake();

    @Override
    public int compareTo(Snake o) {
        return Integer.compare(this.strength, o.strength);
    }
}
