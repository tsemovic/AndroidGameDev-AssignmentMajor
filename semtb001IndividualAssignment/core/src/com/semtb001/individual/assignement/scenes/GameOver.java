package com.semtb001.individual.assignement.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.MainMenu;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class GameOver implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private PlayScreen playScreen;

    private Label headerText;
    private Label subHeaderText;
    public boolean subHeaderTextActive;
    private Label exitText;
    public boolean exitTextActive;

    private Semtb001IndividualAssignment game;
    private Skin skin;
    private BitmapFont buttonFont;
    private Table pausedTable;

    public Sprite backgroundSprite;

    public SpriteBatch batch;
    BitmapFont font;

    public GameOver(SpriteBatch spriteBatch, final Semtb001IndividualAssignment game, final PlayScreen playScreen) {
        this.game = game;
        this.playScreen = playScreen;
        batch = spriteBatch;
        viewport = new FitViewport(Semtb001IndividualAssignment.WORLD_WIDTH * Semtb001IndividualAssignment.PPM , Semtb001IndividualAssignment.WORLD_HEIGHT * Semtb001IndividualAssignment.PPM);
        stage = new Stage(viewport, spriteBatch);

        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));

        pausedTable = new Table();
        pausedTable.top();
        pausedTable.setFillParent(true);

        //https://github.com/libgdx/libgdx/wiki/Gdx-freetype
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/poxel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = (int) (Semtb001IndividualAssignment.PPM * 4);
        BitmapFont gameOverFont = generator.generateFont(parameter);

        parameter.size = (int) (Semtb001IndividualAssignment.PPM * 3);
        BitmapFont lvlCompleteFont = generator.generateFont(parameter);

        parameter.size = (int) (Semtb001IndividualAssignment.PPM * 2);
        buttonFont = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle gameOverStyle = new Label.LabelStyle(gameOverFont, Color.WHITE);
        Label.LabelStyle lvlCompleteStyle = new Label.LabelStyle(lvlCompleteFont, Color.WHITE);


        Label.LabelStyle buttonTextStyle = new Label.LabelStyle(buttonFont, Color.WHITE);

        exitText = new Label("EXIT", buttonTextStyle);

        if(playScreen.getPlayer().playerIsDead){
            headerText = new Label("GAME OVER", gameOverStyle);
            subHeaderText = new Label("TRY AGAIN", buttonTextStyle);
        }else{
            headerText = new Label("LEVEL COMPLETE", lvlCompleteStyle);
            subHeaderText = new Label("TRY AGAIN", buttonTextStyle);

            if(playScreen.getHud().getJewelCount() == playScreen.getBox2dWorldCreator().getJewels().size()){
                if(Integer.valueOf(playScreen.currentLevel.substring(playScreen.currentLevel.length() - 1)) != Semtb001IndividualAssignment.NUMBER_OF_LEVELS){
                    String newLevel = "LEVEL: " + Integer.toString(Integer.valueOf(playScreen.currentLevel.substring(playScreen.currentLevel.length() - 1)) + 1);
                    Semtb001IndividualAssignment.levelsPref.putBoolean(newLevel, true);
                    Semtb001IndividualAssignment.levelsPref.flush();
                    System.out.println(newLevel + " unlcoked");
                    subHeaderText = new Label("NEXT LEVEL", buttonTextStyle);

                }else{
                    subHeaderText = new Label("TRY AGAIN", buttonTextStyle);

                }
            }
            System.out.println("JEWELSSSS");

            System.out.println(playScreen.getHud().getJewelCount());
            System.out.println(playScreen.getBox2dWorldCreator().getJewels().size());

        }

        pausedTable.add(headerText).pad(Semtb001IndividualAssignment.PPM*2);
        pausedTable.row();
        pausedTable.add(subHeaderText);
        pausedTable.row();
        pausedTable.add(exitText);

        Texture backgroundTexture = new Texture("gui/backgroundTint.png");
        backgroundSprite =new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        backgroundSprite.setAlpha(400);

        stage.addActor(pausedTable);

        subHeaderText.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                subHeaderText.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                subHeaderTextActive = true;

                return true;
            }

            //allow the user to drag off the button to not activate a click
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer){
                if(x > 0 && x < subHeaderText.getWidth() && y > 0 && y < subHeaderText.getHeight()){
                    subHeaderText.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                    subHeaderTextActive = true;
                }else{
                    subHeaderText.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
                    subHeaderTextActive = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(subHeaderTextActive) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(game, getNextLevel()));
                }
                subHeaderText.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
            }
        });

        exitText.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exitText.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                exitTextActive = true;
                return true;
            }

            //allow the user to drag off the button to not activate a click
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer){
                if(x > 0 && x < exitText.getWidth() && y > 0 && y < exitText.getHeight()){
                    exitText.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                    exitTextActive = true;
                }else{
                    exitText.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
                    exitTextActive = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(exitTextActive) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
                }
                exitText.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
            }
        });
    }

    public Sprite getBackgroundSprite(){
        return backgroundSprite;
    }

    public String getNextLevel(){
        String level = null;

        if(Integer.parseInt(playScreen.currentLevel.substring(7, 8)) != Semtb001IndividualAssignment.NUMBER_OF_LEVELS){
            level = "LEVEL: " + playScreen.currentLevel.substring(7, 8);
        }else{
            level = playScreen.currentLevel;
        }

        return level;

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
