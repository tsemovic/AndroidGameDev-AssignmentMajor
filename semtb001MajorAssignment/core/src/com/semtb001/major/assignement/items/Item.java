package com.semtb001.major.assignement.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class Item {
    String name;
    Integer health;
    Texture activeTexture;
    Texture inactiveTexture;
    Image image;
    Image activeImg;
    Image inActiveImg;
    Boolean active;
    Boolean pressed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Texture getActiveTexture() {
        return activeTexture;
    }

    public void setActiveTexture(Texture activeTexture) {
        this.activeTexture = activeTexture;
    }

    public Texture getInactiveTexture() {
        return inactiveTexture;
    }

    public void setInactiveTexture(Texture inactiveTexture) {
        this.inactiveTexture = inactiveTexture;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getActiveImg() {
        return activeImg;
    }

    public void setActiveImg(Image activeImg) {
        this.activeImg = activeImg;
    }

    public Image getInActiveImg() {
        return inActiveImg;
    }

    public void setInActiveImg(Image inActiveImg) {
        this.inActiveImg = inActiveImg;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getPressed() {
        return pressed;
    }

    public void setPressed(Boolean pressed) {
        this.pressed = pressed;
    }

    public abstract boolean setPressed();
}
