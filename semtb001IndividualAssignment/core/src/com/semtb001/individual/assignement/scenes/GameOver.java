package com.semtb001.individual.assignement.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

import java.text.DecimalFormat;

/* Class to present a overlay on the game screen showing that the game is over
(this is shown when the player dies and also when the player completes the level */
public class GameOver implements Disposable {

    // GameOver stage viewport, and PlayScreen objects
    public Stage stage;
    private Viewport viewport;
    private PlayScreen playScreen;

    // Objects that will be displayed in the GameOver overlay
    private Label headerText;
    private Label subHeaderText;
    private Label exitText;

    // Variables to determine if the labels are being touched
    public boolean subHeaderTextActive;
    public boolean exitTextActive;

    // Sprite objects for the GameOver background
    public Sprite backgroundSprite;
    public SpriteBatch batch;

    public GameOver(SpriteBatch spriteBatch, final Semtb001IndividualAssignment game,
                    final PlayScreen playScreen) {

        // Instantiate the GameOver spritebatch, viewport, stage, and PlayScreen
        this.playScreen = playScreen;
        batch = spriteBatch;
        viewport = new FillViewport(Semtb001IndividualAssignment.WORLD_WIDTH * Semtb001IndividualAssignment.PPM,
                Semtb001IndividualAssignment.WORLD_HEIGHT * Semtb001IndividualAssignment.PPM);
        stage = new Stage(viewport, spriteBatch);

        // Setup the table that is displayed in the GameOver overlay
        Table pausedTable = new Table();
        pausedTable.top();
        pausedTable.setFillParent(true);

        // Set the exitText label to 'EXIT' (does not change if the player dies or completes the level)
        exitText = new Label("EXIT", Semtb001IndividualAssignment.smallFontFontWhite);

        // If the player is dead
        if (playScreen.getPlayer().playerIsDead) {

            // Set the header text to 'GAME OVER' and the subHeader text to 'TRY AGAIN'
            headerText = new Label("GAME OVER", Semtb001IndividualAssignment.largeFontWhite);
            subHeaderText = new Label("TRY AGAIN", Semtb001IndividualAssignment.smallFontFontWhite);

            // Add the labels to the table
            pausedTable.add(headerText).pad(Semtb001IndividualAssignment.PPM * 2);
            pausedTable.row();
            pausedTable.add(subHeaderText);
            pausedTable.row();
            pausedTable.add(exitText);

        } else {

            // If the player is not dead (completes the level successfully)
            // Set the header text to "LEVEL PASSED" and the subHeader text to "TRY AGAIN"
            headerText = new Label("LEVEL PASSED", Semtb001IndividualAssignment.mediumFontFontWhite);
            subHeaderText = new Label("TRY AGAIN", Semtb001IndividualAssignment.smallFontFontWhite);

            /* If the player has collected all of the coins in the map: unlock the next level
            (and save to the saved data) and set the subHeader text to "NEXT LEVEL" if there is
            a another level to play*/
            if (playScreen.getHud().getCoinCount() == playScreen.getBox2dWorldCreator().getJewels().size()) {
                if (Integer.valueOf(playScreen.currentLevel.substring(playScreen.currentLevel.length() - 1)) != Semtb001IndividualAssignment.NUMBER_OF_LEVELS) {
                    String newLevel = "LEVEL: " + Integer.toString(Integer.valueOf(playScreen.currentLevel.substring(playScreen.currentLevel.length() - 1)) + 1);
                    Semtb001IndividualAssignment.levelsPref.putBoolean(newLevel, true);
                    Semtb001IndividualAssignment.levelsPref.flush();
                    System.out.println(newLevel + " unlcoked");
                    subHeaderText = new Label("NEXT LEVEL",
                            Semtb001IndividualAssignment.smallFontFontWhite);
                } else {

                    /* If there are no other levels to play (completed all levels), set the
                    subHeader text to "ALL LEVELS COMPLETE" */
                    subHeaderText = new Label("ALL LEVELS COMPLETE!",
                            Semtb001IndividualAssignment.smallFontFontWhite);
                }
            }

            // Create the Level Completion Percentage label (percentage of coins collected)
            float jewelCount = playScreen.getHud().getCoinCount();
            float jewelTotal = playScreen.getBox2dWorldCreator().getJewels().size();
            Label levelCompletion = new Label("LEVEL COMPLETION: " +
                    (int) ((jewelCount / jewelTotal) * 100) + "%",
                    Semtb001IndividualAssignment.smallFontFontWhite);

            /* If percentage completed is greater than values in the saved data: update saved data
            with new completion percentage */
            if ((int) ((jewelCount / jewelTotal) * 100) > Semtb001IndividualAssignment.scoresPref.getInteger(playScreen.currentLevel)) {
                Semtb001IndividualAssignment.scoresPref.putInteger(playScreen.currentLevel, (int) ((jewelCount / jewelTotal) * 100));
                Semtb001IndividualAssignment.scoresPref.flush();
            }

            // Add the labels to the table
            pausedTable.add(headerText).pad(Semtb001IndividualAssignment.PPM * 2);
            pausedTable.row();
            pausedTable.add(levelCompletion);
            pausedTable.row();
            pausedTable.add(subHeaderText);
            pausedTable.row();
            pausedTable.add(exitText);
        }

        // Add the table to the stage
        stage.addActor(pausedTable);

        // Set the background sprite the 'backgroundTint' asset
        backgroundSprite = new Sprite(Semtb001IndividualAssignment.assetManager.manager.get(Assets.backgroundTint));
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        backgroundSprite.setAlpha(400);

        //Listener for the subHeader text label
        subHeaderText.addListener(new InputListener() {

            // If the subHeader label is 'touched down' change the font colour to grey
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                subHeaderText.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                subHeaderTextActive = true;

                return true;
            }

            //If the user touches down on the subHeader label and drags
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                /* If the user touch is dragged and still on the subHeader label
                (change to grey font colour and active subHeaderTextActive) */
                if (x > 0 && x < subHeaderText.getWidth() && y > 0 && y < subHeaderText.getHeight()) {
                    subHeaderText.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                    subHeaderTextActive = true;
                } else {

                    /* If the user touch is dragged and not over the subHeader label
                    (de-activate subHeaderTextActive and set the font colour to white) */
                    subHeaderText.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
                    subHeaderTextActive = false;
                }
            }

            /* If the user 'touches up' (lets go of the touch) and subHeaderTextActive is active:
            get the level that user is going to play, dispose the playScreen and set the font
            colour back to white */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (subHeaderTextActive) {
                    playScreen.dispose();
                    playScreen.stopSounds();
                    playScreen.stopMusic();
                    dispose();

                    ((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(game, getNextLevel()));
                }
                subHeaderText.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
            }
        });

        //Listener for the exit text label
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
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                /* If the user touch is dragged and still on the exit label
                (change to grey font colour and active exitTextActive) */
                if (x > 0 && x < exitText.getWidth() && y > 0 && y < exitText.getHeight()) {
                    exitText.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                    exitTextActive = true;
                } else {

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
                if (exitTextActive) {
                    playScreen.dispose();
                    playScreen.stopSounds();
                    playScreen.stopMusic();
                    dispose();

                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));

                }
                exitText.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
            }
        });
    }

    // Method to get the next level that user is going to play
    public String getNextLevel() {

        String level = null;

        // If the player is dead: return the current level that they are playing
        if (playScreen.getPlayer().playerIsDead) {
            level = playScreen.currentLevel;
        } else {

            // If the player player completes the level, reutn the next level
            if (Integer.parseInt(playScreen.currentLevel.substring(7, 8)) != Semtb001IndividualAssignment.NUMBER_OF_LEVELS) {
                level = "LEVEL: " + (Integer.parseInt(playScreen.currentLevel.substring(7, 8)) + 1);
            } else {

                // If there is no next level, return the current level (all levels are completed)
                level = playScreen.currentLevel;
            }
        }
        return level;
    }

    // Get the background sprite image
    public Sprite getBackgroundSprite() {
        return backgroundSprite;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
