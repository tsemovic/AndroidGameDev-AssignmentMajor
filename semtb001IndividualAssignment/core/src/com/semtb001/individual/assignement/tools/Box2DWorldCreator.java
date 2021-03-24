package com.semtb001.individual.assignement.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;

public class Box2DWorldCreator {

    private World world;
    private TiledMap map;
    private BodyDef bodyDef;
    private PolygonShape shape;
    private FixtureDef fixtureDef;
    private Body body;
    private PlayScreen screen;

    public Box2DWorldCreator(PlayScreen playScreen) {
        world = playScreen.getWorld();
        map = playScreen.getMap();
        screen = playScreen;

        bodyDef = new BodyDef();
        shape = new PolygonShape();
        fixtureDef = new FixtureDef();


        //create map walls
        for (MapObject object : map.getLayers().get("groundObject").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            System.out.println("HERERE");
            System.out.println(rect.getX());
            System.out.println(rect.getY());
            System.out.println(rect.getHeight());

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / Semtb001IndividualAssignment.PPM, (rect.getY() + rect.getHeight() / 2) / Semtb001IndividualAssignment.PPM);

            body = world.createBody(bodyDef);
            shape.setAsBox((rect.getWidth() / 2) / Semtb001IndividualAssignment.PPM, (rect.getHeight() / 2) / Semtb001IndividualAssignment.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef).setUserData("WORLD");
        }
    }

}
