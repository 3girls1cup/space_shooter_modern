package com.space_shooter.game.ennemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.space_shooter.game.core.GameAssets;
import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.shared.entities.DrawnEntity;
import com.space_shooter.game.shared.utils.VisualDebugger;
import com.space_shooter.game.walls.Wall;
import com.space_shooter.game.weapons.BasicWeapon;

public class DistanceShooterShip extends EnnemyShip {
    static private final float SAFE_DISTANCE = 30f;
    static private final float MIN_DELAY_BETWEEN_SHOTS = 3.0f;
    static private final float MAX_DELAY_BETWEEN_SHOTS = 10.0f;
    private long lastShotTime = TimeUtils.millis();
    private long nextShotDelay;
    private boolean arrivedInScreen = false;
    private Vector2 playerPosition;
    private Vector2 targetPosition = new Vector2();
    private boolean needNewTargetPosition = false;

    /*#### For debugging purposes ####*/
    private Vector2 maxAngle = new Vector2();
    private Vector2 minAngle = new Vector2();
    private Vector2 snapshotPos = new Vector2();
    /*################################*/

    public DistanceShooterShip(Vector2 position) {
        super(GameAssets.getInstance().getTextureInstance(GameAssets.DISTANCE_SHOOTER), position, "distance_shooter");
        this.color = Color.YELLOW;
        this.health = GameConstants.DISTANCE_SHOOTER_HEALTH;
        this.scoreValue = GameConstants.DISTANCE_SHOOTER_SCORE_VALUE;
        this.speed = (float) (Math.random()
                * (GameConstants.DISTANCE_SHOOTER_MAX_SPEED - GameConstants.DISTANCE_SHOOTER_MIN_SPEED)
                + GameConstants.DISTANCE_SHOOTER_MIN_SPEED);
        this.weaponManager.addWeapon(new BasicWeapon(GameConstants.BASIC_NAME, 20f, 0.4f, GameAssets.getInstance().getTextureInstance(GameAssets.BASIC_WEAPON_PLAYER), 1, -1, 500, true, this.weaponManager));
        this.playerPosition = GameContext.getInstance().getPlayer().getBody().getWorldCenter();

        nextShotDelay = (long) MathUtils.random(MIN_DELAY_BETWEEN_SHOTS, MAX_DELAY_BETWEEN_SHOTS) * 1000L;
    }

    private void shootTowardPlayer() {
        Vector2 direction = new Vector2(playerPosition.x - body.getWorldCenter().x,
                playerPosition.y - body.getWorldCenter().y).nor();
        shoot(direction);
    }
    
    private void manageShooting() {
        if (TimeUtils.millis() - lastShotTime > nextShotDelay) {
            shootTowardPlayer();
            lastShotTime = TimeUtils.millis();  
            nextShotDelay = (long) MathUtils.random(MIN_DELAY_BETWEEN_SHOTS, MAX_DELAY_BETWEEN_SHOTS) * 1000L;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        VisualDebugger.getInstance().drawDistanceShooterDebug(batch, body.getWorldCenter(), targetPosition, playerPosition, snapshotPos, maxAngle, minAngle, SAFE_DISTANCE);
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        if (teleportAnimation.isTeleporting()) {
            teleportAnimation.update(delta);
        } else {
            if (!arrivedInScreen) {
                targetPosition.set(playerPosition);
                goToTargetPosition();

                if (!isOutOfScreen()) {
                    arrivedInScreen = true;
                }
            } else {
                manageShooting();
                handleMovement();
            }
            updateBody();
        }
    }

    private Vector2 getDirectionToTargetPosition() {
        return new Vector2(targetPosition.x - body.getWorldCenter().x, targetPosition.y - body.getWorldCenter().y);
    }

    private void goToTargetPosition() {
        Vector2 direction = getDirectionToTargetPosition();
        if (direction.len() < 1f) {
            setTargetPosition(playerPosition, SAFE_DISTANCE);
        }
        velocity.set(direction.nor());
    }

    private void handleMovement() {
        Vector2 directionToPlayer = new Vector2(playerPosition).sub(body.getWorldCenter());
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
                setTargetPosition(playerPosition, SAFE_DISTANCE);
                needNewTargetPosition = false;
            }
            goToTargetPosition();
        }
    }

    private boolean isTrajectoryInSafeZone() {
        if (Intersector.intersectSegmentCircle(body.getWorldCenter(), targetPosition, playerPosition, SAFE_DISTANCE)) {
            return false;
        }
        return true;
    }

    public void setTargetPosition(Vector2 posEntityToAvoid, float safeDistance) {
        float xC = posEntityToAvoid.x;
        float yC = posEntityToAvoid.y;

        float xA = body.getWorldCenter().x;
        float yA = body.getWorldCenter().y;
        float r = safeDistance;
    
        float d = Vector2.dst(xA, yA, xC, yC);
    
        float alpha = MathUtils.atan2(yC - yA, xC - xA);
    
        if (d > r) {
            float theta = MathUtils.asin(r / d);
            float angle_min = alpha - theta;
            float angle_max = alpha + theta;

            angle_min = (angle_min + MathUtils.PI2) % MathUtils.PI2;
            angle_max = (angle_max + MathUtils.PI2) % MathUtils.PI2;

            float chosen_angle;

            if (MathUtils.randomBoolean()) {
                chosen_angle = MathUtils.random(0, angle_min);
            } else {
                chosen_angle = MathUtils.random(angle_max, MathUtils.PI2);
            }

            /*#### For debugging purposes ####*/
            maxAngle.set(xA + d * MathUtils.cos(angle_max), yA + d * MathUtils.sin(angle_max));
            minAngle.set(xA + d * MathUtils.cos(angle_min), yA + d * MathUtils.sin(angle_min));
            snapshotPos.set(xA, yA);
            /*################################*/

            float maxDistance = calculateMaxDistance(xA, yA, chosen_angle);
            float chosen_distance = MathUtils.random(r + 1, maxDistance);
    
            float targetX = xA + chosen_distance * MathUtils.cos(chosen_angle);
            float targetY = yA + chosen_distance * MathUtils.sin(chosen_angle);
    
            targetPosition.x = MathUtils.clamp(targetX, 0, GameConfig.WORLD_WIDTH);
            targetPosition.y = MathUtils.clamp(targetY, 0, GameConfig.WORLD_HEIGHT);
        }
    }
    
    private float calculateMaxDistance(float xA, float yA, float angle) {
        float maxX = (MathUtils.cos(angle) > 0 ? GameConfig.WORLD_WIDTH - xA : xA);
        float maxY = (MathUtils.sin(angle) > 0 ? GameConfig.WORLD_HEIGHT - yA : yA);
        return Math.min(maxX / Math.abs(MathUtils.cos(angle)), maxY / Math.abs(MathUtils.sin(angle)));
    }

    private Vector2 findEscapeDirection(int screenHedge) {
        Vector2 directionToPlayer = new Vector2(playerPosition).sub(body.getWorldCenter());
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

    private void updateBody() {
        body.setLinearVelocity(velocity.scl(speed));
    }

    @Override 
    public void onCollision(DrawnEntity entity) {
        if (entity instanceof Wall) {
            startTeleportationAnimation(getDirectionToTargetPosition());
        }

        if (entity instanceof EnnemyShip) {
            EnnemyShip ennemyShip = (EnnemyShip) entity;
            setTargetPosition(ennemyShip.getBody().getWorldCenter(), ennemyShip.getSprite().getWidth() + 1);
        }
    }
}
