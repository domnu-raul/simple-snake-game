package com.myjava.game.Classes.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.*;
import com.myjava.game.MyGame;
import com.myjava.game.GameFiles;

public class MenuScreen implements Screen {
    private MyGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private class Background extends Actor {

        private Texture texture = new Texture(GameFiles.MENU_BG.path);

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(texture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        }
    }

    public MenuScreen(MyGame game) {
        this.game = game;
        this.camera = new OrthographicCamera(16, 9);
        this.camera.position.set(this.camera.viewportWidth / 2, this.camera.viewportHeight / 2, 0);
        this.camera.update();

        this.viewport = new FitViewport(16, 9, camera);
        this.viewport.apply();

        this.stage = new Stage(this.viewport, this.game.batch);
    }

    @Override
    public void show() {
        Table table = new Table(game.ui_skin);

        table.setFillParent(true);
        table.center();

        TextButton play_btn = new TextButton("Play Snake", game.ui_skin);
        TextButton leaderboard_btn = new TextButton("Leaderboard", game.ui_skin);
        TextButton load_btn = new TextButton("Load Game",  game.ui_skin);
        TextButton exit_btn = new TextButton("Exit", game.ui_skin);

        table.add(play_btn).padBottom(10).width(400).height(75);
        table.row();
        table.add(leaderboard_btn).padBottom(10).width(400).height(75);
        table.row();
        table.add(load_btn).padBottom(10).width(400).height(75);
        table.row();
        table.add(exit_btn).width(400).height(75);

        play_btn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SnakeScreen(game, 1));
            }
        });

        exit_btn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        load_btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoadScreen(game));
            }
        });


        leaderboard_btn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        stage.addActor(new Background());
        stage.addActor(table);

        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
        this.camera.position.set(this.camera.viewportWidth / 2, this.camera.viewportHeight / 2, 0);
        this.camera.update();

        this.viewport.setWorldHeight(height);
        this.viewport.setWorldWidth(width);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
