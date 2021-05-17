package com.semtb001.major.assignement.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.semtb001.major.assignement.Semtb001MajorAssignment;

// Class for the touchpad (player movement)
public class TouchPad {

    // TouchPad stage and viewport objects
    public Stage stage;
    public Touchpad touchPad;

    // TouchPad style objects
    private Touchpad.TouchpadStyle touchpadStyle;

    // TouchPad skin objects
    private Skin touchpadActiveSkin;
    private Skin touchpadInactiveSkin;

    // TouchPad background objects
    private Drawable touchActiveBackground;
    private Drawable touchInactiveBackground;
    private Texture backgroundActive;
    private Texture backgroundInactive;

    // TouchPad knob objects
    private Drawable touchActiveKnob;
    private Drawable touchInactiveKnob;
    private Texture knobActive;
    private Texture knobInactive;

    // Boolean to track if the touchPad is being touched
    public boolean isTouched;

    public TouchPad() {

        // Create textures for active/inactive touchpad background and knob
        backgroundActive = new Texture("touchPad/touchBackgroundActive.png");
        backgroundInactive = new Texture("touchPad/touchBackgroundInactive.png");
        knobActive = new Texture("touchPad/touchKnobActive.png");
        knobInactive = new Texture("touchPad/touchKnobInactive.png");

        // Create skin for active/inactive touchPad background and knob
        touchpadActiveSkin = new Skin();
        touchpadInactiveSkin = new Skin();

        // Add touchPad background and knob textures to 'active' skin
        touchpadActiveSkin.add("touchBackground", backgroundActive);
        touchpadActiveSkin.add("touchKnob", knobActive);

        // Add touchPad background and knob textures to 'inactive' skin
        touchpadInactiveSkin.add("touchBackground", backgroundInactive);
        touchpadInactiveSkin.add("touchKnob", knobInactive);

        // TouchPad knob size
        float knobSize = Semtb001MajorAssignment.viewport.getScreenWidth() / (Semtb001MajorAssignment.PPM * 8);

        // Set touchPad 'active' background and knob textures to 'active' skin
        touchActiveBackground = touchpadActiveSkin.getDrawable("touchBackground");
        touchActiveKnob = touchpadActiveSkin.getDrawable("touchKnob");
        touchActiveKnob.setMinWidth(Semtb001MajorAssignment.PPM * knobSize);
        touchActiveKnob.setMinHeight(Semtb001MajorAssignment.PPM * knobSize);

        // Set touchPad 'inactive' background and knob textures to 'inactive' skin
        touchInactiveBackground = touchpadInactiveSkin.getDrawable("touchBackground");
        touchInactiveKnob = touchpadInactiveSkin.getDrawable("touchKnob");
        touchInactiveKnob.setMinWidth(Semtb001MajorAssignment.PPM * knobSize);
        touchInactiveKnob.setMinHeight(Semtb001MajorAssignment.PPM * knobSize);

        // Create default touchpad style (inactive)
        touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = touchInactiveBackground;
        touchpadStyle.knob = touchInactiveKnob;

        // Create new TouchPad with the inactive style
        touchPad = new Touchpad(10, touchpadStyle);

        // Set the touchPad bounds(x,y,width,height)
        touchPad.setBounds(Semtb001MajorAssignment.PPM / 2, Semtb001MajorAssignment.PPM / 2, Semtb001MajorAssignment.viewport.getScreenWidth() / 3.5f, Semtb001MajorAssignment.viewport.getScreenWidth() / 3.5f);

        // Create a Stage and add the TouchPad
        stage = new Stage();
        stage.addActor(touchPad);
        Gdx.input.setInputProcessor(stage);

        // Add an input listener to the touchPad
        touchPad.addListener(new InputListener() {

            // If the touchpad is being touched
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                // Change the touchpad background and knob to 'active' (less transparent)
                touchPad.getStyle().background = touchActiveBackground;
                touchPad.getStyle().knob = touchActiveKnob;

                // Set isTouched to true
                isTouched = true;
                return true;
            }

            // If the touchPad is no longer being touched
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                // Return the touchpad background and knob to 'inactive' (more transparent)
                touchPad.getStyle().background = touchInactiveBackground;
                touchPad.getStyle().knob = touchInactiveKnob;

                // Set isTouched to false
                isTouched = false;
            }
        });
    }

}
