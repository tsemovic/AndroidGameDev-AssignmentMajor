package com.semtb001.major.assignement.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.semtb001.major.assignement.sprites.FlyingEnemy;
import com.semtb001.major.assignement.sprites.Coin;
import com.semtb001.major.assignement.sprites.Player;
import com.semtb001.major.assignement.sprites.GroundEnemy;

// Class for handling contact in the world (Box2d)
public class WorldContactListener implements ContactListener {

    private com.semtb001.major.assignement.tools.Box2DWorldCreator creator;

    // World contact listener (listens for Box2D contact in the world)
    public WorldContactListener(Box2DWorldCreator creator) {
        this.creator = creator;
    }

    @Override
    public void beginContact(Contact contact) {

        // Contact fixtures setup
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // If the fixtureA is the player
        if(fixA.getUserData() instanceof Player){

            // If the player has hit a grounded enemy
            if(fixB.getUserData() instanceof GroundEnemy){

                // Set the player to 'dead'
                ((Player) fixA.getUserData()).setPlayerIsDead(true);

                // If the player has hit a flying enemy
            }else if(fixB.getUserData() instanceof FlyingEnemy){

                // Set the player to 'dead'
                ((Player) fixA.getUserData()).setPlayerIsDead(true);

                // If the player has hit a coin
            }else if(fixB.getUserData() instanceof Coin){

                // Execute the 'hit' method for that coin
                ((Coin) fixB.getUserData()).hit();
            }
        }

        // If the fixtureB is the player
        if(fixB.getUserData() instanceof Player){

            // If the player has hit a grounded enemy
            if(fixA.getUserData() instanceof GroundEnemy){

                // Set the player to 'dead'
                ((Player) fixB.getUserData()).setPlayerIsDead(true);

                // If the player has hit a flying enemy
            }else if(fixA.getUserData() instanceof FlyingEnemy){

                // Set the player to 'dead'
                ((Player) fixB.getUserData()).setPlayerIsDead(true);

                // If the player has hit a coin
            }else if(fixA.getUserData() instanceof Coin){

                // Execute the 'hit' method for that coin
                ((Coin) fixB.getUserData()).hit();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
