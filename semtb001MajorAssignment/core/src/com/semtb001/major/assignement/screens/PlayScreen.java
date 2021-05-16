package com.semtb001.major.assignement.screens;

import com.badlogic.gdx.Gdx;
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

    // Player and sheep objects
    public Player player;
    private Queue<Sheep> sheep;

    // Music object for game music loop
    private Music music;

    // Input Multiplexer object (multitouch functionality)
    private InputMultiplexer inputMultiplexer;

    // Boolean to determine if the level brief is displayed
    private boolean levelBriefActive;

    public TouchPad touchPad;
    private Gui gui;
    public float timeCount;

    public boolean levelPassed;
    public int wheatHarvested;
    public int wheatToPassLevel;

    public HashMap<Double, Queue<Vector2>> sheepWaves;

    public PlayScreen(Semtb001MajorAssignment semtb001MajorAssignment, String currentLevel) {

        // Instantiate game and level objects
        game = semtb001MajorAssignment;
        batch = semtb001MajorAssignment.batch;
        this.currentLevel = currentLevel;
        isGameOverCreated = false;

        levelPassed = false;
        wheatHarvested = 0;
        wheatToPassLevel = Integer.parseInt(currentLevel.substring(7, 8)) * 2;

        // Setup game camera
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, Semtb001MajorAssignment.WORLD_WIDTH, Semtb001MajorAssignment.WORLD_HEIGHT);
        box2DDebugRenderer = new Box2DDebugRenderer();

        // Load map level, render the map, and create the game world
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapFiles/level" + currentLevel.substring(7, 8) + ".tmx");
        //map = mapLoader.load("mapFiles/level1.tmx");
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
        gameCamera.position.x = player.box2dBody.getPosition().x + 9;
        gameCamera.position.y = 23;

        // Initially, set the level brief to active so that the level brief is displayed at start up
        levelBriefActive = true;

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

        int numberOfWaves = 3;
        int totalSheep = box2dWorldCreator.getSheepPositions().size();
        double waveTimeIncrements = hud.getWorldTimer() / numberOfWaves;

        for (int i = numberOfWaves; i >= 0; i--) {
            Queue<Vector2> waveQueue = new LinkedList<>();

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

            sheepWaves.put(i * waveTimeIncrements, waveQueue);

        }

        // Set the hoe to be the active item in the player's hotbar
        Set<Item> itemSet = gui.items.keySet();
        for (Item i : itemSet) {
            if (i.getName() == "hoe") {
                i.setActive(true);
            }
        }

        System.out.println(sheepWaves.toString());

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
            player.currentSpeed = Player.Speed.STOP;
        }

        if (Gdx.input.isTouched()) {
            if (itemPressed == 0) {

                for (Item i : itemSet) {
                    if (i.getActive()) {
                        if (i.getName() == "hoe") {
                            if (getCell("grass").getTile() == tileSet.getTile(Semtb001MajorAssignment.GRASS)) {
                                player.resetStateTimer();
                                player.setCurrentState(Player.State.HOE);
                                player.playItemSound();

                                Boolean wetSoil = false;
                                for(Vector2 v : getSurroundingPositionss3x3(getPlayerPos())){
                                    Rectangle r = new Rectangle(v.x, v.y, 1,1 );
                                    if(getCell("water", r).getTile().getId() == Semtb001MajorAssignment.WATER){
                                        wetSoil = true;
                                    }
                                }

                                if (wetSoil) {
                                    getCell("grass").setTile(tileSet.getTile(Semtb001MajorAssignment.WET_SOIL));

                                }else{
                                    getCell("grass").setTile(tileSet.getTile(Semtb001MajorAssignment.DRY_SOIL));
                                }
                            }
                            box2dWorldCreator.harvestWheat();
                        }

                        if (i.getName() == "seeds") {

                            if (timeCount >= 1) {

                                player.resetStateTimer();

                                if (getCell("grass").getTile() == tileSet.getTile(Semtb001MajorAssignment.DRY_SOIL) ||
                                        getCell("grass").getTile() == tileSet.getTile(Semtb001MajorAssignment.WET_SOIL)) {

                                    //stop multiple objects being created for the same bounds
                                    boolean create = true;
                                    for (Wheat w : box2dWorldCreator.wheat) {
                                        if ((w.rectangle.x == (int) (player.box2dBody.getPosition().x * Semtb001MajorAssignment.PPM / 32)) &&
                                                (w.rectangle.y == (int) (player.box2dBody.getPosition().y * Semtb001MajorAssignment.PPM / 32))) {

                                            create = false;
                                        }
                                    }


                                    //create seeds if there are seeds in inventory and seeds are already not growing
                                    if (create) {
                                        if (player.getInventory().getItem("seeds") > 0) {
                                            player.setCurrentState(Player.State.SEEDS);
                                            player.playItemSound();
                                            box2dWorldCreator.createWheat();
                                            player.getInventory().removeItem("seeds", 1);
                                        }
                                    }
                                } else {
                                    //System.out.println("NOT DIRT");
                                }

                            }
                            timeCount = 0;

                        }

                        if (i.getName() == "bucket") {

                            //prevents picking up water and placing it instantly;
                            if (timeCount >= 1) {
                                player.resetStateTimer();

                                // If the bucket is full of water
                                if (i.getHealth() == 100) {

                                    getCell("water").setTile(tileSet.getTile(Semtb001MajorAssignment.WATER));
                                    i.setHealth(0);

                                    for(Vector2 v : getSurroundingPositionss3x3(getPlayerPos())){
                                        Rectangle r = new Rectangle(v.x, v.y, 1,1 );
                                        if(getCell("grass", r).getTile().getId() == Semtb001MajorAssignment.DRY_SOIL){
                                            getCell("grass", r).setTile(tileSet.getTile(Semtb001MajorAssignment.WET_SOIL));
                                        }
                                    }

                                    player.setCurrentState(Player.State.WATER);
                                    player.playItemSound();


                                } else {

                                    // Boolean to track if a water sound should be played
                                    Boolean playWaterSound = false;

                                    // Pick up water from ocean
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
                            ((WateringCan) i).updateWater();
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

        // Draw Player and Enemies
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

        // Draw the game paused and game over screen overlay
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

            player.update(deltaTime);
            gui.update(deltaTime, player);
            hud.update(deltaTime);

            // Handle enemies (movement, sound, and animation frame depending on state)
            handleEnemies(deltaTime);

            for (Wheat wheat : box2dWorldCreator.wheat) {
                wheat.update(deltaTime);
                wheat.updateWater();
            }

            if (wheatHarvested >= wheatToPassLevel) {
                levelPassed = true;
            }

        } else {

            // If the game is paused or the level brief is displayed: pause all game sounds

        }
    }

    public void moveCamera() {
        gameCamera.position.x = player.box2dBody.getPosition().x;
        gameCamera.position.y = player.box2dBody.getPosition().y;
    }

    // Method to handle sheep spawns
    public void handleEnemies(float deltaTime) {

        for (Map.Entry<Double, Queue<Vector2>> kv : sheepWaves.entrySet()) {

            // If the world time is
            if (hud.getWorldTimer() <= kv.getKey()) {

                // Create the enemy
                for (Vector2 sheepPosition : kv.getValue()) {
                    Sheep newSheep = new Sheep(world, this, sheepPosition);
                    sheep.offer(newSheep);
                    kv.getValue().remove(sheepPosition);
                }

                sheepWaves.remove(kv);


            }

        }


        // If there are any ground enemies that are spawned into the map
        if (sheep.size() > 0) {
            for (Sheep s : sheep) {
                s.update(deltaTime);
            }
        }

    }

    // Method to sheep sprites
    public void drawSheep(float delta) {

        // Get all ground enemies that are spawned into the map
        for (Sheep sheep : sheep) {

            // Update the enemy (updates current animation frame, sound, and movement)
            sheep.update(delta);

            if (sheep.getHealth() > 0) {

                // Draw the current enemy animation frame
                game.batch.draw(sheep.currentFrame, sheep.box2dBody.getPosition().x - 1.75f, (float) (sheep.box2dBody.getPosition().y - 1.5f), 3.5f, 3.5f);
            }
        }
    }

    //https://stackoverflow.com/questions/42057796/move-the-player-only-in-45-steps-with-touchpad-in-libgdx
    public void movePlayer(float dx, float dy) {
        int direction = (int) Math.floor((Math.atan2(dy, dx) + Math.PI / 8) / (2 * Math.PI / 8));

        if (direction == 8) direction = 0;
        double angle = direction * (Math.PI / 4);
        player.setAngle(angle);

        //Set the player direction state
        if (angle == 0) {
            player.currentDirection = Player.Direction.E;
        } else if (angle == -0.7853981633974483) {
            player.currentDirection = Player.Direction.SE;
        } else if (angle == -1.5707963267948966) {
            player.currentDirection = Player.Direction.S;
        } else if (angle == -2.356194490192345) {
            player.currentDirection = Player.Direction.SW;
        } else if (angle == -3.141592653589793) {
            player.currentDirection = Player.Direction.W;
        } else if (angle == 2.356194490192345) {
            player.currentDirection = Player.Direction.NW;
        } else if (angle == 1.5707963267948966) {
            player.currentDirection = Player.Direction.N;
        } else if (angle == 0.7853981633974483) {
            player.currentDirection = Player.Direction.NE;
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
        double playerSpeed = 0;
        player.setPreviousSpeed();

        // Set the players speed
        if (angleSpeed > 0.9) {
            player.currentSpeed = Player.Speed.FAST;
            playerSpeed = 0.9;
        } else if (angleSpeed > 0.6) {
            player.currentSpeed = Player.Speed.MEDIUM;
            playerSpeed = 0.6;
        } else if (angleSpeed > 0.3) {
            player.currentSpeed = Player.Speed.SLOW;
            playerSpeed = 0.3;
        } else {
            player.currentSpeed = Player.Speed.STOP;
            playerSpeed = 0;
        }

        player.box2dBody.setLinearVelocity((float) (player.getX() + Math.cos(angle) * (player.maxSpeed * playerSpeed)),
                (float) (player.getY() + Math.sin(angle) * (player.maxSpeed * playerSpeed)));
        player.setCurrentState(Player.State.WALK);
    }

    // Method to draw the player
    public void drawPlayer() {

        /* This code draws the player in different positions due to the Box2D body shape changing
        size depending on the state of the player*/
        HashMap<String, Float> dimensions = player.getFrameDimensions();
        game.batch.draw(player.currentFrame, dimensions.get("x"), dimensions.get("y"), dimensions.get("width"), dimensions.get("height"));

    }

    // Method to get the cell at the players position from the provided layer name in the tmx file
    public TiledMapTileLayer.Cell getCell(String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        return layer.getCell((int) (player.box2dBody.getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int) (player.box2dBody.getPosition().y * Semtb001MajorAssignment.PPM / 32));
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
