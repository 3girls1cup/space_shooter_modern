package com.space_shooter.game.shared.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.space_shooter.game.core.BodyEditorLoader;

public class BodyFactory {
    private BodyEditorLoader loader;
    private static BodyFactory instance;

    public BodyFactory() {
        loader = new BodyEditorLoader(Gdx.files.internal("box2d_scene.json"));
    }

    public static BodyFactory getInstance() {
        if (instance == null) {
            instance = new BodyFactory();
        }
        return instance;
    }

    public Body createBody(World world, BodyType type, float posX, float posY, boolean isBullet, float angleDeg) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(posX, posY);
        bodyDef.bullet = isBullet;
        bodyDef.angle = MathUtils.degreesToRadians * angleDeg;
        Body body = world.createBody(bodyDef);
        return body;
    }

    public Fixture createFixture(Body body, Shape shape, float density, float friction, float restitution, boolean isSensor) {
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

    public void attachComplexeFixture(Body body, String name, float scale, float density, float friction, float restitution, boolean isSensor) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.isSensor = isSensor;
        this.loader.attachFixture(body, name, fixtureDef, scale);
    }
}
