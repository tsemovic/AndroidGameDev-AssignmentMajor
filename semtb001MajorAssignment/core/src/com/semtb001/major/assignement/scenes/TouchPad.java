package com.semtb001.major.assignement.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.semtb001.major.assignement.Semtb001MajorAssignment;

public class TouchPad {

    private OrthographicCamera camera;
    public Stage stage;
    public Touchpad touchPad;

    private Touchpad.TouchpadStyle touchpadStyle;

    private Skin touchpadActiveSkin;
    private Skin touchpadInactiveSkin;

    private Drawable touchActiveBackground;
    private Drawable touchInactiveBackground;

    private Drawable touchActiveKnob;
    private Drawable touchInactiveKnob;

    private Texture backgroundActive;
    private Texture backgroundInactive;

    private Texture knobActive;
    private Texture knobInactive;

    public boolean isTouched;

    private Integer knobSize;

    public TouchPad() {
        camera = new OrthographicCamera();

        knobSize = 6;

        //create textures for active/inactive touchpad background and knob
        backgroundActive = new Texture("touchPad/touchBackgroundActive.png");

        backgroundInactive = new Texture("touchPad/touchBackgroundInactive.png");
        knobActive = new Texture("touchPad/touchKnobActive.png");
        knobInactive = new Texture("touchPad/touchKnobInactive.png");

        //create skin for active/inactive touchpad background and knob
        touchpadActiveSkin = new Skin();
        touchpadInactiveSkin = new Skin();

        //add touchpad background and knob textures to 'active' skin
        touchpadActiveSkin.add("touchBackground", backgroundActive);
        touchpadActiveSkin.add("touchKnob", knobActive);

        //add touchpad background and knob textures to 'inactive' skin
        touchpadInactiveSkin.add("touchBackground", backgroundInactive);
        touchpadInactiveSkin.add("touchKnob", knobInactive);

        //set touchpad 'active' background and knob textures to 'active' skin
        touchActiveBackground = touchpadActiveSkin.getDrawable("touchBackground");
        touchActiveKnob = touchpadActiveSkin.getDrawable("touchKnob");
        touchActiveKnob.setMinWidth(Semtb001MajorAssignment.PPM * knobSize);
        touchActiveKnob.setMinHeight(Semtb001MajorAssignment.PPM * knobSize);

        //set touchpad 'inactive' background and knob textures to 'inactive' skin
        touchInactiveBackground = touchpadInactiveSkin.getDrawable("touchBackground");
        touchInactiveKnob = touchpadInactiveSkin.getDrawable("touchKnob");
        touchInactiveKnob.setMinWidth(Semtb001MajorAssignment.PPM * knobSize);
        touchInactiveKnob.setMinHeight(Semtb001MajorAssignment.PPM * knobSize);

        //create default touchpad style (inactive)
        touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = touchInactiveBackground;
        touchpadStyle.knob = touchInactiveKnob;

        //Create new TouchPad with the inactive style
        touchPad = new Touchpad(0, touchpadStyle);

        //setBounds(x,y,width,height)
        touchPad.setBounds(Semtb001MajorAssignment.PPM / 2, Semtb001MajorAssignment.PPM / 2, Semtb001MajorAssignment.PPM * 15, Semtb001MajorAssignment.PPM * 15);

        //Create a Stage and add TouchPad
        stage = new Stage();
        stage.addActor(touchPad);

        Gdx.input.setInputProcessor(stage);

        touchPad.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                //change the touchpad background and knob to 'active' (less transparent)
                touchPad.getStyle().background = touchActiveBackground;
                touchPad.getStyle().knob = touchActiveKnob;

                isTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                //return the touchpad background and knob to 'inactive' (more transparent)
                touchPad.getStyle().background = touchInactiveBackground;
                touchPad.getStyle().knob = touchInactiveKnob;
                isTouched = false;
            }
        });
    }


}
