package com.semtb001.major.assignement.sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector;
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
import com.semtb001.major.assignement.tools.Assets;

// Class for the Grounded enemy (slime)
public class Sheep extends Sprite {

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

    private float health;
    private double sheepSpeed;

    public Sheep(World world, PlayScreen playScreen, Vector2 pos) {

        // Instantiate world, playscreen, and enemy position
        this.world = world;
        this.playScreen = playScreen;
        this.pos = pos;
        stateTimer = 0;

        health = 100;

        // Define the enemy (Box2D)
        defineEnemy();

        // Temporary array for storing animation frames
        Array<TextureRegion> tempFrames = new Array<TextureRegion>();

        // Get the slime animation frames and add them to slime Animation
        for (int i = 0; i <= 4; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("N"), i * 128, 0, 128, 128));
        }
        slimeAnimation = new Animation(0.1f, tempFrames);

//        // Initialise the enemy sound
//        enemySound = Semtb001MajorAssignment.assetManager.manager.get(Assets.slime);
//        enemySound.play();
//        enemySound.setLooping(true);
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
        currentFrame = (TextureRegion) slimeAnimation.getKeyFrame(stateTimer, true);

        if(health <= 0){
            setCategoryFilter(Semtb001MajorAssignment.DESTROYED);
            box2dBody.setLinearVelocity(0,0);
        }else{
            if(playScreen.getBox2dWorldCreator().wheat.size() > 0) {
                Wheat target = playScreen.getBox2dWorldCreator().wheat.get(0);
                double xDistance = (target.rectangle.getX() + 0.5) - box2dBody.getPosition().x;
                double yDistance = (target.rectangle.getY() + 0.5) - box2dBody.getPosition().y;
                if(xDistance < 0){
                    xDistance = xDistance * -1;
                }
                if(yDistance < 0){
                    yDistance = yDistance * -1;
                }
                double distanceToTarget = Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
                sheepSpeed = distanceToTarget * 2;

                Vector2 vector = new Vector2((float) ((float) ((target.rectangle.getX() + 0.5) - box2dBody.getPosition().x) / sheepSpeed),
                        (float) ((float) ((target.rectangle.getY() + 0.5) - box2dBody.getPosition().y) / sheepSpeed));

                box2dBody.setLinearVelocity(vector);

            }else{
                box2dBody.setLinearVelocity(0,0);
            }
        }
        
        playScreen.getBox2dWorldCreator().destoryWheat(this);


    }

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        box2dBody.getFixtureList().get(0).setFilterData(filter);
    }
}
