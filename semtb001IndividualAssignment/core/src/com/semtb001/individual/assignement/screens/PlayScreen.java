package com.semtb001.individual.assignement.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.semtb001.individual.assignement.sprites.Jewel;
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
    public boolean isPaused;
    public boolean isGameOverCreated;


    private Paused paused;
    private Hud hud;
    private GameOver gameOver;

    private Player player;
    private Queue<GroundEnemy> groundEnemies;
    private Queue<FlyingEnemy> flyingEnemies;
    private Queue<Jewel> jewels;

    private Music music;

    public String currentLevel;

    public PlayScreen(Semtb001IndividualAssignment semtb001IndividualAssignment, String currentLevel) {
        game = semtb001IndividualAssignment;
        gameCamera = new OrthographicCamera();
        batch = semtb001IndividualAssignment.batch;
        this.currentLevel = currentLevel;

        gameCamera.setToOrtho(false, Semtb001IndividualAssignment.WORLD_WIDTH, Semtb001IndividualAssignment.WORLD_HEIGHT);
        gameViewPort = new FitViewport((Gdx.graphics.getWidth() / Semtb001IndividualAssignment.WORLD_WIDTH) / Semtb001IndividualAssignment.PPM, (Gdx.graphics.getHeight() / Semtb001IndividualAssignment.WORLD_HEIGHT) / Semtb001IndividualAssignment.PPM, gameCamera);

        inputMultiplexer = new InputMultiplexer();
        mapLoader = new TmxMapLoader();

        //load map level
        map = mapLoader.load("mapFiles/level" + currentLevel.substring(7, 8) + ".tmx");

        renderer = new OrthogonalTiledMapRenderer(map, Semtb001IndividualAssignment.MPP);

        world = new World(new Vector2(0, -100f), true);

        box2dRenderer = new Box2DDebugRenderer();
        box2dWorldCreator = new Box2DWorldCreator(this);
        world.setContactListener(new WorldContactListener(box2dWorldCreator));

        textureAtlas = new TextureAtlas("texturepack/playerAndEnemy.pack");

        player = new Player(world, this);
        //player.box2dBody.applyLinearImpulse(new Vector2(15f, 0), player.box2dBody.getWorldCenter(), true);

        groundEnemies = new LinkedList<GroundEnemy>();
        flyingEnemies = new LinkedList<FlyingEnemy>();
        jewels = new LinkedList<Jewel>();


        world.setContactListener(new WorldContactListener(box2dWorldCreator));

        hud = new Hud(game.batch, this);
        gameOver = new GameOver(game.batch, game, this);
        paused = new Paused(game.batch, game, this);

        inputMultiplexer.addProcessor(hud.stage);
        inputMultiplexer.addProcessor(gameOver.stage);
        inputMultiplexer.addProcessor(paused.stage);

//        music = Semtb001IndividualAssignment.assetManager.manager.get(Assets.music);
//        music.setLooping(true);
//        music.play();
        isGameOverCreated = false;
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
        renderer.setView(gameCamera);

        player.update(delta);
        moveGameCamera();
        movePlayer();
        checkIfDead(delta);
        handleEnemies(delta);

    }

    @Override
    public void render(float delta) {

        //clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //if the game is not paused, update the game
        if (!isPaused && !player.getGameOver()) {
            update(delta);
        }

        //render the world and object fixtures
        renderer.render();
        box2dRenderer.render(world, gameCamera.combined);
        game.batch.setProjectionMatrix(gameCamera.combined);

        //set the input processer for multiple input (screen touch for jump/slide as well as pause button
        Gdx.input.setInputProcessor(inputMultiplexer);

        //begin the sprite batch for drawing everything
        game.batch.begin();

        drawPlayer();
        drawEnemies(delta);
        drawJewels(delta);

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

        //draw transparent background when the game is paused
        game.batch.begin();
        if (isPaused) {
            paused.getBackgroundSprite().draw(game.batch);
        }else if (player.getGameOver() || getPlayerPos().x >= worldEndPosition) {
            gameOver.getBackgroundSprite().draw(game.batch);
        }
        game.batch.end();

        //draw pause/game over display
        if (isPaused) {
            game.batch.setProjectionMatrix(paused.stage.getCamera().combined);
            paused.stage.draw();
            player.stopSounds();
        } else if (player.getGameOver() || getPlayerPos().x >= worldEndPosition) {
            if(!isGameOverCreated){
                gameOver = new GameOver(game.batch, game, this);
                inputMultiplexer.addProcessor(gameOver.stage);
                isGameOverCreated = true;
            }
            game.batch.setProjectionMatrix(gameOver.stage.getCamera().combined);
            gameOver.stage.draw();
            player.stopSounds();
        }

    }

    private void moveGameCamera() {
        if (!isPaused) {
            gameCamera.position.y = 23;
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
        if (player.box2dBody.getLinearVelocity().x < 10) {
            timeCount += delta;
            if (timeCount >= 0.4) {
                gameOver = new GameOver(game.batch, game, this);
                inputMultiplexer.addProcessor(gameOver.stage);
                timeCount = 0;
                player.playerIsDead = true;
            }
        }
    }

    public void handleEnemies(float delta) {

        //handle grounded enemies
        if (box2dWorldCreator.getGroundEnemyPositions().size() > 0) {
            if (getPlayerPos().x + 100 > box2dWorldCreator.getGroundEnemyPositions().element().x / 32) {
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
            if (getPlayerPos().x + 100 > box2dWorldCreator.getFlyingEnemyPositions().element().x / 32) {
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

    public void drawJewels(float delta) {
        for (Jewel jewel : box2dWorldCreator.getJewels()) {
            jewel.update(delta);
            if (!jewel.collected) {
                game.batch.draw(jewel.currentFrame, jewel.box2dBody.getPosition().x - 1, (float) (jewel.box2dBody.getPosition().y - 1), 2, 2);
            }
        }
    }

    public void drawPlayer() {
        if (player.getState() == Player.State.RUN && (player.previousState == Player.State.SLIDE_START || player.previousState == Player.State.SLIDE_END)) {
            game.batch.draw(player.currentFrame, (float) (player.box2dBody.getPosition().x - 4), (float) (player.box2dBody.getPosition().y - 1.5), 8, 8);
        } else if (player.getState() == Player.State.SLIDE_START || player.getState() == Player.State.SLIDE_END) {
            game.batch.draw(player.currentFrame, (float) (player.box2dBody.getPosition().x - 4), (float) (player.box2dBody.getPosition().y - 1.5), 8, 8);
        } else if (player.getState() == Player.State.JUMP_START) {
            game.batch.draw(player.currentFrame, (float) (player.box2dBody.getPosition().x - 4), (float) (player.box2dBody.getPosition().y - 3), 8, 8);
        } else if (player.getState() == Player.State.JUMP_END) {
            game.batch.draw(player.currentFrame, (float) (player.box2dBody.getPosition().x - 4), (float) (player.box2dBody.getPosition().y - 2), 8, 8);
        } else {
            game.batch.draw(player.currentFrame, (float) (player.box2dBody.getPosition().x - 4), (float) (player.box2dBody.getPosition().y - 2.6), 8, 8);
        }
    }

    public void updateCollectedJewels() {
        hud.update();

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

    public Player getPlayer() {
        return player;
    }

    public Hud getHud(){
        return hud;
    }

    public Box2DWorldCreator getBox2dWorldCreator(){
        return box2dWorldCreator;
    }


}
