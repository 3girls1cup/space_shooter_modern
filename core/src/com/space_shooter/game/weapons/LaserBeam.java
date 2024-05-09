package com.space_shooter.game.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.space_shooter.game.core.GameAssets;
import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.player.PlayerShip;
import com.space_shooter.game.shared.entities.BattleShip;
import com.space_shooter.game.shared.utils.BodyFactory;

public class LaserBeam extends Projectile {
    private static final int FRAME_COLS = 4, FRAME_ROWS = 3;
    private float width;
    private float length;
    private float angle;
    private Animation<TextureRegion> animation;
    private float animationTimer = 0f;
    private TextureRegion currentFrame;
    private int numberOfFrames = FRAME_COLS * FRAME_ROWS;
    private float frameDuration = 1f / numberOfFrames;

    public LaserBeam(float width, int damage, BattleShip owner) {
        this.isDestroyedOnCollision = false;
        this.owner = owner;
        this.damage = damage;
        this.width = width;
        this.length = GameConfig.SCREEN_DIAGONAL;
        this.body = BodyFactory.getInstance().createBody(owner.getWorld(), BodyType.DynamicBody,
                owner.getBody().getWorldCenter().x,
                owner.getBody().getWorldCenter().y, true, 0);
        this.body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, length / 2);
        BodyFactory.getInstance().createFixture(body, shape, 0f, 0f, 0f, true);

        texture = GameAssets.getInstance().getTextureInstance(GameAssets.LASER_BEAM);
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
        sprite.setSize(firstFrame.getRegionWidth() / GameConfig.PIXELS_PER_METER, length);
        sprite.setOrigin(sprite.getWidth() / 2, 0);

    }

    public void setDirection(Vector2 direction) {
        angle = direction.angleRad();
        float angleForBody = angle + MathUtils.PI / 2;
        Vector2 ownerPosition = owner.getBody().getWorldCenter();

        float beamOffsetX = length / 2 * MathUtils.cos(angle);
        float beamOffsetY = length / 2 * MathUtils.sin(angle);

        Vector2 beamStartPos = new Vector2(ownerPosition)
                .add(beamOffsetX, beamOffsetY);   
        
        sprite.setPosition(ownerPosition.x - sprite.getWidth() / 2, ownerPosition.y);
        sprite.setRotation(angle * MathUtils.radiansToDegrees - 90);
        body.setTransform(beamStartPos, angleForBody);
    }

    @Override
    public void update(float delta) {
        if (owner instanceof PlayerShip && !((PlayerShip) owner).isShooting()) {
            markForRemoval();
        }
        animationTimer += delta;
        currentFrame = animation.getKeyFrame(animationTimer, true);
        sprite.setRegion(currentFrame);
    }


    @Override
    public void render(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

}