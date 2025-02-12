package com.space_shooter.game.ennemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.space_shooter.game.core.GameAssets;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.shared.entities.DrawnEntity;
import com.space_shooter.game.walls.Wall;

public class KamikazeShip extends EnnemyShip {
    private Vector2 playerPosition;
    private float targetAngle;
    private float rotationSpeed = 2f;

    public KamikazeShip(Vector2 position, int health) {
        super(GameAssets.getInstance().getTextureInstance(GameAssets.KAMIKAZE), position, "kamikaze", health);
        this.color = Color.BLUE;
        this.scoreValue = GameConstants.KAMIKAZE_SHIP_SCORE_VALUE;
        this.health = health * GameConstants.KAMIKAZE_SHIP_HEALTH_FACTOR;
        this.speed = (float) Math.random()
                * (GameConstants.KAMIKAZE_SHIP_MAX_SPEED - GameConstants.KAMIKAZE_SHIP_MIN_SPEED)
                + GameConstants.KAMIKAZE_SHIP_MIN_SPEED;
        this.playerPosition = GameContext.getInstance().getPlayer().getBody().getWorldCenter();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        float angle = getRealisticRotationAngle(delta);
        if (angle != -500) {
            sprite.setRotation(angle);
        }
        if (teleportAnimation.isTeleporting()) {
            teleportAnimation.update(delta);
        } else if (playerPosition != null) {
            moveTowardPlayer(delta);
        }
    }

    protected float getRealisticRotationAngle(float delta) {
        Vector2 velocity = body.getLinearVelocity();

        if (!velocity.isZero()) {
            targetAngle = velocity.angleDeg() - 90;

            float currentAngle = sprite.getRotation();
            float angleDifference = targetAngle - currentAngle;

            while (angleDifference < -180)
                angleDifference += 360;
            while (angleDifference > 180)
                angleDifference -= 360;

            return currentAngle + angleDifference * rotationSpeed * delta;
        }
        return -500;
    }

    private void moveTowardPlayer(float delta) {
        Vector2 direction = new Vector2(playerPosition).sub(body.getWorldCenter());

        if (direction.len() > GameConstants.KAMIKAZE_SHIP_CLOSE_DISTANCE) {
            float angle = getRealisticRotationAngle(delta);

            if (angle != -500) {
                body.setTransform(body.getPosition(), angle * MathUtils.degreesToRadians);
            }
        }

        body.setLinearVelocity(direction.nor().scl(speed));
    }

    @Override
    public void onCollision(DrawnEntity other) {
        super.onCollision(other);
        if (other instanceof Wall) {
            Vector2 direction = body.getLinearVelocity().nor();
            startTeleportationAnimation(direction.scl(teleportDistance));
        }
    }
}
