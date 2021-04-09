package com.semtb001.individual.assignement.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;
import com.semtb001.individual.assignement.tools.Assets;

// Class for the coins in the map
public class Coin {

    // Coin world and playscreen objects
    private World world;
    private PlayScreen playScreen;

    // Coin texture objects
    private Animation texture;
    public TextureRegion currentFrame;

    // Coin Box2D objects
    public Body box2dBody;
    public FixtureDef fixtureDef;
    private float stateTimer;

    // Variable for the state of the coin (collected or not)
    public boolean collected;

    public Coin(Rectangle position, PlayScreen screen){

        // Instantiate world and playscreen
        world = screen.getWorld();
        playScreen = screen;

        // Set the state time to 0 and collected to false (not collected)
        stateTimer = 0;
        collected = false;

        // Setup Box2D objects
        BodyDef bodyDef = new BodyDef();
        CircleShape shape = new CircleShape();
        fixtureDef = new FixtureDef();

        // Coin is a sensor
        fixtureDef.isSensor = true;

        // Set the body to static
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // Set the fixture category to COIN
        fixtureDef.filter.categoryBits = Semtb001IndividualAssignment.COIN;

        // Set the position to the x and y coordinates of the object in the map file
        bodyDef.position.set(position.x / 32, position.y / 32);

        // Create the body in the world
        box2dBody = world.createBody(bodyDef);

        // Set the radius of the sensor
        shape.setRadius(0.75f);
        fixtureDef.shape = shape;

        // Add the fixture to the body
        box2dBody.createFixture(fixtureDef).setUserData(this);

        // Hold a temporary list of Textures for the coin
        Array<TextureRegion> tempFrames = new Array<TextureRegion>();

        // For the number of frames in the coin animation
        for (int i = 1; i <= 7; i++) {

            // Add the frame to the tempFrames list
            tempFrames.add(new TextureRegion(new Texture("coin.png"), i * 16, 0, 16, 16));
        }

        // Create the coin animation from the list of texture regions
        texture = new Animation(0.1f, tempFrames);
        tempFrames.clear();
    }

    // Method called to update the coin
    public void update(float delta) {

        // If the coin is not collected
        if(!collected) {

            // Update the state timer and set the current animation frame to the animation key frame
            stateTimer += delta;
            currentFrame = (TextureRegion) texture.getKeyFrame(stateTimer, true);
        }
    }

    // Method called when the coin is hit by the player (from WorldContactListener)
    public void hit(){

        // If the coin is not collected
        if(!collected) {

            // Set the coin to 'collected'
            collected = true;

            // Call the updateCollectedCoins method to update the coin counter in the hud
            playScreen.updateCollectedCoins();

            // Play the collected coin sound
            Sound coin = Semtb001IndividualAssignment.assetManager.manager.get(Assets.coin);
            coin.play();
        }
    }
}
