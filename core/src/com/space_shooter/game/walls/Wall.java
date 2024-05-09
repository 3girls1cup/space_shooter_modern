package com.space_shooter.game.walls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.space_shooter.game.core.GameAssets;
import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.shared.entities.DrawnEntity;
import com.space_shooter.game.shared.utils.BodyFactory;

public class Wall extends DrawnEntity {
    private static final float SECURITY_ADDED_WIDTH = 10f;
    private float width;
    private float speed;

    public Wall(float height, int angle, float speed) {
        this.width = GameConfig.SCREEN_DIAGONAL + SECURITY_ADDED_WIDTH;
        this.speed = speed;
        this.body = BodyFactory.getInstance().createBody(GameContext.getInstance().getWorld(), BodyType.KinematicBody, getInitialPosition(angle).x, getInitialPosition(angle).y, false, angle);
        this.body.setUserData(this);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        BodyFactory.getInstance().createFixture(body, shape, 1f, 0f, 1f, false);
        texture = GameAssets.getInstance().getTextureInstance(GameAssets.WALL);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        sprite = new Sprite(texture);
        sprite.setSize(width, height);
        sprite.setOriginCenter();

        int textureWidth = texture.getWidth() / 3;
        int textureHeight = texture.getHeight();
        float uStart = MathUtils.random(0, textureWidth - width) / textureWidth;
        float vStart = MathUtils.random(0, textureHeight - height) / textureHeight;
        float uEnd = (uStart * textureWidth + width) / textureWidth;
        float vEnd = (vStart * textureHeight + height) / textureHeight;

        sprite.setRegion((int) (uStart * textureWidth), (int) (vStart * textureHeight), (int) width, (int) height);
        sprite.setU2(uEnd);
        sprite.setV2(vEnd);
    }

    @Override
    public void update(float delta) {
        if (body.getWorldCenter().x < 0) {
            markForRemoval();
        }

        float velocityX = speed * MathUtils.cos(body.getAngle() + MathUtils.PI / 2);
        float velocityY = speed * MathUtils.sin(body.getAngle() + MathUtils.PI / 2);
        body.setLinearVelocity(velocityX, velocityY);
        sprite.setPosition(body.getWorldCenter().x - sprite.getWidth() / 2, body.getWorldCenter().y - sprite.getHeight() / 2);
        sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

    private Vector2 getInitialPosition(int angle) {
        int angleToUse;
        float adjacent1;

        if (angle < 60) {
            angleToUse = angle;
            adjacent1 = GameConfig.WORLD_HEIGHT / 2f;
        } else if (angle < 90) {
            angleToUse = 90 - angle;
            adjacent1 = GameConfig.WORLD_WIDTH / 2f;
        } else if (angle == 90) {
            return new Vector2(GameConfig.WORLD_WIDTH + 10, GameConfig.WORLD_HEIGHT / 2);
        } else if (angle < 120) {
            angleToUse = angle - 90;
            adjacent1 = GameConfig.WORLD_WIDTH / 2f;
        } else {
            angleToUse = 180 - angle;
            adjacent1 = GameConfig.WORLD_HEIGHT / 2f;
        }

        float opposite1 = adjacent1 * MathUtils.tan(angleToUse * MathUtils.degreesToRadians);
        float hypothenuse2 = GameConfig.WORLD_WIDTH / 2f - opposite1;
        float hypothenuse3 = hypothenuse2 * MathUtils.sin(angleToUse * MathUtils.degreesToRadians);
        float opposite4 = hypothenuse3 * MathUtils.sin(angleToUse * MathUtils.degreesToRadians);
        float adjacent3 = hypothenuse3 * MathUtils.cos(angleToUse * MathUtils.degreesToRadians);

        float initialX;
        float initialY;
        if (angle < 60) {
            initialX = GameConfig.WORLD_WIDTH / 2 + opposite1 + opposite4;
            initialY = -adjacent3;
        } else if (angle < 90) {
            initialX = GameConfig.WORLD_WIDTH + adjacent3;
            initialY = GameConfig.WORLD_HEIGHT / 2 - opposite1 - opposite4;
        } else if (angle < 120) {
            initialX = GameConfig.WORLD_WIDTH + adjacent3;
            initialY = GameConfig.WORLD_HEIGHT / 2 + opposite1 + opposite4;
        } else {
            initialX = GameConfig.WORLD_WIDTH / 2 + opposite1 + opposite4;
            initialY = GameConfig.WORLD_HEIGHT + adjacent3;
        }

        return new Vector2(initialX, initialY);
    }
}
