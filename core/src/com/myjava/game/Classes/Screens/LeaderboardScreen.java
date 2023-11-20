package com.myjava.game.Classes.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myjava.game.Classes.Leaderboard;
import com.myjava.game.MyGame;
import com.myjava.game.GameFiles;

public class LeaderboardScreen implements Screen {
    private MyGame game;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private Leaderboard leaderboard;
    private Table inner_table;
    private ScrollPane pane;

    private class Background extends Actor {

        private Texture texture = new Texture(GameFiles.MENU_BG.path);

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(texture, 0, 0, camera.viewportWidth, camera.viewportHeight);
            batch.end();

            ShapeRenderer shapeRenderer = new ShapeRenderer();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(.1f, .12f, .16f, 0.6f);
            shapeRenderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();
        }
    }

    public LeaderboardScreen(MyGame game) {
        this.game = game;
        this.camera = new OrthographicCamera(16, 9);
        this.camera.position.set(this.camera.viewportWidth / 2, this.camera.viewportHeight / 2, 0);
        this.camera.update();

        this.viewport = new FitViewport(16, 9, camera);
        this.viewport.apply();

        this.stage = new Stage(this.viewport, this.game.batch);
        this.leaderboard = new Leaderboard();
        this.inner_table = this.leaderboard.get_table(game.text_skin);
        this.inner_table.center();
    }


    @Override
    public void show() {

        final Table outer_table = new Table();
        outer_table.setFillParent(true);
        outer_table.center();

        final TextButton menu_button = new TextButton("Back to menu", game.ui_skin);
        menu_button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        TextButton sort_name_btn = new TextButton("Sort by Name", game.ui_skin);
        TextButton sort_score_btn  = new TextButton("Sort by Score", game.ui_skin);
        TextButton sort_level_btn = new TextButton("Sort by Level", game.ui_skin);

        Table sort_btns = new Table(game.ui_skin);
        sort_btns.center();

        sort_btns.add(sort_name_btn).padRight(5).padLeft(5);
        sort_btns.add(sort_score_btn).padRight(5).padLeft(5);
        sort_btns.add(sort_level_btn).padRight(5).padLeft(5);

        sort_name_btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaderboard.sort(Leaderboard.Sorters.BY_NAME);
                inner_table = leaderboard.get_table(game.text_skin).center();
                pane.setActor(inner_table);
            }
        });

        sort_score_btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaderboard.sort(Leaderboard.Sorters.BY_SCORE);
                inner_table = leaderboard.get_table(game.text_skin).center();
                pane.setActor(inner_table);
            }
        });

        sort_level_btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaderboard.sort(Leaderboard.Sorters.BY_LEVEL);
                inner_table = leaderboard.get_table(game.text_skin).center();
                pane.setActor(inner_table);
            }
        });

        pane = new ScrollPane(inner_table, game.ui_skin);
        pane.setScrollbarsVisible(true);
        pane.setSmoothScrolling(false);

        if (leaderboard.loaded) {
            outer_table.add(sort_btns).width(650).padTop(10);
            outer_table.row();
        }

        outer_table.add(pane).padBottom(20).width(650).padTop(20);
        outer_table.row();
        outer_table.add(menu_button).width(650).height(75).padBottom(10);


        this.stage.addActor(new Background());
        this.stage.addActor(outer_table);
        this.stage.setScrollFocus(pane);

        this.stage.addListener(new InputListener() {
            @Override
            public boolean keyDown (InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE)
                    game.setScreen(new MenuScreen(game));
                return true;
            }
        });

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

    }

    @Override
    public void dispose() {

    }
}
