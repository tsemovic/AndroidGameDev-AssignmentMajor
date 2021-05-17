package com.semtb001.major.assignement.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.items.Wheat;
import com.semtb001.major.assignement.screens.PlayScreen;
import com.semtb001.major.assignement.tools.Assets;

import java.util.Random;

// Class for the sheep
public class Sheep extends Sprite {

    // Sheep world and playscreen objects
    private World world;
    private PlayScreen playScreen;

    // Sheep Box2D objects
    private Body box2dBody;
    private float stateTimer;

    private boolean hasBeenDestroyed;

    // Sheep animations
    private Animation N;
    private Animation NE;
    private Animation E;
    private Animation SE;
    private Animation S;
    private Animation SW;
    private Animation W;
    private Animation NW;

    // Sheep animations when hurt
    private Animation hurtN;
    private Animation hurtNE;
    private Animation hurtE;
    private Animation hurtSE;
    private Animation hurtS;
    private Animation hurtSW;
    private Animation hurtW;
    private Animation hurtNW;

    // Sheep animation speed duration
    private float sheepAnimationDuration;

    // Current frame of the sheep animation
    public TextureRegion currentFrame;

    // Sheep position
    private Vector2 pos;

    // Sheep states
    private enum State {N, NE, E, SE, S, SW, W, NW}
    private Sheep.State currentState;
    private Sheep.State previousState;

    // Sheep angle
    private float angle;

    // Sheep speed
    private double currentSpeed = 0f;

    // Sheep health
    private float health;

    // Sheep sounds
    private Sound sheepSound;
    private Sound sheepHurtSound;

    // Objects for when the sheep is hit
    private boolean hit;
    private float hitTimer;

    // Time counter
    private float timeCount;

