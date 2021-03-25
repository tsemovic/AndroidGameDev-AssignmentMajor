package com.semtb001.individual.assignement.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class Player extends Sprite{

    private World world;
    private PlayScreen playScreen;

    public static final int DESTROYED = 0;
    public static final int DEFAULT = 1;
    public static final int PLAYER = 2;
    public static final int WORLD = 3;
    public static final int ENEMY = 3;


    private enum State {RUN, JUMP_START, JUMP_END, SLIDE, FAIL};
    private boolean playerIsDead;
    private State currentState;
    private State previousState;

    private float stateTimer;

    public Body box2dBody;
    public SpriteBatch batch;

    private Animation running;
    private Animation jumpStart;
    private Animation jumpEnd;

    public TextureRegion currentFrame;



    public Player(World world, PlayScreen playScreen) {
        this.world = world;
        this.playScreen = playScreen;
        stateTimer = 0;
        currentState = State.RUN;
        previousState = State.RUN;

        batch = playScreen.batch;
        playerIsDead = false;
        definePlayer();

        Array<TextureRegion> tempFrames = new Array<TextureRegion>();

        //run
        for(int i = 1; i <= 4; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("running"),  i* 256, 0, 256, 256));
        }
        running = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        //jump start
        for(int i = 0; i <= 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("jumping start"),  i* 256, 0, 256, 256));
        }
        jumpStart = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        //jump end
        for(int i = 0; i <= 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("jumping end"),  i* 256, 0, 256, 256));
        }
        jumpEnd = new Animation(0.1f, tempFrames);

        tempFrames.clear();

    }

    public void definePlayer(){
        BodyDef bodyDef = new BodyDef();
        Rectangle rect = new Rectangle();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.filter.categoryBits = Player.PLAYER;
        fixtureDef.filter.maskBits = Player.DEFAULT | Player.WORLD;

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(10, 18);

        box2dBody = world.createBody(bodyDef);

        shape.setAsBox(1, 1);
        //CircleShape cir = new CircleShape();
        //cir.setRadius(15);
        //shape.setAsBox(5/IslandSurvival.PPM, 5/IslandSurvival.PPM);
        //shape.setAsBox((rect.getWidth() / 2), (rect.getHeight() / 2));
        //fixtureDef.shape = cir;
        fixtureDef.shape = shape;
        box2dBody.createFixture(fixtureDef).setUserData(this);

    }

    public void update(float delta){
        currentFrame = getFramesFromAnimation(delta);
        //currentFrame = (TextureRegion) jumpStart.getKeyFrame(stateTimer, true);
        System.out.println(stateTimer);

    }

    private TextureRegion getFramesFromAnimation(float delta) {
        getState();
        TextureRegion returnRegion = null;

        if(currentState == State.JUMP_START) {
            returnRegion = (TextureRegion) jumpStart.getKeyFrame(stateTimer, false);
        }else if(currentState == State.JUMP_END) {
            returnRegion = (TextureRegion) jumpEnd.getKeyFrame(stateTimer, false);
        }else {
            returnRegion = (TextureRegion) running.getKeyFrame(stateTimer, true);
        }

        if(currentState != previousState){
            stateTimer = 0;
        }else{
            stateTimer += delta;
        }
        previousState = currentState;

        return returnRegion;

    }

    public void getState(){
        if(playerIsDead) {
            currentState = State.FAIL;
        }else if(box2dBody.getLinearVelocity().y > 0) {
            currentState = State.JUMP_START;
        }else if(box2dBody.getLinearVelocity().y < 0) {
            currentState = State.JUMP_END;
        }else {
            currentState = State.RUN;
        }
    }

    public void jump(){
        if(currentState != State.JUMP_START && currentState != State.JUMP_END) {
            box2dBody.applyLinearImpulse(new Vector2(0, 40f), box2dBody.getWorldCenter(), true);
            currentState = State.JUMP_START;
        }
    }

    public void slide(){
        box2dBody.applyLinearImpulse(new Vector2(0, 4f), box2dBody.getWorldCenter(), true);
            currentState = State.SLIDE;
    }
}
