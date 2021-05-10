package com.semtb001.major.assignement.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.items.Bucket;
import com.semtb001.major.assignement.items.Hoe;
import com.semtb001.major.assignement.items.Item;
import com.semtb001.major.assignement.items.Seeds;
import com.semtb001.major.assignement.sprites.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Gui {
    Viewport viewport;
    public Stage stage;
    public Player player;
    boolean ActionPressed;

    private Table hotbarTable;

    public Map<Item, String> items;
    Hoe hoe;
    Seeds seeds;
    Bucket bucket;
    private HashMap<String, Label> itemLabels;

    public Gui(Player gamePlayer) {
        viewport = Semtb001MajorAssignment.viewport;
        stage = new Stage();
        player = gamePlayer;

        Gdx.input.setInputProcessor(stage);

        hotbarTable = new Table();
        hotbarTable.bottom();
        hotbarTable.setFillParent(true);
        hotbarTable.padBottom(1);

        //init items array
        items = new HashMap<Item, String>();

        //init items
        hoe = new Hoe();
        seeds = new Seeds();
        bucket = new Bucket();

        //add items to item array
        items.put(bucket, bucket.getName());
        items.put(seeds, seeds.getName());
        items.put(hoe, hoe.getName());


        final Set<Item> itemSet = items.keySet();
        itemLabels = new HashMap<String, Label>();

        for (final Item item : itemSet) {
            if(item.getName() == "seeds"){
                System.out.println(player.getInventory().getItem(item.getName()));
                Label itemLabel = new Label(Integer.toString(player.getInventory().getItem(item.getName())), Semtb001MajorAssignment.tinyFontFontWhite);
                hotbarTable.add(itemLabel);
                itemLabels.put(item.getName(), itemLabel);
            }else{
                hotbarTable.add();
            }
        }

        hotbarTable.row();

        for (final Item item : itemSet) {
            item.getImage().addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    item.setPressed(true);
                    item.setActive(true);

                    for (final Item otherItem : itemSet) {
                        if (item != otherItem) {
                            otherItem.setActive(false);
                        }
                    }
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    item.setPressed(false);
                }
            });

            hotbarTable.add(item.getImage()).size(item.getImage().getWidth(), item.getImage().getHeight());
        }

        stage.addActor(hotbarTable);
    }

    public void update(float dt, Player p) {
        Bucket b = null;

        Set<Item> itemSet = items.keySet();
        for (Item i : itemSet) {
            Texture texture = null;

            if (i.getActive()) {
                texture = i.getActiveTexture();
            } else {
                texture = i.getInactiveTexture();
            }

            if (i.getName() == "bucket") {
                b = (Bucket) i;
                b.updateWater();
            }
            i.getImage().setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));


            Iterator itemIterator = itemLabels.entrySet().iterator();
            while (itemIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry)itemIterator.next();

                if(mapElement.getKey() == i.getName()){
                    Label l = (Label) mapElement.getValue();
                    l.setText(p.getInventory().getItem(i.getName()));
                }
            }
        }

    }

    public Item getItem(String s) {
        Set<Item> itemSet = items.keySet();
        Item selectedItem = null;
        for (Item i : itemSet) {
            if (i.getName() == s) {
                selectedItem = i;
            }
        }
        return selectedItem;
    }
}
