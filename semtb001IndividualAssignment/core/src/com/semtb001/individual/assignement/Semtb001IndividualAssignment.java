package com.semtb001.individual.assignement;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
	public static Preferences scoresPref;
	public static Preferences levelsPref;
	public static Assets assetManager;

	public static BitmapFont largeFont;
	public static BitmapFont mediumFont;
	public static BitmapFont smallFont;
	public static BitmapFont tinyFont;

	public static Label.LabelStyle largeFontWhite;
	public static Label.LabelStyle largeFontGrey;
	public static Label.LabelStyle mediumFontFontWhite;
	public static Label.LabelStyle mediumFontFontGrey;
	public static Label.LabelStyle smallFontFontWhite;
	public static Label.LabelStyle smallFontFontGrey;
	public static Label.LabelStyle tinyFontFontWhite;
	public static Label.LabelStyle tinyFontFontGrey;



	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager = new Assets();
		assetManager.load(); //starts loading assets
		assetManager.manager.finishLoading(); //Continues when done loading.

		//https://github.com/libgdx/libgdx/wiki/Gdx-freetype
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/poxel.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parameter.size = (int) (Semtb001IndividualAssignment.PPM * 4);
		largeFont = generator.generateFont(parameter);

		parameter.size = (int) (Semtb001IndividualAssignment.PPM * 3);
		mediumFont = generator.generateFont(parameter);

		parameter.size = (int) (Semtb001IndividualAssignment.PPM * 2);
		smallFont = generator.generateFont(parameter);

		parameter.size = (int) (Semtb001IndividualAssignment.PPM * 1.5);
		tinyFont = generator.generateFont(parameter);

		generator.dispose();

		largeFontWhite = new Label.LabelStyle(Semtb001IndividualAssignment.largeFont, Color.WHITE);
		largeFontGrey = new Label.LabelStyle(Semtb001IndividualAssignment.largeFont, Color.GRAY);
		mediumFontFontWhite = new Label.LabelStyle(Semtb001IndividualAssignment.mediumFont, Color.WHITE);
		mediumFontFontGrey = new Label.LabelStyle(Semtb001IndividualAssignment.mediumFont, Color.GRAY);
		smallFontFontWhite = new Label.LabelStyle(Semtb001IndividualAssignment.smallFont, Color.WHITE);
		smallFontFontGrey = new Label.LabelStyle(Semtb001IndividualAssignment.smallFont, Color.GRAY);
		tinyFontFontWhite = new Label.LabelStyle(Semtb001IndividualAssignment.tinyFont, Color.WHITE);
		tinyFontFontGrey = new Label.LabelStyle(Semtb001IndividualAssignment.tinyFont, Color.GRAY);

		scoresPref = Gdx.app.getPreferences("scores");
		levelsPref = Gdx.app.getPreferences("levels");

		levelsPref.clear();
		levelsPref.flush();
		scoresPref.clear();
		scoresPref.flush();


		levelsPref.putBoolean("LEVEL: 1", true);
		levelsPref.flush();

		setScreen(new MainMenu(this));
	}

	public static void playMenuClick(){
		Sound menuClick = assetManager.manager.get(Assets.menuClick);
		menuClick.play();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
