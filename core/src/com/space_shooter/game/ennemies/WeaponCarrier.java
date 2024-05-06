package com.space_shooter.game.ennemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.shared.entities.DrawnEntity;

public class WeaponCarrier extends EnnemyShip {
    
    public WeaponCarrier(Vector2 position) {
        super();
        this.color = Color.BLUE;
        this.scoreValue = GameConstants.KAMIKAZE_SHIP_SCORE_VALUE;
        this.health = GameConstants.KAMIKAZE_SHIP_HEALTH;
        this.radius = GameConstants.KAMIKAZE_SHIP_RADIUS;
        this.speed = (float) (Math.random() * (GameConstants.KAMIKAZE_SHIP_MAX_SPEED - GameConstants.KAMIKAZE_SHIP_MIN_SPEED) + GameConstants.KAMIKAZE_SHIP_MIN_SPEED);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y); 

        this.body = world.createBody(bodyDef);
        this.body.setUserData(this);
        
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f; 
        fixtureDef.friction = 0f;
        fixtureDef.restitution= 1f;
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onCollision(DrawnEntity entity) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void died() {

    }
}
