package com.semtb001.individual.assignement.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.semtb001.individual.assignement.scenes.GameOver;
import com.semtb001.individual.assignement.scenes.Hud;
import com.semtb001.individual.assignement.scenes.LevelBrief;
import com.semtb001.individual.assignement.scenes.Paused;
import com.semtb001.individual.assignement.sprites.FlyingEnemy;
import com.semtb001.individual.assignement.sprites.Coin;
import com.semtb001.individual.assignement.sprites.Player;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.sprites.GroundEnemy;
import com.semtb001.individual.assignement.tools.Assets;
import com.semtb001.individual.assignement.tools.Box2DWorldCreator;
import com.semtb001.individual.assignement.tools.ScreenShaker;
import com.semtb001.individual.assignement.tools.WorldContactListener;

import java.util.LinkedList;
import java.util.Queue;

public class PlayScreen implements Screen {

    // Game, camera, and current level objects
    private Semtb001IndividualAssignment game;
    private OrthographicCamera gameCamera;
    public String currentLevel;

    // world map objects
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private float worldEndPosition;

    // Box2D objects
    private Box2DWorldCreator box2dWorldCreator;

    // world step time calculation variables
    private static final float dt = (float) 0.01;
    private float time = (float) 0.0;
    double accumulator = 0.0;

    // Texture objects
    public TextureAtlas textureAtlas;
    public SpriteBatch batch;

    // variables for game pause and game over
    public boolean isPaused;
    public boolean isGameOverCreated;

    // Screen overlay objects
    private Paused paused;
    private Hud hud;
    private GameOver gameOver;
    private LevelBrief levelBrief;

    // Player, Enemy and Coin objects
    private Player player;
    private Queue<GroundEnemy> groundEnemies;
    private Queue<FlyingEnemy> flyingEnemies;
    private Queue<Coin> coins;

    // Music object for game music loop
    private Music music;

    // Input Multiplexer object (multitouch functionality)
    private InputMultiplexer inputMultiplexer;

    // Boolean to determine if the level brief is displayed
    private boolean levelBriefActive;

    public PlayScreen(Semtb001IndividualAssignment semtb001IndividualAssignment, String currentLevel) {

        // Instantiate game and level objects
        game = semtb001IndividualAssignment;
        batch = semtb001IndividualAssignment.batch;
        this.currentLevel = currentLevel;
        isGameOverCreated = false;

        // Setup game camera
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, Semtb001IndividualAssignment.WORLD_WIDTH, Semtb001IndividualAssignment.WORLD_HEIGHT);

