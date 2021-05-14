package com.semtb001.major.assignement.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.scenes.Inventory;
import com.semtb001.major.assignement.screens.PlayScreen;
import com.semtb001.major.assignement.tools.Assets;

import java.util.HashMap;


// Class for the player
public class Player extends Sprite {

    // Player world and playscreen objects
    private World world;
    private PlayScreen playScreen;

    // Player directions
    public enum Direction {N, NE, E, SE, S, SW, W, NW}

    public Direction currentDirection;
    public Direction previousDirection;

    // Player states
    public enum State {IDLE, WALK, HOE, SEEDS, BUCKET}

    public State currentState;
    public State previousState;


    // State timers for the player states
    private float stateTimer;

    // Player walk animations
    private Animation Nwalk;
    private Animation NEwalk;
    private Animation Ewalk;
    private Animation SEwalk;
    private Animation Swalk;
    private Animation SWwalk;
    private Animation Wwalk;
    private Animation NWwalk;

    // Player idle animations
    private Animation Nidle;
    private Animation NEidle;
    private Animation Eidle;
    private Animation SEidle;
    private Animation Sidle;
    private Animation SWidle;
    private Animation Widle;
    private Animation NWidle;

    // Player hoe animations
    private Animation Nhoe;
    private Animation NEhoe;
    private Animation Ehoe;
    private Animation SEhoe;
    private Animation Shoe;
    private Animation SWhoe;
    private Animation Whoe;
    private Animation NWhoe;

    // Player seeds animations
    private Animation Nseeds;
    private Animation NEseeds;
    private Animation Eseeds;
    private Animation SEseeds;
    private Animation Sseeds;
    private Animation SWseeds;
    private Animation Wseeds;
    private Animation NWseeds;

    // Player bucket animations
    private Animation Nbucket;
    private Animation NEbucket;
    private Animation Ebucket;
    private Animation SEbucket;
    private Animation Sbucket;
    private Animation SWbucket;
    private Animation Wbucket;
    private Animation NWbucket;

    // Player item sounds
    private Sound hoeSound;

    float animationTimeCounter;
    float animationTimeDuration;

    // Player's current animation frame
    public TextureRegion currentFrame;

    // Box2D objects
    public Body box2dBody;
    private FixtureDef fixtureDef;
    private PolygonShape shape;
    private Rectangle rect;
    private BodyDef bodyDef;

    private double angle;
    public float maxSpeed = 2f;
    public double currentSpeed = 2f;

    private float walkAnimationSpeed;
    private float idleAnimationSpeed;
    private float hoeAnimationSpeed;
    private float seedsAnimationSpeed;
    private float bucketAnimationSpeed;


    private Inventory inventory;

    public Player(World world, PlayScreen playScreen) {

        // Instantiate world and playscreen
        this.world = world;
        this.playScreen = playScreen;

        // Set the state timer to 0
        stateTimer = 0;

        // Setup the player current states (starts the game running)
        currentDirection = Direction.N;
        previousDirection = null;

        currentState = State.IDLE;
        previousState = null;

        animationTimeCounter = 0;
        animationTimeDuration = 0.07f * 13;

        inventory = new Inventory();

        // Define the player (Box2d)
        definePlayer();

        walkAnimationSpeed = 0.07f;
        idleAnimationSpeed = 0.3f;
        hoeAnimationSpeed = 0.07f;
        seedsAnimationSpeed = 0.07f;
        bucketAnimationSpeed = 0.07f;

        // Temporary array to hold animation frames
        Array<TextureRegion> tempFrames = new Array<TextureRegion>();

        // Player walk Animations
        for (int i = 0; i <= 14; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNwalk"), i * 99, 0, 99, 137));
        }
        Nwalk = new Animation(walkAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 14; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNEwalk"), i * 105, 0, 105, 135));
        }
        NEwalk = new Animation(walkAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 14; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerEwalk"), i * 109, 0, 109, 124));
        }
        Ewalk = new Animation(walkAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 14; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSEwalk"), i * 107, 0, 107, 133));
        }
        SEwalk = new Animation(walkAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 14; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSwalk"), i * 101, 0, 101, 136));
        }
        Swalk = new Animation(walkAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 14; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSWwalk"), i * 108, 0, 108, 133));
        }
        SWwalk = new Animation(walkAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 14; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerWwalk"), i * 114, 0, 114, 124));
        }
        Wwalk = new Animation(walkAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 14; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNWwalk"), i * 109, 0, 109, 134));
        }
        NWwalk = new Animation(walkAnimationSpeed, tempFrames);
        tempFrames.clear();

        // Player idle Animations
        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNidle"), i * 99, 0, 99, 123));
        }
        Nidle = new Animation(idleAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNEidle"), i * 95, 0, 95, 125));
        }
        NEidle = new Animation(idleAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerEidle"), i * 91, 0, 91, 127));
        }
        Eidle = new Animation(idleAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSEidle"), i * 95, 0, 95, 128));
        }
        SEidle = new Animation(idleAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSidle"), i * 100, 0, 100, 127));
        }
        Sidle = new Animation(idleAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSWidle"), i * 97, 0, 97, 128));
        }
        SWidle = new Animation(idleAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerWidle"), i * 92, 0, 92, 126));
        }
        Widle = new Animation(idleAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNWidle"), i * 97, 0, 97, 125));
        }
        NWidle = new Animation(idleAnimationSpeed, tempFrames);
        tempFrames.clear();

