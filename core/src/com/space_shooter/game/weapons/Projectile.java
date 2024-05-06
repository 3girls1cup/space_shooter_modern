package com.space_shooter.game.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.space_shooter.game.ennemies.EnnemyShip;
import com.space_shooter.game.shared.entities.BattleShip;
import com.space_shooter.game.shared.entities.DrawnEntity;

public abstract class Projectile extends DrawnEntity {
    protected float speed;
    protected int damage;
    protected Color color;
    protected float radius;
    protected BattleShip owner;
    protected boolean isDestroyedOnCollision = true;


    @Override
    public void update(float delta) {
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(color);
            shapeRenderer.circle(body.getPosition().x, body.getPosition().y, radius);
            shapeRenderer.end();
    }

    @Override
    public void onCollision(DrawnEntity other) {
        if (other instanceof BattleShip) {
            BattleShip ship = (BattleShip) other;
            if (EnnemyShip.class.isAssignableFrom(ship.getClass()) && EnnemyShip.class.isAssignableFrom(owner.getClass()) || ship == owner) {
                return;
            }

            ship.takeDamage(damage);
            
            if (isDestroyedOnCollision) {
                markForRemoval();
            }
        }
    }
}
