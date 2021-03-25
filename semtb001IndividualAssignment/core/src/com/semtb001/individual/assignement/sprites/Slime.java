package com.semtb001.individual.assignement.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class Slime extends Sprite{

    private World world;
    private PlayScreen playScreen;

    private enum State {RUN, JUMP, SLIDE, FAIL};
    private State currentState;
    private float stateTimer;

    public Body box2dBody;
    public SpriteBatch batch;

    private Animation running;
    public TextureRegion currentFrame;



    public Slime(World world, PlayScreen playScreen) {
        this.world = world;
        this.playScreen = playScreen;
        stateTimer = 0;
        currentState = State.RUN;
        batch = new SpriteBatch();

        definePlayer();

        Array<TextureRegion> tempFrames = new Array<TextureRegion>();
        //get run animation frames and add them to marioRun Animation
        for(int i = 1; i < 4; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("running"),  i* 256, 0, 256, 256));
        }
        running = new Animation(0.1f, tempFrames);

        tempFrames.clear();

    }

    public void definePlayer(){
        BodyDef bodyDef = new BodyDef();
        Rectangle rect = new Rectangle();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.filter.categoryBits = Player.ENEMY;
        //fixtureDef.filter.maskBits = Player.PLAYER | Player.WORLD;

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(160, 250);

        box2dBody = world.createBody(bodyDef);

        shape.setAsBox(40, 40);
        //CircleShape cir = new CircleShape();
        //cir.setRadius(15);
        //shape.setAsBox(5/IslandSurvival.PPM, 5/IslandSurvival.PPM);
        //shape.setAsBox((rect.getWidth() / 2), (rect.getHeight() / 2));
        //fixtureDef.shape = cir;
        fixtureDef.shape = shape;
        box2dBody.createFixture(fixtureDef).setUserData(this);

    }

    public void update(float delta){
        stateTimer += delta;
        //setRegion(getFramesFromAnimation(delta));
        currentFrame = (TextureRegion)running.getKeyFrame(stateTimer, true);

        //setRegion((TextureRegion) running.getKeyFrame(stateTimer, true));
    }

    private TextureRegion getFramesFromAnimation(float delta) {
        TextureRegion returnRegion = null;

        switch(currentState){
            case RUN:
                returnRegion = (TextureRegion) running.getKeyFrame(stateTimer, true);
        }



        return returnRegion;
    }

    public void jump(){
            box2dBody.applyLinearImpulse(new Vector2(0, 4f), box2dBody.getWorldCenter(), true);
            //currentState = State.JUMPING;

    }

    public void slide(){
        box2dBody.applyLinearImpulse(new Vector2(0, 4f), box2dBody.getWorldCenter(), true);
            //currentState = State.JUMPING;

    }
}
