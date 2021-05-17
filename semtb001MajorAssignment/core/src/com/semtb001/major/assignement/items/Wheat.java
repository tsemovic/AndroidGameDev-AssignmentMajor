package com.semtb001.major.assignement.items;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.screens.PlayScreen;

import java.util.Random;

// Wheat class
public class Wheat {

    // Variables for counting time
    public float timeCount;
    public int timeAlive;

    // Boolean to track if the wheat has been destroyed
    public boolean destroyed;

    // Wheat's growth stage
    private enum Stage {SMALL, MEDIUM, LARGE}

    private Stage growth;

    // Wheat's tile
    public TiledMapTile tile;
    private TiledMapTileSet tileSet;

    // Rectangle bounds of the wheat (position)
    public Rectangle bounds;

    // PlayScreen, World and Map objects
    private PlayScreen screen;
    private World world;
    private Map map;

    // Wheat Box2D objects
    private Body body;
    private Fixture fixture;

    // boolean to track if the wheat has water
    private boolean hasWater;

    // The number of seeds that the wheat gives back once harvested
    public int drops;

    public Wheat(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {

        // Instantiate PlayScreen, World and Map objects
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        // BodyDef and FixtureDef for Box2D
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        // Make the object static (doesn't move)
        bdef.type = BodyDef.BodyType.StaticBody;

        // Set the position of the Box2D body to the bounds of the wheat (position)
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / Semtb001MajorAssignment.PPM, (bounds.getY() + bounds.getHeight() / 2) / Semtb001MajorAssignment.PPM);

        // Create the body in the world
        body = world.createBody(bdef);

        // Set the size of the wheat to 1x1
        shape.setAsBox(bounds.getWidth() / 2 / Semtb001MajorAssignment.PPM, bounds.getHeight() / 2 / Semtb001MajorAssignment.PPM);
        fdef.shape = shape;

        // Add the fixtureDef to the body
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);

        // Instantiate timers
        timeCount = 0;
        timeAlive = 0;

        // Instantiate the Wheat growth stage
        growth = Stage.SMALL;

        // Set the tile for the wheat (initially 'WHEAT_SMALL')
        this.screen = screen;
        this.bounds = bounds;
        tileSet = map.getTileSets().getTileSet(0);
        tile = tileSet.getTile(Semtb001MajorAssignment.WHEAT_SMALL);

        // Setup the drop to a random number between 1 and 3 (Number of seeds given to player on harvest)
        drops = new Random().nextInt(3 - 1) + 1;

        // Update the tile
        updateTile();

    }

    // Method to update the wheat tile
    public void update(float dt) {

        // If the wheat has water nearby
        if (hasWater) {

            // Increase the time alive
            if (destroyed != true) {
                timeCount += dt;
                if (timeCount >= 1) {
                    timeCount = 0;
                    timeAlive += 1;
                }
            }

            // If the growth stage is 'SMALL' and has been for 5 seconds
            if (growth == Stage.SMALL) {
                if (timeAlive > 5) {

                    // Set the growth stage to 'MEDIUM'
                    growth = Stage.MEDIUM;

                    // Update the tile to the 'WHEAT_MEDIUM' tile
                    tile = tileSet.getTile(Semtb001MajorAssignment.WHEAT_MEDIUM);
                    updateTile();
                }

                // If the growth stage is 'MEDIUM' and has been for 5 seconds
            } else if (growth == Stage.MEDIUM) {
                if (timeAlive > 10) {

                    // Set the growth stage to 'LARGE'
                    growth = Stage.LARGE;

                    // Update the tile to the 'WHEAT_LARGE' tile
                    tile = tileSet.getTile(Semtb001MajorAssignment.WHEAT_LARGE);
                    updateTile();
                }
            }
        }
    }

    // Method to update the tile
    public void updateTile() {
        screen.getCell("seeds", bounds).setTile(tile);
    }

    // Method to check if the wheat has water nearby
    public void updateWater() {

        // Wheat position in the form of a vector
        Vector2 pos = new Vector2(bounds.x, bounds.y);

        // Loop through all surrounding 3x3 tiles in the water layer
        for (TiledMapTileLayer.Cell cell : screen.getSurroundingCells3x3("water", pos)) {

            // If any of these tiles are sources of water
            if (cell.getTile().getId() == Semtb001MajorAssignment.WATER) {

                // update the hasWater boolean to true
                hasWater = true;
            }
        }

    }

}
