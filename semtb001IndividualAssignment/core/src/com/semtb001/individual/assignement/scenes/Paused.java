package com.semtb001.individual.assignement.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.MainMenu;
import com.semtb001.individual.assignement.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.semtb001.individual.assignement.tools.Assets;


public class Paused implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private PlayScreen playScreen;

    private Label pausedText;
    private Label continueText;
    public boolean continueTextActive;
    private Label exitText;
    public boolean exitTextActive;

    private Semtb001IndividualAssignment game;
    private Table pausedTable;

    public Sprite backgroundSprite;

    public SpriteBatch batch;

    public Paused(SpriteBatch spriteBatch, final Semtb001IndividualAssignment game, final PlayScreen playScreen) {
        this.game = game;
        this.playScreen = playScreen;
        batch = spriteBatch;
        viewport = new FitViewport(Semtb001IndividualAssignment.WORLD_WIDTH * Semtb001IndividualAssignment.PPM , Semtb001IndividualAssignment.WORLD_HEIGHT * Semtb001IndividualAssignment.PPM);
        stage = new Stage(viewport, spriteBatch);

        pausedTable = new Table();
        pausedTable.top();
        pausedTable.setFillParent(true);

        pausedText = new Label("PAUSED", Semtb001IndividualAssignment.largeFontWhite);
        continueText = new Label("CONTINUE", Semtb001IndividualAssignment.smallFontFontWhite);
        exitText = new Label("EXIT", Semtb001IndividualAssignment.smallFontFontWhite);

        pausedTable.add(pausedText).pad(Semtb001IndividualAssignment.PPM*2);
        pausedTable.row();
        pausedTable.add(continueText);
        pausedTable.row();
        pausedTable.add(exitText);

        backgroundSprite = new Sprite(Semtb001IndividualAssignment.assetManager.manager.get(Assets.backgroundTint));
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        backgroundSprite.setAlpha(400);

        stage.addActor(pausedTable);

        continueText.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                continueText.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                continueTextActive = true;

                return true;
            }

            //allow the user to drag off the button to not activate a click
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer){
                if(x > 0 && x < continueText.getWidth() && y > 0 && y < continueText.getHeight()){
                    continueText.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                    continueTextActive = true;
                }else{
                    continueText.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
                    continueTextActive = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(continueTextActive) {
                    playScreen.setPaused(false);
                }
                continueText.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
            }
        });

        exitText.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exitText.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                exitTextActive = true;
                return true;
            }

            //allow the user to drag off the button to not activate a click
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer){
                if(x > 0 && x < exitText.getWidth() && y > 0 && y < exitText.getHeight()){
                    exitText.setStyle(Semtb001IndividualAssignment.smallFontFontGrey);
                    exitTextActive = true;
                }else{
                    exitText.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
                    exitTextActive = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(exitTextActive) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
                }
                exitText.setStyle(Semtb001IndividualAssignment.smallFontFontWhite);
            }
        });
    }

    public Sprite getBackgroundSprite(){
        return backgroundSprite;
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
