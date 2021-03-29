package com.semtb001.individual.assignement.sprites;

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
import com.semtb001.individual.assignement.screens.PlayScreen;

public class GroundEnemy extends Sprite{

    private World world;
    private PlayScreen playScreen;

    private float stateTimer;

    public Body box2dBody;
    public SpriteBatch batch;

    private Animation slimeSliding;
    private Vector2 pos;

    public TextureRegion currentFrame;

    public GroundEnemy(World world, PlayScreen playScreen, Vector2 pos) {
        this.world = world;
        this.playScreen = playScreen;
        this.pos = pos;
        stateTimer = 0;
        batch = new SpriteBatch();

        definePlayer();

        Array<TextureRegion> tempFrames = new Array<TextureRegion>();
        //get run animation frames and add them to marioRun Animation
        for(int i = 0; i < 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("slime"),  i* 160, 0, 160, 80));
        }
        slimeSliding = new Animation(0.1f, tempFrames);

        tempFrames.clear();

    }

    public void definePlayer(){
        BodyDef bodyDef = new BodyDef();
        Rectangle rect = new Rectangle();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.filter.categoryBits = Player.ENEMY;
        fixtureDef.filter.maskBits = Player.PLAYER | Player.WORLD;

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.x / 32, pos.y / 32);

        box2dBody = world.createBody(bodyDef);

        shape.setAsBox(1, 1);

        fixtureDef.shape = shape;

        box2dBody.createFixture(fixtureDef).setUserData(this);

    }

    public void update(float delta){
        stateTimer += delta;
        currentFrame = (TextureRegion)slimeSliding.getKeyFrame(stateTimer, true);

        if (box2dBody.getLinearVelocity().x >= -5f) {
            box2dBody.applyLinearImpulse(new Vector2(-0.5f, 0), box2dBody.getWorldCenter(), true);
        }
    }

    private TextureRegion getFramesFromAnimation(float delta) {
        TextureRegion returnRegion = null;
        return returnRegion;
    }

}
