package com.semtb001.individual.assignement.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private PlayScreen playScreen;

    private Skin skin;
    private float percentageComplete;

    public boolean pausedPressed;


    public Hud(SpriteBatch spriteBatch, final PlayScreen playScreen) {
        this.playScreen = playScreen;
        viewport = new FitViewport(Semtb001IndividualAssignment.WORLD_WIDTH * Semtb001IndividualAssignment.PPM , Semtb001IndividualAssignment.WORLD_HEIGHT * Semtb001IndividualAssignment.PPM);
        stage = new Stage(viewport, spriteBatch);

        Table hudTable = new Table();
        hudTable.top().right();
        hudTable.setFillParent(true);

        //https://github.com/libgdx/libgdx/wiki/Gdx-freetype
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/poxel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = (int) (Semtb001IndividualAssignment.PPM * 2);
        BitmapFont font1 = generator.generateFont(parameter);
        generator.dispose();

        BitmapFont f = new BitmapFont();
        Label.LabelStyle pausedTextStyle = new Label.LabelStyle(font1, Color.WHITE);
        Label l = new Label("ii", pausedTextStyle);

        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        final TextButton button = new TextButton("ii", skin, "default");
//        button.setWidth(600f);
//        button.setHeight(400f);
        hudTable.add(button);
        hudTable.add(l);


//        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
//        progressBarStyle.background = skin.getDrawable("default");
//
//        ProgressBar progressBar = new ProgressBar(0,100,1f,false, progressBarStyle);
//        progressBar.setSize(500, 50);
//        hudTable.add(progressBar);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setPausedPressed(true);
                playScreen.setPaused(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setPausedPressed(false);
            }
        });

        stage.addActor(hudTable);

    }

    public void update(float delta){
        percentageComplete = (playScreen.getPlayerPos().x / playScreen.getWorldEndPosition()) * 100;

    }

    public boolean isPausedPressed() {
        return pausedPressed;
    }

    public void setPausedPressed(boolean pausedPressed) {
        this.pausedPressed = pausedPressed;
    }


    @Override
    public void dispose() {

    }
}
