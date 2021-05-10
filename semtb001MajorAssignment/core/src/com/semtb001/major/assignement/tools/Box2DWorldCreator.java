package com.semtb001.major.assignement.tools;

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
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.screens.PlayScreen;
import com.semtb001.major.assignement.sprites.Coin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Class for creating Box2D map objects
public class Box2DWorldCreator {

    // Game world and map objects
    private World world;
    private TiledMap map;

    // Box2D objects
    private BodyDef bodyDef;
    private PolygonShape shape;
    private FixtureDef fixtureDef;
    private Body body;

    // List's of ground enemies, flying enemies and coins
    private Queue<Vector2> groundEnemyPositions;
    private Queue<Vector2> flyingEnemyPositions;
    private Queue<ScreenShaker> screenShakers;
    private List<Coin> coins;

    public Box2DWorldCreator(PlayScreen playScreen) {

        // Instantiate gmae world and map objects
        world = playScreen.getWorld();
        map = playScreen.getMap();

        // Instantiate Box2D objects
        bodyDef = new BodyDef();
        shape = new PolygonShape();
        fixtureDef = new FixtureDef();

        // Instantiate List's of ground enemies, flying enemies and coins
        groundEnemyPositions = new LinkedList<Vector2>();
        flyingEnemyPositions = new LinkedList<Vector2>();
        screenShakers = new LinkedList<ScreenShaker>();
        coins = new ArrayList<Coin>();

        // Create world ground and objects (floor of map and boxes/platforms)
        for (MapObject object : map.getLayers().get("groundObject").getObjects().getByType(RectangleMapObject.class)) {

            // Make the objects static (doesn't move)
            bodyDef.type = BodyDef.BodyType.StaticBody;

            // Set the position of the world ground and object fixtures in the map file
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / Semtb001MajorAssignment.PPM, (rect.getY() + rect.getHeight() / 2) / Semtb001MajorAssignment.PPM);

            // Create the body in the world
            body = world.createBody(bodyDef);

            // Setup the shape of the object
            shape.setAsBox((rect.getWidth() / 2) / Semtb001MajorAssignment.PPM, (rect.getHeight() / 2) / Semtb001MajorAssignment.PPM);
            fixtureDef.shape = shape;

            // Set the categoryBits of the object to "WORLD" (so player can collide with them)
            fixtureDef.filter.categoryBits = Semtb001MajorAssignment.WORLD;

            // Add the fixture to the body object
            body.createFixture(fixtureDef);
        }

        // Create map coins
        for (MapObject object : map.getLayers().get("coinObject").getObjects().getByType(RectangleMapObject.class)) {

            // Get the size and position of the coin object in the map file
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            // Create a new coin in that position
            Coin newCoin = new Coin(rect, playScreen);

            // Add the coin to the coins list
            coins.add(newCoin);
        }

        // Create world end position
        for (MapObject object : map.getLayers().get("worldEndPosition").getObjects().getByType(RectangleMapObject.class)) {

            // Get the size and position of the 'worldEnd' object in the map file
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            // Set the world end position
            playScreen.setWorldEndPosition(rect.x);
        }

        // Create map shakers
        for (MapObject object : map.getLayers().get("ScreenShakerPositions").getObjects().getByType(RectangleMapObject.class)) {

            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            screenShakers.offer(new ScreenShaker(playScreen, rect));
        }

        // Create grounded enemies
        for (MapObject object : map.getLayers().get("groundEnemyPositions").getObjects().getByType(RectangleMapObject.class)) {

            // Add the grounded enemy position to the groundedEnemyPositions list
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            groundEnemyPositions.offer(new Vector2(rect.x, rect.y));
        }

        // Create flying enemies
        for (MapObject object : map.getLayers().get("flyingEnemyPositions").getObjects().getByType(RectangleMapObject.class)) {

            // Add the flying enemy position to the flyingEnemyPositions list
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            flyingEnemyPositions.offer(new Vector2(rect.x, rect.y));
        }
    }

    //Getters for the list's of enemys and coins
    public Queue<Vector2> getGroundEnemyPositions() {
        return groundEnemyPositions;
    }

    public Queue<Vector2> getFlyingEnemyPositions() {
        return flyingEnemyPositions;
    }

    public Queue<ScreenShaker> getScreenShakerPositions() {
        return screenShakers;
    }

    public List<Coin> getCoins() {
        return coins;
    }

}
