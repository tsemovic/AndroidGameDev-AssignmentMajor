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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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

    public Texture background;
    public Semtb001IndividualAssignment game;
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;

    private OrthographicCamera camera;
    private Label play;
    private Label sandbox;
    private Label highScores;

    private Label blank;

    private Sprite backgroundSprite;

    private MainMenu menu = this;

    public MainMenu(){
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

        //Create buttons
        Label.LabelStyle style1 = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        play = new Label("PLAY", style1);
        play.setColor(Color.WHITE);

        sandbox = new Label("SANDBOX MODE", style1);
        sandbox.setColor(Color.WHITE);

        highScores = new Label("HIGHSCORES", style1);
        highScores.setColor(Color.WHITE);

        blank = new Label("", style1);

        //sprite for background image
        Texture backgroundTexture = new Texture("background.PNG");
        backgroundSprite =new Sprite(backgroundTexture);
        backgroundSprite.setSize(camera.viewportWidth, camera.viewportHeight);
        backgroundSprite.setAlpha(400);

        mainTable.add(play);
        mainTable.row();
        mainTable.add(blank);
        mainTable.row();
        mainTable.add(sandbox);
        mainTable.row();
        mainTable.add(blank);
        mainTable.row();
        mainTable.add(highScores);

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
