package com.semtb001.major.assignement.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.tools.Assets;

public class WateringCan extends Item {

    // Textures for the full and empty watering can
    private Texture wateringCanFullActive;
    private Texture wateringCanFullInactive;
    private Texture wateringCanEmptyActive;
    private Texture wateringCanEmptyInactive;

    public WateringCan() {

        // Setup watering can attributes
        name = "wateringCan";
        health = 0;
        active = false;
        pressed = false;

        // Setup watering can textures
        wateringCanFullActive = Semtb001MajorAssignment.assetManager.manager.get(Assets.wateringCanFullActive);
        wateringCanFullInactive = Semtb001MajorAssignment.assetManager.manager.get(Assets.wateringCanFullInactive);
        wateringCanEmptyActive = Semtb001MajorAssignment.assetManager.manager.get(Assets.wateringCanEmptyActive);
        wateringCanEmptyInactive = Semtb001MajorAssignment.assetManager.manager.get(Assets.wateringCanEmptyInactive);
        activeTexture = wateringCanEmptyActive;
        inactiveTexture = wateringCanEmptyInactive;

        // Setup watering can texture size
        float iconSize = Semtb001MajorAssignment.viewport.getScreenWidth() / (Semtb001MajorAssignment.PPM / 2);

        // Setup watering can active and inactive texture
        activeImg = new Image(activeTexture);
        inActiveImg = new Image(inactiveTexture);

        // Setup watering can texture sizes
        image = new Image(inactiveTexture);
        image.setSize(iconSize, iconSize);
        activeImg.setSize(iconSize, iconSize);
        inActiveImg.setSize(iconSize, iconSize);
    }

    // Method to update the watering can icon
    public void updateWater() {

        // If the watering can is full
        if (health == 100) {

            // Set the active and inactive textures to the 'full' textures
            activeTexture = wateringCanFullActive;
            inactiveTexture = wateringCanFullInactive;

            // If the watering can is empty
        } else {

            // Set the active and inactive textures to the 'empty' textures
            activeTexture = wateringCanEmptyActive;
            inactiveTexture = wateringCanEmptyInactive;
        }
    }


}

