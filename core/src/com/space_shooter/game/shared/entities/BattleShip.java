package com.space_shooter.game.shared.entities;

import org.w3c.dom.Text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.shared.utils.BodyFactory;
import com.space_shooter.game.shared.utils.TeleportAnimation;
import com.space_shooter.game.weapons.WeaponManager;

public abstract class BattleShip extends DrawnEntity {
    protected World world;
    protected int health;
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
    protected TeleportAnimation teleportAnimation;
    


    public BattleShip(Texture texture, Vector2 spawnPosition, String fileName) {
        this.isTeleporting = false;
        this.teleportAnimationTimer = 0f;
        this.teleportStart = new Vector2();
        this.teleportEnd = new Vector2();
        this.world = GameContext.getInstance().getWorld();
        this.weaponManager = new WeaponManager(this);
        this.velocity = new Vector2();
        this.sprite = new Sprite(texture);
        this.sprite.setSize(texture.getWidth() / 10, texture.getHeight() / 10);
        this.body = BodyFactory.getInstance().createBody(world, BodyType.DynamicBody, spawnPosition.x, spawnPosition.y, false, 0);
        BodyFactory.getInstance().attachComplexeFixture(body, fileName, sprite.getWidth(), 1f, 0f, 0f, false);
        this.body.setUserData(this);
        this.body.setFixedRotation(true);
        this.sprite.setOriginCenter();

        this.teleportAnimation = new TeleportAnimation(sprite, body);
    }

    @Override
    public void update(float delta) {
        teleportAnimation.update(delta);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (teleportAnimation.isTeleporting()) {
            teleportAnimation.render(spriteBatch);
        } else {
            Vector2 centerPos = body.getWorldCenter();
            sprite.setPosition(centerPos.x - sprite.getWidth() / 2, centerPos.y - sprite.getHeight() / 2);
            sprite.draw(spriteBatch);
        }
    }

    protected void startTeleportationAnimation(Vector2 direction) {
        if (direction.len() > teleportDistance) {
            direction.setLength(teleportDistance);
        }
        
        Vector2 end = new Vector2(body.getWorldCenter().add(direction));
        teleportAnimation.startTeleportation(body.getWorldCenter(), end);
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
        float x = body.getWorldCenter().x;
        float y = body.getWorldCenter().y;
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
        float x = body.getWorldCenter().x + radius;
        float y = body.getWorldCenter().y + radius;
        return x >= 0 && x <= GameContext.getInstance().getCamera().viewportWidth && y >= 0 && y <= GameContext.getInstance().getCamera().viewportHeight;
    }

    public int isHedgeOfScreen() {
        Vector2 position = body.getWorldCenter();
    
        OrthographicCamera camera = GameContext.getInstance().getCamera();
    
        float minX = camera.position.x - camera.viewportWidth / 2;
        float maxX = camera.position.x + camera.viewportWidth / 2;
        float minY = camera.position.y - camera.viewportHeight / 2;
        float maxY = camera.position.y + camera.viewportHeight / 2;
    
        if (position.x - radius <= minX) {
            return 1; 
        } else if (position.x + radius >= maxX) {
            return 2; 
        } else if (position.y - radius <= minY) {
            return 3; 
        } else if (position.y + radius >= maxY) {
            return 4; 
        }
        return 0; 
    }
    
}
