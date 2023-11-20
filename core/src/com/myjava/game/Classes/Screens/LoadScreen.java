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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.myjava.game.Classes.SaveHandler;
import com.myjava.game.GameFiles;
import com.myjava.game.MyGame;

import java.util.ArrayList;

public class LoadScreen implements Screen {
    private MyGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private ArrayList<SaveHandler> saves;

    private class Background extends Actor {

        private Texture texture = new Texture(GameFiles.MENU_BG.path);

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(texture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        }
    }

    public LoadScreen(MyGame game) {
        this.game = game;
        this.camera = new OrthographicCamera(16, 9);
        this.camera.position.set(this.camera.viewportWidth / 2, this.camera.viewportHeight / 2, 0);
        this.camera.update();

        this.viewport = new FitViewport(16, 9, camera);
        this.viewport.apply();

        this.stage = new Stage(this.viewport, this.game.batch);
        this.saves = game.saves;
    }

    @Override
    public void show() {
        Table inner_table = new Table(game.ui_skin);
        Table outer_table = new Table(game.ui_skin);

        outer_table.setFillParent(true);
        outer_table.center();
        inner_table.center();

        for (final SaveHandler handler: this.saves) {
            String text = String.format("%1$-20s %2$-20s %3$s", handler.get_name(), handler.get_score(), handler.get_level());
            TextButton load_btn = new TextButton(text, game.ui_skin);

            load_btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y){
                    game.setScreen(new SnakeScreen(game, handler));
                }
            });


            inner_table.add(load_btn).padBottom(10).width(600).height(75);
            inner_table.row();
        }

        final TextButton menu_button = new TextButton("Back to menu", game.ui_skin);
        menu_button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });


        ScrollPane pane = new ScrollPane(inner_table, game.ui_skin);
        pane.setScrollbarsVisible(true);
        pane.setSmoothScrolling(false);

        outer_table.add(pane).padBottom(20).width(650).padTop(20).minHeight(500);
        outer_table.row();
        outer_table.add(menu_button).width(650).height(75).padBottom(10);


        stage.addActor(new LoadScreen.Background());
        stage.addActor(outer_table);

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
