package com.space_shooter.game.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.space_shooter.game.ennemies.EnnemyShip;
import com.space_shooter.game.shared.entities.BattleShip;
import com.space_shooter.game.shared.entities.DrawnEntity;

public abstract class Projectile extends DrawnEntity {
    protected float speed;
    protected int damage;
    protected float radius;
    protected BattleShip owner;
    protected boolean isDestroyedOnCollision = true;


    @Override
    public void update(float delta) {
        sprite.setPosition(body.getWorldPoint(body.getLocalCenter()).x, body.getWorldPoint(body.getLocalCenter()).y);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

    @Override
    public void onCollision(DrawnEntity other) {
        if (other instanceof BattleShip) {
            BattleShip ship = (BattleShip) other;
            
            if (EnnemyShip.class.isAssignableFrom(ship.getClass()) && EnnemyShip.class.isAssignableFrom(owner.getClass()) || ship == owner) {
                return;
            }
            if (!ship.isMarkedForRemoval()) ship.takeDamage(damage);
            
            if (isDestroyedOnCollision) {
                markForRemoval();
            }
        }
    }
}
