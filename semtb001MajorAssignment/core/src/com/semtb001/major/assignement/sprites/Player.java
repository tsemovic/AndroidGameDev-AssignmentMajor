package com.semtb001.major.assignement.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
import java.util.Random;


// Class for the player
public class Player extends Sprite {

    // Player world and playscreen objects
    private World world;
    private PlayScreen playScreen;

    // Player states
    public enum State {N, NE, E, SE, S, SW, W, NW}
    public State currentState;
    public State previousState;

    // State timers for the player states
    private float stateTimer;

    // Player animations
    private Animation N;
    private Animation NE;
    private Animation E;
    private Animation SE;
    private Animation S;
    private Animation SW;
    private Animation W;
    private Animation NW;

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

    private Inventory inventory;

    public Player(World world, PlayScreen playScreen) {

        // Instantiate world and playscreen
        this.world = world;
        this.playScreen = playScreen;

        // Set the state timers to 0
        stateTimer = 0;

        // Setup the player current states (starts the game running)
        currentState = State.N;
        previousState = null;

        inventory = new Inventory();

        // Define the player (Box2d)
        definePlayer();

        // Temporary array to hold animation frames
        Array<TextureRegion> tempFrames = new Array<TextureRegion>();

        for (int i = 0; i <= 4; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("N"), i * 128, 0, 128, 128));
        }
        N = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 4; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("NE"), i * 128, 0, 128, 128));
        }
        NE = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 4; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("E"), i * 128, 0, 128, 128));
        }
        E = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 4; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("SE"), i * 128, 0, 128, 128));
        }
        SE = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 4; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("S"), i * 128, 0, 128, 128));
        }
        S = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 4; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("SW"), i * 128, 0, 128, 128));
        }
        SW = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 4; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("W"), i * 128, 0, 128, 128));
        }
        W = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 4; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("NW"), i * 128, 0, 128, 128));
        }
        NW = new Animation(0.1f, tempFrames);
        tempFrames.clear();


        // Set the starting animation frame to N
        currentFrame = (TextureRegion) N.getKeyFrame(0, false);


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
        fixtureDef.filter.maskBits = Semtb001MajorAssignment.WORLD | Semtb001MajorAssignment.ENEMY | Semtb001MajorAssignment.COIN;

        // Setup the body as a dynamic body (ability to move)
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Set the player starting position
        bodyDef.position.set(40, 90);

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

        // Set the current frame of the player depending on the player state (eg. running, jumping, etc.)
        currentFrame = getFramesFromAnimation(delta);

    }

    // Method to get the current player animation frame
    private TextureRegion getFramesFromAnimation(float delta) {

        // Store the current state as 'previous state'
        previousState = currentState;

        // Update current player state
        getState(delta);

        // Texture region that will be returned
        TextureRegion returnRegion = null;

        // If the player state is "FAIL" return the fail animation frame
        if (currentState == State.N) {
            returnRegion = (TextureRegion) N.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0){
                returnRegion = (TextureRegion) N.getKeyFrame(2, true);
            }
        }else if(currentState == State.NE){
            returnRegion = (TextureRegion) NE.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0){
                returnRegion = (TextureRegion) NE.getKeyFrame(2, true);
            }
        }else if(currentState == State.E){
            returnRegion = (TextureRegion) E.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0){
                returnRegion = (TextureRegion) E.getKeyFrame(2, true);
            }
        }else if(currentState == State.SE){
            returnRegion = (TextureRegion) SE.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0){
                returnRegion = (TextureRegion) SE.getKeyFrame(2, true);
            }
        }else if(currentState == State.S){
            returnRegion = (TextureRegion) S.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0){
                returnRegion = (TextureRegion) S.getKeyFrame(2, true);
            }
        }else if(currentState == State.SW){
            returnRegion = (TextureRegion) SW.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0){
                returnRegion = (TextureRegion) SW.getKeyFrame(2, true);
            }
        }else if(currentState == State.W){
            returnRegion = (TextureRegion) W.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0){
                returnRegion = (TextureRegion) W.getKeyFrame(2, true);
            }
        }else if(currentState == State.NW){
            returnRegion = (TextureRegion) NW.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0){
                returnRegion = (TextureRegion) NW.getKeyFrame(2, true);
            }
        }
//        else if (currentState == State.JUMP_START) {
//            returnRegion = (TextureRegion) jumpStart.getKeyFrame(stateTimer, false);
//
//            // If the player state is "JUMP_END" return the JUMP_END animation frame
//        } else if (currentState == State.JUMP_END) {
//            returnRegion = (TextureRegion) jumpEnd.getKeyFrame(stateTimer, false);
//
//            // If the player state is "SLIDE_START" return the SLIDE_START animation frame
//        } else if (currentState == State.SLIDE_START) {
//            returnRegion = (TextureRegion) slideStart.getKeyFrame(stateTimer, false);
//
//            // If the player state is "SLIDE_END" return the SLIDE_END animation frame
//        } else if (currentState == State.SLIDE_END) {
//            returnRegion = (TextureRegion) slideEnd.getKeyFrame(stateTimer, false);
//        } else {
//
//            // Else, return the running animation frame
//            returnRegion = (TextureRegion) running.getKeyFrame(stateTimer, true);
//        }

        // If the current state and previous state aren't the same: reset the state timer
        if (currentState != previousState) {
            stateTimer = 0;

            // If the current state and previous state are the same: increase the state timer
        } else {
            stateTimer += delta;
        }

        // Return the frame
        return returnRegion;
    }

    // Method to update the player state
    public void getState(float delta) {

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

}
