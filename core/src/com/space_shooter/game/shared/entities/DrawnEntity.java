package com.space_shooter.game.shared.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class DrawnEntity {
    protected boolean markedForRemoval = false;
    protected Body body;
    protected Sprite sprite;

    public Sprite getSprite() {
        if (sprite == null) {
            return null;
        }
        return new Sprite(sprite);
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void markForRemoval() {
        markedForRemoval = true;
    }

    public void render() {
    }

    public void render(ShapeRenderer shapeRenderer) {
    }

    public void render(SpriteBatch spriteBatch) {
    }

    public Body getBody() {
        return body;
    }

    public void onCollision(DrawnEntity entity) {
    }

    public void update(float delta) {
    }
}
