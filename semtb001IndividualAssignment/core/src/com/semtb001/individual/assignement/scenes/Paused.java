package com.semtb001.individual.assignement.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import com.island.survival.screens.MainMenu;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class Paused implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private PlayScreen playScreen;

    private Label pausedText;
    private Label continueText;
    private Label exitText;


    private Skin skin;
    BitmapFont font;

    public Paused(SpriteBatch spriteBatch, final PlayScreen playScreen) {
        this.playScreen = playScreen;
        viewport = new FitViewport(Semtb001IndividualAssignment.WORLD_WIDTH * Semtb001IndividualAssignment.PPM , Semtb001IndividualAssignment.WORLD_HEIGHT * Semtb001IndividualAssignment.PPM);
        stage = new Stage(viewport, spriteBatch);

        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));

        Table pausedTable = new Table();
        pausedTable.top();
        pausedTable.setFillParent(true);

        //https://github.com/libgdx/libgdx/wiki/Gdx-freetype
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/poxel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = (int) (Semtb001IndividualAssignment.PPM * 4);
        BitmapFont pausedFont = generator.generateFont(parameter);

        parameter.size = (int) (Semtb001IndividualAssignment.PPM * 2);
        BitmapFont buttonFont = generator.generateFont(parameter);
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


        continueText.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.setPaused(false);
            }
        });

        exitText.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });

        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.Alpha);
        bgPixmap.setBlending(Pixmap.Blending.None);
        bgPixmap.setColor(0.1f, 0.1f, 0.1f, 0.8f);
        bgPixmap.fillRectangle(0,0,32,32);

        pausedTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap))));

        stage.addActor(pausedTable);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
