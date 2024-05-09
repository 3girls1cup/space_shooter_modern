package com.space_shooter.game.shared.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.space_shooter.game.core.GameConfig;
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
    protected float speed;
    protected float teleportDistance;
    protected boolean isTeleporting;
    protected float teleportAnimationTimer;
    protected Vector2 teleportStart;
    protected Vector2 teleportEnd;
    protected TeleportAnimation teleportAnimation;
    protected Animation<TextureRegion> animation;
    private float animationTimer;
    private TextureRegion currentFrame;
    private int FRAME_COLS;
    private int FRAME_ROWS;
    private int numberOfFrames;
    private float frameDuration;
    


    public BattleShip() {
        this.isTeleporting = false;
        this.teleportAnimationTimer = 0f;
        this.teleportStart = new Vector2();
        this.teleportEnd = new Vector2();
        this.weaponManager = new WeaponManager(this);
        this.velocity = new Vector2();
    }

    protected void setStaticSprite(Texture texture) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() / GameConfig.PIXELS_PER_METER, texture.getHeight() / GameConfig.PIXELS_PER_METER);
        sprite.setOriginCenter();
    }

    protected void addBodyToWorld(Vector2 spawnPosition, String fileName) {
        world = GameContext.getInstance().getWorld();
        body = BodyFactory.getInstance().createBody(world, BodyType.DynamicBody, spawnPosition.x, spawnPosition.y, false, 0);
        BodyFactory.getInstance().attachComplexeFixture(body, fileName, sprite.getWidth(), 1f, 0f, 0f, false);
        body.setUserData(this);
        body.setFixedRotation(true);
        this.teleportAnimation = new TeleportAnimation(sprite, body);
    }


    protected void setAnimationSprite(int frame_cols, int frame_rows, Texture texture) {
        this.texture = texture;
        this.FRAME_COLS = frame_cols;
        this.FRAME_ROWS = frame_rows;
        this.numberOfFrames = FRAME_COLS * FRAME_ROWS;
        this.frameDuration = 1f / numberOfFrames;
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / FRAME_COLS, texture.getHeight() / FRAME_ROWS);
        TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(frameDuration, frames);
        TextureRegion firstFrame = new TextureRegion(frames[0]);
        sprite = new Sprite(firstFrame);
        sprite.setSize(firstFrame.getRegionWidth() / GameConfig.PIXELS_PER_METER, firstFrame.getRegionHeight() / GameConfig.PIXELS_PER_METER);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);


    }

    @Override
    public void update(float delta) {
        if (animation != null) {
            animationTimer += delta;
            currentFrame = animation.getKeyFrame(animationTimer, true);
            sprite.setRegion(currentFrame);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (teleportAnimation.isTeleporting()) {
            teleportAnimation.render(spriteBatch);
        } else {
            Vector2 centerPos = body.getWorldCenter();
            sprite.setPosition(centerPos.x - getHalfWidth(), centerPos.y - getHalfHeight());
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
        float x = body.getWorldCenter().x + sprite.getWidth() / 2;
        float y = body.getWorldCenter().y + sprite.getHeight() / 2;
        return x >= 0 && x <= GameContext.getInstance().getCamera().viewportWidth && y >= 0 && y <= GameContext.getInstance().getCamera().viewportHeight;
    }

    public float getHalfWidth() {
        return sprite.getWidth() / 2;
    }

    public float getHalfHeight() {
        return sprite.getHeight() / 2;
    }

    public int isHedgeOfScreen() {
        Vector2 position = body.getWorldCenter();
    
        OrthographicCamera camera = GameContext.getInstance().getCamera();
    
        float minX = camera.position.x - camera.viewportWidth / 2;
        float maxX = camera.position.x + camera.viewportWidth / 2;
        float minY = camera.position.y - camera.viewportHeight / 2;
        float maxY = camera.position.y + camera.viewportHeight / 2;
        float halfWidth = sprite.getWidth() / 2;
        float halfHeight = sprite.getHeight() / 2;

        if (position.x - halfWidth <= minX) {
            return 1; 
        } else if (position.x + halfWidth >= maxX) {
            return 2; 
        } else if (position.y - halfHeight <= minY) {
            return 3; 
        } else if (position.y + halfHeight >= maxY) {
            return 4; 
        }
        return 0; 
    }
    
}
