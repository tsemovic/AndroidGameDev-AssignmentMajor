package com.semtb001.major.assignement.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.items.Wheat;
import com.semtb001.major.assignement.screens.PlayScreen;

// Class for the Grounded enemy (slime)
public class Sheep extends Sprite {

    // Enemy world and playscreen objects
    private World world;
    private PlayScreen playScreen;

    // Enemy Box2D objects
    public Body box2dBody;
    private float stateTimer;

    // Sheep animations
    private Animation N;
    private Animation NE;
    private Animation E;
    private Animation SE;
    private Animation S;
    private Animation SW;
    private Animation W;
    private Animation NW;

    public TextureRegion currentFrame;

    // Enemy position
    private Vector2 pos;

    // Player states
    public enum State {N, NE, E, SE, S, SW, W, NW}

    public Sheep.State currentState;
    public Sheep.State previousState;

    private float angle;
    public double currentSpeed = 2f;

    private float health;

    public Sheep(World world, PlayScreen playScreen, Vector2 pos) {

        // Instantiate world, playscreen, and enemy position
        this.world = world;
        this.playScreen = playScreen;
        this.pos = pos;
        stateTimer = 0;

        health = 100;

        // Setup the sheep current states (starts the game running)
        currentState = Sheep.State.N;
        previousState = null;

        // Define the enemy (Box2D)
        defineEnemy();

        // Temporary array to hold animation frames
        Array<TextureRegion> tempFrames = new Array<TextureRegion>();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepN"), i * 128, 0, 128, 128));
        }
        N = new Animation(0.3f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepE"), i * 128, 0, 128, 128));
        }
        NE = new Animation(0.3f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepE"), i * 128, 0, 128, 128));
        }
        E = new Animation(0.3f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepE"), i * 128, 0, 128, 128));
        }
        SE = new Animation(0.3f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepS"), i * 128, 0, 128, 128));
        }
        S = new Animation(0.3f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepW"), i * 128, 0, 128, 128));
        }
        SW = new Animation(0.3f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepW"), i * 128, 0, 128, 128));
        }
        W = new Animation(0.3f, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepW"), i * 128, 0, 128, 128));
        }
        NW = new Animation(0.3f, tempFrames);
        tempFrames.clear();


