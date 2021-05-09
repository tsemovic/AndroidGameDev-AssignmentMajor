package com.semtb001.major.assignement.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.screens.PlayScreen;
import com.semtb001.major.assignement.tools.Assets;

import java.util.Random;


// Class for the player
public class Player extends Sprite {

    // Player world and playscreen objects
    private World world;
    private com.semtb001.major.assignement.screens.PlayScreen playScreen;

    // Player states
    public enum State {RUN, JUMP_START, JUMP_END, SLIDE_START, SLIDE_END, FAIL}
    private State currentState;
    public State previousState;

    // Variables to determine if the player is dead and the game is over
    public boolean playerIsDead;
    public boolean gameOver;

    // State timers for the player states
    private float stateTimer;
    private double slideStartTimer;
    private double slideEndTimer;

    // Player animations
    private Animation running;
    private Animation jumpStart;
    private Animation jumpEnd;
    private Animation slideStart;
    private Animation slideEnd;
    private Animation fail;

    // Player's current animation frame
    public TextureRegion currentFrame;

    // Box2D objects
    public Body box2dBody;
    private FixtureDef fixtureDef;
    private PolygonShape shape;
    private Rectangle rect;
    private BodyDef bodyDef;

    // Player sounds
    private Sound jumpSound1;
    private Sound jumpSound2;
    private Sound jumpSound3;
    private Sound slideSound1;
    private Sound slideSound2;
    private Sound slideSound3;
    private Sound failSound;