        // Load map level, render the map, and create the game world
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapFiles/level" + currentLevel.substring(7, 8) + ".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, Semtb001IndividualAssignment.MPP);
        world = new World(new Vector2(0, -100), true);

        // Setup the Box2D world creator (creates Box2D bodies and adds them to the world)
        box2dWorldCreator = new Box2DWorldCreator(this);

        // Setup the world contact listener
        world.setContactListener(new WorldContactListener(box2dWorldCreator));

        // Setup the texture atlas for loading in the player and enemy textures
        textureAtlas = Semtb001IndividualAssignment.assetManager.manager.get(Assets.textureAtlas);

        // Instantiate Player, Enemies and Coins
        player = new Player(world, this);
        groundEnemies = new LinkedList<GroundEnemy>();
        flyingEnemies = new LinkedList<FlyingEnemy>();
        coins = new LinkedList<Coin>();

        // Setup the game camera position
        gameCamera.position.x = player.box2dBody.getPosition().x + 9;
        gameCamera.position.y = 23;

        // Setup the screen overlay objects
        hud = new Hud(game.batch, this);
        gameOver = new GameOver(game.batch, game, this);
        paused = new Paused(game.batch, game, this);
        levelBrief = new LevelBrief(game.batch);

        // Initially, set the level brief to active so that the level brief is displayed at start up
        levelBriefActive = true;

        // Input multiplexer setup (allows multitouch between the game, hud, paused, and game over screens)
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(hud.stage);
        inputMultiplexer.addProcessor(gameOver.stage);
        inputMultiplexer.addProcessor(paused.stage);

        // Setup game music loop
        music = Semtb001IndividualAssignment.assetManager.manager.get(Assets.music);
        music.setLooping(true);
        music.play();

    }

    @Override
    public void show() {

    }

    // Method for handling input for the game
    public void inputHandler(float delta) {

        // If the screen is touched when the game is not paused and the game is not over (excluding the pause button)
        if (Gdx.input.isTouched() && !hud.pausedPressed && !isPaused && !isGameOverCreated) {

            // If the top half of the screen is touched: player jump
            if (Gdx.input.getY() < Gdx.graphics.getHeight() / 2) {
                player.jump();
            } else {

                // If the bottom half of the screen is touched: player slide
                player.slide();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.jump();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.slide();
        }
    }

    @Override
    public void render(float delta) {

        /* This code adjusts the time that the world is updated so that the world time step appears
           consistant regardless of how fast the device is that the application is running on. This
           means that the game will not speed up and become unplayable if it's run on a fast device.

           The below code has come from 'https://gafferongames.com/post/fix_your_timestep/' and has
           been modified to fit this project
        */
        accumulator += delta;

        while (accumulator >= dt) {

            // Update game
            updateWorld(time, dt);
            accumulator -= dt;
            time += dt;
        }

        // Render game
        renderWorld(delta);

        // Display the level brief until the timer runs out and it's 'time to play' the game
        if (!levelBrief.timeToPlay) {
            game.batch.setProjectionMatrix(levelBrief.stage.getCamera().combined);
            levelBrief.stage.draw();
            levelBrief.update(delta);
        }
    }


    // Method to render the world
    private void renderWorld(float delta) {

        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the world and object fixtures
        gameCamera.update();
        renderer.setView(gameCamera);
        renderer.render();
        game.batch.setProjectionMatrix(gameCamera.combined);

        // Set the input processor
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Begin the sprite batch for drawing the Player, Enemies, and Coins
        game.batch.begin();

        // Draw Player, Enemies, and Coins
        drawPlayer();
        drawEnemies(delta);
        drawCoins(delta);

        // End the sprite batch for drawing the Player, Enemies, and Coins
        game.batch.end();

        // Draw the heads up display
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        // Draw transparent background when the game is paused or when the game is over
        game.batch.begin();
        if (isPaused) {
            paused.getBackgroundSprite().draw(game.batch);
        } else if (player.getGameOver() || getPlayerPos().x >= worldEndPosition) {
            gameOver.getBackgroundSprite().draw(game.batch);
        }
        game.batch.end();

        // Draw the game paused and game over screen overlay
        if (isPaused) {
            game.batch.setProjectionMatrix(paused.stage.getCamera().combined);
            paused.stage.draw();
        } else if (player.getGameOver() || getPlayerPos().x >= worldEndPosition) {

            // If the game over screen has not yet been created, create it
            if (!isGameOverCreated) {
                gameOver = new GameOver(game.batch, game, this);
                inputMultiplexer.addProcessor(gameOver.stage);
                isGameOverCreated = true;
            }
            game.batch.setProjectionMatrix(gameOver.stage.getCamera().combined);
            gameOver.stage.draw();
        }
    }

    // Method to update the world
    private void updateWorld(float t, float deltaTime) {

        // The world is only updated when the game is not paused and the level brief screen isn't displayed
        if (!isPaused && levelBrief.timeToPlay) {

            // Step the world
            world.step(deltaTime, 6, 2);

            // Handle application input
            inputHandler(deltaTime);

            // Update player (sound, and animation frame depending on state)
            player.update(deltaTime);

            // Move player (constant running velocity)
            movePlayer();

            shakeCamera(deltaTime);

            // Move the game camera
            moveGameCamera();

            // Check if the player is dead (off the left side of the screen)
            checkIfDead();

            // Handle enemies (movement, sound, and animation frame depending on state)
            handleEnemies();

            // If the music is not playing, play music
            if (!music.isPlaying()) {
                music.play();
            }

        } else {

            // If the game is paused or the level brief is displayed: pause all game sounds
            stopSounds();
            pauseMusic();
        }
    }

    // Method to stop enemy sounds from playing
    public void stopSounds() {

        // Stop flying enemy (bee) sounds
        for (FlyingEnemy enemy : flyingEnemies) {
            enemy.stopSound();
        }

        // Stop grounded enemy (slime) sounds
        for (GroundEnemy enemy : groundEnemies) {
            enemy.stopSound();
        }
    }

    // Method to stop game loop music (used when the track needs to be restarted eg. new game)
    public void stopMusic() {
        music.stop();
    }

    // Method to pause game loop music (used when the track needs to be resumed where it was stopped)
    public void pauseMusic() {
        music.pause();
    }

    // Method to move the game camera so that it follows the player
    private void moveGameCamera() {

        // If the game is not paused and the player is not dead
        if (!isPaused && !player.playerIsDead) {

            // If the player hasn't reached the end of the world: move the camera. If the player has
            // reached the end of the world, the camera will not move (player runs off the screen)
            if (player.box2dBody.getPosition().x <= worldEndPosition) {

                // Increase the camera x position by the players maximum linear velocity (15f)
                gameCamera.position.x += 0.15003;
            }
        }
    }

    // Method to move the player along the map
    private void movePlayer() {

        // If the game is not paused
        if (!isPaused) {

            // If the player is not dead and the x axis velocity is less than 15f, move the player
            if (player.box2dBody.getLinearVelocity().x <= 15f && player.playerIsDead == false) {

                // Apply a linear impulse to the player on the x axis
                player.box2dBody.applyLinearImpulse(new Vector2(0.5f, 0), player.box2dBody.getWorldCenter(), true);
            }
        } else {

            // If the game is paused, stop the player from moving (set x and y velocity to 0)
            player.box2dBody.setLinearVelocity(new Vector2(0, 0));
        }
    }

    // Check if the player is dead (has gone off the left edge of the screen)
    private void checkIfDead() {

        // If the player's x position is 15 less than the camera's x position
        if (player.box2dBody.getPosition().x <= gameCamera.position.x - 15 && !player.playerIsDead) {

            // Create a new game over screen and add it to the input multiplexer processors
            gameOver = new GameOver(game.batch, game, this);
            inputMultiplexer.addProcessor(gameOver.stage);

            // Set the player to 'dead'
            player.playerIsDead = true;
        }
    }

    // Method to handle enemy spawns (grounded and flying)
    public void handleEnemies() {

        // If there are grounded enemies that need to be spawned in the map
        if (box2dWorldCreator.getGroundEnemyPositions().size() > 0) {

            // If the player is coming up to where a grounded enemy spawns
            if (getPlayerPos().x + 50 > box2dWorldCreator.getGroundEnemyPositions().element().x / 32) {

                // Create the enemy
                GroundEnemy newGroundEnemy = new GroundEnemy(world, this, box2dWorldCreator.getGroundEnemyPositions().element());
                groundEnemies.offer(newGroundEnemy);

                // Remove the enemy from the 'enemies to be created' list
                box2dWorldCreator.getGroundEnemyPositions().remove();
            }
        }

        // If there are any ground enemies that are spawned into the map
        if (groundEnemies.size() > 0) {

            // If the player has passed the grounded enemy and it's off the screen
            if (groundEnemies.element().box2dBody.getPosition().x < getPlayerPos().x - 20) {

                // Destroy the enemy
                world.destroyBody(groundEnemies.element().box2dBody);
                groundEnemies.remove();
            }
        }

        // If there are flying enemies that need to be spawned in the map
        if (box2dWorldCreator.getFlyingEnemyPositions().size() > 0) {

            // If the player is coming up to where a flying enemy spawns
            if (getPlayerPos().x + 50 > box2dWorldCreator.getFlyingEnemyPositions().element().x / 32) {

                // Create the enemy
                FlyingEnemy newFlyingEnemy = new FlyingEnemy(world, this, box2dWorldCreator.getFlyingEnemyPositions().element());
                flyingEnemies.offer(newFlyingEnemy);

                // Remove the enemy from the 'enemies to be created' list
                box2dWorldCreator.getFlyingEnemyPositions().remove();
            }
        }

        // If there are any flying enemies that are spawned into the map
        if (flyingEnemies.size() > 0) {

            // If the player has passed the flying enemy and it's off the screen
            if (flyingEnemies.element().box2dBody.getPosition().x < getPlayerPos().x - 20) {

                // Destroy the enemy
                world.destroyBody(flyingEnemies.element().box2dBody);
                flyingEnemies.remove();
            }
        }
    }

    // Method to draw enemy sprites
    public void drawEnemies(float delta) {

        // Get all ground enemies that are spawned into the map
        for (GroundEnemy enemy : groundEnemies) {

            // Update the enemy (updates current animation frame, sound, and movement)
            enemy.update(delta);

            // Draw the current enemy animation frame
            game.batch.draw(enemy.currentFrame, enemy.box2dBody.getPosition().x - 1, (float) (enemy.box2dBody.getPosition().y - 1), 5, 5);
        }

        // Get all flying enemies that are spawned into the map
        for (FlyingEnemy enemy : flyingEnemies) {

            // Update the enemy (updates current animation frame, sound, and movement)
            enemy.update(delta);

            // Draw the current enemy animation frame
            game.batch.draw(enemy.currentFrame, enemy.box2dBody.getPosition().x - 1, (float) (enemy.box2dBody.getPosition().y - 1), 2, 2);
        }
    }

    // Method to draw coins in the map
    public void drawCoins(float delta) {

        // Get all coins that are in the map
        for (Coin coin : box2dWorldCreator.getCoins()) {

            // Update the coin (updates current animation frame)
            coin.update(delta);

            // If the coin is not yet collected
            if (!coin.collected) {

                // Draw the current coin animation frame
                game.batch.draw(coin.currentFrame, coin.box2dBody.getPosition().x - 0.75f, (float) (coin.box2dBody.getPosition().y - 0.75f), 1.5f, 1.5f);
            }
        }
    }

    // Method to draw the player
    public void drawPlayer() {

        /* This code draws the player in different positions due to the Box2D body shape changing
        size depending on the state of the player

         If the player is running and was previously sliding */
        if (player.getState() == Player.State.RUN && (player.previousState == Player.State.SLIDE_START || player.previousState == Player.State.SLIDE_END)) {

            // Draw the current player animtaion frame
            game.batch.draw(player.currentFrame, (float) (player.box2dBody.getPosition().x - 4), (float) (player.box2dBody.getPosition().y - 1.5), 8, 8);

            // If the player is sliding
        } else if (player.getState() == Player.State.SLIDE_START || player.getState() == Player.State.SLIDE_END) {

            // Draw the current player animtaion frame (lower than normal as the player is laying down)
            game.batch.draw(player.currentFrame, (float) (player.box2dBody.getPosition().x - 4), (float) (player.box2dBody.getPosition().y - 1.5), 8, 8);

            // If the player is starting to jump
        } else if (player.getState() == Player.State.JUMP_START) {

            // Draw the current player animtaion frame
            game.batch.draw(player.currentFrame, (float) (player.box2dBody.getPosition().x - 4), (float) (player.box2dBody.getPosition().y - 3), 8, 8);

            // If the player is ending the jump
        } else if (player.getState() == Player.State.JUMP_END) {

            // Draw the current player animtaion frame
            game.batch.draw(player.currentFrame, (float) (player.box2dBody.getPosition().x - 4), (float) (player.box2dBody.getPosition().y - 2), 8, 8);

        // Else, Draw the current player animation frame in the regular position
        } else {
            game.batch.draw(player.currentFrame, (float) (player.box2dBody.getPosition().x - 4), (float) (player.box2dBody.getPosition().y - 2.6), 8, 8);
        }
    }

    // Method to shake the game camera (to simulate an earthquake)
    private void shakeCamera(float deltaTime) {

        // If there are any screenShaker's in the map
        if (box2dWorldCreator.getScreenShakerPositions().size() > 0) {

            // set 'shaker' to the head of the queue (closest to player)
            ScreenShaker shaker = box2dWorldCreator.getScreenShakerPositions().element();

            // if the shaker's x position is greater than the player's x position
            if (player.box2dBody.getPosition().x >= shaker.getPosition().x / 32) {

                // If the shaker is not shaking and hasn't been shaked before
                if(!shaker.isShaking() && !shaker.isShakeFinished()){

                    // Set the shaker to 'shaking' for 2 seconds with a magnitude of 0.2f
                    shaker.setShaking(true);
                    shaker.shake(2f, 0.2f);

                    // If the shaker is shaking
                }else{

                    // Update the shaker (translate the gameCamera randomly)
                    shaker.update(deltaTime);
                }

                if(shaker.isShakeFinished()){
                    box2dWorldCreator.getScreenShakerPositions().poll();
                }

            }
        }
    }

    // Method to update the HUD coin counter (increase by 1)
    public void updateCollectedCoins() {
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
        map.dispose();
        world.dispose();
        renderer.dispose();
        hud.dispose();
        gameOver.dispose();
        levelBrief.dispose();
        paused.dispose();
    }

    // Getters and Setters
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

    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }

    public Player getPlayer() {
        return player;
    }

    public Hud getHud() {
        return hud;
    }

    public Box2DWorldCreator getBox2dWorldCreator() {
        return box2dWorldCreator;
    }

    public void setWorldEndPosition(float x) {
        worldEndPosition = x / Semtb001IndividualAssignment.PPM;
    }

    public void setPaused(boolean value) {
        isPaused = value;
    }

}
