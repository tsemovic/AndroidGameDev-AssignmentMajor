package com.semtb001.major.assignement.screens;

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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.semtb001.major.assignement.items.Bucket;
import com.semtb001.major.assignement.items.Item;
import com.semtb001.major.assignement.items.Wheat;
import com.semtb001.major.assignement.scenes.GameOver;
import com.semtb001.major.assignement.scenes.Gui;
import com.semtb001.major.assignement.scenes.Hud;
import com.semtb001.major.assignement.scenes.LevelBrief;
import com.semtb001.major.assignement.scenes.Paused;
import com.semtb001.major.assignement.scenes.TouchPad;
import com.semtb001.major.assignement.sprites.FlyingEnemy;
import com.semtb001.major.assignement.sprites.Coin;
import com.semtb001.major.assignement.sprites.Player;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.sprites.GroundEnemy;
import com.semtb001.major.assignement.tools.Assets;
import com.semtb001.major.assignement.tools.Box2DWorldCreator;
import com.semtb001.major.assignement.tools.ScreenShaker;
import com.semtb001.major.assignement.tools.WorldContactListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class PlayScreen implements Screen {

    // Game, camera, and current level objects
    private Semtb001MajorAssignment game;
    private OrthographicCamera gameCamera;
    public String currentLevel;

    // world map objects
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Box2DDebugRenderer box2DDebugRenderer;
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
    //private GameOver gameOver;
    private LevelBrief levelBrief;

    // Player, Enemy and Coin objects
    public Player player;
    private Queue<GroundEnemy> groundEnemies;
    private Queue<FlyingEnemy> flyingEnemies;
    private Queue<Coin> coins;

    // Music object for game music loop
    private Music music;

    // Input Multiplexer object (multitouch functionality)
    private InputMultiplexer inputMultiplexer;

    // Boolean to determine if the level brief is displayed
    private boolean levelBriefActive;


    public TouchPad touchPad;
    private Gui gui;
    public float timeCount;


    public PlayScreen(Semtb001MajorAssignment semtb001MajorAssignment, String currentLevel) {

        // Instantiate game and level objects
        game = semtb001MajorAssignment;
        batch = semtb001MajorAssignment.batch;
        this.currentLevel = currentLevel;
        isGameOverCreated = false;

        // Setup game camera
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, Semtb001MajorAssignment.WORLD_WIDTH, Semtb001MajorAssignment.WORLD_HEIGHT);
        box2DDebugRenderer = new Box2DDebugRenderer();

        // Load map level, render the map, and create the game world
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("island" + currentLevel.substring(7, 8) + ".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, Semtb001MajorAssignment.MPP);
        world = new World(new Vector2(0, 0), true);

        // Setup the texture atlas for loading in the player and enemy textures
        textureAtlas = Semtb001MajorAssignment.assetManager.manager.get(Assets.textureAtlas);

        // Setup the Box2D world creator (creates Box2D bodies and adds them to the world)
        box2dWorldCreator = new Box2DWorldCreator(this);

        // Setup the world contact listener
        world.setContactListener(new WorldContactListener(box2dWorldCreator));

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
        //gameOver = new GameOver(game.batch, game, this);
        paused = new Paused(game.batch, game, this);
        levelBrief = new LevelBrief(game.batch);

        // Initially, set the level brief to active so that the level brief is displayed at start up
        levelBriefActive = true;

        touchPad = new TouchPad();
        gui = new Gui(player);

        // Input multiplexer setup (allows multitouch between the game, hud, paused, and game over screens)
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(hud.stage);
        //inputMultiplexer.addProcessor(gameOver.stage);
        inputMultiplexer.addProcessor(paused.stage);
        inputMultiplexer.addProcessor(touchPad.stage);
        inputMultiplexer.addProcessor(gui.stage);

//        // Setup game music loop
//        music = Semtb001MajorAssignment.assetManager.manager.get(Assets.music);
//        music.setLooping(true);
//        music.play();

    }

    @Override
    public void show() {

    }

    // Method for handling input for the game
    public void inputHandler(float delta) {
        TiledMapTileSet tileSet = map.getTileSets().getTileSet(0);
        Set<Item> itemSet = gui.items.keySet();

        timeCount += dt;
        Integer itemPressed = 0;

        for (Item i : itemSet) {
            if (i.getPressed()) {
                gui.update(dt, player);
                itemPressed = 1;
            }
        }

        if (touchPad.isTouched) {
            movePlayer(touchPad.touchPad.getKnobPercentX(), touchPad.touchPad.getKnobPercentY());
            itemPressed = 1;
        } else {
            player.box2dBody.setLinearVelocity(0, 0);
            player.currentSpeed = 0;
        }

        if (Gdx.input.isTouched()) {
            if (itemPressed == 0) {

                for (Item i : itemSet) {
                    if (i.getActive()) {

                        if (i.getName() == "hoe") {
                            if (getCell("grass").getTile() == tileSet.getTile(42)) {
                                getCell("grass").setTile(tileSet.getTile(403));
                            }
                            box2dWorldCreator.harvestWheat();
                        }

                        if (i.getName() == "seeds") {
                            if (getCell("grass").getTile() == tileSet.getTile(403)) {

                                //stop multiple objects being created for the same bounds
                                boolean create = true;
                                for (Wheat w : box2dWorldCreator.wheat) {
                                    if ((w.rectangle.x == (int) (player.box2dBody.getPosition().x * Semtb001MajorAssignment.PPM / 32)) &&
                                            (w.rectangle.y == (int) (player.box2dBody.getPosition().y * Semtb001MajorAssignment.PPM / 32))) {

                                        create = false;
                                    }
                                }
                                //create seeds if there are seeds in inventory
                                if (create) {
                                    if (player.getInventory().getItem("seeds") > 0) {
                                        box2dWorldCreator.createWheat();
                                        player.getInventory().removeItem("seeds", 1);
                                    }
                                }
                            } else {
                                //System.out.println("NOT DIRT");
                            }
                        }

                        if (i.getName() == "bucket") {
                            //prevents picking up water and placing it instantly;
                            if (timeCount >= 1) {
                                if (i.getHealth() == 100) {
                                    getCell("grass").setTile(tileSet.getTile(137));
                                    i.setHealth(0);

                                } else {
                                    for (TiledMapTileLayer.Cell cell : getSurroundingBucketCells("water", getPlayerPos())) {
                                        if (cell.getTile() == tileSet.getTile(137)) {
                                            i.setHealth(100);
                                        }
                                    }
                                    //pickup water block that has been placed
                                    if (getCell("grass").getTile() == tileSet.getTile(137)) {
                                        getCell("grass").setTile(tileSet.getTile(42));
                                        i.setHealth(100);
                                    }
                                }
                                timeCount = 0;
                            }
                            ((Bucket) i).updateWater();
                            System.out.println(i.getHealth());
                        }
                    }
                }
            }
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
        box2DDebugRenderer.render(world, gameCamera.combined);
        game.batch.setProjectionMatrix(gameCamera.combined);

        // Set the input processor
        Gdx.input.setInputProcessor(inputMultiplexer);
        touchPad.stage.draw();
        gui.stage.draw();

        moveCamera();

        // Begin the sprite batch for drawing the Player, Enemies, and Coins
        game.batch.begin();

        // Draw Player, Enemies, and Coins
        drawPlayer();
//        drawEnemies(delta);

        // End the sprite batch for drawing the Player, Enemies, and Coins
        game.batch.end();

        // Draw the heads up display
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        // Draw transparent background when the game is paused or when the game is over
        game.batch.begin();
//        if (isPaused) {
//            paused.getBackgroundSprite().draw(game.batch);
//        } else if (player.getGameOver() || getPlayerPos().x >= worldEndPosition) {
//            gameOver.getBackgroundSprite().draw(game.batch);
//        }
        game.batch.end();

//        // Draw the game paused and game over screen overlay
//        if (isPaused) {
//            game.batch.setProjectionMatrix(paused.stage.getCamera().combined);
//            paused.stage.draw();
//        } else if (player.getGameOver() || getPlayerPos().x >= worldEndPosition) {
//
//            // If the game over screen has not yet been created, create it
//            if (!isGameOverCreated) {
//                gameOver = new GameOver(game.batch, game, this);
//                inputMultiplexer.addProcessor(gameOver.stage);
//                isGameOverCreated = true;
//            }
//            game.batch.setProjectionMatrix(gameOver.stage.getCamera().combined);
//            gameOver.stage.draw();
//        }
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
            gui.update(deltaTime, player);
            hud.update(deltaTime);
            // Handle enemies (movement, sound, and animation frame depending on state)
            handleEnemies();

            for (Wheat wheat : box2dWorldCreator.wheat) {
                wheat.update(deltaTime);
                wheat.updateWater();
            }

        } else {

            // If the game is paused or the level brief is displayed: pause all game sounds

        }
    }

    public void moveCamera(){
        gameCamera.position.x = player.box2dBody.getPosition().x;
        gameCamera.position.y = player.box2dBody.getPosition().y;
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

//    // Method to draw enemy sprites
//    public void drawEnemies(float delta) {
//
//        // Get all ground enemies that are spawned into the map
//        for (GroundEnemy enemy : groundEnemies) {
//
//            // Update the enemy (updates current animation frame, sound, and movement)
//            enemy.update(delta);
//
//            // Draw the current enemy animation frame
//            game.batch.draw(enemy.currentFrame, enemy.box2dBody.getPosition().x - 1, (float) (enemy.box2dBody.getPosition().y - 1), 5, 5);
//        }
//
//        // Get all flying enemies that are spawned into the map
//        for (FlyingEnemy enemy : flyingEnemies) {
//
//            // Update the enemy (updates current animation frame, sound, and movement)
//            enemy.update(delta);
//
//            // Draw the current enemy animation frame
//            game.batch.draw(enemy.currentFrame, enemy.box2dBody.getPosition().x - 1, (float) (enemy.box2dBody.getPosition().y - 1), 2, 2);
//        }
//    }
//https://stackoverflow.com/questions/42057796/move-the-player-only-in-45-steps-with-touchpad-in-libgdx
public void movePlayer(float dx, float dy) {
    int direction = (int) Math.floor((Math.atan2(dy, dx) + Math.PI / 8) / (2 * Math.PI / 8));

    if (direction == 8) direction = 0;
    double angle = direction * (Math.PI / 4);
    player.setAngle(angle);

    //Set the player direction state
    if (angle == 0) {
        player.currentState = Player.State.E;
    } else if (angle == -0.7853981633974483) {
        player.currentState = Player.State.SE;
    } else if (angle == -1.5707963267948966) {
        player.currentState = Player.State.S;
    } else if (angle == -2.356194490192345) {
        player.currentState = Player.State.SW;
    } else if (angle == -3.141592653589793) {
        player.currentState = Player.State.W;
    } else if (angle == 2.356194490192345) {
        player.currentState = Player.State.NW;
    } else if (angle == 1.5707963267948966) {
        player.currentState = Player.State.N;
    } else if (angle == 0.7853981633974483) {
        player.currentState = Player.State.NE;
    }

    //make player face the direction of travel
    player.box2dBody.setTransform(player.box2dBody.getPosition(), (float) angle);

    //change the x and y direction percentages to positive so that when they are used to
    // move the player, the player doesn't move in the opposite direction due to negative velocity;
    if (dx < 0) {
        dx = dx * -1;
    }
    if (dy < 0) {
        dy = dy * -1;
    }
    double angleSpeed = Math.sqrt((dx * dx) + (dy * dy));

    player.box2dBody.setLinearVelocity((float) (player.getX() + Math.cos(angle) * (player.maxSpeed * angleSpeed)),
            (float) (player.getY() + Math.sin(angle) * (player.maxSpeed * angleSpeed)));

    // Set the players speed
    if(angleSpeed > 0.9){
        player.currentSpeed = 1;
    }else if(angleSpeed > 0.6){
        player.currentSpeed = 0.6;
    }else if(angleSpeed > 0.3){
        player.currentSpeed = 0.3;
    }else{
        player.currentSpeed = angleSpeed;
    }
}

    // Method to draw the player
    public void drawPlayer() {

        /* This code draws the player in different positions due to the Box2D body shape changing
        size depending on the state of the player

         If the player is running and was previously sliding */

            //game.batch.draw(player.currentFrame, (float) (player.box2dBody.getPosition().x - 4), (float) (player.box2dBody.getPosition().y - 2.6), 8, 8);

    }

    public TiledMapTileLayer.Cell getCell(String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        return layer.getCell((int) (player.box2dBody.getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int) (player.box2dBody.getPosition().y * Semtb001MajorAssignment.PPM / 32));
    }

    public TiledMapTileLayer.Cell getCell(String layerName, Rectangle bounds) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        return layer.getCell((int) bounds.x, (int) bounds.y);
    }

    public List<TiledMapTileLayer.Cell> getSurroundingWheatCells(String layerName, Rectangle bounds) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        List<TiledMapTileLayer.Cell> cells = new ArrayList<TiledMapTileLayer.Cell>();

        //layer of target
        cells.add(layer.getCell((int) bounds.x + 1, (int) bounds.y));
        cells.add(layer.getCell((int) bounds.x + 2, (int) bounds.y));
        cells.add(layer.getCell((int) bounds.x - 1, (int) bounds.y));
        cells.add(layer.getCell((int) bounds.x - 2, (int) bounds.y));

        //layer 1 above target
        cells.add(layer.getCell((int) bounds.x, (int) bounds.y + 1));
        cells.add(layer.getCell((int) bounds.x + 1, (int) bounds.y + 1));
        cells.add(layer.getCell((int) bounds.x + 2, (int) bounds.y + 1));
        cells.add(layer.getCell((int) bounds.x - 1, (int) bounds.y - 1));
        cells.add(layer.getCell((int) bounds.x - 2, (int) bounds.y - 1));

        //layer 2 above target
        cells.add(layer.getCell((int) bounds.x, (int) bounds.y + 2));
        cells.add(layer.getCell((int) bounds.x + 1, (int) bounds.y + 2));
        cells.add(layer.getCell((int) bounds.x + 2, (int) bounds.y + 2));
        cells.add(layer.getCell((int) bounds.x - 1, (int) bounds.y + 2));
        cells.add(layer.getCell((int) bounds.x - 2, (int) bounds.y + 2));

        //layer 1 below target
        cells.add(layer.getCell((int) bounds.x, (int) bounds.y - 1));
        cells.add(layer.getCell((int) bounds.x + 1, (int) bounds.y - 1));
        cells.add(layer.getCell((int) bounds.x + 2, (int) bounds.y - 1));
        cells.add(layer.getCell((int) bounds.x - 1, (int) bounds.y - 1));
        cells.add(layer.getCell((int) bounds.x - 2, (int) bounds.y - 1));

        //layer 2 below target
        cells.add(layer.getCell((int) bounds.x, (int) bounds.y - 2));
        cells.add(layer.getCell((int) bounds.x + 1, (int) bounds.y - 2));
        cells.add(layer.getCell((int) bounds.x + 2, (int) bounds.y - 2));
        cells.add(layer.getCell((int) bounds.x - 1, (int) bounds.y - 2));
        cells.add(layer.getCell((int) bounds.x - 2, (int) bounds.y - 2));

        return cells;
    }

    public List<TiledMapTileLayer.Cell> getSurroundingBucketCells(String layerName, Vector2 bounds) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        List<TiledMapTileLayer.Cell> cells = new ArrayList<TiledMapTileLayer.Cell>();

        cells.add(layer.getCell((int) bounds.x + 1, (int) bounds.y));
        cells.add(layer.getCell((int) bounds.x - 1, (int) bounds.y));

        cells.add(layer.getCell((int) bounds.x, (int) bounds.y - 1));
        cells.add(layer.getCell((int) bounds.x + 1, (int) bounds.y - 1));
        cells.add(layer.getCell((int) bounds.x - 1, (int) bounds.y - 1));

        cells.add(layer.getCell((int) bounds.x, (int) bounds.y + 1));
        cells.add(layer.getCell((int) bounds.x + 1, (int) bounds.y + 1));
        cells.add(layer.getCell((int) bounds.x - 1, (int) bounds.y + 1));

        return cells;
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
        //gameOver.dispose();
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
        Vector2 pos = new Vector2((int) (player.box2dBody.getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int) (player.box2dBody.getPosition().y * Semtb001MajorAssignment.PPM / 32));
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
        worldEndPosition = x / Semtb001MajorAssignment.PPM;
    }

    public void setPaused(boolean value) {
        isPaused = value;
    }

}
