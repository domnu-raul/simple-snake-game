package com.myjava.game.Interfaces;

import com.myjava.game.Classes.SnakePlayer;

public interface Playable {
    public void set_direction(int x, int y);
    public void set_player(SnakePlayer player);
    public void set_sprinting(boolean is_sprinting);
}
