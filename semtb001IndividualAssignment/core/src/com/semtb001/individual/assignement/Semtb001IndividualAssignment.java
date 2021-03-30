package com.semtb001.individual.assignement;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.semtb001.individual.assignement.screens.MainMenu;
import com.semtb001.individual.assignement.screens.PlayScreen;
import com.semtb001.individual.assignement.tools.Assets;

public class Semtb001IndividualAssignment extends Game {
	public SpriteBatch batch;

	public static final float PPM = 32; // PPM = Pixel per Meter
	public static final float MPP = 1 / PPM; // MPP = Meter per Pixel

	public static final int WORLD_PIXEL_WIDTH = 1920/2;
	public static final int WORLD_PIXEL_HEIGHT = 1080/2;
	public static final float WORLD_WIDTH = WORLD_PIXEL_WIDTH / PPM; //in meter
	public static final float WORLD_HEIGHT = WORLD_PIXEL_HEIGHT / PPM; //in meter
	public static final int NUMBER_OF_LEVELS = 2;
	public Preferences scoresPref;
	public static Preferences levelsPref;
	public static Assets assetManager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager = new Assets();
		assetManager.load(); //starts loading assets
		assetManager.manager.finishLoading(); //Continues when done loading.

		scoresPref = Gdx.app.getPreferences("scores");
		levelsPref = Gdx.app.getPreferences("levels");

		levelsPref.clear();
		levelsPref.flush();

		levelsPref.putBoolean("LEVEL: 1", true);
		levelsPref.flush();

		setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
