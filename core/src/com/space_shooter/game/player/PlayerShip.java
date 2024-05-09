package com.space_shooter.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.space_shooter.game.core.GameAssets;
import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.ennemies.EnnemyShip;
import com.space_shooter.game.shared.entities.BattleShip;
import com.space_shooter.game.shared.entities.DrawnEntity;
import com.space_shooter.game.walls.Wall;
import com.space_shooter.game.weapons.BasicWeapon;
import com.space_shooter.game.weapons.LaserWeapon;

public class PlayerShip extends BattleShip {
    private Camera camera;
    private float timeSinceLastTeleport = 0f;
    private float timeSinceLastCollision = 0f;
    private boolean isShooting = false;

    public PlayerShip() {
        // super(GameAssets.getInstance().getTextureInstance(GameAssets.PLAYER_SHIP), new Vector2(GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2), "player_ship");
        super();
        this.teleportDistance = 100f;
        this.color = Color.RED;
        this.speed = GameConstants.PLAYER_SHIP_SPEED;
        this.camera = GameContext.getInstance().getCamera();
        this.health = GameConstants.PLAYER_SHIP_HEALTH;
        this.weaponManager.addWeapon(new BasicWeapon(GameConstants.BASIC_NAME, 100f, 0.3f, GameAssets.getInstance().getTextureInstance(GameAssets.BASIC_WEAPON_PLAYER), 1, -1, 500, true, this.weaponManager));
        this.weaponManager.addWeapon(new LaserWeapon(GameConstants.LASER_NAME, 1, 500, 100, true, this.weaponManager));
        setStaticSprite(GameAssets.getInstance().getTextureInstance(GameAssets.PLAYER_SHIP));
        // setAnimationSprite(7,3, GameAssets.getInstance().getTextureInstance(GameAssets.ANIMATED_PLAYER_SHIP));
        addBodyToWorld(new Vector2(GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2), "player_ship");
        this.body.setTransform(body.getWorldCenter(), -90 * MathUtils.degreesToRadians);

        this.sprite.setRotation(-90);
    }

    @Override
    public void update(float delta) {
        if (teleportAnimation.isTeleporting()) {
            teleportAnimation.update(delta);
        } else {
            super.update(delta);
            
            handleInput();

            Vector2 position = body.getPosition();

            float minX = camera.position.x - camera.viewportWidth / 2;
            float maxX = camera.position.x + camera.viewportWidth / 2 - sprite.getHeight();
            float minY = camera.position.y - camera.viewportHeight / 2 + sprite.getWidth();
            float maxY = camera.position.y + camera.viewportHeight / 2;

            float clampedX = Math.max(minX, Math.min(position.x, maxX));
            float clampedY = Math.max(minY, Math.min(position.y, maxY));

            if (position.x != clampedX || position.y != clampedY) {
                body.setTransform(clampedX, clampedY, body.getAngle());
            }

            timeSinceLastTeleport += delta;
        }
        timeSinceLastCollision += delta;
    }

    public void handleInput() {
        isShooting = false;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
                && timeSinceLastTeleport >= GameConstants.TELEPORT_COOLDOWN) {
            startTeleportationAnimation(getDirectionFromMouse());
        } else {
            velocity.set(0, 0);

            if (weaponManager.getCurrentWeapon().isAutomatic()) {
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    isShooting = true;
                    shootTowardsMouse();
                }
            } else {
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    isShooting = true;
                    shootTowardsMouse();
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                weaponManager.switchToNextWeapon();
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocity.x -= 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocity.x += 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                velocity.y += 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                velocity.y -= 1;
            }

            body.setLinearVelocity(velocity.scl(speed));
        }
    }

    public boolean isShooting() {
        return isShooting;
    }

    private void shootTowardsMouse() {
        Vector2 direction = getDirectionFromMouse().nor();
        shoot(direction);
    }

    @Override
    public void startTeleportationAnimation(Vector2 direction) {
        super.startTeleportationAnimation(direction);
        timeSinceLastTeleport = 0f;
    }

    private Vector2 getDirectionFromMouse() {
        Vector3 mousePosInWorld = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        return new Vector2(mousePosInWorld.x, mousePosInWorld.y).sub(body.getWorldCenter());
    }

    public void increaseTeleportDistance(float amount) {
        if (teleportDistance + amount > GameConstants.MAX_TELEPORT_DISTANCE) {
            return;
        }
        teleportDistance += amount;
    }

    @Override
    public void onCollision(DrawnEntity entity) {
        if (entity instanceof Wall) {
            if (timeSinceLastCollision > GameConstants.COLLISION_COOLDOWN) {
                takeDamage(1);
                timeSinceLastCollision = 0f;
            }
        } else if (entity instanceof EnnemyShip) {
            takeDamage(1);
            timeSinceLastCollision = 0f;

            EnnemyShip ennemyShip = (EnnemyShip) entity;
            ennemyShip.takeDamage(1);
        }
    }
}
