package com.semtb001.major.assignement.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

        // Set the state timers to 0
        stateTimer = 0;

        // Setup the player current states (starts the game running)
        currentDirection = Direction.N;
        previousDirection = null;

        currentState = State.IDLE;

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


        // Set the starting animation frame to N
        currentFrame = (TextureRegion) Nidle.getKeyFrame(idleAnimationSpeed, false);


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

        updateItemAnimation(delta);

        // Set the current frame of the player depending on the player state (eg. running, jumping, etc.)
        currentFrame = getFramesFromAnimation(delta);

    }

    // Method to get the current player animation frame
    private TextureRegion getFramesFromAnimation(float delta) {

        // Store the current state as 'previous state'
        previousDirection = currentDirection;

        // Texture region that will be returned
        TextureRegion returnRegion = null;


        if (currentState == State.WALK && currentSpeed == 0.0) {
            currentState = State.IDLE;
        }

        // If the player state is "FAIL" return the fail animation frame
        if (currentDirection == Direction.N) {
            switch (currentState) {
                case IDLE:
                    returnRegion = (TextureRegion) Nidle.getKeyFrame(stateTimer, true);
                    break;
                case WALK:
                    returnRegion = (TextureRegion) Nwalk.getKeyFrame(stateTimer, true);
                    break;
                case HOE:
                    returnRegion = (TextureRegion) Nhoe.getKeyFrame(stateTimer, true);
                    break;
                case SEEDS:
                    returnRegion = (TextureRegion) Nseeds.getKeyFrame(stateTimer, true);
                    break;
                case BUCKET:
                    returnRegion = (TextureRegion) Nbucket.getKeyFrame(stateTimer, true);
                    break;
            }
        } else if (currentDirection == Direction.NE) {
            switch (currentState) {
                case IDLE:
                    returnRegion = (TextureRegion) NEidle.getKeyFrame(stateTimer, true);
                    break;
                case WALK:
                    returnRegion = (TextureRegion) NEwalk.getKeyFrame(stateTimer, true);
                    break;
                case HOE:
                    returnRegion = (TextureRegion) NEhoe.getKeyFrame(stateTimer, true);
                    break;
                case SEEDS:
                    returnRegion = (TextureRegion) NEseeds.getKeyFrame(stateTimer, true);
                    break;
                case BUCKET:
                    returnRegion = (TextureRegion) NEbucket.getKeyFrame(stateTimer, true);
                    break;
            }
        } else if (currentDirection == Direction.E) {
            switch (currentState) {
                case IDLE:
                    returnRegion = (TextureRegion) Eidle.getKeyFrame(stateTimer, true);
                    break;
                case WALK:
                    returnRegion = (TextureRegion) Ewalk.getKeyFrame(stateTimer, true);
                    break;
                case HOE:
                    returnRegion = (TextureRegion) Ehoe.getKeyFrame(stateTimer, true);
                    break;
                case SEEDS:
                    returnRegion = (TextureRegion) Eseeds.getKeyFrame(stateTimer, true);
                    break;
                case BUCKET:
                    returnRegion = (TextureRegion) Ebucket.getKeyFrame(stateTimer, true);
                    break;
            }
        } else if (currentDirection == Direction.SE) {
            switch (currentState) {
                case IDLE:
                    returnRegion = (TextureRegion) SEidle.getKeyFrame(stateTimer, true);
                    break;
                case WALK:
                    returnRegion = (TextureRegion) SEwalk.getKeyFrame(stateTimer, true);
                    break;
                case HOE:
                    returnRegion = (TextureRegion) SEhoe.getKeyFrame(stateTimer, true);
                    break;
                case SEEDS:
                    returnRegion = (TextureRegion) SEseeds.getKeyFrame(stateTimer, true);
                    break;
                case BUCKET:
                    returnRegion = (TextureRegion) SEbucket.getKeyFrame(stateTimer, true);
                    break;
            }
        } else if (currentDirection == Direction.S) {
            switch (currentState) {
                case IDLE:
                    returnRegion = (TextureRegion) Sidle.getKeyFrame(stateTimer, true);
                    break;
                case WALK:
                    returnRegion = (TextureRegion) Swalk.getKeyFrame(stateTimer, true);
                    break;
                case HOE:
                    returnRegion = (TextureRegion) Shoe.getKeyFrame(stateTimer, true);
                    break;
                case SEEDS:
                    returnRegion = (TextureRegion) Sseeds.getKeyFrame(stateTimer, true);
                    break;
                case BUCKET:
                    returnRegion = (TextureRegion) Sbucket.getKeyFrame(stateTimer, true);
                    break;
            }
        } else if (currentDirection == Direction.SW) {
            switch (currentState) {
                case IDLE:
                    returnRegion = (TextureRegion) SWidle.getKeyFrame(stateTimer, true);
                    break;
                case WALK:
                    returnRegion = (TextureRegion) SWwalk.getKeyFrame(stateTimer, true);
                    break;
                case HOE:
                    returnRegion = (TextureRegion) SWhoe.getKeyFrame(stateTimer, true);
                    break;
                case SEEDS:
                    returnRegion = (TextureRegion) SWseeds.getKeyFrame(stateTimer, true);
                    break;
                case BUCKET:
                    returnRegion = (TextureRegion) SWbucket.getKeyFrame(stateTimer, true);
                    break;
            }
        } else if (currentDirection == Direction.W) {
            switch (currentState) {
                case IDLE:
                    returnRegion = (TextureRegion) Widle.getKeyFrame(stateTimer, true);
                    break;
                case WALK:
                    returnRegion = (TextureRegion) Wwalk.getKeyFrame(stateTimer, true);
                    break;
                case HOE:
                    returnRegion = (TextureRegion) Whoe.getKeyFrame(stateTimer, true);
                    break;
                case SEEDS:
                    returnRegion = (TextureRegion) Wseeds.getKeyFrame(stateTimer, true);
                    break;
                case BUCKET:
                    returnRegion = (TextureRegion) Wbucket.getKeyFrame(stateTimer, true);
                    break;
            }
        } else if (currentDirection == Direction.NW) {
            switch (currentState) {
                case IDLE:
                    returnRegion = (TextureRegion) NWidle.getKeyFrame(stateTimer, true);
                    break;
                case WALK:
                    returnRegion = (TextureRegion) NWwalk.getKeyFrame(stateTimer, true);
                    break;
                case HOE:
                    returnRegion = (TextureRegion) NWhoe.getKeyFrame(stateTimer, true);
                    break;
                case SEEDS:
                    returnRegion = (TextureRegion) NWseeds.getKeyFrame(stateTimer, true);
                    break;
                case BUCKET:
                    returnRegion = (TextureRegion) NWbucket.getKeyFrame(stateTimer, true);
                    break;
            }
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

    private float getDurationFromSpeed(double currentSpeed) {

        float returnSpeed = 0;

        if (currentSpeed > 0.9) {
            returnSpeed = 0.06f;
        } else if (currentSpeed > 0.06) {
            returnSpeed = 0.1f;
        } else if (currentSpeed > 0.3) {
            returnSpeed = 0.125f;
        } else {
            returnSpeed = 0.15f;
        }

        return returnSpeed;
    }

    public void updateItemAnimation(float delta) {
        if (currentState == State.HOE) {


        }

        switch (currentState) {
            case HOE:
                if (animationTimeDuration > 0) {
                    System.out.println(animationTimeDuration);
                    animationTimeDuration -= delta;
                } else {
                    currentState = State.IDLE;
                    animationTimeDuration = hoeAnimationSpeed * 13;
                }
                animationTimeCounter = 0;

            case BUCKET:
                if (animationTimeDuration > 0) {
                    System.out.println(animationTimeDuration);
                    animationTimeDuration -= delta;
                } else {
                    currentState = State.IDLE;
                    animationTimeDuration = bucketAnimationSpeed * 18;
                }
                animationTimeCounter = 0;

            case SEEDS:
                if (animationTimeDuration > 0) {
                    System.out.println(animationTimeDuration);
                    animationTimeDuration -= delta;
                } else {
                    currentState = State.IDLE;
                    animationTimeDuration = seedsAnimationSpeed * 11;
                }
                animationTimeCounter = 0;
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