        // Player hoe Animations
        for (int i = 0; i <= 12; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNhoe"), i * 113, 0, 113, 181));
        }
        Nhoe = new Animation(hoeAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 12; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNEhoe"), i * 179, 0, 179, 161));
        }
        NEhoe = new Animation(hoeAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 12; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerEhoe"), i * 218, 0, 218, 155));
        }
        Ehoe = new Animation(hoeAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 12; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSEhoe"), i * 196, 0, 196, 187));
        }
        SEhoe = new Animation(hoeAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 12; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerShoe"), i * 141, 0, 141, 209));
        }
        Shoe = new Animation(hoeAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 12; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSWhoe"), i * 166, 0, 166, 200));
        }
        SWhoe = new Animation(hoeAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 12; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerWhoe"), i * 170, 0, 170, 180));
        }
        Whoe = new Animation(hoeAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 12; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNWhoe"), i * 150, 0, 150, 185));
        }
        NWhoe = new Animation(hoeAnimationSpeed, tempFrames);
        tempFrames.clear();

        // Player seeds Animations
        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNseeds"), i * 107, 0, 107, 125));
        }
        Nseeds = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNEseeds"), i * 128, 0, 128, 126));
        }
        NEseeds = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerEseeds"), i * 147, 0, 147, 127));
        }
        Eseeds = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSEseeds"), i * 128, 0, 128, 146));
        }
        SEseeds = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSseeds"), i * 105, 0, 105, 158));
        }
        Sseeds = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSWseeds"), i * 100, 0, 100, 146));
        }
        SWseeds = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerWseeds"), i * 100, 0, 100, 126));
        }
        Wseeds = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNWseeds"), i * 104, 0, 104, 126));
        }
        NWseeds = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        // Player seeds Animations
        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNbucket"), i * 119, 0, 119, 125));
        }
        Nbucket = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNEbucket"), i * 146, 0, 146, 126));
        }
        NEbucket = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerEbucket"), i * 150, 0, 150, 142));
        }
        Ebucket = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSEbucket"), i * 141, 0, 141, 157));
        }
        SEbucket = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSbucket"), i * 109, 0, 109, 160));
        }
        Sbucket = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerSWbucket"), i * 116, 0, 116, 154));
        }
        SWbucket = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerWbucket"), i * 121, 0, 121, 154));
        }
        Wbucket = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 10; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("playerNWbucket"), i * 116, 0, 116, 126));
        }
        NWbucket = new Animation(seedsAnimationSpeed, tempFrames);
        tempFrames.clear();

        // Set the starting animation frame to N
        currentFrame = (TextureRegion) Nidle.getKeyFrame(idleAnimationSpeed, false);

        hoeSound = Semtb001MajorAssignment.assetManager.manager.get(Assets.hoeSound);



    }

    // Method to define the player in Box2D
    public void definePlayer() {

        // Instantiate Box2D objects
        bodyDef = new BodyDef();
        rect = new Rectangle();
        shape = new PolygonShape();
        fixtureDef = new FixtureDef();

        // Set the player category to 'PLAYER' and maskBits (can collide with) to "WORLD", "ENEMY", and "COIN"
        fixtureDef.filter.categoryBits = Semtb001MajorAssignment.PLAYER;
        fixtureDef.filter.maskBits = Semtb001MajorAssignment.WORLD | Semtb001MajorAssignment.SHEEP;

        // Setup the body as a dynamic body (ability to move)
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Set the player starting position
        bodyDef.position.set(75, 75);

        // Create the body in the world
        box2dBody = world.createBody(bodyDef);

        // Set the shape of the body to a circle

        CircleShape cir = new CircleShape();
        cir.setRadius(0.3f);
        fixtureDef.shape = cir;

        // Add the fixture to the body
        box2dBody.createFixture(fixtureDef).setUserData(this);
    }

    // Method called to update the player (sounds, animation frame, and states)
    public void update(float delta) {

        //updateItemAnimation(delta);

        // Set the current frame of the player depending on the player state (eg. running, jumping, etc.)
        currentFrame = getFramesFromAnimation(delta);

    }

    // Method to get the current player animation frame
    private TextureRegion getFramesFromAnimation(float delta) {

        // Store the current state as 'previous state'
        previousDirection = currentDirection;
        previousState = currentState;

        // Texture region that will be returned
        TextureRegion returnRegion = null;


        if (currentState == State.WALK && currentSpeed == 0.0) {
            currentState = State.IDLE;
        }

        // Embedded switch statement to set the return region for each direction within each state
        switch(currentDirection){
            case N:
                switch (currentState) {
                    case IDLE:
                        returnRegion = (TextureRegion) Nidle.getKeyFrame(stateTimer, true);
                        break;
                    case WALK:
                        returnRegion = (TextureRegion) Nwalk.getKeyFrame(stateTimer, true);
                        break;
                    case HOE:
                        returnRegion = (TextureRegion) Nhoe.getKeyFrame(stateTimer, false);
                        if(Nhoe.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case SEEDS:
                        returnRegion = (TextureRegion) Nseeds.getKeyFrame(stateTimer, false);
                        if(Nseeds.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case BUCKET:
                        returnRegion = (TextureRegion) Nbucket.getKeyFrame(stateTimer, false);
                        if(Nbucket.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                }
                break;
            case NE:
                switch (currentState) {
                    case IDLE:
                        returnRegion = (TextureRegion) NEidle.getKeyFrame(stateTimer, true);
                        break;
                    case WALK:
                        returnRegion = (TextureRegion) NEwalk.getKeyFrame(stateTimer, true);
                        break;
                    case HOE:
                        returnRegion = (TextureRegion) NEhoe.getKeyFrame(stateTimer, false);
                        if(NEhoe.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case SEEDS:
                        returnRegion = (TextureRegion) NEseeds.getKeyFrame(stateTimer, false);
                        if(NEseeds.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case BUCKET:
                        returnRegion = (TextureRegion) NEbucket.getKeyFrame(stateTimer, false);
                        if(NEbucket.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                }
                break;
            case E:
                switch (currentState) {
                    case IDLE:
                        returnRegion = (TextureRegion) Eidle.getKeyFrame(stateTimer, true);
                        break;
                    case WALK:
                        returnRegion = (TextureRegion) Ewalk.getKeyFrame(stateTimer, true);
                        break;
                    case HOE:
                        returnRegion = (TextureRegion) Ehoe.getKeyFrame(stateTimer, false);
                        if(Ehoe.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case SEEDS:
                        returnRegion = (TextureRegion) Eseeds.getKeyFrame(stateTimer, false);
                        if(Nseeds.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case BUCKET:
                        returnRegion = (TextureRegion) Ebucket.getKeyFrame(stateTimer, false);
                        if(Nbucket.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                }
                break;
            case SE:
                switch (currentState) {
                    case IDLE:
                        returnRegion = (TextureRegion) SEidle.getKeyFrame(stateTimer, true);
                        break;
                    case WALK:
                        returnRegion = (TextureRegion) SEwalk.getKeyFrame(stateTimer, true);
                        break;
                    case HOE:
                        returnRegion = (TextureRegion) SEhoe.getKeyFrame(stateTimer, false);
                        if(SEhoe.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case SEEDS:
                        returnRegion = (TextureRegion) SEseeds.getKeyFrame(stateTimer, false);
                        if(SEseeds.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case BUCKET:
                        returnRegion = (TextureRegion) SEbucket.getKeyFrame(stateTimer, false);
                        if(SEbucket.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                }
                break;
            case S:
                switch (currentState) {
                    case IDLE:
                        returnRegion = (TextureRegion) Sidle.getKeyFrame(stateTimer, true);
                        break;
                    case WALK:
                        returnRegion = (TextureRegion) Swalk.getKeyFrame(stateTimer, true);
                        break;
                    case HOE:
                        returnRegion = (TextureRegion) Shoe.getKeyFrame(stateTimer, false);
                        if(Shoe.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case SEEDS:
                        returnRegion = (TextureRegion) Sseeds.getKeyFrame(stateTimer, false);
                        if(Sseeds.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case BUCKET:
                        returnRegion = (TextureRegion) Sbucket.getKeyFrame(stateTimer, false);
                        if(Sbucket.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                }
                break;
            case SW:
                switch (currentState) {
                    case IDLE:
                        returnRegion = (TextureRegion) SWidle.getKeyFrame(stateTimer, true);
                        break;
                    case WALK:
                        returnRegion = (TextureRegion) SWwalk.getKeyFrame(stateTimer, true);
                        break;
                    case HOE:
                        returnRegion = (TextureRegion) SWhoe.getKeyFrame(stateTimer, false);
                        if(SWhoe.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case SEEDS:
                        returnRegion = (TextureRegion) SWseeds.getKeyFrame(stateTimer, false);
                        if(SWseeds.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case BUCKET:
                        returnRegion = (TextureRegion) SWbucket.getKeyFrame(stateTimer, false);
                        if(SWbucket.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                }
                break;
            case W:
                switch (currentState) {
                    case IDLE:
                        returnRegion = (TextureRegion) Widle.getKeyFrame(stateTimer, true);
                        break;
                    case WALK:
                        returnRegion = (TextureRegion) Wwalk.getKeyFrame(stateTimer, true);
                        break;
                    case HOE:
                        returnRegion = (TextureRegion) Whoe.getKeyFrame(stateTimer, false);
                        if(Whoe.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case SEEDS:
                        returnRegion = (TextureRegion) Wseeds.getKeyFrame(stateTimer, false);
                        if(Wseeds.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case BUCKET:
                        returnRegion = (TextureRegion) Wbucket.getKeyFrame(stateTimer, false);
                        if(Wbucket.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                }
                break;
            case NW:
                switch (currentState) {
                    case IDLE:
                        returnRegion = (TextureRegion) NWidle.getKeyFrame(stateTimer, true);
                        break;
                    case WALK:
                        returnRegion = (TextureRegion) NWwalk.getKeyFrame(stateTimer, true);
                        break;
                    case HOE:
                        returnRegion = (TextureRegion) NWhoe.getKeyFrame(stateTimer, false);
                        if(NWhoe.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case SEEDS:
                        returnRegion = (TextureRegion) NWseeds.getKeyFrame(stateTimer, false);
                        if(NWseeds.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                    case BUCKET:
                        returnRegion = (TextureRegion) NWbucket.getKeyFrame(stateTimer, false);
                        if(NWbucket.isAnimationFinished(stateTimer)){
                            currentState = State.IDLE;
                        }
                        break;
                }
                break;
        }

        // If the current state and previous state aren't the same: reset the state timer
        if (currentDirection != previousDirection) {
            stateTimer = 0;
            // If the current state and previous state are the same: increase the state timer
        } else {
            stateTimer += delta;
        }

        // Return the frame
        return returnRegion;
    }

    public void resetStateTimer(){
        stateTimer = 0;
    }

    // Method to get the frame size and position to be drawn for the player
    // (All player images are unfortunately different dimensions so scaling is required for each frame)
    public HashMap<String, Float> getFrameDimensions() {

        // HashMap to store the x, y, width and height dimensions
        HashMap<String, Float> dimensions = new HashMap<>();

        // Base scale of the images
        float baseSize = 1;

        // Instantiate the with, height, x and y values
        float height = 0;
        float width = 0;
        float x = 0;
        float y = 0;

        // Embedded switch statements to go though all of the player directions within all of the player states
        switch (currentState) {

            // If the player state is 'HOE'
            case HOE:
                switch (currentDirection) {
                    case N:
                        x = box2dBody.getPosition().x - 1.5f;
                        y = box2dBody.getPosition().y - 0.5f;
                        height = baseSize * 2.5f;
                        width = baseSize * 2.5f;
                        break;
                    case NE:
                        x = box2dBody.getPosition().x - 2f;
                        y = box2dBody.getPosition().y - 0.5f;
                        height = baseSize * 2.5f;
                        width = baseSize * 3.5f;
                        break;
                    case E:
                        x = box2dBody.getPosition().x - 2.75f;
                        y = box2dBody.getPosition().y - 0.5f;
                        height = baseSize * 2.75f;
                        width = baseSize * 4.5f;
                        break;
                    case SE:
                        x = box2dBody.getPosition().x - 3.25f;
                        y = box2dBody.getPosition().y - 0.5f;
                        height = baseSize * 2.75f;
                        width = baseSize * 4.5f;
                        break;
                    case S:
                        x = box2dBody.getPosition().x - 3.25f;
                        y = box2dBody.getPosition().y - 0.75f;
                        height = baseSize * 3f;
                        width = baseSize * 3.5f;
                        break;
                    case SW:
                        x = box2dBody.getPosition().x - 3f;
                        y = box2dBody.getPosition().y - 0.5f;
                        height = baseSize * 2.75f;
                        width = baseSize * 3.75f;
                        break;
                    case W:
                        x = box2dBody.getPosition().x - 2.75f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2.75f;
                        width = baseSize * 4f;
                        break;
                    case NW:
                        x = box2dBody.getPosition().x - 2f;
                        y = box2dBody.getPosition().y - 0.5f;
                        height = baseSize * 2.5f;
                        width = baseSize * 3.5f;
                        break;
                }
                break;

            // If the player state is 'SEEDS'
            case SEEDS:
                switch (currentDirection) {
                    case N:
                        x = box2dBody.getPosition().x - 1.75f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2.4f;
                        break;
                    case NE:
                        x = box2dBody.getPosition().x - 1.5f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2.75f;
                        break;
                    case E:
                        x = box2dBody.getPosition().x - 1.675f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 3.25f;
                        break;
                    case SE:
                        x = box2dBody.getPosition().x - 1.42f;
                        y = box2dBody.getPosition().y - 0.565f;
                        height = baseSize * 2.27f;
                        width = baseSize * 2.67f;
                        break;
                    case S:
                        x = box2dBody.getPosition().x - 1.72f;
                        y = box2dBody.getPosition().y - 0.785f;
                        height = baseSize * 2.5f;
                        width = baseSize * 2.35f;
                        break;
                    case SW:
                        x = box2dBody.getPosition().x - 1.55f;
                        y = box2dBody.getPosition().y - 0.55f;
                        height = baseSize * 2.25f;
                        width = baseSize * 2.075f;
                        break;
                    case W:
                        x = box2dBody.getPosition().x - 1.85f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2.125f;
                        break;
                    case NW:
                        x = box2dBody.getPosition().x - 1.575f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2.175f;
                        break;
                }
                break;

            // If the player state is 'BUCKET'
            case BUCKET:
                switch (currentDirection) {
                    case N:
                        x = box2dBody.getPosition().x - 1.8f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2.7f;
                        break;
                    case NE:
                        x = box2dBody.getPosition().x - 1.5f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 3.15f;
                        break;
                    case E:
                        x = box2dBody.getPosition().x - 1.645f;
                        y = box2dBody.getPosition().y - 0.5f;
                        height = baseSize * 2.25f;
                        width = baseSize * 3.25f;
                        break;
                    case SE:
                        x = box2dBody.getPosition().x - 1.475f;
                        y = box2dBody.getPosition().y - 0.75f;
                        height = baseSize * 2.5f;
                        width = baseSize * 3f;
                        break;
                    case S:
                        x = box2dBody.getPosition().x - 1.75f;
                        y = box2dBody.getPosition().y - 0.825f;
                        height = baseSize * 2.55f;
                        width = baseSize * 2.45f;
                        break;
                    case SW:
                        x = box2dBody.getPosition().x - 1.855f;
                        y = box2dBody.getPosition().y - 0.73f;
                        height = baseSize * 2.5f;
                        width = baseSize * 2.4f;
                        break;
                    case W:
                        x = box2dBody.getPosition().x - 2.46f;
                        y = box2dBody.getPosition().y - 0.7f;
                        height = baseSize * 2.45f;
                        width = baseSize * 2.7f;
                        break;
                    case NW:
                        x = box2dBody.getPosition().x - 1.9f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2.4f;
                        break;
                }
                break;

            // If the player state is 'IDLE'
            case IDLE:
                switch (currentDirection) {
                    case N:
                        x = box2dBody.getPosition().x - 1.75f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2.25f;
                        break;
                    case NE:
                        x = box2dBody.getPosition().x - 1.5f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2f;
                        break;
                    case E:
                        x = box2dBody.getPosition().x - 1.75f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2f;
                        break;
                    case SE:
                        x = box2dBody.getPosition().x - 1.5f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2f;
                        break;
                    case S:
                        x = box2dBody.getPosition().x - 1.75f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2.25f;
                        break;
                    case SW:
                        x = box2dBody.getPosition().x - 1.5f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2f;
                        break;
                    case W:
                        x = box2dBody.getPosition().x - 1.75f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2f;
                        break;
                    case NW:
                        x = box2dBody.getPosition().x - 1.5f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2f;
                        break;
                }
                break;

            // If the player state is 'WALK'
            case WALK:
                switch (currentDirection) {
                    case N:
                        x = box2dBody.getPosition().x - 1.75f;
                        y = box2dBody.getPosition().y - 0.5f;
                        height = baseSize * 2.25f;
                        width = baseSize * 2.25f;
                        break;
                    case NE:
                        x = box2dBody.getPosition().x - 1.5f;
                        y = box2dBody.getPosition().y - 0.5f;
                        height = baseSize * 2.25f;
                        width = baseSize * 2.25f;
                        break;
                    case E:
                        x = box2dBody.getPosition().x - 1.5f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2f;
                        width = baseSize * 2.25f;
                        break;
                    case SE:
                        x = box2dBody.getPosition().x - 1.5f;
                        y = box2dBody.getPosition().y - 0.375f;
                        height = baseSize * 2.15f;
                        width = baseSize * 2.25f;
                        break;
                    case S:
                        x = box2dBody.getPosition().x - 1.7f;
                        y = box2dBody.getPosition().y - 0.375f;
                        height = baseSize * 2.15f;
                        width = baseSize * 2.25f;
                        break;
                    case SW:
                        x = box2dBody.getPosition().x - 1.475f;
                        y = box2dBody.getPosition().y - 0.275f;
                        height = baseSize * 2.075f;
                        width = baseSize * 2.3f;
                        break;
                    case W:
                        x = box2dBody.getPosition().x - 1.6f;
                        y = box2dBody.getPosition().y - 0.25f;
                        height = baseSize * 2.075f;
                        width = baseSize * 2.375f;
                        break;
                    case NW:
                        x = box2dBody.getPosition().x - 1.5f;
                        y = box2dBody.getPosition().y - 0.5f;
                        height = baseSize * 2.25f;
                        width = baseSize * 2.25f;
                        break;
                }
                break;

            // Default (just in case)
            default:
                x = box2dBody.getPosition().x - 1.5f;
                y = box2dBody.getPosition().y - 0.25f;
                height = 2f;
                width = 2f;
        }

        // Add the dimensions to the hashMap
        dimensions.put("x", x);
        dimensions.put("y", y);
        dimensions.put("height", height);
        dimensions.put("width", width);

        // Return the dimensions hashmap
        return dimensions;

    }



    public void playItemSound() {

        switch (currentState) {
            case HOE:
                hoeSound.play();
                break;
            case BUCKET:

                break;
            case SEEDS:

                break;
        }
    }

    public void setAngle(double a) {
        angle = a;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void addInventory(String item, int count) {
        inventory.addItem(item, count);
    }

    public void setCurrentState(State state) {
        currentState = state;
    }

}
