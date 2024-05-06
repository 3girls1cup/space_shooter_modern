package com.space_shooter.game.shared.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.space_shooter.game.shared.entities.DrawnEntity;

public class GameContactListener implements ContactListener {
      @Override
    public void beginContact(Contact contact) {
        
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        DrawnEntity entityA = (DrawnEntity) fixtureA.getBody().getUserData();
        DrawnEntity entityB = (DrawnEntity) fixtureB.getBody().getUserData();

        if (entityA != null && entityB != null) {
            entityA.onCollision(entityB);
            entityB.onCollision(entityA);
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
