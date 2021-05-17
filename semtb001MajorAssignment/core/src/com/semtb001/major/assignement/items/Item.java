package com.semtb001.major.assignement.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

// Abstract class that all hotbar items extend
public abstract class Item {

    // Item attributes
    String name;
    Integer health;
    Texture activeTexture;
    Texture inactiveTexture;
    Image image;
    Image activeImg;
    Image inActiveImg;
    Boolean active;
    Boolean pressed;

    // Setters and Getters
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

    public Texture getInactiveTexture() {
        return inactiveTexture;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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
}
