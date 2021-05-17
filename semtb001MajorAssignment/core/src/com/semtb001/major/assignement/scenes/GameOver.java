package com.semtb001.major.assignement.scenes;

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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.screens.MainMenu;
import com.semtb001.major.assignement.screens.PlayScreen;
import com.semtb001.major.assignement.tools.Assets;

/* Class to present a overlay on the game screen showing that the game is over
(this is shown when the hud game timer runs out */
public class GameOver implements Disposable {

    // GameOver stage viewport, and PlayScreen objects
    public Stage stage;
    private Viewport viewport;
    private PlayScreen screen;

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

    // Wheat counter objects
    public boolean levelPassed;
    public int wheatCount;
    public int wheatTotal;

    public GameOver(SpriteBatch spriteBatch, final Semtb001MajorAssignment game,
                    final PlayScreen playScreen) {

        // Instantiate the GameOver spritebatch, viewport, stage, and PlayScreen
        this.screen = playScreen;
        batch = spriteBatch;
        viewport = Semtb001MajorAssignment.viewport;
        stage = new Stage(viewport, spriteBatch);

        // Instantiate levelPassed, and wheat counts
        levelPassed = screen.getLevelPassed();
        wheatCount = screen.getWheatHarvested();
        wheatTotal = screen.getWheatToPassLevel();

        // Setup the table that is displayed in the GameOver overlay
        Table pausedTable = new Table();
        pausedTable.center();
        pausedTable.setFillParent(true);

        // Set the exitText label to 'EXIT', headText to 'GAME OVER' and subHeaderText to 'TRY AGAIN'
        exitText = new Label("EXIT", Semtb001MajorAssignment.smallFontFontWhite);
        headerText = new Label("GAME OVER", Semtb001MajorAssignment.largeFontWhite);
        subHeaderText = new Label("TRY AGAIN", Semtb001MajorAssignment.smallFontFontWhite);

        // If the level has been completed
        if (levelPassed) {

            // Set the header text to "LEVEL PASSED" and the subHeader text to "TRY AGAIN"
            headerText = new Label("LEVEL PASSED", Semtb001MajorAssignment.mediumFontFontWhite);
            subHeaderText = new Label("TRY AGAIN", Semtb001MajorAssignment.smallFontFontWhite);

            /* If the player has harvested the required wheat: unlock the next level
            (and save to the saved data) and set the subHeader text to "NEXT LEVEL" if there is
            a another level to play*/
            if (Integer.valueOf(playScreen.currentLevel.substring(playScreen.currentLevel.length() - 1)) != Semtb001MajorAssignment.NUMBER_OF_LEVELS) {
                String newLevel = "LEVEL: " + Integer.toString(Integer.valueOf(playScreen.currentLevel.substring(playScreen.currentLevel.length() - 1)) + 1);
                Semtb001MajorAssignment.levelsPref.putBoolean(newLevel, true);
                Semtb001MajorAssignment.levelsPref.flush();
                System.out.println(newLevel + " unlcoked");
                subHeaderText = new Label("NEXT LEVEL",
                        Semtb001MajorAssignment.smallFontFontWhite);
            } else {

                    /* If there are no other levels to play (completed all levels), set the
                    subHeader text to "ALL LEVELS COMPLETE" */
                subHeaderText = new Label("ALL LEVELS COMPLETE!",
                        Semtb001MajorAssignment.smallFontFontWhite);
            }
        }

        // Create the level completion label (shows how much wheat was harvested vs the required amount
        Label levelCompletion = new Label("WHEAT HARVESTED: " + wheatCount + " / " + wheatTotal,
                Semtb001MajorAssignment.smallFontFontWhite);

            /* If the amount of wheat harvested is greater than values in the saved data: update saved data
            with new harvest counts */
        if (wheatCount > Semtb001MajorAssignment.scoresPref.getInteger(playScreen.currentLevel)) {
            Semtb001MajorAssignment.scoresPref.putInteger(playScreen.currentLevel, wheatCount);
            Semtb001MajorAssignment.scoresPref.flush();
        }

        // Add the labels to the table
        pausedTable.add(headerText).pad(1);
        pausedTable.row();
        pausedTable.add(levelCompletion);
        pausedTable.row();
        pausedTable.add(subHeaderText);
        pausedTable.row();
        pausedTable.add(exitText);

        // Add the table to the stage
        stage.addActor(pausedTable);

        // Set the background sprite the 'backgroundTint' asset
        backgroundSprite = new Sprite(Semtb001MajorAssignment.assetManager.manager.get(Assets.backgroundTint));
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        backgroundSprite.setAlpha(600);

        //Listener for the subHeader text label
        subHeaderText.addListener(new InputListener() {

            // If the label is 'touched down' change the font colour to grey
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                subHeaderText.setStyle(Semtb001MajorAssignment.smallFontFontGrey);
                subHeaderTextActive = true;

                return true;
            }

            //If the user touches down on the label and drags
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                /* If the user touch is dragged and still on the label
                (change to grey font colour and active subHeaderTextActive) */
                if (x > 0 && x < subHeaderText.getWidth() && y > 0 && y < subHeaderText.getHeight()) {
                    subHeaderText.setStyle(Semtb001MajorAssignment.smallFontFontGrey);
                    subHeaderTextActive = true;
                } else {

                    /* If the user touch is dragged and not over the label
                    (de-activate subHeaderTextActive and set the font colour to white) */
                    subHeaderText.setStyle(Semtb001MajorAssignment.smallFontFontWhite);
                    subHeaderTextActive = false;
                }
            }

            /* If the user 'touches up' (lets go of the touch) and subHeaderTextActive is active:
            get the level that user is going to play, dispose the playScreen and set the font
            colour back to white */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (subHeaderTextActive && screen.isGameOverCreated) {
                    playScreen.dispose();
                    dispose();
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(game, getNextLevel()));
                }
                subHeaderText.setStyle(Semtb001MajorAssignment.smallFontFontWhite);
            }
        });

        //Listener for the exit text label
        exitText.addListener(new InputListener() {

            // If the exit label is 'touched down' change the font colour to grey
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exitText.setStyle(Semtb001MajorAssignment.smallFontFontGrey);
                exitTextActive = true;
                return true;
            }

            //If the user touches down on the exit label and drags
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

                /* If the user touch is dragged and still on the exit label
                (change to grey font colour and active exitTextActive) */
                if (x > 0 && x < exitText.getWidth() && y > 0 && y < exitText.getHeight()) {
                    exitText.setStyle(Semtb001MajorAssignment.smallFontFontGrey);
                    exitTextActive = true;
                } else {

                    /* If the user touch is dragged and not over the exit label
                    (de-activate exitTextActive and set the font colour to white) */
                    exitText.setStyle(Semtb001MajorAssignment.smallFontFontWhite);
                    exitTextActive = false;
                }
            }

            /* If the user 'touches up' (lets go of the touch) and exitTextActive is active:
            return to the main menu and dispose the playScreen and set the font colour back to white */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (exitTextActive && screen.isGameOverCreated) {
                    playScreen.dispose();
                    dispose();

                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
                }
            }
        });

    }

    // Method to get the next level that user is going to play
    public String getNextLevel() {

        String level = null;

        // If the player is dead: return the current level that they are playing
        if (!screen.getLevelPassed()) {
            level = screen.currentLevel;
        } else {

            // If the player player completes the level, reutn the next level
            if (Integer.parseInt(screen.currentLevel.substring(7, 8)) != Semtb001MajorAssignment.NUMBER_OF_LEVELS) {
                level = "LEVEL: " + (Integer.parseInt(screen.currentLevel.substring(7, 8)) + 1);
            } else {

                // If there is no next level, return the current level (all levels are completed)
                level = screen.currentLevel;
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
