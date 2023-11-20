package com.myjava.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.myjava.game.Classes.SaveHandler;
import com.myjava.game.Classes.Screens.MenuScreen;

import java.io.*;
import java.util.ArrayList;

public class MyGame extends Game {
	public SpriteBatch batch;
	public TextureAtlas atlas;
	public Skin text_skin;
	public Skin ui_skin;
	public ArrayList<SaveHandler> saves;

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.atlas = new TextureAtlas();
		this.text_skin = new Skin(Gdx.files.internal("uiskins/text-skin/text-skin.json"));
		this.ui_skin = new Skin(Gdx.files.internal("uiskins/plain-james/skin/plain-james-ui.json"));
		this.saves = import_saves("data/save.dat");
		setScreen(new MenuScreen(this));

		for (SaveHandler s: this.saves) {
			s.print();
		}

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

		export_saves("data/save.dat", this.saves);
		batch.dispose();
	}

	public void load_textures(FileHandle path, int size, String ... names) {
		this.atlas.dispose();
		this.atlas = new TextureAtlas();
		Texture t = new Texture(path);

		int k = 0;
		for (String texture_name : names)
			this.atlas.addRegion(texture_name, t, 0, size * k++, size, size);
	}

	private ArrayList<SaveHandler> import_saves(String file_path) {
		ArrayList<SaveHandler> out = new ArrayList<SaveHandler>();

		try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file_path))) {
			out = (ArrayList<SaveHandler>) objectInputStream.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return out;
	}

	private void export_saves(String filePath, ArrayList<SaveHandler> list) {
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
			objectOutputStream.writeObject(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
