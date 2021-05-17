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
import com.semtb001.major.assignement.sprites.Player;
import com.semtb001.major.assignement.sprites.Sheep;

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

    // List's of sheep positions and wheat
    private Queue<Vector2> sheepPositions;
    private List<Wheat> wheat;

    // PlayScreen object
    private PlayScreen screen;

    public Box2DWorldCreator(PlayScreen playScreen) {

        // Instantiate gmae world and map objects
        world = playScreen.getWorld();
        map = playScreen.getMap();
        screen = playScreen;

        // Instantiate Box2D objects
        bodyDef = new BodyDef();
        shape = new PolygonShape();
        fixtureDef = new FixtureDef();

        // Instantiate List's of sheep and wheat
        sheepPositions = new LinkedList<Vector2>();
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

        // Create sheep
        for (MapObject object : map.getLayers().get("sheepObject").getObjects().getByType(RectangleMapObject.class)) {

            // Add the sheep position to the sheepPositions list
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            sheepPositions.offer(new Vector2(rect.x, rect.y));
        }
        
    }

    // Method to create wheat
    public void createWheat() {

        // Create wheat at the players position
        Rectangle rect = new Rectangle();
        rect.set(screen.getPlayerPos().x, screen.getPlayerPos().y, 1, 1);
        Wheat newWheat = new Wheat(world, map, rect, screen);

        // Add the new wheat to the list of wheat
        wheat.add(newWheat);
    }

    // Method to harvest the wheat
    public void harvestWheat() {

        // If the player is standing on fully grown wheat
        TiledMapTileSet tileSet = map.getTileSets().getTileSet(0);
        if (screen.getCell("seeds").getTile() == tileSet.getTile(Semtb001MajorAssignment.WHEAT_LARGE)) {

            // Loop through the list of wheat
            Wheat tempWheat = null;
            for (Wheat wheatToRemove : wheat) {

                // If the wheat has the same position as the player
                if ((wheatToRemove.bounds.x == (int) (screen.player.getBox2dBody().getPosition().x * Semtb001MajorAssignment.PPM / 32)) &&
                        (wheatToRemove.bounds.y == (int) (screen.player.getBox2dBody().getPosition().y * Semtb001MajorAssignment.PPM / 32))) {
                    screen.player.addInventory("seeds", wheatToRemove.getDrops());

                    // Set the tempWheat to the wheat object
                    tempWheat = wheatToRemove;
                    wheatToRemove.setDestroyed(true);

                    // Add wheat harvested (increases counter)
                    screen.addWheatHarvested();

                    // Set the wheat tile to 'blank'
                    screen.getCell("seeds").setTile(tileSet.getTile(Semtb001MajorAssignment.BLANK));

                    // Change the players state to 'HOE'
                    screen.getPlayer().setCurrentState(Player.State.HOE);
                    screen.getPlayer().resetStateTimer();

                    // Play the player item sound
                    screen.getPlayer().playItemSound();
                }
            }

            // Remove the harvested wheat from the wheat list
            wheat.remove(tempWheat);
        }
    }

    // Method to destroy wheat (when a sheep eats it)
    public void destroyWheat(Sheep sheep) {
        TiledMapTileSet tileSet = map.getTileSets().getTileSet(0);

        // Get the sheep position
        Vector2 pos = new Vector2((int) (sheep.getBox2dBody().getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int) (sheep.getBox2dBody().getPosition().y * Semtb001MajorAssignment.PPM / 32));

        // Get the wheat cell that the sheep is standing on
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("seeds");
        TiledMapTileLayer.Cell cell = layer.getCell((int) (sheep.getBox2dBody().getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int) (sheep.getBox2dBody().getPosition().y * Semtb001MajorAssignment.PPM / 32));

        // Instantiate the wheat to remove
        Wheat removeWheat = null;

        // Loop through the list of wheat
        for (Wheat tempWheat : screen.getBox2dWorldCreator().wheat) {

            // If the wheat has the same position as the sheep
            if (tempWheat.bounds.getX() == pos.x && tempWheat.bounds.getY() == pos.y) {

                // Set the removeWheat to the tempWheat
                removeWheat = tempWheat;
                tempWheat.setDestroyed(true);

                // Set the cell that the wheat was on to 'blank'
                cell.setTile(tileSet.getTile(Semtb001MajorAssignment.BLANK));

            }
        }

        // Remove the wheat from the wheat list
        wheat.remove(removeWheat);
    }

    //Getters for the list's of sheep and wheat
    public Queue<Vector2> getSheepPositions() {
        return sheepPositions;
    }

    public List<Wheat> getWheat() {
        return wheat;
    }
}
