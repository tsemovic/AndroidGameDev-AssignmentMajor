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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.major.assignement.Semtb001MajorAssignment;

import java.util.List;

// Class to display Help Menu (Game Instructions)
public class Help implements Screen {

    // Help Menu game, spritebatch, stage, viewport, and camera objects
    public Semtb001MajorAssignment game;
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    // Objects that will be displayed in the Help Menu
    private Label backLabel;
    private Label helpLabel;
    private Label descriptionLabel;

    // Help Menu Background sprite object
    private Sprite backgroundSprite;

    public Help(Semtb001MajorAssignment semtb001MajorAssignment){

        // Instantiate game and spritebatch
        game = semtb001MajorAssignment;
        batch = new SpriteBatch();

        // Setup camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Semtb001MajorAssignment.WORLD_WIDTH, Semtb001MajorAssignment.WORLD_HEIGHT);

        // Setup viewport
        viewport = Semtb001MajorAssignment.viewport;
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
        Table helpTable = new Table();
        helpTable.setFillParent(true);
        helpTable.center();

        // Create 'back' table to be displayed in the stage
        Table backTable = new Table();
        backTable.setFillParent(true);
        backTable.top().left();
        backTable.padTop(20);
        backTable.padLeft(50);

        // Create 'main' table to be displayed in the 'help' table
        Table mainTable = new Table();

        // Create label to be displayed in the 'help' table
        helpLabel = new Label("HELP" , Semtb001MajorAssignment.largeFontWhite);
        helpLabel.setColor(Color.WHITE);

        // Create 'back' label to be displayed in the 'back' table
        backLabel = new Label("<BACK", Semtb001MajorAssignment.smallFontFontWhite);
        backLabel.setColor(Color.WHITE);

        // Create label that contains brief game instructions
        descriptionLabel = new Label("HARVEST THE AMOUNT OF WHEAT SHOWN IN THE LEVEL BRIEF.\n" +
                "SELECT AN ITEM IN THE HOT-BAR AND TAP ANY VACANT \nSCREEN SPACE TO USE THAT ITEM.\n" +
                "WHEAT IS GROWN BY PLANTING SEEDS ON TILLED SOIL AND \nENSURING THAT THERE IS WATER NEARBY." +
                "\nCAREFUL!! THE SHEEP ARE VERY HUNGRY!",
                Semtb001MajorAssignment.miniFontFontWhite);
        descriptionLabel.setColor(Color.WHITE);

        // Add labels to the 'help' table
        helpTable.add(helpLabel).padBottom(Semtb001MajorAssignment.PPM);
        helpTable.row();
        helpTable.add(descriptionLabel).center();
        helpTable.row();

        // Add 'back' label to the 'back' table
        backTable.add(backLabel);

        // Add the 'main' table to the 'help' table
        helpTable.add(mainTable);

        // Add the 'help' and 'back' tables to the stage
        stage.addActor(helpTable);
        stage.addActor(backTable);

        // Add input listener to the 'back' label
        backLabel.addListener(new ClickListener() {
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
        stage.dispose();
    }
}
