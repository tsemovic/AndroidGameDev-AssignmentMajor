package com.semtb001.individual.assignement.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;


public class Paused implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private PlayScreen playScreen;

    private Label pausedText;

    public Paused(SpriteBatch spriteBatch, final PlayScreen playScreen) {
        this.playScreen = playScreen;
        viewport = new FitViewport(Semtb001IndividualAssignment.WORLD_WIDTH * Semtb001IndividualAssignment.PPM , Semtb001IndividualAssignment.WORLD_HEIGHT * Semtb001IndividualAssignment.PPM);
        stage = new Stage(viewport, spriteBatch);

        Table pausedTable = new Table();
        pausedTable.center();
        pausedTable.setFillParent(true);

        Label.LabelStyle style1 = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        pausedText = new Label("PAUSED", style1);

        pausedTable.add(pausedText);

        pausedText.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.setPaused(false);
            }
        });

        stage.addActor(pausedTable);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
