package com.space_shooter.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.ennemies.EnnemyShip;
import com.space_shooter.game.shared.entities.BattleShip;
import com.space_shooter.game.shared.entities.DrawnEntity;
import com.space_shooter.game.shared.utils.BodyFactory;
import com.space_shooter.game.walls.Wall;
import com.space_shooter.game.weapons.BasicWeapon;
import com.space_shooter.game.weapons.LaserWeapon;

public class PlayerShip extends BattleShip {
    private Camera camera;
    private float timeSinceLastTeleport = 0f;
    private float timeSinceLastCollision = 0f;
    private boolean isShooting = false;

    public PlayerShip() {
        super();
        this.teleportDistance = 20f;
        this.color = Color.RED;
        this.speed = GameConstants.PLAYER_SHIP_SPEED;
        this.camera = GameContext.getInstance().getCamera();
        this.health = GameConstants.PLAYER_SHIP_HEALTH;
        this.radius = GameConstants.PLAYER_SHIP_RADIUS;
        this.weaponManager.addWeapon(new BasicWeapon(GameConstants.BASIC_NAME, 100f, 0.3f, Color.WHITE, 1, -1, 500, true, this.weaponManager));
        this.weaponManager.addWeapon(new LaserWeapon(GameConstants.LASER_NAME, 1, 500, 100, true, this.weaponManager));

        this.body = BodyFactory.createBody(world, BodyType.DynamicBody, GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2, false, 0);
        this.body.setUserData(this);
    
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        BodyFactory.createFixture(body, shape, 1f, 0f, 1f, false);
    }

    @Override
    public void update(float delta) {
        if (isTeleporting) {
            updateTeleportationAnimation(delta);
        } else {
            handleInput();

            Vector2 position = body.getPosition();
            float halfWidth = radius;
            float halfHeight = radius;

            float minX = camera.position.x - camera.viewportWidth / 2 + halfWidth;
            float maxX = camera.position.x + camera.viewportWidth / 2 - halfWidth;
            float minY = camera.position.y - camera.viewportHeight / 2 + halfHeight;
            float maxY = camera.position.y + camera.viewportHeight / 2 - halfHeight;

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

            velocity.scl(speed);
            body.setLinearVelocity(velocity);
        }
    }

    // @Override
    // public void render(ShapeRenderer shapeRenderer) {
    //     super.render(shapeRenderer);
    //     if (true) {
    //         // Dessiner la ligne de direction
    //         Vector2 bodyPos = body.getPosition();
    //         Vector2 mouseDirection = getDirectionFromMouse();
    //         shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    //         shapeRenderer.setColor(Color.RED);  // Couleur de la ligne
    //         shapeRenderer.line(bodyPos.x, bodyPos.y, mouseDirection.x, mouseDirection.y);
    //         shapeRenderer.end();
        
    //     }
    // }

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
        return new Vector2(mousePosInWorld.x, mousePosInWorld.y).sub(body.getPosition());
    }

    public void increaseTeleportDistance(float amount) {
        if (teleportDistance + amount > GameConstants.MAX_TELEPORT_DISTANCE) {
            return;
        }
        teleportDistance += amount;
    }

    @Override
    public void onCollision(DrawnEntity entity) {
        if (timeSinceLastCollision < GameConstants.COLLISION_COOLDOWN) {
            return;
        }
        if (entity instanceof EnnemyShip || entity instanceof Wall) {
            takeDamage(1);
            timeSinceLastCollision = 0f;
        }
    }
}