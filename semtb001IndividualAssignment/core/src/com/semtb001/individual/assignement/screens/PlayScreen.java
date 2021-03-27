package com.semtb001.individual.assignement.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.individual.assignement.scenes.GameOver;
import com.semtb001.individual.assignement.scenes.Hud;
import com.semtb001.individual.assignement.scenes.Paused;
import com.semtb001.individual.assignement.sprites.FlyingEnemy;
import com.semtb001.individual.assignement.sprites.Player;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.sprites.GroundEnemy;
import com.semtb001.individual.assignement.tools.Box2DWorldCreator;
import com.semtb001.individual.assignement.tools.WorldContactListener;

import java.util.LinkedList;
import java.util.Queue;

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

    private float timeCount;
    private float worldEndPosition;

    public TextureAtlas textureAtlas;
    public SpriteBatch batch;
    private boolean isPaused;

    private Paused paused;
    private Hud hud;
    private GameOver gameOver;

    private Player player;
    private Queue<GroundEnemy> groundEnemies;
    private Queue<FlyingEnemy> flyingEnemies;


    public PlayScreen(Semtb001IndividualAssignment semtb001IndividualAssignment) {
        game = semtb001IndividualAssignment;
        gameCamera = new OrthographicCamera();
        batch = semtb001IndividualAssignment.batch;

        gameCamera.setToOrtho(false, Semtb001IndividualAssignment.WORLD_WIDTH, Semtb001IndividualAssignment.WORLD_HEIGHT);
        gameViewPort = new FitViewport((Gdx.graphics.getWidth() / Semtb001IndividualAssignment.WORLD_WIDTH) / Semtb001IndividualAssignment.PPM, (Gdx.graphics.getHeight() / Semtb001IndividualAssignment.WORLD_HEIGHT) / Semtb001IndividualAssignment.PPM, gameCamera);

        inputMultiplexer = new InputMultiplexer();
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapFiles/level2.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, Semtb001IndividualAssignment.MPP);

        world = new World(new Vector2(0, -100f), true);

        box2dRenderer = new Box2DDebugRenderer();
        box2dWorldCreator = new Box2DWorldCreator(this);
        world.setContactListener(new WorldContactListener(box2dWorldCreator));

        textureAtlas = new TextureAtlas("texturepack/playerAndEnemy.pack");

        player = new Player(world, this);
        player.box2dBody.applyLinearImpulse(new Vector2(15f, 0), player.box2dBody.getWorldCenter(), true);

        groundEnemies = new LinkedList<GroundEnemy>();
        flyingEnemies = new LinkedList<FlyingEnemy>();

        world.setContactListener(new WorldContactListener(box2dWorldCreator));

        hud = new Hud(game.batch, this);
        gameOver = new GameOver(game.batch, game, this);
        paused = new Paused(game.batch, game, this);

        inputMultiplexer.addProcessor(hud.stage);
        inputMultiplexer.addProcessor(gameOver.stage);
        inputMultiplexer.addProcessor(paused.stage);
    }

    @Override
    public void show() {

    }

    public void inputHandler(float delta) {

        //if the screen is touched (excluding the pause button)
        if (Gdx.input.isTouched() && !hud.pausedPressed && !isPaused) {

            //if the top half of the screen is touched: player jump
            if (Gdx.input.getY() < Gdx.graphics.getHeight() / 2) {
                player.jump();

                //if the bottom half of the screen is touched: player slide
            } else {
                player.slide();
            }
        }
    }

    public void update(float delta) {
        inputHandler(delta);
        world.step(1 / 60f, 6, 2);

        gameCamera.update();
        player.update(delta);
        moveGameCamera();
        movePlayer();
        checkIfDead(delta);
        handleEnemies(delta);

        renderer.setView(gameCamera);
    }

    @Override
    public void render(float delta) {

        //clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //if the game is paused, draw the pause menu
        if (isPaused) {
            game.batch.setProjectionMatrix(paused.stage.getCamera().combined);
            paused.stage.draw();
            delta = 0;
        }
        if (player.getGameOver()) {
            game.batch.setProjectionMatrix(gameOver.stage.getCamera().combined);
            gameOver.stage.draw();
            delta = 0;
        }

        //if the game is not paused, update the game
        if (!isPaused && !player.getGameOver()) {
            update(delta);
        }

        game.batch.setProjectionMatrix(gameCamera.combined);
        Gdx.input.setInputProcessor(inputMultiplexer);
        renderer.render();
        box2dRenderer.render(world, gameCamera.combined);

        //begin the sprite batch for drawing everything
        game.batch.begin();

        drawPlayer();
        drawEnemies(delta);

        //draw transparent background when the game is paused
        if (isPaused) {
            paused.getBackgroundSprite().draw(game.batch);
        }
        if (player.getGameOver()) {
            gameOver.getBackgroundSprite().draw(game.batch);
        }

        //end the sprite batch for drawing everything
        game.batch.end();

        //draw the heads up display
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    private void moveGameCamera() {
        if (!isPaused) {
            gameCamera.position.y = 22;
            if (player.box2dBody.getPosition().x <= worldEndPosition) {
                gameCamera.position.x = player.box2dBody.getPosition().x + 8;
            }
        }
    }

    private void movePlayer() {
        if (!isPaused) {
            if (player.box2dBody.getLinearVelocity().x <= 15f && player.playerIsDead == false) {
                player.box2dBody.applyLinearImpulse(new Vector2(0.5f, 0), player.box2dBody.getWorldCenter(), true);
            }
        } else {
            player.box2dBody.setLinearVelocity(new Vector2(0, 0));
        }
    }

    private void checkIfDead(float delta) {
//        if (player.box2dBody.getLinearVelocity().x < 10) {
//            timeCount += delta;
//            if (timeCount >= 0.4) {
//                timeCount = 0;
//                player.playerIsDead = true;
//            }
//        }
    }

    public void handleEnemies(float delta) {

        //handle grounded enemies
        if (box2dWorldCreator.getGroundEnemyPositions().size() > 0) {
            if (getPlayerPos().x + 50 > box2dWorldCreator.getGroundEnemyPositions().element().x / 32) {
                GroundEnemy newGroundEnemy = new GroundEnemy(world, this, box2dWorldCreator.getGroundEnemyPositions().element());
                groundEnemies.offer(newGroundEnemy);
                box2dWorldCreator.getGroundEnemyPositions().remove();
            }
        }
        if (groundEnemies.size() > 0) {
            if (groundEnemies.element().box2dBody.getPosition().x < getPlayerPos().x - 10) {
                world.destroyBody(groundEnemies.element().box2dBody);
                groundEnemies.remove();
            }
        }

        //handle flying enemies
        if (box2dWorldCreator.getFlyingEnemyPositions().size() > 0) {
            if (getPlayerPos().x + 50 > box2dWorldCreator.getFlyingEnemyPositions().element().x / 32) {
                FlyingEnemy newFlyingEnemy = new FlyingEnemy(world, this, box2dWorldCreator.getFlyingEnemyPositions().element());
                flyingEnemies.offer(newFlyingEnemy);
                box2dWorldCreator.getFlyingEnemyPositions().remove();
            }
        }
        if (flyingEnemies.size() > 0) {
            if (flyingEnemies.element().box2dBody.getPosition().x < getPlayerPos().x - 10) {
                world.destroyBody(flyingEnemies.element().box2dBody);
                flyingEnemies.remove();
            }
        }
    }

    public void drawEnemies(float delta) {
        //draw ground enemy animation frames
        for (GroundEnemy enemy : groundEnemies) {
            enemy.update(delta);
            game.batch.draw(enemy.currentFrame, enemy.box2dBody.getPosition().x - 1, (float) (enemy.box2dBody.getPosition().y - 1), 5, 5);
        }

        for (FlyingEnemy enemy : flyingEnemies) {
            enemy.update(delta);
            game.batch.draw(enemy.currentFrame, enemy.box2dBody.getPosition().x - 1, (float) (enemy.box2dBody.getPosition().y - 1), 5, 5);
        }
    }

    public void drawPlayer() {
        if (player.getState() == Player.State.RUN && (player.previousState == Player.State.SLIDE_START || player.previousState == Player.State.SLIDE_END || player.previousState == Player.State.JUMP_START || player.previousState == Player.State.JUMP_END)) {
            game.batch.draw(player.currentFrame, player.box2dBody.getPosition().x - 5, (float) (player.box2dBody.getPosition().y - 1), 10, 10);
        } else if (player.getState() == Player.State.SLIDE_START || player.getState() == Player.State.SLIDE_END) {
            game.batch.draw(player.currentFrame, player.box2dBody.getPosition().x - 5, (float) (player.box2dBody.getPosition().y - 1), 10, 10);
        } else if (player.getState() == Player.State.JUMP_START || player.getState() == Player.State.JUMP_END) {
            game.batch.draw(player.currentFrame, player.box2dBody.getPosition().x - 5, (float) (player.box2dBody.getPosition().y - 2.2), 10, 10);
        }else{
            game.batch.draw(player.currentFrame, player.box2dBody.getPosition().x - 5, (float) (player.box2dBody.getPosition().y - 3.2), 10, 10);

        }
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

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public Vector2 getPlayerPos() {
        Vector2 pos = new Vector2((int) (player.box2dBody.getPosition().x * Semtb001IndividualAssignment.PPM / 32),
                (int) (player.box2dBody.getPosition().y * Semtb001IndividualAssignment.PPM / 32));
        return pos;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public float getWorldEndPosition() {
        return worldEndPosition;
    }

    public void setWorldEndPosition(float x) {
        worldEndPosition = x / Semtb001IndividualAssignment.PPM;
    }

    public void setPaused(boolean value) {
        isPaused = value;
    }

}
