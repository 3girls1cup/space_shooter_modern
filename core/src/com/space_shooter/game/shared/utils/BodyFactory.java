package com.space_shooter.game.shared.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyFactory {
    public static Body createBody(World world, BodyType type, float posX, float posY, boolean isBullet, float angleDeg) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(posX, posY);
        bodyDef.bullet = isBullet;
        bodyDef.angle = MathUtils.degreesToRadians * angleDeg;
        Body body = world.createBody(bodyDef);
        return body;
    }

    public static Fixture createFixture(Body body, Shape shape, float density, float friction, float restitution, boolean isSensor) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.isSensor = isSensor;
        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
        return fixture;
    }
}
