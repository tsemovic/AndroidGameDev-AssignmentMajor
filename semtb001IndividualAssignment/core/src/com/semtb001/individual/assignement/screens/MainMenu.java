package com.semtb001.individual.assignement.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;

import java.util.logging.Level;

public class MainMenu implements Screen {

    private MainMenu menu = this;
    public Semtb001IndividualAssignment game;
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    private BitmapFont buttonFont;
    private Label title;
    private Label play;
    private Label highscores;
    private Label exit;
    private boolean playActive;
    private boolean highscoresActive;
    private boolean exitActive;

    private Sprite backgroundSprite;


    public MainMenu(Semtb001IndividualAssignment game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Semtb001IndividualAssignment.WORLD_WIDTH, Semtb001IndividualAssignment.WORLD_HEIGHT);

        viewport = new FillViewport(Semtb001IndividualAssignment.WORLD_WIDTH * Semtb001IndividualAssignment.PPM, Semtb001IndividualAssignment.WORLD_HEIGHT * Semtb001IndividualAssignment.PPM);

        camera.update();

        stage = new Stage(viewport, batch);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();


        //https://github.com/libgdx/libgdx/wiki/Gdx-freetype
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/poxel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = (int) (Semtb001IndividualAssignment.PPM * 4);
        BitmapFont titleFont = generator.generateFont(parameter);

        parameter.size = (int) (Semtb001IndividualAssignment.PPM * 2);
        buttonFont = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle titleTextStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label.LabelStyle buttonTextStyle = new Label.LabelStyle(buttonFont, Color.WHITE);

        title = new Label("GAME TITLE", titleTextStyle);
        play = new Label("PLAY", buttonTextStyle);
        highscores = new Label("HIGHSCORES", buttonTextStyle);
        exit = new Label("EXIT", buttonTextStyle);

        mainTable.add(title).pad(Semtb001IndividualAssignment.PPM / 2);
        mainTable.row();
        mainTable.add(play).pad(Semtb001IndividualAssignment.PPM / 4);
        mainTable.row();
        mainTable.add(highscores).pad(Semtb001IndividualAssignment.PPM / 4);
        mainTable.row();
        mainTable.add(exit).pad(Semtb001IndividualAssignment.PPM / 4);


        //sprite for background image
        Texture backgroundTexture = new Texture("gui/mainMenuBackground.png");
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(camera.viewportWidth, camera.viewportHeight);
        backgroundSprite.setAlpha(400);


        play.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                play.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                playActive = true;
                return true;
            }

            //allow the user to drag off the button to not activate a click
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (x > 0 && x < play.getWidth() && y > 0 && y < play.getHeight()) {
                    play.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                    playActive = true;
                } else {
                    play.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
                    playActive = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (playActive) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new LevelSelect(game));

                }
                play.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
            }
        });

        highscores.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                highscores.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                highscoresActive = true;
                return true;
            }

            //allow the user to drag off the button to not activate a click
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (x > 0 && x < highscores.getWidth() && y > 0 && y < highscores.getHeight()) {
                    highscores.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                    highscoresActive = true;
                } else {
                    highscores.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
                    highscoresActive = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (highscoresActive) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new HighScores(game));
                }
                highscores.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
            }
        });

        exit.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exit.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                exitActive = true;
                return true;
            }

            //allow the user to drag off the button to not activate a click
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (x > 0 && x < exit.getWidth() && y > 0 && y < exit.getHeight()) {
                    exit.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                    exitActive = true;
                } else {
                    exit.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
                    exitActive = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (exitActive) {
                    Gdx.app.exit();
                    System.exit(0);
                }
                exit.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
            }
        });

        stage.addActor(mainTable);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();
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
