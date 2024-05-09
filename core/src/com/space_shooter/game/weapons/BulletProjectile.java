package com.space_shooter.game.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.space_shooter.game.shared.entities.BattleShip;
import com.space_shooter.game.shared.utils.BodyFactory;

public class BulletProjectile extends Projectile {
    public BulletProjectile(Vector2 velocity, int damage, BattleShip owner, float radius, Texture texture, float speed) {
        this.damage = damage;
        this.owner = owner;
        this.radius = radius;
        this.texture = texture;
        this.speed = speed;

        this.sprite = new Sprite(texture);
        this.sprite.setSize(texture.getWidth() / 10, texture.getHeight() / 10);
        this.body = BodyFactory.getInstance().createBody(owner.getWorld(), BodyType.DynamicBody, owner.getBody().getWorldCenter().x + owner.getHalfWidth() * velocity.x, owner.getBody().getWorldCenter().y + owner.getHalfHeight() * velocity.y, true, velocity.angleDeg() - 90);
        BodyFactory.getInstance().attachComplexeFixture(body, "basic_projectile_player", this.sprite.getWidth(), 0f, 0f, 0f, true);
        this.body.setUserData(this);
        body.setLinearVelocity(velocity.scl(speed));

        this.sprite.setOrigin(body.getLocalCenter().x, body.getLocalCenter().y);
        this.sprite.setRotation(velocity.angleDeg() - 90);
    }

}
