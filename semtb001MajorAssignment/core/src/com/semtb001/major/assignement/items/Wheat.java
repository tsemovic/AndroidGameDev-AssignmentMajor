package com.semtb001.major.assignement.items;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.screens.PlayScreen;
import com.semtb001.major.assignement.sprites.Player;
import com.semtb001.major.assignement.tools.InteractiveTileObjects;

import java.util.Random;

public class Wheat extends InteractiveTileObjects implements Disposable {

    public float timeCount;
    public int timeAlive;
    public boolean destroyed;
    public Rectangle rectangle;
    public int growth;
    public TiledMapTile tile;
    private TiledMapTileSet tileSet;
    private PlayScreen gameScreen;
    private boolean hasWater;
    public int drops;

    public Wheat(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        super(world, map, bounds);

        fixture.setUserData(this);
        timeCount = 0;
        growth = 0;
        gameScreen = screen;
        rectangle = bounds;
        tileSet = map.getTileSets().getTileSet(0);
        tile = tileSet.getTile(Semtb001MajorAssignment.WHEAT_SMALL);
        updateTile();

        //when wheat is fully grown drop between 1 and 3 wheat;
        drops = new Random().nextInt(3-1) + 1;
    }

    @Override
    public void onCollision(Player userData) {
        //System.out.println("wheat");
    }

    @Override
    public void onCollision() {
//        System.out.println("wheat");
    }

    public void update(float dt) {
        if(hasWater) {
            if (destroyed != true) {
                timeCount += dt;
                if (timeCount >= 1) {
                    timeCount = 0;
                    timeAlive += 1;
                }
            }

            if (growth == 0) {
                if (timeAlive > 5) {
                    growth = 1;
                    tile = tileSet.getTile(Semtb001MajorAssignment.WHEAT_MEDIUM);
                    updateTile();
                }
            } else if (growth == 1) {
                if (timeAlive > 10) {
                    growth = 2;
                    tile = tileSet.getTile(Semtb001MajorAssignment.WHEAT_LARGE);
                    updateTile();
                }
            }
        }
    }

    public void updateTile() {
        gameScreen.getCell("seeds", rectangle).setTile(tile);
    }

    public void updateWater(){

        boolean water = false;
        Vector2 pos = new Vector2(rectangle.x, rectangle.y);
        for(TiledMapTileLayer.Cell cell : gameScreen.getSurroundingCells3x3("water", pos)){

            if(cell.getTile().getId() == Semtb001MajorAssignment.WATER) {
                water = true;
            }
        }

        if(water){
            hasWater = true;
        }

    }

    @Override
    public void dispose() {

    }
}
