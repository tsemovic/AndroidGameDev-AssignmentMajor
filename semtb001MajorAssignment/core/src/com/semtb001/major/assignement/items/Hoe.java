package com.semtb001.major.assignement.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.tools.Assets;

public class Hoe extends Item{

    public Hoe(){
        name = "hoe";
        health = 100;
        active = false;
        pressed = false;

        activeTexture = Semtb001MajorAssignment.assetManager.manager.get(Assets.hoeActive);
        inactiveTexture = Semtb001MajorAssignment.assetManager.manager.get(Assets.hoeInactive);

        float iconSize = Semtb001MajorAssignment.viewport.getScreenWidth()/(Semtb001MajorAssignment.PPM/2);

        activeImg = new Image(activeTexture);
        inActiveImg = new Image(inactiveTexture);
        image = new Image(inactiveTexture);
        image.setSize(iconSize, iconSize);

        activeImg.setSize(iconSize, iconSize);
        inActiveImg.setSize(iconSize, iconSize);
    }

    @Override
    public boolean setPressed() {
        return false;
    }
}
