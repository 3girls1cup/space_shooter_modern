package com.space_shooter.game.ennemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.space_shooter.game.core.GameAssets;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.core.GameContext;

public class KamikazeShip extends EnnemyShip{

    private Vector2 playerPosition;

    public KamikazeShip(Vector2 position) {
        super(GameAssets.getInstance().getTextureInstance(GameAssets.KAMIKAZE), position, "kamikaze");
        this.color = Color.BLUE;
        this.scoreValue = GameConstants.KAMIKAZE_SHIP_SCORE_VALUE;
        this.health = GameConstants.KAMIKAZE_SHIP_HEALTH;
        this.speed = (float) (Math.random() * (GameConstants.KAMIKAZE_SHIP_MAX_SPEED - GameConstants.KAMIKAZE_SHIP_MIN_SPEED) + GameConstants.KAMIKAZE_SHIP_MIN_SPEED);
        this.playerPosition = GameContext.getInstance().getPlayer().getBody().getWorldCenter();
        this.body.setFixedRotation(true);
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

    private void moveTowardPlayer(float delta) {
        Vector2 direction = new Vector2(playerPosition).sub(body.getWorldCenter());

        if (direction.len() > GameConstants.KAMIKAZE_SHIP_CLOSE_DISTANCE) {
            float angle = getRealisticRotationAngle(delta);
            
            if (angle != -500) {
                body.setTransform(body.getWorldCenter(), angle * MathUtils.degreesToRadians);
            }
        }

        body.setLinearVelocity(direction.nor().scl(speed));
    }
}
