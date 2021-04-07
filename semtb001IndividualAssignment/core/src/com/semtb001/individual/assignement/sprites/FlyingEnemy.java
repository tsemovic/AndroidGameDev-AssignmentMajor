package com.semtb001.individual.assignement.sprites;

import com.badlogic.gdx.audio.Music;
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
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;
import com.semtb001.individual.assignement.tools.Assets;

public class FlyingEnemy extends Sprite{

    private World world;
    private PlayScreen playScreen;

    private float stateTimer;

    public Body box2dBody;
    public SpriteBatch batch;

    private Animation flyFlying;
    private Vector2 pos;

    public TextureRegion currentFrame;

    private Music enemySound;

    public FlyingEnemy(World world, PlayScreen playScreen, Vector2 pos) {
        this.world = world;
        this.playScreen = playScreen;
        this.pos = pos;
        stateTimer = 0;
        batch = new SpriteBatch();

        defineEnemy();

        Array<TextureRegion> tempFrames = new Array<TextureRegion>();
        //get run animation frames and add them to marioRun Animation
        for(int i = 0; i < 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("bee"),  0, 0, 56, 48));
        }
        //get run animation frames and add them to marioRun Animation
        for(int i = 0; i < 2; i++) {
            tempFrames.add(new TextureRegion(playScreen.textureAtlas.findRegion("bee_fly"),  0, 0, 56, 48));
        }
        flyFlying = new Animation(0.1f, tempFrames);

        tempFrames.clear();

        box2dBody.setGravityScale(0f);
        box2dBody.setLinearVelocity(new Vector2(-.01f, 0));

        enemySound = Semtb001IndividualAssignment.assetManager.manager.get(Assets.bee);
        enemySound.play();
        enemySound.setLooping(true);
    }

    public void defineEnemy(){
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.filter.categoryBits = Player.ENEMY;
        fixtureDef.filter.maskBits = Player.PLAYER | Player.WORLD | Player.DEFAULT;

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.x / 32, pos.y / 32);

        fixtureDef.isSensor = true;

        box2dBody = world.createBody(bodyDef);

        shape.setAsBox(1, 1);

        fixtureDef.shape = shape;

        box2dBody.createFixture(fixtureDef).setUserData(this);
    }

    public void update(float delta){
        stateTimer += delta;
        currentFrame = (TextureRegion)flyFlying.getKeyFrame(stateTimer, true);

        if (box2dBody.getLinearVelocity().x >= -5f) {
            box2dBody.applyLinearImpulse(new Vector2(-0.5f, 0), box2dBody.getWorldCenter(), true);
        }

        if(playScreen.getPlayer().box2dBody.getPosition().x < box2dBody.getPosition().x) {
            if (playScreen.getPlayer().box2dBody.getPosition().x + 10 > box2dBody.getPosition().x) {
                enemySound.play();
                enemySound.setVolume(1f);
            } else if (playScreen.getPlayer().box2dBody.getPosition().x + 20 > box2dBody.getPosition().x) {
                enemySound.play();
                enemySound.setVolume(0.7f);
            } else if (playScreen.getPlayer().box2dBody.getPosition().x + 30 > box2dBody.getPosition().x) {
                enemySound.play();
                enemySound.setVolume(0.4f);
            } else if (playScreen.getPlayer().box2dBody.getPosition().x + 40 > box2dBody.getPosition().x) {
                enemySound.play();
                enemySound.setVolume(0.1f);
            }else{
                enemySound.setVolume(0.0f);
            }
        }else{
            if (playScreen.getPlayer().box2dBody.getPosition().x < box2dBody.getPosition().x + 2) {
                enemySound.play();
                enemySound.setVolume(0.5f);
            }else if (playScreen.getPlayer().box2dBody.getPosition().x < box2dBody.getPosition().x + 10) {
                enemySound.play();
                enemySound.setVolume(0.2f);
            }else{
                enemySound.setVolume(0.0f);

            }
        }
    }

    private TextureRegion getFramesFromAnimation(float delta) {
        TextureRegion returnRegion = null;
        return returnRegion;
    }

    public void stopSound() {
        enemySound.stop();
    }
}
