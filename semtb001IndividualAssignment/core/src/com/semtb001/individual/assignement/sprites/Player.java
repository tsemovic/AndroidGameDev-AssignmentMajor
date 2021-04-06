package com.semtb001.individual.assignement.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
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
import com.semtb001.individual.assignement.tools.Assets;

import java.util.Random;

import javax.swing.event.CellEditorListener;

public class Player extends Sprite {

    private World world;
    private PlayScreen playScreen;

    public static final short DESTROYED = 0;
    public static final short DEFAULT = 1;
    public static final short PLAYER = 2;
    public static final short WORLD = 4;
    public static final short ENEMY = 8;
    public static final short JEWEL = 4;


    public enum State {RUN, JUMP_START, JUMP_END, SLIDE_START, SLIDE_END, FAIL}

    private State currentState;
    public State previousState;

    public boolean playerIsDead;
    public float deadTimer;

    public boolean gameOver;

    private float stateTimer;
    private double slideStartTimer;
    private double slideEndTimer;

    public Body box2dBody;
    public SpriteBatch batch;

    private Animation running;
    private Animation jumpStart;
    private Animation jumpEnd;
    private Animation slideStart;
    private Animation slideEnd;
    private Animation fail;

    public TextureRegion currentFrame;

    private FixtureDef fixtureDef;
    private PolygonShape shape;
    private Rectangle rect;
    private BodyDef bodyDef;

    private Sound jumpSound1;
    private Sound jumpSound2;
    private Sound jumpSound3;
    private Sound slideSound1;
    private Sound slideSound2;
    private Sound slideSound3;
    private Sound failSound;


