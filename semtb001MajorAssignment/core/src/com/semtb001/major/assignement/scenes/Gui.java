package com.semtb001.major.assignement.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.items.Hoe;
import com.semtb001.major.assignement.items.Item;
import com.semtb001.major.assignement.items.Seeds;
import com.semtb001.major.assignement.items.WateringCan;
import com.semtb001.major.assignement.sprites.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

// Class for the gui (hotbar on the bottom of the screen)
public class Gui {

    // Objects for the viewport, stage and player
    Viewport viewport;
    public Stage stage;
    public Player player;

    // Table to hold the items in the inventory
    private Table hotbarTable;

    // Items in the inventory
    public Map<Item, String> items;
    Hoe hoe;
    Seeds seeds;
    WateringCan wateringCan;

    // Object for item labels
    private HashMap<String, Label> itemLabels;

    public Gui(Player gamePlayer) {

        // Instantiate the viewport, stage and player
        viewport = Semtb001MajorAssignment.viewport;
        stage = new Stage();
        player = gamePlayer;
        Gdx.input.setInputProcessor(stage);

        // Setup the hotbarTable
        hotbarTable = new Table();
        hotbarTable.bottom();
        hotbarTable.setFillParent(true);
        hotbarTable.padBottom(1);

        // Initialise the items array
        items = new HashMap<Item, String>();

        // Initialise items
        hoe = new Hoe();
        seeds = new Seeds();
        wateringCan = new WateringCan();

        // Add items to the items array
        items.put(wateringCan, wateringCan.getName());
        items.put(seeds, seeds.getName());
        items.put(hoe, hoe.getName());

        // Initialise itemLabels array
        final Set<Item> itemSet = items.keySet();
        itemLabels = new HashMap<String, Label>();

        // For each item in the items array
        for (final Item item : itemSet) {

            // If the item name is 'seeds' (to show the number of seeds avaliable to plant)
            if (item.getName() == "seeds") {

                // Initialise itemLabel (labels above the items in the hotbar)
                Label itemLabel = new Label(Integer.toString(player.getInventory().getItemValue(item.getName())),
                        Semtb001MajorAssignment.tinyFontFontWhite);

                // Add the itemLabel to the hotbar
                hotbarTable.add(itemLabel);

                // Add the itemLabel to the itemLabels array
                itemLabels.put(item.getName(), itemLabel);

            } else {

                // If the items isn't seeds, add 'blank'
                hotbarTable.add();
            }
        }

        // Add a row to the hotbar Table
        hotbarTable.row();

        // For each item in the items array
        for (final Item item : itemSet) {

            // Add an input listener to the item
            item.getImage().addListener(new InputListener() {

                // If the item is 'touched'
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    // Set the item pressed and active to 'true'
                    item.setPressed(true);
                    item.setActive(true);

                    // Set all of the other items in the array to 'inactive'
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

            // Add the item image to the hotbarTable
            hotbarTable.add(item.getImage()).size(item.getImage().getWidth(), item.getImage().getHeight());
        }

        // Add the hotbarTable to the stage
        stage.addActor(hotbarTable);
    }

    // Method to update the hotbar images
    public void update(float dt, Player player) {

        // For each item in the items array
        Set<Item> itemSet = items.keySet();
        for (Item i : itemSet) {

            // texture for the item
            Texture texture = null;

            // If the item is active: set the displayed texture to the items 'active' texture
            if (i.getActive()) {
                texture = i.getActiveTexture();

                // If the item is inactive: set the displayed texture to the items 'inactive' texture
            } else {
                texture = i.getInactiveTexture();
            }

            // If the item is the 'wateringCan'
            if (i.getName() == "wateringCan") {

                // Update the water
                ((WateringCan) i).updateWater();

            }

            // Update the items image to the new texture
            i.getImage().setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));

            /* Loop through all the items in the itemLabels array and update their text
            to the text of the item in the players inventory (seeds count decrease when planted)*/
            Iterator itemIterator = itemLabels.entrySet().iterator();
            while (itemIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry) itemIterator.next();

                if (mapElement.getKey() == i.getName()) {
                    Label l = (Label) mapElement.getValue();
                    l.setText(player.getInventory().getItemValue(i.getName()));
                }
            }
        }

    }

}
