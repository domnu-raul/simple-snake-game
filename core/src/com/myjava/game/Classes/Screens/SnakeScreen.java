package com.myjava.game.Classes.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.*;
import com.myjava.game.Classes.*;
import com.myjava.game.Classes.Exceptions.InvalidLevelDataException;
import com.myjava.game.Classes.GameObjects.EnemySnake;
import com.myjava.game.Classes.GameObjects.PlayableSnake;
import com.myjava.game.Classes.GameObjects.SnakeMap;
import com.myjava.game.MyGame;
import com.myjava.game.GameFiles;

public class SnakeScreen implements Screen {
    private MyGame game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private PlayableSnake playable_snake;
    private EnemySnake enemy_snake;
    private SnakeMap map_;
    private SnakePlayer player;
    private boolean running;
    private int level;
    private SnakeHud hud;
    public SaveHandler state;
    public SnakeScreen(MyGame game, int level) {
        this.game = game;
        this.level = level;
        this.player = new SnakePlayer();
        this.running = true;

        this.load(level);
        Gdx.input.setInputProcessor(player);
    }


    public SnakeScreen(MyGame game, SaveHandler handler) {
        this.game = game;
        this.level = handler.get_level();
        this.player = new SnakePlayer();
        this.player.add_score(handler.get_score());
        this.running = true;

        try {
            this.load(level);
        } catch (InvalidLevelDataException e) {
            this.level++;
            this.load(level);
            e.printStackTrace();
        }

        Gdx.input.setInputProcessor(player);
    }

    public void set_running() {
        this.running = !this.running;
    }

    public int get_level() {
        return this.level;
    }

    public void load(int level) throws InvalidLevelDataException {
        this.level = level;

        this.map_ = new SnakeMap(this.level, this.game);
        this.camera = new OrthographicCamera(this.map_.get_width(), this.map_.get_height());

        this.viewport = new FitViewport(this.map_.get_width(), this.map_.get_height(), this.camera);
        this.viewport.apply(true);

        JsonValue json_file = new JsonReader().parse(GameFiles.get_level_data(this.level));

        int snake_size = json_file.getInt("snake_size");
        float snake_speed = json_file.getFloat("snake_speed");
        JsonValue json_snake_direction = json_file.get("snake_direction");
        JsonValue json_snake_segments = json_file.get("snake_segments");
        Vector2[] snake_segments = new Vector2[snake_size];

        for (int i = 0; i < snake_size; i++) {
            JsonValue segment = json_snake_segments.get(i);
            float x = segment.getFloat("x"), y = segment.getFloat("y");
            snake_segments[i] = new Vector2(x, y);
            if (this.map_.get_cell((int) x, (int) y) != '\0') throw new InvalidLevelDataException("Snake's initial position collides with terrain");
        }

        this.playable_snake = new PlayableSnake(this.game, this.map_, snake_speed, snake_segments);
        this.enemy_snake = new EnemySnake(this.game, this.map_, snake_speed,
                new Vector2(3, 1),
                new Vector2(4, 1),
                new Vector2(5, 1)
        );

        this.playable_snake.set_direction(
                    json_snake_direction.getInt("x"),
                    json_snake_direction.getInt("y")
        );

        this.player.set_character(this.playable_snake);
        this.playable_snake.set_player(this.player);

        this.state = new SaveHandler(level, player.get_score());
        this.hud = new SnakeHud(game, this, playable_snake, enemy_snake, player);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (this.running && this.playable_snake.is_alive) {
            this.playable_snake.move(delta);
            if (enemy_snake != null) {
                this.enemy_snake.move(delta);
                if ((this.enemy_snake.collides_with_snake() || this.playable_snake.collides_with_snake())) {
                    if (this.enemy_snake.compareTo(this.playable_snake) < 0) {
                        this.enemy_snake.clear();
                        this.enemy_snake = null;
                        this.player.add_score(5);
                    }
                    else this.playable_snake.is_alive = false;
                }

            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
                this.running = !this.running;
                if (!this.running)
                    this.hud.set_screen(SnakeHud.State.PAUSE);
                else
                    this.hud.set_screen(SnakeHud.State.RUNNING);
            }
        }

        if (!this.playable_snake.is_alive)
            this.hud.set_screen(SnakeHud.State.DEATH);

        if (this.enemy_snake == null) this.map_.door.open();

        if (this.playable_snake.enters_door()) {
            this.hud.clear_tables();
            this.load(this.map_.door.get_next_level());
        }

        this.game.batch.setProjectionMatrix(this.camera.combined);
        this.map_.draw();
        this.playable_snake.draw();
        if (enemy_snake != null)
            this.enemy_snake.draw();

        this.hud.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        this.camera.viewportWidth = this.map_.get_width();
        this.camera.viewportHeight = this.map_.get_height();
        this.camera.position.set(this.camera.viewportWidth / 2f, this.camera.viewportHeight / 2f, 0);
        this.camera.update();
    }

    @Override
    public void pause() { }
    @Override
    public void resume() { }
    @Override
    public void hide() { }
    @Override
    public void dispose() { }
}
