package com.semtb001.individual.assignement;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class Semtb001IndividualAssignment extends Game {
	public SpriteBatch batch;

	public static float SCREEN_WIDTH;
	public static float SCREEN_HEIGHT;
	public static final float PPM = 10; // PPM = Pixel per Meter
	//public static final float WORLD_HEIGHT = 1400;
	//public static final float WORLD_WIDTH = 2333;

	//public static final float WORLD_HEIGHT = 480;
	//public static final float WORLD_WIDTH = 800;


	public static final int WORLD_PIXEL_WIDTH = 1920/4;
	public static final int WORLD_PIXEL_HEIGHT = 1080/4;
	public static final float WORLD_WIDTH = WORLD_PIXEL_WIDTH / PPM; //in meter
	public static final float WORLD_HEIGHT = WORLD_PIXEL_HEIGHT / PPM; //in meter
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
