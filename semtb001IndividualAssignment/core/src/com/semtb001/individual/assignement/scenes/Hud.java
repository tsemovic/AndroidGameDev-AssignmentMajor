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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private PlayScreen playScreen;

    private Label pause;
    private Label jewels;
    private Label jewelCountLabel;
    private Integer jewelCount;

    public boolean pausedPressed;

    public Hud(SpriteBatch spriteBatch, final PlayScreen playScreen) {
        this.playScreen = playScreen;
        viewport = new FillViewport(Semtb001IndividualAssignment.WORLD_WIDTH * Semtb001IndividualAssignment.PPM , Semtb001IndividualAssignment.WORLD_HEIGHT * Semtb001IndividualAssignment.PPM);
        stage = new Stage(viewport, spriteBatch);
        jewelCount = 0;

        //table setup
        Table hudTable = new Table();
        hudTable.top().padTop(30);
        hudTable.setFillParent(true);

        pause = new Label("ii", Semtb001IndividualAssignment.smallFontFontWhite);
        jewels = new Label("JEWELS: ", Semtb001IndividualAssignment.tinyFontFontWhite);
        jewelCountLabel = new Label(Integer.toString(jewelCount), Semtb001IndividualAssignment.tinyFontFontWhite);

        hudTable.add(jewels).padLeft(Semtb001IndividualAssignment.PPM * 2);
        hudTable.add(jewelCountLabel);
        hudTable.add(pause).right().expandX().padRight(Semtb001IndividualAssignment.PPM * 2);

        stage.addActor(hudTable);

        //pause label click listener
        pause.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pause.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                pausedPressed = true;
                return true;
            }

            //allow the user to drag off the button to not activate a click
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (x > 0 && x < pause.getWidth() && y > 0 && y < pause.getHeight()) {
                    pause.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                    pausedPressed = true;
                } else {
                    pause.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
                    pausedPressed = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pausedPressed) {
                    playScreen.setPaused(true);
                }
                pausedPressed = false;
                pause.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
            }
        });

    }

    public void update(){
        jewelCount++;
        jewelCountLabel.setText(jewelCount);
    }

    @Override
    public void dispose() {

    }

    public Integer getJewelCount(){
        return jewelCount;
    }
}
