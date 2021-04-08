package com.semtb001.individual.assignement.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.tools.Assets;

// Class for the main menu (shown at startup)
public class MainMenu implements Screen {

    // Main Menu game, spritebatch, stage, viewport, and camera objects
    public Semtb001IndividualAssignment game;
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    // Objects that will be displayed in the Main Menu
    private Label title;
    private Label play;
    private Label highscores;
    private Label exit;

    // Variables to determine if the labels are being touched
    private boolean playActive;
    private boolean highscoresActive;
    private boolean exitActive;

    // Main Menu Background sprite object
    private Sprite backgroundSprite;

    public MainMenu(Semtb001IndividualAssignment game) {

        // Instantiate game and spritebatch
        this.game = game;
        batch = new SpriteBatch();

        // Setup camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Semtb001IndividualAssignment.WORLD_WIDTH, Semtb001IndividualAssignment.WORLD_HEIGHT);

        // Setup viewport
        viewport = new FillViewport(Semtb001IndividualAssignment.WORLD_WIDTH * Semtb001IndividualAssignment.PPM, Semtb001IndividualAssignment.WORLD_HEIGHT * Semtb001IndividualAssignment.PPM);
        camera.update();

        // Setup stage
        stage = new Stage(viewport, batch);

    }

    @Override
    public void show() {

        // Add the stage to the input processors
        Gdx.input.setInputProcessor(stage);

        // Create Table to be displayed in the stage
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        // Create labels to be displayed in the table
        title = new Label("GAME TITLE", Semtb001IndividualAssignment.largeFontWhite);
        play = new Label("PLAY", Semtb001IndividualAssignment.smallFontFontWhite);
        highscores = new Label("STATISTICS", Semtb001IndividualAssignment.smallFontFontWhite);
        exit = new Label("EXIT", Semtb001IndividualAssignment.smallFontFontWhite);

        // Add the labels to the table
        mainTable.add(title).pad(Semtb001IndividualAssignment.PPM / 2);
        mainTable.row();
        mainTable.add(play).pad(Semtb001IndividualAssignment.PPM / 4);
        mainTable.row();
        mainTable.add(highscores).pad(Semtb001IndividualAssignment.PPM / 4);
        mainTable.row();
        mainTable.add(exit).pad(Semtb001IndividualAssignment.PPM / 4);

        // Add the table to the stage
        stage.addActor(mainTable);

        // Setup the background image sprite
        backgroundSprite = new Sprite(Semtb001IndividualAssignment.assetManager.manager.get(Assets.menuBackground));
        backgroundSprite.setSize(camera.viewportWidth, camera.viewportHeight);
        backgroundSprite.setAlpha(400);

        // Play label input listener
        play.addListener(new InputListener() {

            // If the label is 'touched down' change the font colour to grey
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                play.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                playActive = true;
                return true;
            }

            //If the user touches down on the label and drags
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                /* If the user touch is dragged and still on the label
                (change to grey font colour and active playActive) */
                if (x > 0 && x < play.getWidth() && y > 0 && y < play.getHeight()) {
                    play.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                    playActive = true;
                } else {

                    /* If the user touch is dragged and not over the label
                    (de-activate playActive and set the font colour to white) */
                    play.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
                    playActive = false;
                }
            }

            /* If the user 'touches up' (lets go of the touch) and playActive is active:
            set the screen to the LevelSelect menu and set the font colour back to white */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (playActive) {
                    Semtb001IndividualAssignment.playMenuClick();
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new LevelSelect(game));
                }
                play.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
            }
        });

        // Highscores label input listener
        highscores.addListener(new InputListener() {

            // If the label is 'touched down' change the font colour to grey
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                highscores.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                highscoresActive = true;
                return true;
            }

            //If the user touches down on the label and drags
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                /* If the user touch is dragged and still on the label
                (change to grey font colour and active highscoresActive) */
                if (x > 0 && x < highscores.getWidth() && y > 0 && y < highscores.getHeight()) {
                    highscores.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                    highscoresActive = true;
                } else {

                    /* If the user touch is dragged and not over the label
                    (de-activate highscoresActive and set the font colour to white) */
                    highscores.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
                    highscoresActive = false;
                }
            }

            /* If the user 'touches up' (lets go of the touch) and playActive is active:
            set the screen to the Statistics menu and set the font colour back to white */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (highscoresActive) {
                    Semtb001IndividualAssignment.playMenuClick();
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new Statistics(game));
                }
                highscores.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
            }
        });

        // Exit label input listener
        exit.addListener(new InputListener() {

            // If the label is 'touched down' change the font colour to grey
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exit.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                exitActive = true;
                return true;
            }

            //If the user touches down on the label and drags
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                /* If the user touch is dragged and still on the label
                (change to grey font colour and active exitActive) */
                if (x > 0 && x < exit.getWidth() && y > 0 && y < exit.getHeight()) {
                    exit.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                    exitActive = true;
                } else {

                    /* If the user touch is dragged and not over the label
                    (de-activate exitActive and set the font colour to white) */
                    exit.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
                    exitActive = false;
                }
            }

            /* If the user 'touches up' (lets go of the touch) and exitActive is active:
            exit the application and set the font colour back to white */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (exitActive) {
                    Semtb001IndividualAssignment.playMenuClick();
                    Gdx.app.exit();
                    System.exit(0);
                }
                exit.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
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
