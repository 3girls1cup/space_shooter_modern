package com.space_shooter.game.walls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.shared.entities.DrawnEntity;
import com.space_shooter.game.shared.utils.BodyFactory;

public class Wall extends DrawnEntity {
    private float width;
    private float height;
    private Color color;
    private float speed;

    public Wall(float height, int angle, float speed) {
        this.width = GameConfig.SCREEN_DIAGONAL;
        this.speed = speed;
        this.height = height;
        this.color = getRandomColor();
        this.body = BodyFactory.createBody(GameContext.getInstance().getWorld(), BodyType.KinematicBody, getInitialPosition(angle).x, getInitialPosition(angle).y, false, angle);
        this.body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        BodyFactory.createFixture(body, shape, 1f, 0f, 1f, false);
    }

    @Override
    public void update(float delta) {
        if (body.getPosition().x < 0) {
            markForRemoval();
        }

        float velocityX = speed * MathUtils.cos(body.getAngle() + MathUtils.PI / 2);
        float velocityY = speed * MathUtils.sin(body.getAngle() + MathUtils.PI / 2);
        body.setLinearVelocity(velocityX, velocityY);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.identity();
        shapeRenderer.translate(body.getPosition().x, body.getPosition().y, 0);
        shapeRenderer.rotate(0, 0, 1, body.getAngle() * MathUtils.radiansToDegrees);
        shapeRenderer.rect(-width / 2, -height / 2, width, height);
        shapeRenderer.identity();
        shapeRenderer.end();
    }

    private Color getRandomColor() {
        return new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
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