    public Player(World world, PlayScreen playScreen) {
        this.world = world;
        this.playScreen = playScreen;
        stateTimer = 0;
        slideStartTimer = 0;
        slideEndTimer = 0;

        deadTimer = 0;
        gameOver = false;

        currentState = State.RUN;
        previousState = State.SLIDE_END;

        slideSound1 = Semtb001IndividualAssignment.assetManager.manager.get(Assets.slide1);
        slideSound2 = Semtb001IndividualAssignment.assetManager.manager.get(Assets.slide2);
        slideSound3 = Semtb001IndividualAssignment.assetManager.manager.get(Assets.slide3);

        jumpSound1 = Semtb001IndividualAssignment.assetManager.manager.get(Assets.jump1);
        jumpSound2 = Semtb001IndividualAssignment.assetManager.manager.get(Assets.jump2);
        jumpSound3 = Semtb001IndividualAssignment.assetManager.manager.get(Assets.jump3);

        failSound = Semtb001IndividualAssignment.assetManager.manager.get(Assets.fail);


        batch = playScreen.batch;
        playerIsDead = false;
        definePlayer();

        Array<TextureRegion> tempFrames = new Array<TextureRegion>();

        //run
        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("running"), i * 256, 0, 256, 256));
        }
        for (int i = 0; i <= 3; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("running"), i * 256, 256, 256, 256));
        }
        running = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        //jump start
        for (int i = 0; i <= 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("jumping start"), i * 256, 0, 256, 256));
        }
        jumpStart = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        //jump end
        for (int i = 0; i <= 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("jumping end"), i * 256, 0, 256, 256));
        }
        jumpEnd = new Animation(0.1f, tempFrames);

        tempFrames.clear();

        //slide start
        for (int i = 0; i <= 1; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sliding start"), i * 256, 0, 256, 256));
        }
        slideStart = new Animation(0.3f, tempFrames);
        tempFrames.clear();

        //slide end
        for (int i = 0; i <= 1; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("sliding end"), i * 256, 0, 256, 256));
        }
        slideEnd = new Animation(0.3f, tempFrames);
        tempFrames.clear();

        //fail
        for (int i = 0; i <= 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("deading"), i * 256, 0, 256, 256));
        }
        fail = new Animation(0.1f, tempFrames);
        tempFrames.clear();

        box2dBody.applyLinearImpulse(new Vector2(15f, 0), box2dBody.getWorldCenter(), true);
        currentFrame = (TextureRegion) running.getKeyFrame(0, false);

    }

    public void definePlayer() {
        bodyDef = new BodyDef();
        rect = new Rectangle();
        shape = new PolygonShape();
        fixtureDef = new FixtureDef();

        fixtureDef.filter.categoryBits = Player.PLAYER;
        fixtureDef.filter.maskBits = Player.DEFAULT | Player.WORLD | Player.ENEMY | Player.JEWEL;

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(10, 19);

        box2dBody = world.createBody(bodyDef);

        shape.setAsBox(1, (float) 2.1);

        fixtureDef.shape = shape;
        box2dBody.createFixture(fixtureDef).setUserData(this);
    }

    public void update(float delta) {
        updateBodyAndFixture();
        updateSounds();
        currentFrame = getFramesFromAnimation(delta);

        if (playerIsDead) {
            deadTimer += delta;
            if (deadTimer > 2) {
                gameOver = true;
            }
            currentState = State.FAIL;
        }
    }

    private void updateSounds() {

        if (currentState == State.JUMP_START && previousState != State.JUMP_START) {
            Random r = new Random();
            int result = r.nextInt(4-1) + 1;

            if(result == 1){
                jumpSound1.play();
            }else if(result == 2){
                jumpSound2.play();
            }else if(result == 3){
                jumpSound3.play();
            }
        }

        if (currentState == State.SLIDE_START && previousState != State.SLIDE_START) {
            Random r = new Random();
            int result = r.nextInt(4-1) + 1;

            if(result == 1){
                slideSound1.play();
            }else if(result == 2){
                slideSound2.play();
            }else if(result == 3){
                slideSound3.play();
            }
        }

        if (currentState == State.FAIL && previousState != State.FAIL) {
            failSound.play();
        }

    }

    private TextureRegion getFramesFromAnimation(float delta) {

        //store the current state as 'previous state'
        previousState = currentState;

        //update current state
        getState(delta);
        TextureRegion returnRegion = null;

        if (currentState == State.FAIL) {
            returnRegion = (TextureRegion) fail.getKeyFrame(stateTimer, false);
        } else if (currentState == State.JUMP_START) {
            returnRegion = (TextureRegion) jumpStart.getKeyFrame(stateTimer, false);
        } else if (currentState == State.JUMP_END) {
            returnRegion = (TextureRegion) jumpEnd.getKeyFrame(stateTimer, false);
        } else if (currentState == State.SLIDE_START) {
            returnRegion = (TextureRegion) slideStart.getKeyFrame(stateTimer, false);
        } else if (currentState == State.SLIDE_END) {
            returnRegion = (TextureRegion) slideEnd.getKeyFrame(stateTimer, false);
        } else {
            returnRegion = (TextureRegion) running.getKeyFrame(stateTimer, true);
        }

        if (currentState != previousState) {
            stateTimer = 0;
        } else {
            stateTimer += delta;
        }

        return returnRegion;
    }

    public void getState(float delta) {
        if (playerIsDead) {
            currentState = State.FAIL;
        } else {
            if (slideStartTimer == 0 && slideEndTimer == 0) {
                if (box2dBody.getLinearVelocity().y > 0) {
                    currentState = State.JUMP_START;
                } else if (box2dBody.getLinearVelocity().y < 0) {
                    currentState = State.JUMP_END;
                } else {
                    currentState = State.RUN;
                }
            } else {
                if (currentState == State.SLIDE_START) {
                    if (slideStartTimer > 0) {
                        slideStartTimer -= delta;
                    } else {
                        slideStartTimer = 0;
                        currentState = State.SLIDE_END;
                    }
                } else if (currentState == State.SLIDE_END) {
                    if (slideEndTimer > 0) {
                        slideEndTimer -= delta;
                    } else {
                        slideEndTimer = 0;
                        currentState = State.RUN;
                    }
                }
            }
        }
    }

    public void updateBodyAndFixture() {

        //if currently sliding and previously wasn't (reduce box height to slide under enemies)
        if ((currentState == State.SLIDE_START || currentState == State.SLIDE_END) &&
                (previousState != State.SLIDE_START && previousState != State.SLIDE_END)) {

            Vector2 currentPosition = box2dBody.getPosition();
            world.destroyBody(box2dBody);

            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(currentPosition);

            bodyDef.type = BodyDef.BodyType.DynamicBody;
            box2dBody = world.createBody(bodyDef);

            fixtureDef = new FixtureDef();
            shape = new PolygonShape();
            shape.setAsBox(1, (float) 0.8);
            fixtureDef.filter.categoryBits = Player.PLAYER;
            fixtureDef.filter.maskBits = Player.DEFAULT | Player.WORLD | Player.ENEMY | Player.JEWEL;
            fixtureDef.shape = shape;

            box2dBody.createFixture(fixtureDef).setUserData(this);
            box2dBody.applyLinearImpulse(new Vector2(15f, 0), box2dBody.getWorldCenter(), true);

            //if currently running and was previously sliding or jumping (return to normal box height)
        } else if ((currentState == State.RUN) &&
                (previousState == State.SLIDE_START || previousState == State.SLIDE_END)) {

            Vector2 currentPosition = box2dBody.getPosition();
            currentPosition.y = currentPosition.y + 2;

            world.destroyBody(box2dBody);

            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(currentPosition);

            bodyDef.type = BodyDef.BodyType.DynamicBody;
            box2dBody = world.createBody(bodyDef);

            fixtureDef = new FixtureDef();
            shape = new PolygonShape();
            shape.setAsBox(1, (float) 2.1);
            fixtureDef.filter.categoryBits = Player.PLAYER;
            fixtureDef.filter.maskBits = Player.DEFAULT | Player.WORLD | Player.ENEMY | Player.JEWEL;
            fixtureDef.shape = shape;

            box2dBody.createFixture(fixtureDef).setUserData(this);
            box2dBody.applyLinearImpulse(new Vector2(15f, 0), box2dBody.getWorldCenter(), true);

        } else if ((currentState == State.RUN) &&
                (previousState == State.JUMP_START || previousState == State.JUMP_END)) {

            Vector2 currentPosition = box2dBody.getPosition();

            world.destroyBody(box2dBody);

            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(currentPosition);

            bodyDef.type = BodyDef.BodyType.DynamicBody;
            box2dBody = world.createBody(bodyDef);

            fixtureDef = new FixtureDef();
            shape = new PolygonShape();
            shape.setAsBox(1, (float) 2.1);
            fixtureDef.filter.categoryBits = Player.PLAYER;
            fixtureDef.filter.maskBits = Player.DEFAULT | Player.WORLD | Player.ENEMY | Player.JEWEL;
            fixtureDef.shape = shape;

            box2dBody.createFixture(fixtureDef).setUserData(this);
            box2dBody.applyLinearImpulse(new Vector2(15f, 0), box2dBody.getWorldCenter(), true);
        }

    }

    public void jump() {
        if (currentState == State.RUN) {
            box2dBody.applyLinearImpulse(new Vector2(0, 39f), box2dBody.getWorldCenter(), true);
            currentState = State.JUMP_START;
        }
    }

    public void slide() {
        if (currentState == State.RUN) {
            currentState = State.SLIDE_START;
            slideStartTimer = 1.3;
            slideEndTimer = 0.1;

        }
    }

    public State getState() {
        return currentState;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public void setCurrentState(State state) {
        currentState = state;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public boolean getPlayerIsDead() {
        return playerIsDead;
    }

    public TiledMapTileLayer.Cell getCellPlayerIsOn(String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer) playScreen.getMap().getLayers().get(layerName);
        return layer.getCell((int) 10, (int) 15);
    }

}
