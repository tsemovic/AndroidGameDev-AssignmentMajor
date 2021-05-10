package com.semtb001.major.assignement.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.items.Wheat;
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

    private PlayScreen screen;

    public List<Wheat> wheat;


    public Box2DWorldCreator(PlayScreen playScreen) {

        // Instantiate gmae world and map objects
        world = playScreen.getWorld();
        map = playScreen.getMap();
        screen = playScreen;

        // Instantiate Box2D objects
        bodyDef = new BodyDef();
        shape = new PolygonShape();
        fixtureDef = new FixtureDef();

        // Instantiate List's of ground enemies, flying enemies and coins
        groundEnemyPositions = new LinkedList<Vector2>();
        flyingEnemyPositions = new LinkedList<Vector2>();
        screenShakers = new LinkedList<ScreenShaker>();
        coins = new ArrayList<Coin>();

        wheat = new ArrayList<Wheat>();


        // Create world ground and objects (floor of map and boxes/platforms)
        for (MapObject object : map.getLayers().get("waterObject").getObjects().getByType(RectangleMapObject.class)) {

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

//        // Create grounded enemies
//        for (MapObject object : map.getLayers().get("groundEnemyPositions").getObjects().getByType(RectangleMapObject.class)) {
//
//            // Add the grounded enemy position to the groundedEnemyPositions list
//            Rectangle rect = ((RectangleMapObject) object).getRectangle();
//            groundEnemyPositions.offer(new Vector2(rect.x, rect.y));
//        }
    }

    public void createWheat() {
        Rectangle rect = new Rectangle();
        rect.set(screen.getPlayerPos().x, screen.getPlayerPos().y, 1, 1);
        Wheat newWheat = new Wheat(world, map, rect, screen);
        wheat.add(newWheat);
    }


    public void harvestWheat() {
        TiledMapTileSet tileSet = map.getTileSets().getTileSet(0);
        if (screen.getCell("seeds").getTile() == tileSet.getTile(426)) {
            Wheat removeWheat = null;
            for (Wheat w : wheat) {
                if ((w.rectangle.x == (int) (screen.player.box2dBody.getPosition().x * Semtb001MajorAssignment.PPM / 32)) &&
                        (w.rectangle.y == (int) (screen.player.box2dBody.getPosition().y * Semtb001MajorAssignment.PPM / 32))) {
                    screen.player.addInventory("seeds", w.drops);
                    removeWheat = w;
                    w.destroyed = true;
                    w.dispose();
                    screen.getHud().wheatHarvested();
                    screen.getCell("seeds").setTile(tileSet.getTile(231));
                }
            }
            wheat.remove(removeWheat);
        }
    }

//    public void destoryWheat(Sheep s) {
//        TiledMapTileSet tileSet = gameMap.getTileSets().getTileSet(0);
//        Vector2 pos = new Vector2((int) (s.b2body.getPosition().x * IslandSurvival.PPM / 32),
//                (int) (s.b2body.getPosition().y * IslandSurvival.PPM / 32));
//        TiledMapTileLayer layer = (TiledMapTileLayer) gameMap.getLayers().get("seeds");
//        TiledMapTileLayer.Cell cell = layer.getCell((int) (s.b2body.getPosition().x * IslandSurvival.PPM / 32),
//                (int) (s.b2body.getPosition().y * IslandSurvival.PPM / 32));
//        Wheat removeWheat = null;
//
//        for (Wheat w : gameScreen.getCreator().wheat) {
//            if (w.rectangle.getX() == pos.x && w.rectangle.getY() == pos.y) {
//                removeWheat = w;
//                w.destroyed = true;
//                w.dispose();
//                cell.setTile(tileSet.getTile(231));
//
//            }
//
//        }
//        wheat.remove(removeWheat);
//    }

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