package com.semtb001.major.assignement.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.semtb001.major.assignement.Semtb001MajorAssignment;

public class Bucket extends Item{

    public Bucket(){
        name = "bucket";
        health = 0;
        active = false;
        pressed = false;

        activeTexture = new Texture("bucketActive.png");
        inactiveTexture = new Texture("bucketInactive.png");

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
            activeTexture = new Texture("waterBucketActive.png");
            inactiveTexture = new Texture("waterBucketInactive.png");
        }else{
            activeTexture = new Texture("bucketActive.png");
            inactiveTexture = new Texture("bucketInactive.png");
        }
    }

    @Override
    public boolean setPressed() {
        return false;
    }
}

