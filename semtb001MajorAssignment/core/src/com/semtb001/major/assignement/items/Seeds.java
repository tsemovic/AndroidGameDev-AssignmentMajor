package com.semtb001.major.assignement.items;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.tools.Assets;

// Class for the seeds item in the hotbar
public class Seeds extends Item {

    public Seeds() {

        // Setup seeds attributes
        name = "seeds";
        health = 100;

        // Variables to 'activate' the seeds (for drawing the correct texture)
        active = false;
        pressed = false;

        // Seeds textures
        activeTexture = Semtb001MajorAssignment.assetManager.manager.get(Assets.seedsActive);
        inactiveTexture = Semtb001MajorAssignment.assetManager.manager.get(Assets.seedsInactive);
        activeImg = new Image(activeTexture);
        inActiveImg = new Image(inactiveTexture);

        // Seeds texture size
        float iconSize = Semtb001MajorAssignment.viewport.getScreenWidth() / (Semtb001MajorAssignment.PPM / 2);

        // Instantiate the seeds Image and its size
        image = new Image(inactiveTexture);
        image.setSize(iconSize, iconSize);
        activeImg.setSize(iconSize, iconSize);
        inActiveImg.setSize(iconSize, iconSize);
    }

}
