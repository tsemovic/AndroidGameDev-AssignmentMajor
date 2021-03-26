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
    private Skin skin;
    private BitmapFont buttonFont;
    private Table pausedTable;

    public Sprite backgroundSprite;

    public SpriteBatch batch;
    BitmapFont font;

    public Paused(SpriteBatch spriteBatch, final Semtb001IndividualAssignment game, final PlayScreen playScreen) {
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
        BitmapFont pausedFont = generator.generateFont(parameter);

        parameter.size = (int) (Semtb001IndividualAssignment.PPM * 2);
        buttonFont = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle pausedTextStyle = new Label.LabelStyle(pausedFont, Color.WHITE);

        Label.LabelStyle buttonTextStyle = new Label.LabelStyle(buttonFont, Color.WHITE);

        pausedText = new Label("PAUSED", pausedTextStyle);
        continueText = new Label("CONTINUE", buttonTextStyle);
        exitText = new Label("EXIT", buttonTextStyle);


        pausedTable.add(pausedText).pad(Semtb001IndividualAssignment.PPM*2);
        pausedTable.row();
        pausedTable.add(continueText);
        pausedTable.row();
        pausedTable.add(exitText);

        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.Alpha);
        bgPixmap.setBlending(Pixmap.Blending.None);
        bgPixmap.setColor(1f, 1f, 1f, 1f);
        bgPixmap.fillRectangle(0,0,32,32);

        pausedTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap))));


        Texture backgroundTexture = new Texture("gui/pausedBackground.png");
        backgroundSprite =new Sprite(backgroundTexture);
        backgroundSprite.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        backgroundSprite.setAlpha(400);


        stage.addActor(pausedTable);


        continueText.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                continueText.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                continueTextActive = true;

                return true;
            }

            //allow the user to drag off the button to not activate a click
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer){
                if(x > 0 && x < continueText.getWidth() && y > 0 && y < continueText.getHeight()){
                    continueText.setStyle(new Label.LabelStyle(buttonFont, Color.GRAY));
                    continueTextActive = true;
                }else{
                    continueText.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
                    continueTextActive = false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(continueTextActive) {
                    playScreen.setPaused(false);
                }
                continueText.setStyle(new Label.LabelStyle(buttonFont, Color.WHITE));
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
                if(x > 0 && x < exitText.getWidth() && y > 0 && y < continueText.getHeight()){
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


    @Override
    public void dispose() {
        stage.dispose();
    }
}
