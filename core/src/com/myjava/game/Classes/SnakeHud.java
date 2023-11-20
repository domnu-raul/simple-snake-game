package com.myjava.game.Classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myjava.game.Classes.Exceptions.InvalidNameException;
import com.myjava.game.Classes.Screens.MenuScreen;
import com.myjava.game.Classes.Screens.SnakeScreen;
import com.myjava.game.Classes.GameObjects.EnemySnake;
import com.myjava.game.Classes.GameObjects.PlayableSnake;
import com.myjava.game.MyGame;

public class SnakeHud {
    public enum State {
        DEATH,
        PAUSE,
        RUNNING,
        SAVE;
        final static Table table_death = new Table();
        final static Table table_pause = new Table();
        final static Table table_running = new Table();
        final static Table table_save = new Table();
        public Table get_table() {
            switch (this) {
                case DEATH:
                    return table_death;
                case PAUSE:
                    return table_pause;
                case SAVE:
                    return table_save;
                default:
                    return table_running;
            }
        };
    }

    private MyGame game;
    private Stage stage;
    private Viewport viewport;
    private State state;
    private SnakePlayer player;

    public SnakeHud(final MyGame game, final SnakeScreen screen, final PlayableSnake p_snake, final EnemySnake e_snake, final SnakePlayer player) {
        this.game = game;
        this.player = player;

        this.viewport = new FitViewport(1920, 1080);
        this.stage = new Stage(this.viewport, this.game.batch);

        load_running_table(game, p_snake, e_snake, player);
        load_pause_table(game, screen);
        load_death_table(game, screen);
        load_save_table(game, screen);

        this.set_screen(State.RUNNING);
    }

    public void set_screen(State s) {
        if (this.state == s) return;

        if (s == State.RUNNING) {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
            Gdx.input.setInputProcessor(player);
        }
        else {
            Gdx.input.setInputProcessor(this.stage);
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        }

        this.state = s;
        this.stage.clear();
        this.stage.addActor(s.get_table());
    }

    public void render(float dt) {
        this.stage.act();
        this.stage.draw();
    }

    public void clear_tables() {
        for (State s: State.values())
            s.get_table().clear();
    }

    private void load_death_table(final MyGame game, final SnakeScreen screen) {
        Table death_table = State.DEATH.get_table();
        TextButton play_again_button = new TextButton("Play Again", game.ui_skin);
        TextButton exit_button_death = new TextButton("Exit to menu", game.ui_skin);

        final TextField name_input = new TextField("", game.ui_skin);
        name_input.setMessageText(" Enter your name...");
        final Label err_label = new Label("", game.text_skin);
        err_label.setWrap(true);

        play_again_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    Leaderboard.add(name_input.getText(), player.get_score(), screen.get_level());
                    clear_tables();
                    game.setScreen(new SnakeScreen(game, screen.get_level()));
                } catch (InvalidNameException e) {
                    err_label.setText(e.getMessage());
                }
            }
        });

        exit_button_death.addListener(new ClickListener() {
            @Override

            public void clicked(InputEvent event, float x, float y) {
                try {
                    Leaderboard.add(name_input.getText(), player.get_score(), screen.get_level());
                    clear_tables();
                    game.setScreen(new MenuScreen(game));
                } catch (InvalidNameException e) {
                    err_label.setText(e.getMessage());
                }
            }
        });

        death_table.setFillParent(true);
        death_table.center();
        death_table.add(new Label("You died!", game.text_skin)).padBottom(10);
        death_table.row();
        death_table.add(name_input).width(400).height(75).padBottom(10);
        death_table.row();
        death_table.add(play_again_button).width(400).height(75).padBottom(10);
        death_table.row();
        death_table.add(exit_button_death).width(400).height(75);
        death_table.row();
        death_table.add(err_label).width(400).height(75).padTop(10);
        death_table.padTop(75);
    }

    private void load_pause_table(final MyGame game, final SnakeScreen screen) {
        Table pause_table = State.PAUSE.get_table();
        TextButton resume_button = new TextButton("Resume", game.ui_skin);
        TextButton exit_button = new TextButton("Exit to menu", game.ui_skin);
        TextButton save_button = new TextButton("Save Game", game.ui_skin);

        resume_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.set_running();
                set_screen(State.RUNNING);
            }
        });

        exit_button.addListener(new ClickListener() {
            @Override

            public void clicked(InputEvent event, float x, float y) {
                clear_tables();
                game.setScreen(new MenuScreen(game));
            }
        });

        save_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                set_screen(State.SAVE);
            }
        });

        pause_table.setFillParent(true);
        pause_table.center();
        pause_table.add(new Label("Game Paused", this.game.text_skin)).padBottom(10);
        pause_table.row();
        pause_table.add(resume_button).width(400).height(75).padBottom(10);
        pause_table.row();
        pause_table.add(save_button).width(400).height(75).padBottom(10);
        pause_table.row();
        pause_table.add(exit_button).width(400).height(75);
    }

    private void load_running_table(MyGame game, final PlayableSnake p_snake, final EnemySnake e_snake, final SnakePlayer player) {
        Table running_table = State.RUNNING.get_table();

        final Label score_label = new Label("", game.text_skin);
        final Label stamina_label = new Label("", game.text_skin);
        final Label p_strength_label = new Label("", game.text_skin);
        final Label e_strength_label = new Label("", game.text_skin);

        stamina_label.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                stamina_label.setText(String.format("Stamina: %02d", p_snake.get_stamina()));
                return false;
            }
        });

        score_label.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                score_label.setText(String.format("Score: %03d", player.get_score()));
                return false;
            }
        });

        p_strength_label.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                p_strength_label.setText(String.format("Strength: %02d", p_snake.get_strength()));
                return false;
            }
        });

        e_strength_label.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                e_strength_label.setText(String.format("Enemy Strength: %02d", e_snake.get_strength()));
                return false;
            }
        });

        running_table.setFillParent(true);
        running_table.top();
        running_table.add(score_label).left().padLeft(10);
        running_table.add(stamina_label).right().padRight(10);
        running_table.row().expand();
        running_table.add(p_strength_label).padTop(1000).left().padLeft(10);
        running_table.add(e_strength_label).padTop(1000).right().padRight(10);
    }

    private void load_save_table(final MyGame game, final SnakeScreen screen) {
        Table save_table = State.SAVE.get_table();
        TextButton save_button = new TextButton("Save", game.ui_skin);

        final TextField name_input = new TextField("", game.ui_skin);
        name_input.setMessageText(" Enter your name...");
        final Label err_label = new Label("", game.text_skin);
        err_label.setWrap(true);

        save_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (!screen.state.is_saved()) {
                        screen.state.setName(name_input.getText());
                        game.saves.add(screen.state);
                        screen.state.save();
                    }
                    name_input.setText("");
                    screen.set_running();
                    set_screen(State.RUNNING);
                } catch (InvalidNameException e) {
                    err_label.setText(e.getMessage());
                }
            }
        });

        save_table.setFillParent(true);
        save_table.center();
        save_table.add(name_input).width(400).height(75).padBottom(10);
        save_table.row();
        save_table.add(save_button).width(400).height(75).padBottom(10);
        save_table.row();
        save_table.add(err_label).width(400).height(75).padTop(10);
        save_table.padTop(75);
    }
}
