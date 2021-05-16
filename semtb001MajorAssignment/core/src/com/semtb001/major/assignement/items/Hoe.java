package com.semtb001.major.assignement.items;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.tools.Assets;

// Class for the hoe item in the hotbar
public class Hoe extends Item {

    public Hoe() {

        // Setup hoe attributes
        name = "hoe";
        health = 100;

        // Variables to 'activate' the hoe (for drawing the correct texture)
        active = false;
        pressed = false;

        // Hoe textures
        activeTexture = Semtb001MajorAssignment.assetManager.manager.get(Assets.hoeActive);
        inactiveTexture = Semtb001MajorAssignment.assetManager.manager.get(Assets.hoeInactive);
        activeImg = new Image(activeTexture);
        inActiveImg = new Image(inactiveTexture);

        // Hoe texture size
        float iconSize = Semtb001MajorAssignment.viewport.getScreenWidth() / (Semtb001MajorAssignment.PPM / 2);

        // Instantiate the hoe Image and its size
        image = new Image(inactiveTexture);
        image.setSize(iconSize, iconSize);
        activeImg.setSize(iconSize, iconSize);
        inActiveImg.setSize(iconSize, iconSize);
    }

}
