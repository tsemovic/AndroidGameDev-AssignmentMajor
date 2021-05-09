package com.semtb001.major.assignement.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.major.assignement.Semtb001MajorAssignment;

/* Class to present a overlay on the game screen showing the level brief (a count down until
the game starts */
public class LevelBrief implements Disposable {

    // LevelBrief stage and viewport objects
    public Stage stage;
    private Viewport viewport;

    // Obects that will be displayed in the LevelBrief
    private Label startLabel;
    private Label countdownLabel;

    // Variables for the countdown time
    private Integer countdown;
    private float timeCount;

    // Variable for when the countdown has stopped and it's time to play the game
    public boolean timeToPlay;

    public LevelBrief(SpriteBatch spriteBatch) {

        // Instantiate the viewport and stage
        viewport = new FillViewport(Semtb001MajorAssignment.WORLD_WIDTH * Semtb001MajorAssignment.PPM , Semtb001MajorAssignment.WORLD_HEIGHT * Semtb001MajorAssignment.PPM);
        stage = new Stage(viewport, spriteBatch);

        // Set the countdown timer to 3 seconds and set timeToPlay to false
        countdown = 3;
        timeToPlay = false;

        // Setup table that is displayed in the LevelBrief
        Table hudTable = new Table();
        hudTable.center();
        hudTable.setFillParent(true);


        // Setup the labels that will go inside of the table
        startLabel = new Label("STARTING IN:", Semtb001MajorAssignment.smallFontFontWhite);
        countdownLabel = new Label(Integer.toString(countdown), Semtb001MajorAssignment.tinyFontFontWhite);

        // Add the labels to the table
        hudTable.add(startLabel).padLeft(Semtb001MajorAssignment.PPM * 2);
        hudTable.row();
        hudTable.add(countdownLabel);

        // Add the table to the stage
        stage.addActor(hudTable);
    }

    // Method to update the countdown time
    public void update(float dt){

        // This code executes every 1 second
        timeCount += dt;
        if(timeCount >= 1){

            // If the countdown timer is not yet 0, subtract 1
            if (countdown > 1) {
                countdown--;
            } else {

                // If the countdown time is 0, it's time to play the game
                timeToPlay = true;
            }

            // Update the countDownLabel text to the countDown timer
            countdownLabel.setText(countdown);
            timeCount = 0;
        }
    }

    @Override
    public void dispose() {

    }

}
