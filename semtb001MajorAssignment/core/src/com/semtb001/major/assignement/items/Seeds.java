package com.semtb001.major.assignement.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.semtb001.major.assignement.Semtb001MajorAssignment;

public class Seeds extends Item{

    public Seeds(){
        name = "seeds";
        health = 100;
        active = false;
        pressed = false;
        activeTexture = new Texture("seedsActive.png");
        inactiveTexture = new Texture("seedsInactive.png");

        activeImg = new Image(activeTexture);
        inActiveImg = new Image(inactiveTexture);
        if(active){
            image = new Image(activeTexture);
        }else{
            image = new Image(inactiveTexture);
        }
        //image = new Image(activeTexture);
        image.setSize(Semtb001MajorAssignment.ICONSIZE, Semtb001MajorAssignment.ICONSIZE);

        activeImg.setSize(Semtb001MajorAssignment.ICONSIZE, Semtb001MajorAssignment.ICONSIZE);
        inActiveImg.setSize(Semtb001MajorAssignment.ICONSIZE, Semtb001MajorAssignment.ICONSIZE);
    }

    @Override
    public boolean setPressed() {
        return false;
    }
}
