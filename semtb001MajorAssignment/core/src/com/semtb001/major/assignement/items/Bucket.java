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

        activeImg = new Image(activeTexture);
        inActiveImg = new Image(inactiveTexture);
        image = new Image(inactiveTexture);
        image.setSize(Semtb001MajorAssignment.ICONSIZE, Semtb001MajorAssignment.ICONSIZE);

        activeImg.setSize(Semtb001MajorAssignment.ICONSIZE, Semtb001MajorAssignment.ICONSIZE);
        inActiveImg.setSize(Semtb001MajorAssignment.ICONSIZE, Semtb001MajorAssignment.ICONSIZE);
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

