package com.space_shooter.game.ennemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.utils.TimeUtils;
import com.space_shooter.game.core.GameAssets;
import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.shared.entities.DrawnEntity;
import com.space_shooter.game.shared.utils.BodyFactory;
import com.space_shooter.game.walls.Wall;
import com.space_shooter.game.weapons.BasicWeapon;

public class DistanceShooterShip extends EnnemyShip {
    static private final float SAFE_DISTANCE = 30f;
    private long lastShotTime = TimeUtils.nanoTime();
    private float minTimeBetweenShots = 3.0f; 
    private float maxTimeBetweenShots = 10.0f; 
    private boolean arrivedInScreen = false;
    private Vector2 playerPosition;
    private Vector2 targetPosition = new Vector2();
    private boolean needNewTargetPosition = false;

    public DistanceShooterShip(Vector2 position) {
        super(GameAssets.getInstance().getTextureInstance(GameAssets.DISTANCE_SHOOTER), position, "distance_shooter");
        this.color = Color.YELLOW;
        this.health = GameConstants.DISTANCE_SHOOTER_HEALTH;
        this.radius = GameConstants.DISTANCE_SHOOTER_RADIUS;
        this.scoreValue = GameConstants.DISTANCE_SHOOTER_SCORE_VALUE;
        this.speed = (float) (Math.random()
                * (GameConstants.DISTANCE_SHOOTER_MAX_SPEED - GameConstants.DISTANCE_SHOOTER_MIN_SPEED)
                + GameConstants.DISTANCE_SHOOTER_MIN_SPEED);
        this.weaponManager.addWeapon(new BasicWeapon(GameConstants.BASIC_NAME, 20f, 0.4f, GameAssets.getInstance().getTextureInstance(GameAssets.BASIC_WEAPON_PLAYER), 1, -1, 500, true, this.weaponManager));
        this.playerPosition = GameContext.getInstance().getPlayer().getBody().getPosition();
    }

    private void shootTowardPlayer() {
        Vector2 direction = new Vector2(playerPosition.x - body.getPosition().x,
                playerPosition.y - body.getPosition().y).nor();
        shoot(direction);
    }


    
    private void manageShooting(float delta) {
        float timeSinceLastShot = TimeUtils.nanosToMillis(TimeUtils.timeSinceNanos(lastShotTime)) / 1000.0f;
    
        float shootingInterval = MathUtils.random(minTimeBetweenShots, maxTimeBetweenShots);
    
        if (timeSinceLastShot > shootingInterval) {
            shootTowardPlayer();
            lastShotTime = TimeUtils.nanoTime();  
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (teleportAnimation.isTeleporting()) {
            teleportAnimation.update(delta);
        } else {
            if (!arrivedInScreen) {
                goToTargetPosition();

                if (!isOutOfScreen()) {
                    arrivedInScreen = true;
                }
            } else {
                manageShooting(delta);
                maintainSafeDistance();
            }
            updateVelocity(delta);
        }
    }
    

    private void goToTargetPosition() {
        Vector2 direction = new Vector2(targetPosition.x - body.getPosition().x,
                targetPosition.y - body.getPosition().y);
        if (direction.len() < 1f) {
            setTargetPosition();

        }
        velocity.set(direction.nor());
    }

    private void maintainSafeDistance() {
        Vector2 directionToPlayer = new Vector2(playerPosition).sub(body.getPosition());
        float distanceToPlayer = directionToPlayer.len();

        if (distanceToPlayer < SAFE_DISTANCE) {
            int hedgeOfScreen = isHedgeOfScreen();
            if (hedgeOfScreen != 0) {
                velocity.set(findEscapeDirection(hedgeOfScreen));
            } else {
                velocity.set(directionToPlayer).scl(-1).nor();
            }
            needNewTargetPosition = true;
        } else {
            if (needNewTargetPosition || !isTrajectoryInSafeZone()) {
                setTargetPosition();
                needNewTargetPosition = false;
            }
            goToTargetPosition();
        }
    }

    private boolean isTrajectoryInSafeZone() {

        if (Intersector.intersectSegmentCircle(body.getPosition(), targetPosition, playerPosition, SAFE_DISTANCE)) {
            return false;
        }

        return true;
    }

    private void setTargetPosition() {
        Vector2 directionToPlayer = new Vector2(playerPosition).sub(body.getPosition());
        Vector2 perpendicularDirection = new Vector2(directionToPlayer.y, -directionToPlayer.x).nor();

        float randomOffset = MathUtils.random(-SAFE_DISTANCE, SAFE_DISTANCE);
        Vector2 offset = new Vector2(perpendicularDirection).scl(randomOffset);

        targetPosition.set(body.getPosition()).add(offset);

        targetPosition.x = MathUtils.clamp(targetPosition.x, 0, GameConfig.WORLD_WIDTH);
        targetPosition.y = MathUtils.clamp(targetPosition.y, 0, GameConfig.WORLD_HEIGHT);

    }

    private Vector2 findEscapeDirection(int screenHedge) {
        Vector2 directionToPlayer = new Vector2(playerPosition).sub(body.getPosition());
        Vector2 escapeDirection = new Vector2();

        if (screenHedge == 1 || screenHedge == 2) {
            escapeDirection.set(directionToPlayer.y, -directionToPlayer.x).nor();
        } else if (screenHedge == 3 || screenHedge == 4) {
            escapeDirection.set(-directionToPlayer.y, directionToPlayer.x).nor();
        }

        if (escapeDirection.len() == 0) {
            escapeDirection.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f)).nor();
        }

        return escapeDirection;
    }

    private void updateVelocity(float delta) {
        body.setLinearVelocity(velocity.scl(speed));
    }

    @Override 
    public void onCollision(DrawnEntity entity) {
        if (entity instanceof Wall) {
            startTeleportationAnimation(body.getLinearVelocity());
        }
    }
}