    public Sheep(World world, PlayScreen playScreen, Vector2 pos) {

        // Instantiate world, playscreen, and enemy position
        this.world = world;
        this.playScreen = playScreen;
        this.pos = pos;
        stateTimer = 0;

        // Sheep health
        health = 100;

        // Boolean if the sheep has been hit or not
        hit = false;

        // Timer for duration of 'red' sheep (when hit)
        hitTimer = 0.5f;

        // Time counter (for when the sheep is hit)
        timeCount = 0;

        // Boolean to track if the sheep has been destroyed
        hasBeenDestroyed = false;

        // Setup the sheep current states (starts the game running)
        currentState = Sheep.State.N;
        previousState = null;

        // Set the sheep animation duration to 0.4f
        sheepAnimationDuration = 0.4f;

        // Define the sheep in Box2D
        defineSheep();

        // Temporary array to hold animation frames
        Array<TextureRegion> tempFrames = new Array<TextureRegion>();

        // Instantiate the sheep animations for all directions
        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepN"), i * 128, 0, 128, 128));
        }
        N = new Animation(sheepAnimationDuration, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepE"), i * 128, 0, 128, 128));
        }
        NE = new Animation(sheepAnimationDuration, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepE"), i * 128, 0, 128, 128));
        }
        E = new Animation(sheepAnimationDuration, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepE"), i * 128, 0, 128, 128));
        }
        SE = new Animation(sheepAnimationDuration, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepS"), i * 128, 0, 128, 128));
        }
        S = new Animation(sheepAnimationDuration, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepW"), i * 128, 0, 128, 128));
        }
        SW = new Animation(sheepAnimationDuration, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepW"), i * 128, 0, 128, 128));
        }
        W = new Animation(sheepAnimationDuration, tempFrames);
        tempFrames.clear();

        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sheepW"), i * 128, 0, 128, 128));
        }
        NW = new Animation(sheepAnimationDuration, tempFrames);
        tempFrames.clear();

        // Instantiation the sheep hurt Animations
        hurtN = new Animation(sheepAnimationDuration, new TextureRegion(playScreen.textureAtlas.findRegion("sheepN"), 512, 0, 128, 128));
        hurtNE = new Animation(sheepAnimationDuration, new TextureRegion(playScreen.textureAtlas.findRegion("sheepE"), 512, 0, 128, 128));
        hurtE = new Animation(sheepAnimationDuration, new TextureRegion(playScreen.textureAtlas.findRegion("sheepE"), 512, 0, 128, 128));
        hurtSE = new Animation(sheepAnimationDuration, new TextureRegion(playScreen.textureAtlas.findRegion("sheepE"), 512, 0, 128, 128));
        hurtS = new Animation(sheepAnimationDuration, new TextureRegion(playScreen.textureAtlas.findRegion("sheepS"), 512, 0, 128, 128));
        hurtSW = new Animation(sheepAnimationDuration, new TextureRegion(playScreen.textureAtlas.findRegion("sheepW"), 512, 0, 128, 128));
        hurtW = new Animation(sheepAnimationDuration, new TextureRegion(playScreen.textureAtlas.findRegion("sheepW"), 512, 0, 128, 128));
        hurtNW = new Animation(sheepAnimationDuration, new TextureRegion(playScreen.textureAtlas.findRegion("sheepW"), 512, 0, 128, 128));

        // Set the starting animation frame to N
        currentFrame = (TextureRegion) N.getKeyFrame(0f, false);

        // Random boolean generator (to choose between sheep sound 1 or 2)
        Random r = new Random();
        if (r.nextBoolean()) {
            sheepSound = Semtb001MajorAssignment.assetManager.manager.get(Assets.sheep1);
        } else {
            sheepSound = Semtb001MajorAssignment.assetManager.manager.get(Assets.sheep2);
        }

        // Play the sheep sound on a loop
        sheepSound.loop();

        sheepHurtSound = Semtb001MajorAssignment.assetManager.manager.get(Assets.sheepHurt);
    }

    // Define the sheep in box2d
    public void defineSheep() {

        // Initialise Box2D objects
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        // Setup body as a sheep that can collide with the the world and the player
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

        // Set the shape of the body to a 1x1 cube
        fixtureDef.shape = shape;

        // Add the fixture to the body
        box2dBody.createFixture(fixtureDef).setUserData(this);
    }

    // Method to update the sheep
    public void update(float delta) {

        // If the sheep is hit
        if (hit) {

            // If the hitTimer is greater than 0
            if (hitTimer > 0) {

                // Reduce the hitTimer by delta time
                hitTimer -= delta;

                // If the hitTimer is less or equal to 0
            } else {

                // Set hit to false and reset the hitTimer
                hit = false;
                hitTimer = 0.5f;
            }
        }

        // Update the state timer and set the current animation frame to the animation key frame
        stateTimer += delta;
        currentFrame = getFramesFromAnimation(delta);

        // If the sheep health is less or equal to 0
        if (health <= 0) {

            // If the sheep hasn't yet been destroyed
            if (!hasBeenDestroyed) {

                // Destroy the sheep (box2D) and stop the sheep sounds
                sheepSound.stop();
                world.destroyBody(box2dBody);
                hasBeenDestroyed = true;
            }

            // If the sheep is alive
        } else {

            // If there is any wheat planted
            if (playScreen.getBox2dWorldCreator().getWheat().size() > 0) {

                // Set the sheep target wheat to the first wheat in the list
                Wheat target = playScreen.getBox2dWorldCreator().getWheat().get(0);

                // Set the x and y distance to the target
                double xDistance = (target.bounds.getX() + 0.5) - box2dBody.getPosition().x;
                double yDistance = (target.bounds.getY() + 0.5) - box2dBody.getPosition().y;

                // If the distances are negative, convert them to positive
                if (xDistance < 0) {
                    xDistance = xDistance * -1;
                }
                if (yDistance < 0) {
                    yDistance = yDistance * -1;
                }

                // Get the distance to the target (Pythagoras' Theorem)
                double distanceToTarget = Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));

                // Set the sheep current speed
                currentSpeed = distanceToTarget * 2;

                // Instantiate a vector for the sheep movement towards the target
                Vector2 vector = new Vector2((float) ((float) ((target.bounds.getX() + 0.5) - box2dBody.getPosition().x) / currentSpeed),
                        (float) ((float) ((target.bounds.getY() + 0.5) - box2dBody.getPosition().y) / currentSpeed));

                // Set the sheep linear velocity to the vector towards the target
                box2dBody.setLinearVelocity(vector);

                // Set sheep angle to the direction it's moving
                angle = (float) Math.atan2((double) box2dBody.getLinearVelocity().y, (double) box2dBody.getLinearVelocity().x);
                box2dBody.setTransform(box2dBody.getWorldCenter(), angle);

                // Update the sheeps state
                updateState();

                // If there are no wheat objects in the world, stop the sheep's movement
            } else {
                box2dBody.setLinearVelocity(0, 0);
                currentSpeed = 0;
            }
        }

        // Destroy the wheat (if there is any, and if the sheep is where the wheat is located)
        playScreen.getBox2dWorldCreator().destroyWheat(this);

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
        } else if (angleDegrees < -157.5) {
            currentState = Sheep.State.W;
        } else if (angleDegrees < -112.5) {
            currentState = Sheep.State.SW;
        } else if (angleDegrees < -67.5) {
            currentState = Sheep.State.S;
        } else if (angleDegrees < -22.5) {
            currentState = Sheep.State.SE;
        } else {
            currentState = Sheep.State.E;
        }
    }

    // Method to get the current sheep animation frame
    private TextureRegion getFramesFromAnimation(float delta) {

        // Store the current state as 'previous state'
        previousState = currentState;

        // Texture region that will be returned
        TextureRegion returnRegion = null;

        /* Set the returnRegion to the animation frame based on the sheep direction and if they are
        'hit', and if they are walking or not*/
        if (currentState == Sheep.State.N) {
            returnRegion = (TextureRegion) N.getKeyFrame(stateTimer, true);
            if (hit) {
                returnRegion = (TextureRegion) hurtN.getKeyFrame(0, true);
            } else if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) N.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.NE) {
            returnRegion = (TextureRegion) NE.getKeyFrame(stateTimer, true);

            if (hit) {
                returnRegion = (TextureRegion) hurtNE.getKeyFrame(stateTimer, true);
            } else if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) NE.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.E) {
            returnRegion = (TextureRegion) E.getKeyFrame(stateTimer, true);

            if (hit) {
                returnRegion = (TextureRegion) hurtE.getKeyFrame(stateTimer, true);
            } else if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) E.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.SE) {
            returnRegion = (TextureRegion) SE.getKeyFrame(stateTimer, true);

            if (hit) {
                returnRegion = (TextureRegion) hurtSE.getKeyFrame(stateTimer, true);
            } else if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) SE.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.S) {
            returnRegion = (TextureRegion) S.getKeyFrame(stateTimer, true);

            if (hit) {
                returnRegion = (TextureRegion) hurtS.getKeyFrame(stateTimer, true);
            } else if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) S.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.SW) {
            returnRegion = (TextureRegion) SW.getKeyFrame(stateTimer, true);

            if (hit) {
                returnRegion = (TextureRegion) hurtSW.getKeyFrame(stateTimer, true);
            } else if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) SW.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.W) {
            returnRegion = (TextureRegion) W.getKeyFrame(stateTimer, true);

            if (hit) {
                returnRegion = (TextureRegion) hurtW.getKeyFrame(stateTimer, true);
            } else if (currentSpeed == 0.0) {
                returnRegion = (TextureRegion) W.getKeyFrame(0.2f, true);
            }
        } else if (currentState == Sheep.State.NW) {
            returnRegion = (TextureRegion) NW.getKeyFrame(stateTimer, true);

            if (hit) {
                returnRegion = (TextureRegion) hurtNW.getKeyFrame(stateTimer, true);
            } else if (currentSpeed == 0.0) {
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

    // Method called when the sheep is 'hit'
    public void sheepHit() {

        // Reduce sheep health by 40, and set 'hit' to true, and play the sheepHurtSound
        health = health - 40;
        hit = true;
        sheepHurtSound.play();
    }

    // Getters and Setters
    public float getHealth() {
        return health;
    }

    public Body getBox2dBody(){
        return box2dBody;
    }

}
