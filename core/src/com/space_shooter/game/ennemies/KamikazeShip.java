package com.space_shooter.game.ennemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.shared.utils.BodyFactory;

public class KamikazeShip extends EnnemyShip{

    private Vector2 playerPosition;

    public KamikazeShip(Vector2 position) {
        super();
        this.color = Color.BLUE;
        this.scoreValue = GameConstants.KAMIKAZE_SHIP_SCORE_VALUE;
        this.health = GameConstants.KAMIKAZE_SHIP_HEALTH;
        this.radius = GameConstants.KAMIKAZE_SHIP_RADIUS;
        this.speed = (float) (Math.random() * (GameConstants.KAMIKAZE_SHIP_MAX_SPEED - GameConstants.KAMIKAZE_SHIP_MIN_SPEED) + GameConstants.KAMIKAZE_SHIP_MIN_SPEED);
        this.playerPosition = GameContext.getInstance().getPlayer().getBody().getPosition();
        this.body = BodyFactory.createBody(world, BodyType.DynamicBody, position.x, position.y, false, 0);
        this.body.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        BodyFactory.createFixture(body, shape, 1f, 0f, 1f, false);
    }

    @Override
    public void update(float delta) {
        if (isTeleporting) {
            updateTeleportationAnimation(delta);
        } else if (playerPosition != null) {
            moveTowardPlayer();
        }
    }

    private void moveTowardPlayer() {
        Vector2 direction = new Vector2(playerPosition.x - body.getPosition().x, playerPosition.y - body.getPosition().y).nor();
        body.setLinearVelocity(direction.scl(speed));
    }
}
