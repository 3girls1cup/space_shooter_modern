package com.space_shooter.game.shared.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class DrawnEntity {
    protected boolean markedForRemoval = false;
    protected Body body;

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void markForRemoval() {
        markedForRemoval = true;
    }

    public void render(ShapeRenderer shapeRenderer) {
    }

    public Body getBody() {
        return body;
    }

    public void onCollision(DrawnEntity entity) {
    }

    public void update(float delta) {
    }
}
