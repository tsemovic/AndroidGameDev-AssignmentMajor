package com.semtb001.individual.assignement.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class Player {

    private World world;
    private PlayScreen playScreen;

    public static final int DESTROYED = 0;
    public static final int DEFAULT = 1;
    public static final int PLAYER = 2;
    public static final int WORLD = 3;

    private enum State {RUN, JUMP, SLIDE, FAIL};
    private State currentState;

    public Body box2dBody;
    private SpriteBatch batch;
    private Sprite sprite;

    public Player(World world, PlayScreen playScreen) {
        this.world = world;
        this.playScreen = playScreen;

        definePlayer();
    }

    public void definePlayer(){
        BodyDef bodyDef = new BodyDef();
        Rectangle rect = new Rectangle();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        currentState = State.RUN;

        fixtureDef.filter.categoryBits = Player.PLAYER;
        fixtureDef.filter.maskBits = Player.DEFAULT | Player.WORLD;

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(10, 250);

        box2dBody = world.createBody(bodyDef);

        shape.setAsBox(5, 10);
        //CircleShape cir = new CircleShape();
        //cir.setRadius(15);
        //shape.setAsBox(5/IslandSurvival.PPM, 5/IslandSurvival.PPM);
        //shape.setAsBox((rect.getWidth() / 2), (rect.getHeight() / 2));
        //fixtureDef.shape = cir;
        fixtureDef.shape = shape;
        box2dBody.createFixture(fixtureDef).setUserData("PLAYER");



    }

    public void update(float delta){
        System.out.println(box2dBody.getPosition());
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
