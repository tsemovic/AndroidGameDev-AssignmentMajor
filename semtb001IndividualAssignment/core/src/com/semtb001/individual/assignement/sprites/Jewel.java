package com.semtb001.individual.assignement.sprites;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;
import com.semtb001.individual.assignement.tools.Assets;

public class Jewel {

    private Rectangle position;
    public Body box2dBody;
    private World world;
    private PlayScreen playScreen;
    private Animation texture;
    public TextureRegion currentFrame;

    public FixtureDef fixtureDef;
    private float stateTimer;

    public boolean collected;


    public Jewel(Rectangle position, PlayScreen screen){
        this.position = position;
        world = screen.getWorld();
        playScreen = screen;

        stateTimer = 0;
        collected = false;

        BodyDef bodyDef = new BodyDef();
        Rectangle rect = new Rectangle();
        CircleShape shape = new CircleShape();
        fixtureDef = new FixtureDef();

        fixtureDef.isSensor = true;

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x / 32, position.y / 32);

        box2dBody = world.createBody(bodyDef);

        shape.setRadius(1);
        fixtureDef.shape = shape;

        box2dBody.createFixture(fixtureDef).setUserData(this);

        Array<TextureRegion> tempFrames = new Array<TextureRegion>();

        //texture
        for (int i = 1; i <= 3; i++) {
            tempFrames.add(new TextureRegion(new Texture("jewel.png"), i * 35, 0, 35, 35));
        }
        texture = new Animation(0.4f, tempFrames);
        tempFrames.clear();
    }

    public void update(float delta) {
        if(!collected) {
            stateTimer += delta;
            currentFrame = (TextureRegion) texture.getKeyFrame(stateTimer, true);
        }
    }

    public void hit(){
        if(!collected) {
            fixtureDef.filter.maskBits = Player.DESTROYED;
            collected = true;
            playScreen.updateCollectedJewels();
            Sound jewel = Semtb001IndividualAssignment.assetManager.manager.get(Assets.jewel);
            jewel.play();
        }
    }
}
