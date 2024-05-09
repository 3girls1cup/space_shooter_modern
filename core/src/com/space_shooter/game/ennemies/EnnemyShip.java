package com.space_shooter.game.ennemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.shared.entities.BattleShip;
import com.space_shooter.game.shared.entities.DrawnEntity;
import com.space_shooter.game.walls.Wall;

public abstract class EnnemyShip extends BattleShip {
    protected int scoreValue;
    private float targetAngle;
    private float rotationSpeed = 2.0f;

    public EnnemyShip(Texture texture, Vector2 spawnPosition, String fileName) {
        super(texture, spawnPosition, fileName);
        this.teleportDistance = 40f;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    @Override
    public void died() {
        super.died();
        GameContext.getInstance().getGamePlayManager().notifyEnnemyKilled(this);
    }

    @Override
    public void onCollision(DrawnEntity entity) {
        if (entity instanceof Wall) {
            startTeleportationAnimation(body.getLinearVelocity());
        }
    }


    protected float getRealisticRotationAngle(float delta) {
        Vector2 velocity = body.getLinearVelocity();

        if (!velocity.isZero()) {
            targetAngle = velocity.angleDeg() - 90;
    
            float currentAngle = sprite.getRotation();
            float angleDifference = targetAngle - currentAngle;
    
            while (angleDifference < -180) angleDifference += 360;
            while (angleDifference > 180) angleDifference -= 360;
    
            return currentAngle + angleDifference * rotationSpeed * delta;
        }
        return -500;
    }
}
