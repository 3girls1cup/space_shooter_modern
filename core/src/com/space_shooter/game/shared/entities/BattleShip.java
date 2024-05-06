package com.space_shooter.game.shared.entities;

import org.w3c.dom.Text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.weapons.WeaponManager;

public abstract class BattleShip extends DrawnEntity {
    protected World world;
    protected int health;
    protected Texture texture;
    protected Color color;
    protected WeaponManager weaponManager;
    protected Vector2 velocity;
    protected float radius;
    protected float speed;
    protected float teleportDistance;
    protected boolean isTeleporting;
    protected float teleportAnimationTimer;
    protected Vector2 teleportStart;
    protected Vector2 teleportEnd;

    public BattleShip() {
        this.isTeleporting = false;
        this.teleportAnimationTimer = 0f;
        this.teleportStart = new Vector2();
        this.teleportEnd = new Vector2();
        this.world = GameContext.getInstance().getWorld();
        this.weaponManager = new WeaponManager(this);
        this.velocity = new Vector2();
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (isTeleporting) {
            shapeRenderer.setColor(color);
    
            float progress = teleportAnimationTimer / GameConstants.TELEPORT_ANIMATION_DURATION;
            for (int i = 0; i < GameConstants.NUM_TELEPORT_CIRCLES; i++) {
                float t = progress * (GameConstants.NUM_TELEPORT_CIRCLES - i) / GameConstants.NUM_TELEPORT_CIRCLES;
                Vector2 pos = new Vector2(teleportStart).lerp(teleportEnd, t);
                shapeRenderer.circle(pos.x, pos.y, radius * (1 - progress));
            }
        } else {
            shapeRenderer.setColor(color);
            int segments = 6 + (int) (6 * Math.cbrt(radius));
            shapeRenderer.circle(body.getPosition().x, body.getPosition().y, radius, segments);
        }
        shapeRenderer.end();
    }

    protected void updateTeleportationAnimation(float deltaTime) {
        teleportAnimationTimer += deltaTime;
        if (teleportAnimationTimer >= GameConstants.TELEPORT_ANIMATION_DURATION) {
            isTeleporting = false;
            body.setTransform(teleportEnd, body.getAngle());
        }
    }

    protected void startTeleportationAnimation(Vector2 direction) {
        if (direction.len() > teleportDistance) {
            direction.setLength(teleportDistance);
        } 
        teleportEnd.set(body.getPosition().add(direction));
        teleportStart.set(body.getPosition());
        isTeleporting = true;
        teleportAnimationTimer = 0f;
    }

    public WeaponManager getWeaponManager() {
        return weaponManager;
    }

    public void shoot(Vector2 direction) {
        weaponManager.getCurrentWeapon().manageFireAllowance(direction);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            died();
        }
    }

    protected void died() {
        markForRemoval();
    }

    public int getHealth() {
        return health;
    }

    public World getWorld() {
        return world;
    }

    public float getRadius() {
        return radius;
    }

    public int getQuadrantPosition() {
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        if (x > GameContext.getInstance().getCamera().viewportWidth / 2) {
            if (y > GameContext.getInstance().getCamera().viewportHeight / 2) {
                return 1;
            } else {
                return 4;
            }
        } else {
            if (y > GameContext.getInstance().getCamera().viewportHeight / 2) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    public boolean isOutOfScreen() {
        return isHedgeOfScreen() != 0;
    }
    
    public boolean isInsideScreen() {
        float x = body.getPosition().x + radius;
        float y = body.getPosition().y + radius;
        return x >= 0 && x <= GameContext.getInstance().getCamera().viewportWidth && y >= 0 && y <= GameContext.getInstance().getCamera().viewportHeight;
    }

    public int isHedgeOfScreen() {
        Vector2 position = body.getPosition();
    
        OrthographicCamera camera = GameContext.getInstance().getCamera();
    
        float minX = camera.position.x - camera.viewportWidth / 2;
        float maxX = camera.position.x + camera.viewportWidth / 2;
        float minY = camera.position.y - camera.viewportHeight / 2;
        float maxY = camera.position.y + camera.viewportHeight / 2;
    
        if (position.x - radius <= minX) {
            return 1; // Gauche
        } else if (position.x + radius >= maxX) {
            return 2; // Droite
        } else if (position.y - radius <= minY) {
            return 3; // Bas
        } else if (position.y + radius >= maxY) {
            return 4; // Haut
        }
        return 0; // Pas sur un bord
    }
    
}