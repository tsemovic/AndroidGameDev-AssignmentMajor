package com.semtb001.individual.assignement.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    private Player player;

    public PlayScreen(Semtb001IndividualAssignment semtb001IndividualAssignment) {
        game = semtb001IndividualAssignment;
        gameCamera = new OrthographicCamera();
        gameViewPort = new FitViewport( semtb001IndividualAssignment.WORLD_WIDTH / semtb001IndividualAssignment.PPM, semtb001IndividualAssignment.WORLD_HEIGHT / semtb001IndividualAssignment.PPM, gameCamera);
        inputMultiplexer = new InputMultiplexer();

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapFiles/level1.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1/semtb001IndividualAssignment.PPM);

        world = new World(new Vector2(0, 0), true);

        box2dRenderer = new Box2DDebugRenderer();
        box2dWorldCreator = new Box2DWorldCreator(this);
        world.setContactListener(new WorldContactListener(box2dWorldCreator));

        player = new Player(world, this);

        gameCamera.position.set(gameViewPort.getScreenWidth() / 2, gameViewPort.getScreenHeight() / 2, 0);

    }

    @Override
    public void show() {

    }

    public void inputHandler(float delta){
        if(Gdx.input.isTouched()){
            gameCamera.position.x += 100 * delta;
        }
    }

    public void update(float delta){

        renderer.setView(gameCamera);
    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);
        inputHandler(delta);
        game.batch.setProjectionMatrix(gameCamera.combined);
        world.step(1 / 60f, 6, 2);

        Gdx.input.setInputProcessor(inputMultiplexer);

        renderer.render();

        box2dRenderer.render(world, gameCamera.combined);

        //make camera follow player
        //gameCamera.position.x = player.box2dBody.getPosition().x;
        //gameCamera.position.y = player.box2dBody.getPosition().y;
        gameCamera.update();


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
}
