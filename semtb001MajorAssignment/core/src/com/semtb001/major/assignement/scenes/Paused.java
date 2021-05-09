package com.semtb001.individual.assignement.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.semtb001.individual.assignement.screens.MainMenu;
import com.semtb001.individual.assignement.screens.PlayScreen;
import com.semtb001.individual.assignement.tools.Assets;

// Class to present a overlay on the game screen showing the pause screen
public class Paused implements Disposable {

    // Paused stage, viewport and PlayScren objects
    public Stage stage;
    private Viewport viewport;
    private PlayScreen playScreen;

    // Objects that will be displayed in the paused overlay
    private Label pausedText;
    private Label continueText;
    private Label exitText;

    // Variables to determine if the labels are being touched
    public boolean exitTextActive;
    public boolean continueTextActive;

    // Sprite objects for the GameOver background
    public Sprite backgroundSprite;
    public SpriteBatch batch;

    public Paused(SpriteBatch spriteBatch, final Semtb001IndividualAssignment game,
                  final PlayScreen playScreen) {

        // Instantiate the paused spritebatch, viewport, stage, and PlayScreen
        this.playScreen = playScreen;
        batch = spriteBatch;
        viewport = new FillViewport(Semtb001IndividualAssignment.WORLD_WIDTH * Semtb001IndividualAssignment.PPM , Semtb001IndividualAssignment.WORLD_HEIGHT * Semtb001IndividualAssignment.PPM);
        stage = new Stage(viewport, spriteBatch);

        // Setup the table that is displayed in the paused overlay
        Table pausedTable = new Table();
        pausedTable.top();
        pausedTable.setFillParent(true);

        // Setup the labels that will go inside of the table
        pausedText = new Label("PAUSED", Semtb001IndividualAssignment.largeFontWhite);
        continueText = new Label("CONTINUE", Semtb001IndividualAssignment.smallFontFontWhite);
        exitText = new Label("EXIT", Semtb001IndividualAssignment.smallFontFontWhite);

        // Add the labels to the table
        pausedTable.add(pausedText).pad(Semtb001IndividualAssignment.PPM*2);
        pausedTable.row();
        pausedTable.add(continueText);
        pausedTable.row();
        pausedTable.add(exitText);

        // Add the table to the stage
        stage.addActor(pausedTable);

        // Set the background sprite the 'backgroundTint' asset
        backgroundSprite = new Sprite(Semtb001IndividualAssignment.assetManager.manager.get(Assets.backgroundTint));
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        backgroundSprite.setAlpha(400);

        // Continue label click listener
        continueText.addListener(new InputListener() {

            // If the Continue label is 'touched down' change the font colour to grey
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                continueText.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                continueTextActive = true;

                return true;
            }

            //If the user touches down on the Continue label and drags
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer){

                /* If the user touch is dragged and still on the Continue label
                (change to grey font colour and active continueTextActive) */
                if(x > 0 && x < continueText.getWidth() && y > 0 && y < continueText.getHeight()){
                    continueText.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                    continueTextActive = true;
                }else{

                    /* If the user touch is dragged and not over the Continue label
                    (de-activate continueTextActive and set the font colour to white) */
                    continueText.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
                    continueTextActive = false;
                }
            }

            /* If the user 'touches up' (lets go of the touch) and continueTextActive is active:
            resume the game and set the font colour to white */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(continueTextActive) {
                    playScreen.setPaused(false);
                }
                continueText.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
            }
        });

        // exitText label click listener
        exitText.addListener(new InputListener() {

            // If the exit label is 'touched down' change the font colour to grey
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exitText.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                exitTextActive = true;
                return true;
            }

            //If the user touches down on the exit label and drags
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer){

                /* If the user touch is dragged and still on the exit label
                (change to grey font colour and active exitTextActive) */
                if(x > 0 && x < exitText.getWidth() && y > 0 && y < exitText.getHeight()){
                    exitText.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                    exitTextActive = true;
                }else{

                    /* If the user touch is dragged and not over the exit label
                    (de-activate exitTextActive and set the font colour to white) */
                    exitText.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
                    exitTextActive = false;
                }
            }

            /* If the user 'touches up' (lets go of the touch) and exitTextActive is active:
            return to the main menu and dispose the playScreen and set the font colour back to white */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(exitTextActive) {
                    playScreen.stopMusic();
                    playScreen.stopSounds();
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
                }
                exitText.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
            }
        });
    }

    // Get the background sprite image
    public Sprite getBackgroundSprite(){
        return backgroundSprite;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
