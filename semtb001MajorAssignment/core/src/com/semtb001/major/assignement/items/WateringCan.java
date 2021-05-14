package com.semtb001.major.assignement.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.semtb001.major.assignement.Semtb001MajorAssignment;
import com.semtb001.major.assignement.tools.Assets;

public class WateringCan extends Item{

    private Texture wateringCanFullActive;
    private Texture wateringCanFullInactive;
    private Texture wateringCanEmptyActive;
    private Texture wateringCanEmptyInactive;

    public WateringCan(){
        name = "bucket";
        health = 0;
        active = false;
        pressed = false;

        wateringCanFullActive = Semtb001MajorAssignment.assetManager.manager.get(Assets.wateringCanFullActive);
        wateringCanFullInactive = Semtb001MajorAssignment.assetManager.manager.get(Assets.wateringCanFullInactive);
        wateringCanEmptyActive = Semtb001MajorAssignment.assetManager.manager.get(Assets.wateringCanEmptyActive);
        wateringCanEmptyInactive = Semtb001MajorAssignment.assetManager.manager.get(Assets.wateringCanEmptyInactive);

        activeTexture = wateringCanEmptyActive;
        inactiveTexture = wateringCanEmptyInactive;

        float iconSize = Semtb001MajorAssignment.viewport.getScreenWidth()/(Semtb001MajorAssignment.PPM/2);

        activeImg = new Image(activeTexture);
        inActiveImg = new Image(inactiveTexture);
        image = new Image(inactiveTexture);
        image.setSize(iconSize, iconSize);

        activeImg.setSize(iconSize, iconSize);
        inActiveImg.setSize(iconSize, iconSize);
    }

    public void updateWater(){
        if(health == 100){
            activeTexture = wateringCanFullActive;
            inactiveTexture = wateringCanFullInactive;
        }else{
            activeTexture = wateringCanEmptyActive;
            inactiveTexture = wateringCanEmptyInactive;
        }
    }

    @Override
    public boolean setPressed() {
        return false;
    }
}

