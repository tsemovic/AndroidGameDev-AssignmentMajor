package com.semtb001.individual.assignement.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.individual.assignement.sprites.Player;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.sprites.Slime;
import com.semtb001.individual.assignement.tools.Box2DWorldCreator;
import com.semtb001.individual.assignement.tools.WorldContactListener;

public class PlayScreen implements Screen {

    private Semtb001IndividualAssignment game;
    private OrthographicCamera gameCamera;
    private Viewport gameViewPort;
    private InputMultiplexer inputMultiplexer;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer box2dRenderer;
    private Box2DWorldCreator box2dWorldCreator;

    public TextureAtlas textureAtlas;
    private Player player;

    public PlayScreen(Semtb001IndividualAssignment semtb001IndividualAssignment) {
        game = semtb001IndividualAssignment;
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, ( Semtb001IndividualAssignment.WORLD_WIDTH) / Semtb001IndividualAssignment.PPM, (Semtb001IndividualAssignment.WORLD_HEIGHT) / Semtb001IndividualAssignment.PPM);
        //gameViewPort = new FitViewport((Gdx.graphics.getWidth() / Semtb001IndividualAssignment.WORLD_WIDTH) / Semtb001IndividualAssignment.PPM, (Gdx.graphics.getHeight() / Semtb001IndividualAssignment.WORLD_HEIGHT) / Semtb001IndividualAssignment.PPM, gameCamera);

        inputMultiplexer = new InputMultiplexer();
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapFiles/level1.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1f/Semtb001IndividualAssignment.PPM);

        world = new World(new Vector2(0, -100f), true);

        box2dRenderer = new Box2DDebugRenderer();
        box2dWorldCreator = new Box2DWorldCreator(this);
        world.setContactListener(new WorldContactListener(box2dWorldCreator));

        textureAtlas = new TextureAtlas("texturepack/playerAndSlime.pack");

        player = new Player(world, this);

        world.setContactListener(new WorldContactListener(box2dWorldCreator));


    }

    @Override
    public void show() {

    }

    public void inputHandler(float delta){
        if (Gdx.input.isTouched()) {
            if (Gdx.input.getY() < Gdx.graphics.getHeight() / 2){
                player.jump();
            } else {
                //touchde left
            }
        }
    }

    public void update(float delta){
        inputHandler(delta);
        world.step(1 / 60f, 6, 2);

        gameCamera.update();
        player.update(delta);

        renderer.setView(gameCamera);

    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //game.batch.setProjectionMatrix(gameCamera.combined);

        Gdx.input.setInputProcessor(inputMultiplexer);

        renderer.render();

        box2dRenderer.render(world, gameCamera.combined);

        //make camera follow player
        //gameCamera.position.x = player.box2dBody.getPosition().x + 170;
        //gameCamera.position.y = 370;

        gameCamera.position.x = player.box2dBody.getPosition().x;
        gameCamera.position.y = player.box2dBody.getPosition().y;

        if(player.box2dBody.getLinearVelocity().x <= 50f){
            player.box2dBody.applyLinearImpulse(new Vector2(1f, 0), player.box2dBody.getWorldCenter(), true);

        }

        player.batch.begin();
        //player.batch.draw(player.currentFrame, 370, getPlayerPos().y*3 - (128*5));
        player.batch.end();

        //System.out.println((int) getPlayerPos().x);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    public Vector2 getPlayerPos() {
        Vector2 pos = new Vector2(player.box2dBody.getPosition().x, player.box2dBody.getPosition().y);
        return pos;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }
}
