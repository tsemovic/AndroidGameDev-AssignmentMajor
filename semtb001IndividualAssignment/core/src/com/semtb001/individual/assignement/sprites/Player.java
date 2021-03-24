package com.semtb001.individual.assignement.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class Player {

    private World world;
    private PlayScreen playScreen;

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


        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(50, 50);

        box2dBody = world.createBody(bodyDef);
    }
}
