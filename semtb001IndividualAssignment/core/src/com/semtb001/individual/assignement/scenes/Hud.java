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

    private Label pause;
    public boolean pausedPressed;
    private BitmapFont pauseFont;


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
        pauseFont = generator.generateFont(parameter);
        generator.dispose();

        final Label.LabelStyle pausedTextStyle = new Label.LabelStyle(pauseFont, Color.WHITE);
        pause = new Label("ii", pausedTextStyle);

        hudTable.add(pause).padRight(Semtb001IndividualAssignment.PPM * 2);

        stage.addActor(hudTable);

        //pause label click listener
        pause.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pause.setStyle(new Label.LabelStyle(pauseFont, Color.GRAY));
                pausedPressed = true;
                return true;
            }

            //allow the user to drag off the button to not activate a click
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (x > 0 && x < pause.getWidth() && y > 0 && y < pause.getHeight()) {
                    pause.setStyle(new Label.LabelStyle(pauseFont, Color.GRAY));
                    pausedPressed = true;
                } else {
                    pause.setStyle(new Label.LabelStyle(pauseFont, Color.WHITE));
                    pausedPressed = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pausedPressed) {
                    playScreen.setPaused(true);
                }
                pausedPressed = false;
                pause.setStyle(new Label.LabelStyle(pauseFont, Color.WHITE));
            }
        });

    }

    @Override
    public void dispose() {

    }
}
