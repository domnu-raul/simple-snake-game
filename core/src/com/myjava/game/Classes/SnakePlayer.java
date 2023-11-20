package com.myjava.game.Classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.myjava.game.Classes.Abstract.Player;
import com.myjava.game.Interfaces.Playable;

import java.util.Scanner;

public class SnakePlayer extends Player {
    public boolean keyDown(int keycode)
    {
        switch (keycode)
        {
            case Input.Keys.A:
                super.character.set_direction(-1, 0);
                break;
            case Input.Keys.D:
                super.character.set_direction(1, 0);
                break;
            case Input.Keys.W:
                super.character.set_direction(0, 1);
                break;
            case Input.Keys.S:
                super.character.set_direction(0, -1);
                break;
            case Input.Keys.LEFT:
                super.character.set_direction(-1, 0);
                break;
            case Input.Keys.RIGHT:
                super.character.set_direction(1, 0);
                break;
            case Input.Keys.UP:
                super.character.set_direction(0, 1);
                break;
            case Input.Keys.DOWN:
                super.character.set_direction(0, -1);
                break;
            case Input.Keys.SPACE:
                super.character.set_sprinting(true);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode)
        {
            case Input.Keys.SPACE:
                super.character.set_sprinting(false);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            super.touch_start.set(screenX, screenY);
        }
        this.character.set_sprinting(true);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            Vector2 touch_end = new Vector2(screenX, screenY);
            Vector2 swipe_direction = touch_end.cpy().sub(touch_start);

            if (swipe_direction.len() > 100) {
                if (Math.abs(swipe_direction.x) > Math.abs(swipe_direction.y)) {
                    if (swipe_direction.x > 0) {
                        super.character.set_direction(1, 0);
                    } else {
                        super.character.set_direction(-1, 0);
                    }
                } else {
                    if (swipe_direction.y > 0) {
                        super.character.set_direction(0, -1);
                    } else {
                        super.character.set_direction(0, 1);
                    }
                }
            }
        }
        this.character.set_sprinting(false);
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }


    public void add_score(int n) {
        this.score += n;
    }

    public int get_score() {
        return this.score;
    }
    
    public void set_character(Playable character) {
        this.character = character;
    }
    public SnakePlayer() {
        this.score = 0;
    }
}