    public Player(World world, PlayScreen playScreen) {

        // Instantiate world and playscreen
        this.world = world;
        this.playScreen = playScreen;

        // Set the state timers to 0
        stateTimer = 0;
        slideStartTimer = 0;
        slideEndTimer = 0;

        // Set the game over to false and playerIsDead to false
        gameOver = false;
        playerIsDead = false;

        // Setup the player current states (starts the game running)
        currentState = State.RUN;
        previousState = null;

        // Instantiate the player slide sounds
        slideSound1 = Semtb001MajorAssignment.assetManager.manager.get(com.semtb001.major.assignement.tools.Assets.slide1);
        slideSound2 = Semtb001MajorAssignment.assetManager.manager.get(com.semtb001.major.assignement.tools.Assets.slide2);
        slideSound3 = Semtb001MajorAssignment.assetManager.manager.get(com.semtb001.major.assignement.tools.Assets.slide3);

        // Instantiate the player jump sounds
        jumpSound1 = Semtb001MajorAssignment.assetManager.manager.get(com.semtb001.major.assignement.tools.Assets.jump1);
        jumpSound2 = Semtb001MajorAssignment.assetManager.manager.get(com.semtb001.major.assignement.tools.Assets.jump2);
        jumpSound3 = Semtb001MajorAssignment.assetManager.manager.get(com.semtb001.major.assignement.tools.Assets.jump3);

        // Instantiate the player fail sound
        failSound = Semtb001MajorAssignment.assetManager.manager.get(Assets.fail);

        // Define the player (Box2d)
        definePlayer();

        // Temporary array to hold animation frames
        Array<TextureRegion> tempFrames = new Array<TextureRegion>();

        // Setup run animation
        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("running"), i * 256, 0, 256, 256));
        }
        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("running"), i * 256, 256, 256, 256));
        }
        running = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        // Setup jump start animation
        for (int i = 0; i <= 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("jumping start"), i * 256, 0, 256, 256));
        }
        jumpStart = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        // Setup jump end animation
        for (int i = 0; i <= 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("jumping end"), i * 256, 0, 256, 256));
        }
        jumpEnd = new Animation(0.1f, tempFrames);

        tempFrames.clear();

        // Setup slide start animation
        for (int i = 0; i <= 1; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sliding start"), i * 256, 0, 256, 256));
        }
        slideStart = new Animation(0.3f, tempFrames);
        tempFrames.clear();

        // Setup slide end animation
        for (int i = 0; i <= 1; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sliding end"), i * 256, 0, 256, 256));
        }
        slideEnd = new Animation(0.3f, tempFrames);
        tempFrames.clear();

        // Setup fail animation
        for (int i = 0; i <= 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("deading"), i * 256, 0, 256, 256));
        }
        fail = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        // Set the starting animation frame to 'run'
        currentFrame = (TextureRegion) running.getKeyFrame(3, false);

        // Initially add a linear impulse to the player to start moving straight away
        box2dBody.applyLinearImpulse(new Vector2(15f, 0), box2dBody.getWorldCenter(), true);

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
        bodyDef.position.set(10, 20);

        // Create the body in the world
        box2dBody = world.createBody(bodyDef);

        // Set the shape of the body to a 1x2.1 cube
        shape.setAsBox(1, (float) 2.1);
        fixtureDef.shape = shape;

        // Add the fixture to the body
        box2dBody.createFixture(fixtureDef).setUserData(this);
    }

    // Method called to update the player (sounds, animation frame, and states)
    public void update(float delta) {

        // Update the player Box2D body shape depending on the player state (eg. shorter if player is sliding)
        updateBodyAndFixture();

        // Play the appropriate sounds depending on the players state
        updateSounds();

        // Set the current frame of the player depending on the player state (eg. running, jumping, etc.)
        currentFrame = getFramesFromAnimation(delta);

        // If the player is dead, set game over to true and change the player state to "FAIL"
        if (playerIsDead) {
            gameOver = true;
            currentState = State.FAIL;
        }
    }

    // Method to play the sounds for the player
    private void updateSounds() {

        // If the player is starting to jump
        if (currentState == State.JUMP_START && previousState != State.JUMP_START) {

            // Get a random number between 1 and 3 (because there are 3 different jump sounds)
            Random r = new Random();
            int result = r.nextInt(4-1) + 1;

            // Play the appropriate jump sound depending on the random number generated
            if(result == 1){
                jumpSound1.play();
            }else if(result == 2){
                jumpSound2.play();
            }else{
                jumpSound3.play();
            }

        }

        // If the player is starting to slide
        if (currentState == State.SLIDE_START && previousState != State.SLIDE_START) {

            // Get a random number between 1 and 3 (because there are 3 different slide sounds)
            Random r = new Random();
            int result = r.nextInt(4-1) + 1;

            // Play the appropriate jump sound depending on the random number generated
            if(result == 1){
                slideSound1.play();
            }else if(result == 2){
                slideSound2.play();
            }else{
                slideSound3.play();
            }
        }

        // If the player is failing (dead)
        if (currentState == State.FAIL && previousState != State.FAIL) {

            // Play the fail sound
            failSound.play();
        }

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
        if (currentState == State.FAIL) {
            returnRegion = (TextureRegion) fail.getKeyFrame(stateTimer, false);

            // If the player state is "JUMP_START" return the JUMP_START animation frame
        } else if (currentState == State.JUMP_START) {
            returnRegion = (TextureRegion) jumpStart.getKeyFrame(stateTimer, false);

            // If the player state is "JUMP_END" return the JUMP_END animation frame
        } else if (currentState == State.JUMP_END) {
            returnRegion = (TextureRegion) jumpEnd.getKeyFrame(stateTimer, false);

            // If the player state is "SLIDE_START" return the SLIDE_START animation frame
        } else if (currentState == State.SLIDE_START) {
            returnRegion = (TextureRegion) slideStart.getKeyFrame(stateTimer, false);

            // If the player state is "SLIDE_END" return the SLIDE_END animation frame
        } else if (currentState == State.SLIDE_END) {
            returnRegion = (TextureRegion) slideEnd.getKeyFrame(stateTimer, false);
        } else {

            // Else, return the running animation frame
            returnRegion = (TextureRegion) running.getKeyFrame(stateTimer, true);
        }

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

        // If the player is dead, set the state to "FAIL"
        if (playerIsDead) {
            currentState = State.FAIL;

            // If the player is not dead
        } else {

            // If the player is not sliding
            if (slideStartTimer == 0 && slideEndTimer == 0) {

                // If the player's y velocity is greater than 0 (moving upwards)
                if (box2dBody.getLinearVelocity().y > 0) {

                    // Change the player's state to "JUMP_START"
                    currentState = State.JUMP_START;

                    // If the player's y velocity is less than 0 (moving down / falling)
                } else if (box2dBody.getLinearVelocity().y < 0) {

                    // Change the player's state to "JUMP_END"
                    currentState = State.JUMP_END;

                    // Else, set the player's state to "RUN"
                } else {
                    currentState = State.RUN;
                }

                // If the player is sliding
            } else {

                // If the player's state is "SLIDE_START"
                if (currentState == State.SLIDE_START) {

                    // If the slide start timer is greater then 0: decrease it by delta
                    if (slideStartTimer > 0) {
                        slideStartTimer -= delta;

                        // If the slide start timer is 0: change player's state to "SLIDE_END"
                    } else {
                        slideStartTimer = 0;
                        currentState = State.SLIDE_END;
                    }

                    // If the player's state is "SLIDE_END"
                } else if (currentState == State.SLIDE_END) {

                    // If the slide end timer is greater then 0: decrease it by delta
                    if (slideEndTimer > 0) {
                        slideEndTimer -= delta;

                        // If the slide end timer is 0: change the player's state to "RUN"
                    } else {
                        slideEndTimer = 0;
                        currentState = State.RUN;
                    }
                }
            }
        }
    }

    // Method to update the player's Box2D body shape
    public void updateBodyAndFixture() {

        // If currently sliding and previously wasn't (reduce box height to slide under enemies)
        if ((currentState == State.SLIDE_START || currentState == State.SLIDE_END) &&
                (previousState != State.SLIDE_START && previousState != State.SLIDE_END)) {

            // Destroy the current body's fixture
            box2dBody.destroyFixture(box2dBody.getFixtureList().get(0));

            // Update the size of the fixture shape
            shape.setAsBox(1, (float) 0.8);
            fixtureDef.shape = shape;

            // Add the fixture to the body
            box2dBody.createFixture(fixtureDef).setUserData(this);

            // Set the player's linear velocity to 15f
            box2dBody.setLinearVelocity(15f, 0f);

            //if currently running and was previously sliding or jumping (return to normal box height)
        } else if ((currentState == State.RUN) &&
                (previousState == State.SLIDE_START || previousState == State.SLIDE_END)) {

            // Destroy the current body's fixture
            box2dBody.destroyFixture(box2dBody.getFixtureList().get(0));

            // Update the size of the fixture shape
            shape.setAsBox(1, (float) 2.1);
            fixtureDef.shape = shape;

            // Add the fixture to the body
            box2dBody.createFixture(fixtureDef).setUserData(this);

            // Set the player's linear velocity to 15f
            box2dBody.setLinearVelocity(15f, 0f);

        } else if ((currentState == State.RUN) &&
                (previousState == State.JUMP_START || previousState == State.JUMP_END)) {

            // Destroy the current body's fixture
            box2dBody.destroyFixture(box2dBody.getFixtureList().get(0));

            // Update the size of the fixture shape
            shape.setAsBox(1, (float) 2.1);
            fixtureDef.shape = shape;

            // Add the fixture to the body
            box2dBody.createFixture(fixtureDef).setUserData(this);

            // Set the player's linear velocity to 15f
            box2dBody.setLinearVelocity(15f, 0f);
        }

    }

    /* Method to make the player jump (called from the input handler when the user touches the
    top half of the screen*/
    public void jump() {

        // If the player is running (prevents double jumps)
        if (box2dBody.getLinearVelocity().y == 0 && currentState == State.RUN) {

            // Apply a linear velocity on the y axis
            box2dBody.applyLinearImpulse(new Vector2(0, 39f), box2dBody.getWorldCenter(), true);

            // Update the player state to "JUMP_START"
            currentState = State.JUMP_START;
        }
    }

    /* Method to make the player slide (called from the input handler when the user touches the
    bottom half of the screen*/
    public void slide() {

        // If the player is running (prevents continuous sliding)
        if (box2dBody.getLinearVelocity().y == 0 && currentState == State.RUN) {

            // Update the player state to "SLIDE_START"
            currentState = State.SLIDE_START;

            // Update slide timers (how long the player slides for)
            slideStartTimer = 0.9;
            slideEndTimer = 0.1;
        }
    }

    // Getters and Setters
    public void setPlayerIsDead(boolean value){
        playerIsDead = value;
    }

    public State getState() {
        return currentState;
    }

    public boolean getGameOver() {
        return gameOver;
    }

}
