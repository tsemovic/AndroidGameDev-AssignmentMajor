package com.semtb001.major.assignement;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.major.assignement.screens.MainMenu;
import com.semtb001.major.assignement.tools.Assets;

public class Semtb001MajorAssignment extends Game {

	// Game sprite batch object
	public SpriteBatch batch;

	// Variables for the world dimensions
	public static final float PPM = 32; // PPM = Pixel per Meter
	public static final float MPP = 1 / PPM; // MPP = Meter per Pixel
	public static final int WORLD_PIXEL_WIDTH = 1920/4;
	public static final int WORLD_PIXEL_HEIGHT = 1080/4;
	public static final float WORLD_WIDTH = WORLD_PIXEL_WIDTH / PPM; //in meter
	public static final float WORLD_HEIGHT = WORLD_PIXEL_HEIGHT / PPM; //in meter

	// Viewport for the game
	public static final Viewport viewport  = new FillViewport(Semtb001MajorAssignment.WORLD_WIDTH * Semtb001MajorAssignment.PPM * 2, Semtb001MajorAssignment.WORLD_HEIGHT * Semtb001MajorAssignment.PPM * 2);

	// Number of levels in the game
	public static final int NUMBER_OF_LEVELS = 6;

	// Object types (used for Box2D collisions)
	public static final short PLAYER = 1;
	public static final short WORLD = 2;
	public static final short SHEEP = 4;

	// Saved data objects
	public static Preferences scoresPref;
	public static Preferences levelsPref;

	// Asset manager object
	public static Assets assetManager;

	// Font objects
	public static BitmapFont largeFont;
	public static BitmapFont mediumFont;
	public static BitmapFont smallFont;
	public static BitmapFont tinyFont;
	public static BitmapFont miniFont;

	// Label styles objects (used for all labels in the game)
	public static Label.LabelStyle largeFontWhite;
	public static Label.LabelStyle largeFontGrey;
	public static Label.LabelStyle mediumFontFontWhite;
	public static Label.LabelStyle mediumFontFontGrey;
	public static Label.LabelStyle smallFontFontWhite;
	public static Label.LabelStyle smallFontFontGrey;
	public static Label.LabelStyle tinyFontFontWhite;
	public static Label.LabelStyle tinyFontFontGrey;
	public static Label.LabelStyle miniFontFontWhite;

	// TileSet ID's of interactive tiles in the game (for readability)
	public static final int GRASS = 132;
	public static final int WATER = 137;
	public static final int DRY_SOIL = 99;
	public static final int WET_SOIL = 139;
	public static final int WHEAT_SMALL = 386;
	public static final int WHEAT_MEDIUM = 425;
	public static final int WHEAT_LARGE = 426;
	public static final int BLANK = 231;

	@Override
	public void create () {

		// Game batch setup
		batch = new SpriteBatch();

		// Load game assets
		assetManager = new Assets();
		assetManager.load();
		assetManager.manager.finishLoading();

		//ICONSIZE = (WORLD_PIXEL_HEIGHT / SCREEN_HEIGHT) * PPM * 2;
		/* This code is from the Gdx-freetype repository in the libgdx github wiki
		(https://github.com/libgdx/libgdx/wiki/Gdx-freetype). It is used to load in
		 true type format fonts so they can be used in labels, buttons etc.*/
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/KBTheFlowerFarm.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		// Setup large font style
		parameter.size = (int) (Semtb001MajorAssignment.PPM * 3);
		largeFont = generator.generateFont(parameter);

		// Setup medium font style
		parameter.size = (int) (Semtb001MajorAssignment.PPM * 3);
		mediumFont = generator.generateFont(parameter);

		// Setup small font style
		parameter.size = (int) (Semtb001MajorAssignment.PPM * 1.8);
		smallFont = generator.generateFont(parameter);

		// Setup tiny font style
		parameter.size = (int) (Semtb001MajorAssignment.PPM * 1.3);
		tinyFont = generator.generateFont(parameter);

		// Setup mini font style
		parameter.size = (int) (Semtb001MajorAssignment.PPM * 0.8);
		miniFont = generator.generateFont(parameter);

		// Dispose the .ttf font generator as all fonts have now been created
		generator.dispose();

		// Setup large font style in white and grey
		largeFontWhite = new Label.LabelStyle(Semtb001MajorAssignment.largeFont, Color.WHITE);
		largeFontGrey = new Label.LabelStyle(Semtb001MajorAssignment.largeFont, Color.GRAY);

		// Setup medium font style in white and grey
		mediumFontFontWhite = new Label.LabelStyle(Semtb001MajorAssignment.mediumFont, Color.WHITE);
		mediumFontFontGrey = new Label.LabelStyle(Semtb001MajorAssignment.mediumFont, Color.GRAY);

		// Setup small font style in white and grey
		smallFontFontWhite = new Label.LabelStyle(Semtb001MajorAssignment.smallFont, Color.WHITE);
		smallFontFontGrey = new Label.LabelStyle(Semtb001MajorAssignment.smallFont, Color.GRAY);

		// Setup tiny font style in white and grey
		tinyFontFontWhite = new Label.LabelStyle(Semtb001MajorAssignment.tinyFont, Color.WHITE);
		tinyFontFontGrey = new Label.LabelStyle(Semtb001MajorAssignment.tinyFont, Color.GRAY);

		// Setup mini font style in white
		miniFontFontWhite = new Label.LabelStyle(Semtb001MajorAssignment.miniFont, Color.WHITE);

		// Setup the saved data for the scores and levels
		scoresPref = Gdx.app.getPreferences("scores");
		levelsPref = Gdx.app.getPreferences("levels");

		// Only used when resetting the saved data
//		levelsPref.clear();
//		levelsPref.flush();
//		scoresPref.clear();
//		scoresPref.flush();

		// Adding 'LEVEL: 1' to the levels data so that level 1 can be played from the beginning even when scores are reset
		levelsPref.putBoolean("LEVEL: 1", true);
		levelsPref.flush();

		// Set the screen to the main menu on startup
		setScreen(new MainMenu(this));

		// Setup game music loop
		Music music = Semtb001MajorAssignment.assetManager.manager.get(Assets.music);
		music.setLooping(true);
		music.play();
	}

	// Method to play the 'menu click' sound (used when buttons are pressed in any of the menu's)
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
		super.dispose();
		assetManager.dispose();
		batch.dispose();
		smallFont.dispose();
		mediumFont.dispose();
		largeFont.dispose();
		miniFont.dispose();
	}
}
