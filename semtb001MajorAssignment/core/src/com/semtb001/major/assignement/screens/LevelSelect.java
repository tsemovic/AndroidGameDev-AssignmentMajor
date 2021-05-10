package com.semtb001.major.assignement.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.tools.Assets;

import java.util.ArrayList;
import java.util.List;

// Class to display the level select menu screen
public class LevelSelect implements Screen {

    // Level Select game, batch, stage, viewport and camera objects
    public Semtb001MajorAssignment game;
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    // Objects that will be displayed in the LevelSelect menu
    private Label back;
    private Label levelsLabel;

    // List of levels
    private List<Label> levels;

    // Background for the LevelSelect menu screen
    private Sprite backgroundSprite;

    public LevelSelect(Semtb001MajorAssignment semtb001MajorAssignment) {

        // Instantiate LevelSelect game and spritebatch
        game = semtb001MajorAssignment;
        batch = new SpriteBatch();

        // Setup camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Semtb001MajorAssignment.WORLD_WIDTH, Semtb001MajorAssignment.WORLD_HEIGHT);

        // Setup viewport
        viewport = new FillViewport(Semtb001MajorAssignment.WORLD_WIDTH * Semtb001MajorAssignment.PPM * 2, Semtb001MajorAssignment.WORLD_HEIGHT * Semtb001MajorAssignment.PPM * 2);
        camera.update();

        // Create the stage
        stage = new Stage(viewport, batch);

    }

    @Override
    public void show() {

        // Add the stage to the input processor
        Gdx.input.setInputProcessor(stage);

        // Setup the background image sprite
        backgroundSprite = new Sprite(Semtb001MajorAssignment.assetManager.manager.get(Assets.menuBackground));
        backgroundSprite.setSize(camera.viewportWidth, camera.viewportHeight);
        backgroundSprite.setAlpha(400);

        // Setup levels table to be displayed in the stage
        Table levelLabelTable = new Table();
        levelLabelTable.setFillParent(true);
        levelLabelTable.center();

        //Create main Table to be displayed in the 'levels' table (holds labels for each level)
        Table mainTable = new Table();

        // Setup back table to be displayed in the main table
        Table backTable = new Table();
        backTable.setFillParent(true);
        backTable.top().left();
        backTable.padTop(20);
        backTable.padLeft(50);

        // Create labels for the 'levels' table
        levelsLabel = new Label("LEVELS", Semtb001MajorAssignment.mediumFontFontWhite);
        levelsLabel.setColor(Color.WHITE);

        // Create labels for the 'back' table
        back = new Label("<BACK", Semtb001MajorAssignment.smallFontFontWhite);
        back.setColor(Color.WHITE);

        // Add the 'back' label to the 'back' table
        backTable.add(back);

        // Create an array list for the levels labels
        levels = new ArrayList<Label>();

        // Add the 'levels' label to the 'levels' table
        levelLabelTable.add(levelsLabel);
        levelLabelTable.row();

        // Executes code for the number of levels in the game
        for (int i = 1; i <= Semtb001MajorAssignment.NUMBER_OF_LEVELS; i++) {

            // Create a new label for the current level in the loop
            Label currentLevel = new Label("LEVEL: " + Integer.toString(i), Semtb001MajorAssignment.tinyFontFontWhite);

            // If the level is unlocked (from saved data) display the level with a white font colour
            if (game.levelsPref.getBoolean("LEVEL: " + Integer.toString(i))) {
                currentLevel.setColor(Color.WHITE);
            } else {

                // If the level is locked display the level with a grey font colour
                currentLevel.setColor(Color.GRAY);
            }

            // Add a new row every 3 levels (displays levels as a grid if/when more levels are added)
            if ((i-1) % 3 == 0) {
                mainTable.row();
            }

            // Add the current level to the 'main' table
            levels.add(currentLevel);
            mainTable.add(currentLevel).pad(Semtb001MajorAssignment.PPM);

        }

        // Add the 'main' table to the 'levels' table (table within a table)
        levelLabelTable.add(mainTable);


        // Add the 'level' and 'back' tables to the stage
        stage.addActor(levelLabelTable);
        stage.addActor(backTable);

        // Loops through each level
        for (final Label currentLevel : levels) {

            // If the level is unlocked
            if (game.levelsPref.getBoolean(currentLevel.getText().toString())) {

                // Add an event listener to the current level label
                currentLevel.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Semtb001MajorAssignment.playMenuClick();

                        // If the level is touched: play the game on that level
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(game, currentLevel.getText().toString()));
                    }
                });
            }
        }

        // Add input listener to the 'back' label
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Semtb001MajorAssignment.playMenuClick();

                // If the 'back' label is touched, return to the main menu
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
            }
        });

    }

    @Override
    public void render(float delta) {

        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        // Draw the background image
        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();

        // Draw the stage
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
