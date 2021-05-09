package com.semtb001.individual.assignement.tools;

import com.badlogic.gdx.math.Rectangle;
import com.semtb001.individual.assignement.screens.PlayScreen;
import java.util.Random;

// Class for screen shaker (simulates earthquake)
public class ScreenShaker {

    // PlayScreen object (location of the camera that will be shaking)
    private PlayScreen playScreen;

    // Position object of the screenShaker
    private Rectangle position;

    // Duration and size objects of the shaking
    private float duration;
    private float size;

    // Objects for determining if the shaker is in use
    private boolean shaking;
    private boolean shakeFinished;

    // Time counter object for counting in seconds (from delta time)
    private float timeCount;

    public ScreenShaker(PlayScreen playScreen, Rectangle position) {

        // Instantiate playScreen and the position of the shaker
        this.playScreen = playScreen;
        this.position = position;

        // Set the shaking booleans to false
        shaking = false;
        shakeFinished = false;
    }

    // Method to shake the screen (duration and size)
    public void shake(float duration, float size){
        this.duration = duration;
        this.size = size;

    }

    // Method to update the shaker
    public void update(float deltaTime) {

        // Setup the random generator
        Random r = new Random();

        // newX and newY value to translate is a random float multiplied by 'size'
        float newX = r.nextFloat() * size;
        float newY = r.nextFloat() * size;

        // plusMinus is a random boolean. If true: values are positive. If false: values become negative
        boolean plusMinus = r.nextBoolean();
        if(!plusMinus){
            newX = newX * -1;
            newY = newY * -1;
        }

        // If the shaker has not finished: translate the game camera
        if (!shakeFinished) {
            playScreen.getGameCamera().translate(newX, newY);
        }

        // This code executes every 1 second
        timeCount += deltaTime;
        if(timeCount >= 1){

            // If the duration timer is not yet 0, subtract 1
            if (duration > 1) {
                duration--;
            } else {

                /* If the duration time is 0, reset the gameCamera to its original position and
                set the 'shakeFinished' to true*/
                shakeFinished = true;
                playScreen.getGameCamera().position.y = 23;
                playScreen.getGameCamera().position.x = playScreen.getPlayer().box2dBody.getPosition().x + 8;
            }
            timeCount = 0;
        }

    }

    public Rectangle getPosition() {
        return position;
    }

    public boolean isShaking() {
        return shaking;
    }

    public void setShaking(boolean shaking) {
        this.shaking = shaking;
    }

    public boolean isShakeFinished() {
        return shakeFinished;
    }

    public void setShakeFinished(boolean shakeFinished) {
        this.shakeFinished = shakeFinished;
    }
}
