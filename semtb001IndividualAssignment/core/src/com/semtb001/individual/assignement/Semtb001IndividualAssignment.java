package com.semtb001.individual.assignement;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class Semtb001IndividualAssignment extends Game {
	SpriteBatch batch;

	public static float SCREEN_WIDTH;
	public static float SCREEN_HEIGHT;
	public static final float PPM = 32; // PPM = Pixel per Meter
	public static final float WORLD_HEIGHT = 480;
	public static final float WORLD_WIDTH = 800;

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
