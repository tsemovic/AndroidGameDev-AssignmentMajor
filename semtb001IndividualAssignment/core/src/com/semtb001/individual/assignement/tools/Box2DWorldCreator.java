package com.semtb001.individual.assignement.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.semtb001.individual.assignement.Semtb001IndividualAssignment;
import com.semtb001.individual.assignement.screens.PlayScreen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import sun.awt.image.ImageWatched;

public class Box2DWorldCreator {

    private World world;
    private TiledMap map;
    private BodyDef bodyDef;
    private PolygonShape shape;
    private FixtureDef fixtureDef;
    private Body body;
    private PlayScreen screen;

    private Queue<Vector2> groundEnemyPositions;

    public Box2DWorldCreator(PlayScreen playScreen) {
        world = playScreen.getWorld();
        map = playScreen.getMap();
        screen = playScreen;

        bodyDef = new BodyDef();
        shape = new PolygonShape();
        fixtureDef = new FixtureDef();

        groundEnemyPositions = new LinkedList<Vector2>();

        //create map ground
        for (MapObject object : map.getLayers().get("groundObject").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / Semtb001IndividualAssignment.PPM, (rect.getY() + rect.getHeight() / 2) / Semtb001IndividualAssignment.PPM);

            body = world.createBody(bodyDef);
            shape.setAsBox((rect.getWidth() / 2) / Semtb001IndividualAssignment.PPM, (rect.getHeight() / 2) / Semtb001IndividualAssignment.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef).setUserData("WORLD");
        }

        //create map objects
        for (MapObject object : map.getLayers().get("objectObject").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / Semtb001IndividualAssignment.PPM, (rect.getY() + rect.getHeight() / 2) / Semtb001IndividualAssignment.PPM);

            body = world.createBody(bodyDef);
            shape.setAsBox((rect.getWidth() / 2) / Semtb001IndividualAssignment.PPM, (rect.getHeight() / 2) / Semtb001IndividualAssignment.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef).setUserData("OBJECT");
        }

        //set world end position
        for (MapObject object : map.getLayers().get("worldEndPosition").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.setWorldEndPosition(rect.x);
        }

        //get enemies
        for (MapObject object : map.getLayers().get("slimePositions").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            groundEnemyPositions.offer(new Vector2(rect.x, rect.y));
        }
    }

    public Queue<Vector2> getGroundEnemyPositions(){
        return groundEnemyPositions;
    }

}
