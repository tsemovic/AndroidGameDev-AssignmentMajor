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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;

import java.util.ArrayList;
import java.util.List;

public class Statistics implements Screen {

    public Semtb001IndividualAssignment game;
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;

    private Label back;
    private List<Label> levels;
    private Integer numberOfLevels;
    private Label levelsLabel;
    private Label descriptionLabel;


    private Sprite backgroundSprite;

    public Statistics(Semtb001IndividualAssignment semtb001IndividualAssignment){
        game = semtb001IndividualAssignment;
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

        //sprite for background image
        Texture backgroundTexture = new Texture("gui/mainMenuBackground.png");
        backgroundSprite =new Sprite(backgroundTexture);
        backgroundSprite.setSize(camera.viewportWidth, camera.viewportHeight);
        backgroundSprite.setAlpha(400);


        Table levelLabelTable = new Table();
        levelLabelTable.setFillParent(true);
        levelLabelTable.center();

        Table backTable = new Table();
        backTable.setFillParent(true);
        backTable.top().left().pad(20);

        Table mainTable = new Table();

        levelsLabel = new Label("STATISTICS" ,Semtb001IndividualAssignment.largeFontWhite);
        levelsLabel.setColor(Color.WHITE);

        descriptionLabel = new Label("LEVEL COMPLETION PERCENTAGE", Semtb001IndividualAssignment.tinyFontFontWhite);
        descriptionLabel.setColor(Color.WHITE);

        back = new Label("<BACK", Semtb001IndividualAssignment.smallFontFontWhite);
        back.setColor(Color.WHITE);

        numberOfLevels = Semtb001IndividualAssignment.NUMBER_OF_LEVELS;
        levels = new ArrayList<Label>();

        levelLabelTable.add(levelsLabel).padBottom(Semtb001IndividualAssignment.PPM);
        levelLabelTable.row();
        levelLabelTable.add(descriptionLabel);
        levelLabelTable.row();

        backTable.add(back);

        for(int i = 1; i <= numberOfLevels; i++){
            Label currentLevel = new Label("LEVEL: " + Integer.toString(i) + " = " +
                    game.scoresPref.getInteger("LEVEL: " + Integer.toString(i)) + "%", Semtb001IndividualAssignment.tinyFontFontWhite);
            currentLevel.setColor(Color.WHITE);

            mainTable.row();
            levels.add(currentLevel);
            mainTable.add(currentLevel);
        }
        levelLabelTable.add(mainTable);

        stage.addActor(levelLabelTable);
        stage.addActor(backTable);

        //Listeners
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        //add background image
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
