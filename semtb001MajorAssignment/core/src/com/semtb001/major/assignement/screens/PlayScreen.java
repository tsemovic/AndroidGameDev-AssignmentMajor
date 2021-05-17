package com.semtb001.major.assignement.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
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
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.items.Item;
import com.semtb001.major.assignement.items.WateringCan;
import com.semtb001.major.assignement.items.Wheat;
import com.semtb001.major.assignement.scenes.GameOver;
import com.semtb001.major.assignement.scenes.Gui;
import com.semtb001.major.assignement.scenes.Hud;
import com.semtb001.major.assignement.scenes.LevelBrief;
import com.semtb001.major.assignement.scenes.Paused;
import com.semtb001.major.assignement.scenes.TouchPad;
import com.semtb001.major.assignement.sprites.Player;
import com.semtb001.major.assignement.sprites.Sheep;
import com.semtb001.major.assignement.tools.Assets;
import com.semtb001.major.assignement.tools.Box2DWorldCreator;
import com.semtb001.major.assignement.tools.WorldContactListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    // Box2D objects
    private Box2DWorldCreator box2dWorldCreator;

    // World step time calculation variables
    private static final float dt = (float) 0.01;
    private float time = (float) 0.0;
    double accumulator = 0.0;

    // Texture objects
    public TextureAtlas textureAtlas;
    public SpriteBatch batch;

    // Objects for if the game is paused or game is over
    public boolean isPaused;
    public boolean isGameOverCreated;

    // Screen overlay objects
    private Paused paused;
    private Hud hud;
    private GameOver gameOver;
    private LevelBrief levelBrief;
    public TouchPad touchPad;
    private Gui gui;

    // Player and sheep objects
    public Player player;
    private Queue<Sheep> sheep;

    // Input Multiplexer object (multitouch functionality)
    private InputMultiplexer inputMultiplexer;

    // World time counter
    public float timeCount;

    // Objects for tracking the number of wheat harvested and passing the level
    public boolean levelPassed;
    public int wheatHarvested;
    public int wheatToPassLevel;

    // HashMap to store the sheep waves (used to spawn sheep in at various times throughout the level)
    public HashMap<Double, Queue<Vector2>> sheepWaves;

    public PlayScreen(Semtb001MajorAssignment semtb001MajorAssignment, String currentLevel) {

        // Instantiate game and level objects
        game = semtb001MajorAssignment;
        batch = semtb001MajorAssignment.batch;
        this.currentLevel = currentLevel;
        isGameOverCreated = false;
        levelPassed = false;
        wheatHarvested = 0;

        // The wheat required to pass the level is 2 * the level number
        wheatToPassLevel = Integer.parseInt(currentLevel.substring(7, 8)) * 2;

        // Setup game camera
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, Semtb001MajorAssignment.WORLD_WIDTH, Semtb001MajorAssignment.WORLD_HEIGHT);
        box2DDebugRenderer = new Box2DDebugRenderer();

        // Load map level, render the map, and create the game world
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapFiles/level" + currentLevel.substring(7, 8) + ".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, Semtb001MajorAssignment.MPP);
        world = new World(new Vector2(0, 0), true);

        // Setup the texture atlas for loading in the player and enemy textures
        textureAtlas = Semtb001MajorAssignment.assetManager.manager.get(Assets.textureAtlas);

        // Setup the Box2D world creator (creates Box2D bodies and adds them to the world)
        box2dWorldCreator = new Box2DWorldCreator(this);

        // Setup the world contact listener
        world.setContactListener(new WorldContactListener(box2dWorldCreator));

        // Instantiate Player, Sheep and SheepWaves
        player = new Player(world, this);
        sheep = new LinkedList<Sheep>();
        sheepWaves = new HashMap<Double, Queue<Vector2>>();

        // Setup the game camera position
        gameCamera.position.x = player.getBox2dBody().getPosition().x + 9;
        gameCamera.position.y = 23;

        // Setup the screen overlay objects
        hud = new Hud(game.batch, this);
        gameOver = new GameOver(game.batch, game, this);
        paused = new Paused(game.batch, game, this);
        levelBrief = new LevelBrief(game.batch, this);
        touchPad = new TouchPad();
        gui = new Gui(player);

        // Input multiplexer setup (allows multitouch between the game, hud, paused, and game over screens)
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(hud.stage);
        inputMultiplexer.addProcessor(gameOver.stage);
        inputMultiplexer.addProcessor(paused.stage);
        inputMultiplexer.addProcessor(touchPad.stage);
        inputMultiplexer.addProcessor(gui.stage);

        // Setup the sheep waves (3 waves, 1 at the beginning, 1 at 1/3 through the time, 1 at 2/3 through the time)
        setupSheepWaves(3);

        // Set the hoe to be the active item in the player's hotbar
        Set<Item> itemSet = gui.items.keySet();
        for (Item i : itemSet) {
            if (i.getName() == "hoe") {
                i.setActive(true);
            }
        }

    }

    @Override
    public void show() {

    }

    // Method for handling input for the game
    public void inputHandler() {

        // Setup the tileset and the itemset objects
        TiledMapTileSet tileSet = map.getTileSets().getTileSet(0);
        Set<Item> itemSet = gui.items.keySet();

        // Increase the time count by delta time
        timeCount += dt;
        Integer itemPressed = 0;

        // Loop through each item in the itemSet
        for (Item i : itemSet) {

            // If the item is pressed
            if (i.getPressed()) {

                // Update the gui to show the active item
                gui.update(dt, player);
                itemPressed = 1;
            }
        }

        // If the touchPad is touched
        if (touchPad.isTouched) {

            // Move the player
            movePlayer(touchPad.touchPad.getKnobPercentX(), touchPad.touchPad.getKnobPercentY());
            itemPressed = 1;

            // If the touchPad is not being touched
        } else {

            // Stop all player movement
            player.getBox2dBody().setLinearVelocity(0, 0);
            player.setCurrentSpeed(Player.Speed.STOP);
        }

        // If a vacant space of the screen is touched (anywhere except overlay objects such as touchpad, pause, or the hotbar)
        if (Gdx.input.isTouched()) {
            if (itemPressed == 0) {

                // For each item in the itemset
                for (Item i : itemSet) {

                    // If the item is 'active'
                    if (i.getActive()) {

                        // If the item is a 'hoe'
                        if (i.getName() == "hoe") {

                            // If the player is currently standing on 'grass'
                            if (getCell("grass").getTile() == tileSet.getTile(Semtb001MajorAssignment.GRASS)) {

                                // Handle player animations
                                player.resetStateTimer();
                                player.setCurrentState(Player.State.HOE);
                                player.playItemSound();

                                // Boolean to track if the soil should be wet
                                Boolean wetSoil = false;

                                // For each tile surrounding the player in a 3x3 grid
                                for (Vector2 v : getSurroundingPositionss3x3(getPlayerPos())) {
                                    Rectangle r = new Rectangle(v.x, v.y, 1, 1);

                                    // If the tile is a source of water
                                    if (getCell("water", r).getTile().getId() == Semtb001MajorAssignment.WATER) {

                                        // Set wetSoil to true
                                        wetSoil = true;
                                    }
                                }

                                // If the wetSoil boolean is true, till the grass to show wet soil
                                if (wetSoil) {
                                    getCell("grass").setTile(tileSet.getTile(Semtb001MajorAssignment.WET_SOIL));

                                    // If the wetSoil boolean is false, till the grass to show dry soil
                                } else {
                                    getCell("grass").setTile(tileSet.getTile(Semtb001MajorAssignment.DRY_SOIL));
                                }
                            }

                            // Try to harvest wheat in the box2dWorldCreator
                            box2dWorldCreator.harvestWheat();
                        }

                        // If the item is 'seeds'
                        if (i.getName() == "seeds") {

                            // If 1 second has passed since touching the screen (prevents rapid placement)
                            if (timeCount >= 1) {

                                // Reset the players state timer
                                player.resetStateTimer();

                                // If the player is standing on either dry soil or wet soil
                                if (getCell("grass").getTile() == tileSet.getTile(Semtb001MajorAssignment.DRY_SOIL) ||
                                        getCell("grass").getTile() == tileSet.getTile(Semtb001MajorAssignment.WET_SOIL)) {

                                    // Boolean to track if wheat should be created
                                    boolean create = true;

                                    // For each wheat in the box2dWorldCreator
                                    for (Wheat w : box2dWorldCreator.wheat) {

                                        // If there is already seeds (wheat) at this location, wheat can't be created at this location
                                        if ((w.bounds.x == (int) (player.getBox2dBody().getPosition().x * Semtb001MajorAssignment.PPM / 32)) &&
                                                (w.bounds.y == (int) (player.getBox2dBody().getPosition().y * Semtb001MajorAssignment.PPM / 32))) {
                                            create = false;
                                        }
                                    }

                                    // If wheat can be created
                                    if (create) {

                                        // If the player has more than 0 seeds in thier inventory
                                        if (player.getInventory().getItemValue("seeds") > 0) {

                                            // Set the players state to 'SEEDS'
                                            player.setCurrentState(Player.State.SEEDS);

                                            // Play the item sound
                                            player.playItemSound();

                                            // Create wheat in box2dWorldCreator
                                            box2dWorldCreator.createWheat();

                                            // And remove 1 seed from the players inventory
                                            player.getInventory().removeItem("seeds", 1);
                                        }
                                    }
                                }
                            }

                            // Reset the timeCount to 0
                            timeCount = 0;

                        }

                        // If the item is a 'wateringCan'
                        if (i.getName() == "wateringCan") {


                            // If 1 second has passed since touching the screen (prevents rapid placement)
                            if (timeCount >= 1) {

                                // Reset the players state timer to 0
                                player.resetStateTimer();

                                // If the bucket is full of water
                                if (i.getHealth() == 100) {

                                    // Water the ground and empty the wateringCan
                                    getCell("water").setTile(tileSet.getTile(Semtb001MajorAssignment.WATER));
                                    i.setHealth(0);

                                    // For each tile in a 3x3 grid surrounding the tile which was watered
                                    for (Vector2 v : getSurroundingPositionss3x3(getPlayerPos())) {

                                        // If the tile is 'dry' soil, change it to 'wet' soil
                                        Rectangle r = new Rectangle(v.x, v.y, 1, 1);
                                        if (getCell("grass", r).getTile().getId() == Semtb001MajorAssignment.DRY_SOIL) {
                                            getCell("grass", r).setTile(tileSet.getTile(Semtb001MajorAssignment.WET_SOIL));
                                        }
                                    }

                                    // Set the player's state to 'WATER'
                                    player.setCurrentState(Player.State.WATER);

                                    // Play the item sound
                                    player.playItemSound();

                                    // If the bucket is empty
                                } else {

                                    // Boolean to track if a water sound should be played
                                    Boolean playWaterSound = false;

                                    // If the a tile in a 3x3 grid around the player is water
                                    for (TiledMapTileLayer.Cell cell : getSurroundingCells3x3("water", getPlayerPos())) {
                                        if (cell.getTile() == tileSet.getTile(137)) {

                                            // Fill the bucket up with water
                                            i.setHealth(100);

                                            // Play water sound to true
                                            playWaterSound = true;
                                        }
                                    }

                                    // Play the item sound
                                    if (playWaterSound) {
                                        player.playItemSound();
                                    }
                                }
                            }

                            // Update the wateringCan's water (for the hotbar item)
                            ((WateringCan) i).updateWater();

                            // Reset the timeCount to 0
                            timeCount = 0;

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

            // Draw transparent overlay behind the level brief
            game.batch.begin();
            gameOver.getBackgroundSprite().draw(game.batch);
            game.batch.end();

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

        moveCamera();

        // Begin the sprite batch for drawing the Player, Enemies, and Coins
        game.batch.begin();

        // Draw Player and Sheep
        drawPlayer();
        drawSheep(delta);

        // End the sprite batch for drawing the Player, Enemies, and Coins
        game.batch.end();

        // Draw the heads up display
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        // Draw the touchpad
        touchPad.stage.draw();

        // Draw the gui
        gui.stage.draw();

        // Draw transparent background when the game is paused or when the game is over
        game.batch.begin();
        if (isPaused) {
            paused.getBackgroundSprite().draw(game.batch);
        } else if (hud.getTimeUp()) {
            gameOver.getBackgroundSprite().draw(game.batch);
        }
        game.batch.end();

        // Draw the game paused overlay
        if (isPaused) {
            game.batch.setProjectionMatrix(paused.stage.getCamera().combined);
            paused.stage.draw();
        } else if (hud.getTimeUp()) {

            // If the game over screen has not yet been created, create it
            if (!isGameOverCreated) {
                gameOver = new GameOver(game.batch, game, this);
                inputMultiplexer.addProcessor(gameOver.stage);
                isGameOverCreated = true;
            }

            // Draw the gameOver screen
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
            inputHandler();

            // Update the player
            player.update(deltaTime);

            // Update the screen overlays
            gui.update(deltaTime, player);
            hud.update(deltaTime);

            // Handle sheep (movement, sound, and animation frame)
            handleSheep(deltaTime);

            // Loop through the wheat and update them
            for (Wheat wheat : box2dWorldCreator.wheat) {
                wheat.update(deltaTime);
                wheat.updateWater();
            }

            // If the amount of wheat harvested is more or equal to the amount to pass the level
            if (wheatHarvested >= wheatToPassLevel) {

                // Set levelPassed to true
                levelPassed = true;
            }

        }
    }

    // Method to move the game camera
    public void moveCamera() {

        // Set the camera position to the players position
        gameCamera.position.x = player.getBox2dBody().getPosition().x;
        gameCamera.position.y = player.getBox2dBody().getPosition().y;
    }

    // Method to setup the sheep waves
    public void setupSheepWaves(int waves) {

        // Set the number of waves that the sheep spawn in
        int numberOfWaves = waves;
        int totalSheep = box2dWorldCreator.getSheepPositions().size();
        double waveTimeIncrements = hud.getWorldTimer() / numberOfWaves;

        // For each wave
        for (int i = numberOfWaves; i >= 0; i--) {
            Queue<Vector2> waveQueue = new LinkedList<>();

            // Split up the sheep and add them to the wave queue
            if (totalSheep > numberOfWaves) {
                for (int y = 0; y <= Math.floor(totalSheep / 3); y++) {
                    if (box2dWorldCreator.getSheepPositions().size() > 0) {
                        waveQueue.add(box2dWorldCreator.getSheepPositions().poll());
                    }
                }
            } else {
                if (box2dWorldCreator.getSheepPositions().size() > 0) {
                    waveQueue.add(box2dWorldCreator.getSheepPositions().poll());
                }
            }

            // Add the wave queue and the time increments to the sheep waves
            sheepWaves.put(i * waveTimeIncrements, waveQueue);
        }
    }

    // Method to handle sheep spawns
    public void handleSheep(float deltaTime) {

        // For each sheep in the sheep waves queue
        for (Map.Entry<Double, Queue<Vector2>> kv : sheepWaves.entrySet()) {

            // If the world time is less or equal to the time that the wave should spawn
            if (hud.getWorldTimer() <= kv.getKey()) {

                // Create the sheep in the sheep wave queue
                for (Vector2 sheepPosition : kv.getValue()) {
                    Sheep newSheep = new Sheep(world, this, sheepPosition);
                    sheep.offer(newSheep);
                    kv.getValue().remove(sheepPosition);
                }

                // Remove the created sheep from the queue
                sheepWaves.remove(kv);
            }
        }

        // If there are any sheep that are spawned into the map
        if (sheep.size() > 0) {

            // Loop through the sheep in the map and update them
            for (Sheep s : sheep) {
                s.update(deltaTime);
            }
        }

    }

    // Method to draw sheep sprites
    public void drawSheep(float delta) {

        // Get all sheep that are spawned into the map
        for (Sheep sheep : sheep) {

            // Update the sheep (updates current animation frame, sound, and movement)
            sheep.update(delta);

            // If the sheep are alive
            if (sheep.getHealth() > 0) {

                // Draw the current enemy animation frame
                game.batch.draw(sheep.currentFrame, sheep.getBox2dBody().getPosition().x - 1.75f,
                        (float) (sheep.getBox2dBody().getPosition().y - 1.5f), 3.5f, 3.5f);
            }
        }
    }

    /* Method to move the player in the direction of the touchPad

    This code has been implement from the source:
    https://stackoverflow.com/questions/42057796/move-the-player-only-in-45-steps-with-touchpad-in-libgdx*/
    public void movePlayer(float dx, float dy) {

        // Get the angle of the touchPad knob
        int direction = (int) Math.floor((Math.atan2(dy, dx) + Math.PI / 8) / (2 * Math.PI / 8));
        if (direction == 8) direction = 0;
        double angle = direction * (Math.PI / 4);

        // Set the player's direction state
        if (angle == 0) {
            player.setCurrentDirection(Player.Direction.E);
        } else if (angle == -0.7853981633974483) {
            player.setCurrentDirection(Player.Direction.SE);
        } else if (angle == -1.5707963267948966) {
            player.setCurrentDirection(Player.Direction.S);
        } else if (angle == -2.356194490192345) {
            player.setCurrentDirection(Player.Direction.SW);
        } else if (angle == -3.141592653589793) {
            player.setCurrentDirection(Player.Direction.W);
        } else if (angle == 2.356194490192345) {
            player.setCurrentDirection(Player.Direction.NW);
        } else if (angle == 1.5707963267948966) {
            player.setCurrentDirection(Player.Direction.N);
        } else if (angle == 0.7853981633974483) {
            player.setCurrentDirection(Player.Direction.NE);
        }

        // Make player face the direction of travel
        player.getBox2dBody().setTransform(player.getBox2dBody().getPosition(), (float) angle);

        /* Change the x and y direction percentages to positive so that when they are used to
        move the player, the player doesn't move in the opposite direction due to negative velocity */
        if (dx < 0) {
            dx = dx * -1;
        }
        if (dy < 0) {
            dy = dy * -1;
        }

        /* Get the touchPad knob percentage (small if the knob is hardly moved, 100% (1) if the knob
        is at the edge of the touchPad background

        This is used for setting the players speed, so the harder the knob is moved, the faster
        the player will move*/
        double angleSpeed = Math.sqrt((dx * dx) + (dy * dy));
        double playerSpeed = 0;
        player.setPreviousSpeed();

        // Set the players speed
        if (angleSpeed > 0.9) {
            player.setCurrentSpeed(Player.Speed.FAST);
            playerSpeed = 0.9;
        } else if (angleSpeed > 0.6) {
            player.setCurrentSpeed(Player.Speed.MEDIUM);
            playerSpeed = 0.6;
        } else if (angleSpeed > 0.3) {
            player.setCurrentSpeed(Player.Speed.SLOW);
            playerSpeed = 0.3;
        } else {
            player.setCurrentSpeed(Player.Speed.STOP);
            playerSpeed = 0;
        }

        // Set the player's movement based on the angle they are moving and the angle speed
        player.getBox2dBody().setLinearVelocity((float) (player.getX() + Math.cos(angle) * (player.getMaxSpeed() * playerSpeed)),
                (float) (player.getY() + Math.sin(angle) * (player.getMaxSpeed() * playerSpeed)));

        // Set the player's state to 'WALK'
        player.setCurrentState(Player.State.WALK);
    }

    // Method to draw the player
    public void drawPlayer() {

        /* This code draws the player in different positions due to the Box2D body shape changing
        size depending on the state of the player*/
        HashMap<String, Float> dimensions = player.getFrameDimensions();
        game.batch.draw(player.getCurrentFrame(), dimensions.get("x"), dimensions.get("y"), dimensions.get("width"), dimensions.get("height"));

    }

    // Method to get the cell at the players position from the provided layer name in the tmx file
    public TiledMapTileLayer.Cell getCell(String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        return layer.getCell((int) (player.getBox2dBody().getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int) (player.getBox2dBody().getPosition().y * Semtb001MajorAssignment.PPM / 32));
    }

    // Method to get the cell at the provided position from the provided layer name in the tmx file
    public TiledMapTileLayer.Cell getCell(String layerName, Rectangle bounds) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        return layer.getCell((int) bounds.x, (int) bounds.y);
    }

    // Method to get cells surrounding the provided bounds in a 5x5 area
    public List<TiledMapTileLayer.Cell> getSurroundingCells5x5(String layerName, Rectangle bounds) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        List<TiledMapTileLayer.Cell> cells = new ArrayList<TiledMapTileLayer.Cell>();

        // Try to add surrounding cells (5x5) to the list
        try {
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

        } catch (Exception e) {

        }

        return cells;
    }

    // Method to get cells surrounding the provided bounds in a 3x3 area
    public List<TiledMapTileLayer.Cell> getSurroundingCells3x3(String layerName, Vector2 bounds) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        List<TiledMapTileLayer.Cell> cells = new ArrayList<TiledMapTileLayer.Cell>();

        cells.add(layer.getCell((int) bounds.x, (int) bounds.y));

        // Try to add surrounding cells (3x3) to the list
        try {
            cells.add(layer.getCell((int) bounds.x + 1, (int) bounds.y));
            cells.add(layer.getCell((int) bounds.x - 1, (int) bounds.y));

            cells.add(layer.getCell((int) bounds.x, (int) bounds.y - 1));
            cells.add(layer.getCell((int) bounds.x + 1, (int) bounds.y - 1));
            cells.add(layer.getCell((int) bounds.x - 1, (int) bounds.y - 1));

            cells.add(layer.getCell((int) bounds.x, (int) bounds.y + 1));
            cells.add(layer.getCell((int) bounds.x + 1, (int) bounds.y + 1));
            cells.add(layer.getCell((int) bounds.x - 1, (int) bounds.y + 1));
        } catch (Exception e) {

        }

        return cells;
    }

    // Method to get cells surrounding the provided bounds in a 3x3 area
    public List<Vector2> getSurroundingPositionss3x3(Vector2 bounds) {
        List<Vector2> positions = new ArrayList<>();

        positions.add(new Vector2((int) bounds.x, (int) bounds.y));

        // Try to add surrounding cells (3x3) to the list
        try {
            positions.add(new Vector2((int) bounds.x + 1, (int) bounds.y));
            positions.add(new Vector2((int) bounds.x - 1, (int) bounds.y));

            positions.add(new Vector2((int) bounds.x, (int) bounds.y - 1));
            positions.add(new Vector2((int) bounds.x + 1, (int) bounds.y - 1));
            positions.add(new Vector2((int) bounds.x - 1, (int) bounds.y - 1));

            positions.add(new Vector2((int) bounds.x, (int) bounds.y + 1));
            positions.add(new Vector2((int) bounds.x + 1, (int) bounds.y + 1));
            positions.add(new Vector2((int) bounds.x - 1, (int) bounds.y + 1));
        } catch (Exception e) {

        }

        return positions;
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
        Vector2 pos = new Vector2((int) (player.getBox2dBody().getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int) (player.getBox2dBody().getPosition().y * Semtb001MajorAssignment.PPM / 32));
        return pos;
    }

    public Player getPlayer() {
        return player;
    }

    public Box2DWorldCreator getBox2dWorldCreator() {
        return box2dWorldCreator;
    }

    public void setPaused(boolean value) {
        isPaused = value;
    }

    public int getWheatHarvested() {
        return wheatHarvested;
    }

    public void addWheatHarvested() {
        wheatHarvested++;
    }

    public boolean getLevelPassed() {
        return levelPassed;
    }

    public int getWheatToPassLevel() {
        return wheatToPassLevel;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
