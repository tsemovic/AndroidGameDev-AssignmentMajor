package com.semtb001.individual.assignement.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class LevelBrief implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private PlayScreen playScreen;

    private Label startLabel;
    private Label countdownLabel;
    private Integer countdown;

    public boolean timeToPlay;
    private float timeCount;

    public LevelBrief(SpriteBatch spriteBatch, final PlayScreen playScreen) {
        this.playScreen = playScreen;
        viewport = new FillViewport(Semtb001IndividualAssignment.WORLD_WIDTH * Semtb001IndividualAssignment.PPM , Semtb001IndividualAssignment.WORLD_HEIGHT * Semtb001IndividualAssignment.PPM);
        stage = new Stage(viewport, spriteBatch);
        countdown = 3;
        timeToPlay = false;

        //table setup
        Table hudTable = new Table();
        hudTable.center();
        hudTable.setFillParent(true);

        startLabel = new Label("STARTING IN:", Semtb001IndividualAssignment.smallFontFontWhite);
        countdownLabel = new Label(Integer.toString(countdown), Semtb001IndividualAssignment.tinyFontFontWhite);

        hudTable.add(startLabel).padLeft(Semtb001IndividualAssignment.PPM * 2);
        hudTable.row();
        hudTable.add(countdownLabel);

        stage.addActor(hudTable);

    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (countdown > 1) {
                countdown--;
            } else {
                timeToPlay = true;
            }
            countdownLabel.setText(countdown);
            timeCount = 0;
        }
    }

    @Override
    public void dispose() {

    }

}
