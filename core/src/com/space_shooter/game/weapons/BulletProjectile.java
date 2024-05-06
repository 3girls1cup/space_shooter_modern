package com.space_shooter.game.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.space_shooter.game.shared.entities.BattleShip;
import com.space_shooter.game.shared.utils.BodyFactory;

public class BulletProjectile extends Projectile{
    public BulletProjectile(Vector2 velocity, int damage, BattleShip owner, float radius, Color color, float speed) {
        this.damage = damage;
        this.owner = owner;
        this.radius = radius;
        this.color = color;
        this.speed = speed;
        this.body = BodyFactory.createBody(owner.getWorld(), BodyType.DynamicBody, owner.getBody().getPosition().x + owner.getRadius() * velocity.x, owner.getBody().getPosition().y + owner.getRadius() * velocity.y, true, 0);
        this.body.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        BodyFactory.createFixture(body, shape, 0f, 0f, 0f, true);

        body.setLinearVelocity(velocity.scl(speed));
    }

}
