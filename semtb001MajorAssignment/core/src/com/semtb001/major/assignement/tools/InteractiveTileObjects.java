package com.semtb001.major.assignement.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.screens.PlayScreen;
import com.semtb001.major.assignement.sprites.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class InteractiveTileObjects {
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected PlayScreen screen;
    protected MapObject object;

    protected Fixture fixture;

    public InteractiveTileObjects(World world, TiledMap map, Rectangle bounds){
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / Semtb001MajorAssignment.PPM, (bounds.getY() + bounds.getHeight() / 2) / Semtb001MajorAssignment.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / Semtb001MajorAssignment.PPM, bounds.getHeight() / 2 / Semtb001MajorAssignment.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

    }

    public abstract void onCollision(Player userData);

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("grass");
        return layer.getCell((int)(body.getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y * Semtb001MajorAssignment.PPM / 32));
    }


    public List<TiledMapTileLayer.Cell> getTreeCells(){
        List<TiledMapTileLayer.Cell> cells = new ArrayList<TiledMapTileLayer.Cell>();

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("trees");

        //main stump
        cells.add(layer.getCell((int)(body.getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y * Semtb001MajorAssignment.PPM / 32)));

        //left and right shadow
        cells.add(layer.getCell((int)(body.getPosition().x+1 * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y * Semtb001MajorAssignment.PPM / 32)));
        cells.add(layer.getCell((int)(body.getPosition().x-1 * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y * Semtb001MajorAssignment.PPM / 32)));

        //trunk
        cells.add(layer.getCell((int)(body.getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y+1 * Semtb001MajorAssignment.PPM / 32)));
        cells.add(layer.getCell((int)(body.getPosition().x+1 * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y+1 * Semtb001MajorAssignment.PPM / 32)));
        cells.add(layer.getCell((int)(body.getPosition().x-1 * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y+1 * Semtb001MajorAssignment.PPM / 32)));

        //lower leaves
        cells.add(layer.getCell((int)(body.getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y+2 * Semtb001MajorAssignment.PPM / 32)));
        cells.add(layer.getCell((int)(body.getPosition().x+1 * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y+2 * Semtb001MajorAssignment.PPM / 32)));
        cells.add(layer.getCell((int)(body.getPosition().x-1 * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y+2 * Semtb001MajorAssignment.PPM / 32)));

        //upper leaves
        cells.add(layer.getCell((int)(body.getPosition().x * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y+3 * Semtb001MajorAssignment.PPM / 32)));
        cells.add(layer.getCell((int)(body.getPosition().x+1 * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y+3 * Semtb001MajorAssignment.PPM / 32)));
        cells.add(layer.getCell((int)(body.getPosition().x-1 * Semtb001MajorAssignment.PPM / 32),
                (int)(body.getPosition().y+3 * Semtb001MajorAssignment.PPM / 32)));

        return cells;
    }

    public abstract void onCollision();
}
