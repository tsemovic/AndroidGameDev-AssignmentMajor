package com.semtb001.major.assignement.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.screens.PlayScreen;

/* Class to present a overlay on the game screen showing the number of coins collected and the
pause functionality */
public class Hud implements Disposable {

    // HUD stage and viewport objects
    public Stage stage;
    private Viewport viewport;

    // Objects that will be displayed in the HUD
    private Label pause;
    private Label coins;
    private Label coinCountLabel;

    // Variable for counting the coins that have been collected
    private Integer coinCoint;

    public boolean pausedPressed;

    public Hud(SpriteBatch spriteBatch, final PlayScreen playScreen) {

        // Instantiate the viewport and stage objects
        viewport = new FillViewport(Semtb001MajorAssignment.WORLD_WIDTH * Semtb001MajorAssignment.PPM, Semtb001MajorAssignment.WORLD_HEIGHT * Semtb001MajorAssignment.PPM);
        stage = new Stage(viewport, spriteBatch);

        // Set the number of coints collected to 0
        coinCoint = 0;

        // Setup the table that is displayed in the HUD
        Table hudTable = new Table();
        hudTable.top().padTop(30);
        hudTable.setFillParent(true);

        // Setup the labels that will go inside of the table
        pause = new Label("ii", Semtb001MajorAssignment.smallFontFontWhite);
        coins = new Label("COINS: ", Semtb001MajorAssignment.tinyFontFontWhite);
        coinCountLabel = new Label(Integer.toString(coinCoint), Semtb001MajorAssignment.tinyFontFontWhite);

        // Set the HUD labels to have an opacity of 75% so that the game view isn't as obstructed
        float hudTextAlpha = 0.75f;
        pause.setColor(1, 1, 1, hudTextAlpha);
        coins.setColor(1, 1, 1, hudTextAlpha);
        coinCountLabel.setColor(1, 1, 1, hudTextAlpha);

        // Add the labels to the table
        hudTable.add(coins).padLeft(Semtb001MajorAssignment.PPM * 2);
        hudTable.add(coinCountLabel);
        hudTable.add(pause).right().expandX().padRight(Semtb001MajorAssignment.PPM * 2);

        // Add the table to the stage
        stage.addActor(hudTable);

        // Pause label input listener
        pause.addListener(new InputListener() {

            // If the paused label is 'touched down' change the font colour to grey
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pause.setStyle(Semtb001MajorAssignment.smallFontFontGrey);
                pausedPressed = true;
                return true;
            }

            //If the user touches down on the pause label and drags
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                /* If the user touch is dragged and still on the pause label
                (change to grey font colour and active pausedPressed) */
                if (x > 0 && x < pause.getWidth() && y > 0 && y < pause.getHeight()) {
                    pause.setStyle(Semtb001MajorAssignment.smallFontFontGrey);
                    pausedPressed = true;
                } else {

                    /* If the user touch is dragged and not over the pause label
                    (de-activate pausedPressed and set the font colour to white) */
                    pause.setStyle(Semtb001MajorAssignment.smallFontFontWhite);
                    pausedPressed = false;
                }
            }

            /* If the user 'touches up' (lets go of the touch) and pausedPressed is active: pause
            the game and set the font colour back to white */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pausedPressed) {
                    playScreen.setPaused(true);
                }
                pausedPressed = false;
                pause.setStyle(Semtb001MajorAssignment.smallFontFontWhite);
            }
        });

    }

    // Method to update the coin counter and associated label (called when the user collects a coin)
    public void update() {
        coinCoint++;
        coinCountLabel.setText(coinCoint);
    }

    @Override
    public void dispose() {

    }

    // Getter for the coin count (number of coins collected by the player)
    public Integer getCoinCount() {
        return coinCoint;
    }
}
