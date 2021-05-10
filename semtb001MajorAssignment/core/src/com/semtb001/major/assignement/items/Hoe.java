package com.semtb001.major.assignement.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.semtb001.major.assignement.Semtb001MajorAssignment;

public class Hoe extends Item{

    public Hoe(){
        name = "hoe";
        health = 100;
        active = false;
        pressed = false;

        activeTexture = new Texture("hoeActive.png");
        inactiveTexture = new Texture("hoeInactive.png");

        activeImg = new Image(activeTexture);
        inActiveImg = new Image(inactiveTexture);
        image = new Image(inactiveTexture);
        image.setSize(Semtb001MajorAssignment.ICONSIZE, Semtb001MajorAssignment.ICONSIZE);

        activeImg.setSize(Semtb001MajorAssignment.ICONSIZE, Semtb001MajorAssignment.ICONSIZE);
        inActiveImg.setSize(Semtb001MajorAssignment.ICONSIZE, Semtb001MajorAssignment.ICONSIZE);
    }

    @Override
    public boolean setPressed() {
        return false;
    }
}
