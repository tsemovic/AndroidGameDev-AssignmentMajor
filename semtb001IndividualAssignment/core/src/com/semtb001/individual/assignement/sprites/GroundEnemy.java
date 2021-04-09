package com.semtb001.individual.assignement.sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;
import com.semtb001.individual.assignement.tools.Assets;

// Class for the Grounded enemy (slime)
public class GroundEnemy extends Sprite {

    // Enemy world and playscreen objects
    private World world;
    private PlayScreen playScreen;

    // Enemy Box2D objects
    public Body box2dBody;
    private float stateTimer;

    // Enemy animation
    private Animation slimeAnimation;
    public TextureRegion currentFrame;

    // Enemy position
    private Vector2 pos;

    // Enemy sounds
    private Music enemySound;

    public GroundEnemy(World world, PlayScreen playScreen, Vector2 pos) {

        // Instantiate world, playscreen, and enemy position
        this.world = world;
        this.playScreen = playScreen;
        this.pos = pos;
        stateTimer = 0;

        // Define the enemy (Box2D)
        defineEnemy();

        // Temporary array for storing animation frames
        Array<TextureRegion> tempFrames = new Array<TextureRegion>();

        // Get the slime animation frames and add them to slime Animation
        for (int i = 0; i < 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("slime"), i * 160, 0, 160, 80));
        }
        slimeAnimation = new Animation(0.1f, tempFrames);

        // Initialise the enemy sound
        enemySound = Semtb001IndividualAssignment.assetManager.manager.get(Assets.slime);
        enemySound.play();
        enemySound.setLooping(true);
    }

    public void defineEnemy() {

        // Initialise Box2D objects
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        // Setup body as an enemy that can collide with the the world (exclude the player)
        fixtureDef.filter.categoryBits = Semtb001IndividualAssignment.ENEMY;
        fixtureDef.filter.maskBits = Semtb001IndividualAssignment.WORLD;

        // Setup the body as a dynamic body (ability to move)
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Set the position of the body to the x and y coordinates of the enemy object in the map file
        bodyDef.position.set(pos.x / 32, pos.y / 32);

        // Create the body in the world
        box2dBody = world.createBody(bodyDef);

        // Set the shape of the body to a 1x1 cube
        shape.setAsBox(1, 1);
        fixtureDef.shape = shape;

        // Add the fixture to the body
        box2dBody.createFixture(fixtureDef).setUserData(this);

        // Create a 'new' fixture (sensor) on the body that can detect player collisions
        fixtureDef.filter.categoryBits = Semtb001IndividualAssignment.ENEMY;

        // Allow the fixture to detect collisions from the player and the world
        fixtureDef.filter.maskBits = Semtb001IndividualAssignment.PLAYER | Semtb001IndividualAssignment.WORLD;

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
        currentFrame = (TextureRegion) slimeAnimation.getKeyFrame(stateTimer, true);

        // If the enemy is moving slower than -5f (velocity of 5 going left)
        if (box2dBody.getLinearVelocity().x >= -5f) {

            // Apply linear impulse to the enemy on the x axis
            box2dBody.applyLinearImpulse(new Vector2(-0.5f, 0), box2dBody.getWorldCenter(), true);
        }

        /* This code makes the enemy sound gradually louder as they approach and gradually quieter
        as they pass

        If the enemy is ahead of the player */
        if (playScreen.getPlayer().box2dBody.getPosition().x < box2dBody.getPosition().x) {

            // If the player x position plus 10 is greater than the enemy position: play enemy sound at max volume
            if (playScreen.getPlayer().box2dBody.getPosition().x + 10 > box2dBody.getPosition().x) {
                enemySound.play();
                enemySound.setVolume(1f);

                // If the player x position plus 20 is greater than the enemy position: play enemy sound at 70% volume
            } else if (playScreen.getPlayer().box2dBody.getPosition().x + 20 > box2dBody.getPosition().x) {
                enemySound.play();
                enemySound.setVolume(0.7f);

                // If the player x position plus 30 is greater than the enemy position: play enemy sound at 40% volume
            } else if (playScreen.getPlayer().box2dBody.getPosition().x + 30 > box2dBody.getPosition().x) {
                enemySound.play();
                enemySound.setVolume(0.4f);

                // If the player x position plus 10 is greater than the enemy position: play enemy sound at 10% volume
            } else if (playScreen.getPlayer().box2dBody.getPosition().x + 40 > box2dBody.getPosition().x) {
                enemySound.play();
                enemySound.setVolume(0.1f);

                // Else, set enemy sound to 0% volume
            } else {
                enemySound.setVolume(0.0f);
            }

            // If the enemy is behind the player
        } else {

            // If the player x position is less than the enemy position plus 2: play enemy sound at 50% volume
            if (playScreen.getPlayer().box2dBody.getPosition().x < box2dBody.getPosition().x + 2) {
                enemySound.play();
                enemySound.setVolume(0.5f);

                // If the player x position is less than the enemy position plus 10: play enemy sound at 50% volume
            } else if (playScreen.getPlayer().box2dBody.getPosition().x < box2dBody.getPosition().x + 10) {
                enemySound.play();
                enemySound.setVolume(0.2f);

                // Else, set enemy sound to 0% volume
            } else {
                enemySound.setVolume(0.0f);
            }
        }

    }

    // Method to stop the enemy sound (used when the game is paused etc.)
    public void stopSound() {
        enemySound.stop();
    }
}
