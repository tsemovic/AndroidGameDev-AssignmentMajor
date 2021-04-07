package com.semtb001.individual.assignement.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.semtb001.individual.assignement.sprites.FlyingEnemy;
import com.semtb001.individual.assignement.sprites.Coin;
import com.semtb001.individual.assignement.sprites.Player;
import com.semtb001.individual.assignement.sprites.GroundEnemy;


public class WorldContactListener implements ContactListener {

    private Box2DWorldCreator creator;

    public WorldContactListener(Box2DWorldCreator creator) {
        this.creator = creator;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() instanceof Player){
            if(fixB.getUserData() instanceof GroundEnemy){
                ((Player) fixA.getUserData()).setPlayerIsDead(true);
            }else if(fixB.getUserData() instanceof FlyingEnemy){
                ((Player) fixA.getUserData()).setPlayerIsDead(true);
            }else if(fixB.getUserData() instanceof Coin){
                ((Coin) fixB.getUserData()).hit();
            }
        }

        if(fixB.getUserData() instanceof Player){
            if(fixA.getUserData() instanceof GroundEnemy){
                ((Player) fixB.getUserData()).setPlayerIsDead(true);
            }else if(fixA.getUserData() instanceof FlyingEnemy){
                ((Player) fixB.getUserData()).setPlayerIsDead(true);
            }else if(fixA.getUserData() instanceof Coin){
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