        // Set the starting animation frame to N
        currentFrame = (TextureRegion) N.getKeyFrame(0f, false);
    }

    public void defineEnemy() {

        // Initialise Box2D objects
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        // Setup body as an enemy that can collide with the the world (exclude the player)
        fixtureDef.filter.categoryBits = Semtb001MajorAssignment.SHEEP;
        fixtureDef.filter.maskBits = Semtb001MajorAssignment.WORLD | Semtb001MajorAssignment.PLAYER;

        // Setup the body as a dynamic body (ability to move)
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Set the position of the body to the x and y coordinates of the enemy object in the map file
        bodyDef.position.set(pos.x / 32, pos.y / 32);

        // Create the body in the world
        box2dBody = world.createBody(bodyDef);

        CircleShape cir = new CircleShape();
        cir.setRadius(0.3f);
        fixtureDef.shape = cir;

        // Add the fixture to the body
        box2dBody.createFixture(fixtureDef).setUserData(this);

        // Create a 'new' fixture (sensor) on the body that can detect player collisions
        fixtureDef.filter.categoryBits = Semtb001MajorAssignment.SHEEP;

        // Allow the fixture to detect collisions from the player and the world
        fixtureDef.filter.maskBits = Semtb001MajorAssignment.PLAYER | Semtb001MajorAssignment.WORLD;

        // Setup the fixture as a sensor
        fixtureDef.isSensor = true;

        // Set the shape of the body to a 1x1 cube
        fixtureDef.shape = shape;

        // Add the fixture to the body
        box2dBody.createFixture(fixtureDef).setUserData(this);
    }

    // Method to update the enemy
    public void update(float delta) {

        // Update the state timer and set the current animation frame to the animation key frame
        stateTimer += delta;
        currentFrame = getFramesFromAnimation(delta);

        if (health <= 0) {
            setCategoryFilter(Semtb001MajorAssignment.DESTROYED);
            box2dBody.setLinearVelocity(0, 0);
            currentSpeed = 0f;
        } else {
            if (playScreen.getBox2dWorldCreator().wheat.size() > 0) {
                Wheat target = playScreen.getBox2dWorldCreator().wheat.get(0);
                double xDistance = (target.rectangle.getX() + 0.5) - box2dBody.getPosition().x;
                double yDistance = (target.rectangle.getY() + 0.5) - box2dBody.getPosition().y;

                if (xDistance < 0) {
                    xDistance = xDistance * -1;
                }
                if (yDistance < 0) {
                    yDistance = yDistance * -1;
                }
                double distanceToTarget = Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
                currentSpeed = distanceToTarget * 2;

                Vector2 vector = new Vector2((float) ((float) ((target.rectangle.getX() + 0.5) - box2dBody.getPosition().x) / currentSpeed),
                        (float) ((float) ((target.rectangle.getY() + 0.5) - box2dBody.getPosition().y) / currentSpeed));

                box2dBody.setLinearVelocity(vector);

                // Set sheep angle to the direction it's moving
                angle = (float) Math.atan2((double) box2dBody.getLinearVelocity().y, (double) box2dBody.getLinearVelocity().x);
                box2dBody.setTransform(box2dBody.getWorldCenter(), angle);

                updateState();

            } else {
                box2dBody.setLinearVelocity(0, 0);
            }
        }

        playScreen.getBox2dWorldCreator().destoryWheat(this);


    }

    // Method to update the state of the sheep based on it's direction angle
    private void updateState() {

        // Get the angle of the sheep direction in degrees
        float angleDegrees = angle * MathUtils.radiansToDegrees;

        if (angleDegrees > 157.5) {
            currentState = Sheep.State.W;
        } else if (angleDegrees > 112.5) {
            currentState = Sheep.State.NW;
        } else if (angleDegrees > 67.5) {
            currentState = Sheep.State.N;
        } else if (angleDegrees > 22.5) {
            currentState = Sheep.State.NE;
        }else if (angleDegrees < -157.5){
            currentState = Sheep.State.W;
        }else if (angleDegrees < -112.5){
            currentState = Sheep.State.SW;
        }else if (angleDegrees < -67.5){
            currentState = Sheep.State.S;
        }else if (angleDegrees < -22.5){
            currentState = Sheep.State.SE;
        }else{
            currentState = Sheep.State.E;
        }
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        box2dBody.getFixtureList().get(0).setFilterData(filter);
    }


    // Method to get the current player animation frame
    private TextureRegion getFramesFromAnimation(float delta) {

        // Store the current state as 'previous state'
        previousState = currentState;

        // Texture region that will be returned
        TextureRegion returnRegion = null;

        // If the player state is "FAIL" return the fail animation frame
        if (currentState == Sheep.State.N) {
            N.setFrameDuration(getDurationFromSpeed(currentSpeed));
            returnRegion = (TextureRegion) N.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) N.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.NE) {
            NE.setFrameDuration(getDurationFromSpeed(currentSpeed));
            returnRegion = (TextureRegion) NE.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) NE.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.E) {
            E.setFrameDuration(getDurationFromSpeed(currentSpeed));
            returnRegion = (TextureRegion) E.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) E.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.SE) {
            SE.setFrameDuration(getDurationFromSpeed(currentSpeed));
            returnRegion = (TextureRegion) SE.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) SE.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.S) {
            S.setFrameDuration(getDurationFromSpeed(currentSpeed));
            returnRegion = (TextureRegion) S.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) S.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.SW) {
            SW.setFrameDuration(getDurationFromSpeed(currentSpeed));
            returnRegion = (TextureRegion) SW.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) SW.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.W) {
            W.setFrameDuration(getDurationFromSpeed(currentSpeed));
            returnRegion = (TextureRegion) W.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) W.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.NW) {
            NW.setFrameDuration(getDurationFromSpeed(currentSpeed));
            returnRegion = (TextureRegion) NW.getKeyFrame(stateTimer, true);
            if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) NW.getKeyFrame(0.2f, true);
            }
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

    public void sheepHit() {
        health = health - 50;
    }

}
