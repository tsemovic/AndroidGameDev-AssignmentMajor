package com.semtb001.individual.assignement;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.semtb001.individual.assignement.screens.MainMenu;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class Semtb001IndividualAssignment extends Game {
	public SpriteBatch batch;

	public static final float PPM = 32; // PPM = Pixel per Meter
	public static final float MPP = 1 / PPM; // MPP = Meter per Pixel
	public static float ICONSIZE = 2 * PPM; // MPP = Meter per Pixel
	public static float SCREEN_WIDTH;
	public static float SCREEN_HEIGHT;

	public static final int WORLD_PIXEL_WIDTH = 1920/2;
	public static final int WORLD_PIXEL_HEIGHT = 1080/2;
	public static final float WORLD_WIDTH = WORLD_PIXEL_WIDTH / PPM; //in meter
	public static final float WORLD_HEIGHT = WORLD_PIXEL_HEIGHT / PPM; //in meter
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";

	@Override
	public void create () {
		batch = new SpriteBatch();
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
