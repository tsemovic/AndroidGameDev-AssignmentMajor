package com.semtb001.major.assignement.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

import java.util.ArrayList;
import java.util.List;

// Class to display Statistics Menu (Level Statistics)
public class Statistics implements Screen {

    // Statistics Menu game, spritebatch, stage, viewport, and camera objects
    public Semtb001MajorAssignment game;
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    // Objects that will be displayed in the Statistics Menu
    private Label back;
    private Label levelsLabel;
    private Label descriptionLabel;

    // List of levels in the game
    private List<Label> levels;

    // Statistics Menu Background sprite object
    private Sprite backgroundSprite;

    public Statistics(Semtb001MajorAssignment semtb001MajorAssignment){

        // Instantiate game and spritebatch
        game = semtb001MajorAssignment;
        batch = new SpriteBatch();

        // Setup camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Semtb001MajorAssignment.WORLD_WIDTH, Semtb001MajorAssignment.WORLD_HEIGHT);

        // Setup viewport
        viewport = new FillViewport(Semtb001MajorAssignment.WORLD_WIDTH * Semtb001MajorAssignment.PPM, Semtb001MajorAssignment.WORLD_HEIGHT * Semtb001MajorAssignment.PPM);
        camera.update();

        // Setup stage
        stage = new Stage(viewport, batch);
    }

    @Override
    public void show() {

        // Add the stage to the input processor
        Gdx.input.setInputProcessor(stage);

        // Setup the background image sprite
        Texture backgroundTexture = new Texture("gui/mainMenuBackground.png");
        backgroundSprite =new Sprite(backgroundTexture);
        backgroundSprite.setSize(camera.viewportWidth, camera.viewportHeight);
        backgroundSprite.setAlpha(400);

        // Create 'level' table to be displayed in the stage
        Table levelLabelTable = new Table();
        levelLabelTable.setFillParent(true);
        levelLabelTable.center();

        // Create 'back' table to be displayed in the stage
        Table backTable = new Table();
        backTable.setFillParent(true);
        backTable.top().left();
        backTable.padTop(20);
        backTable.padLeft(50);

        // Create 'main' table to be displayed in the 'level' table
        Table mainTable = new Table();

        // Create label to be displayed in the 'level' table
        levelsLabel = new Label("STATISTICS" , Semtb001MajorAssignment.largeFontWhite);
        levelsLabel.setColor(Color.WHITE);
        descriptionLabel = new Label("LEVEL COMPLETION PERCENTAGE", Semtb001MajorAssignment.tinyFontFontWhite);
        descriptionLabel.setColor(Color.WHITE);

        // Create 'back' label to be displayed in the 'back' table
        back = new Label("<BACK", Semtb001MajorAssignment.smallFontFontWhite);
        back.setColor(Color.WHITE);

        // Add labels to the 'level' table
        levelLabelTable.add(levelsLabel).padBottom(Semtb001MajorAssignment.PPM);
        levelLabelTable.row();
        levelLabelTable.add(descriptionLabel);
        levelLabelTable.row();

        // Add 'back' label to the 'back' table
        backTable.add(back);

        // Add the 'main' table to the 'level' table
        levelLabelTable.add(mainTable);

        // Add the 'level' and 'back' tables to the stage
        stage.addActor(levelLabelTable);
        stage.addActor(backTable);

        // Create a list of the levels in the game
        levels = new ArrayList<Label>();

        // Executes code for the number of levels in the game
        for(int i = 1; i <= Semtb001MajorAssignment.NUMBER_OF_LEVELS; i++){

            // Create a new label for the current level in the loop (level completion percentage)
            Label currentLevel = new Label("LEVEL: " + Integer.toString(i) + " = " +
                    game.scoresPref.getInteger("LEVEL: " + Integer.toString(i)) + "%", Semtb001MajorAssignment.tinyFontFontWhite);
            currentLevel.setColor(Color.WHITE);

            // Add the current level label to the main table
            mainTable.row();
            levels.add(currentLevel);
            mainTable.add(currentLevel);
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
