package com.space_shooter.game.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.player.PlayerShip;
import com.space_shooter.game.shared.entities.BattleShip;
import com.space_shooter.game.shared.utils.BodyFactory;

public class LaserBeam extends Projectile {
    private float width;
    private float length;
    private float angle;
    private float shipOffsetX;
    private float shipOffsetY;

    public LaserBeam(float width, int damage, BattleShip owner) {
        this.isDestroyedOnCollision = false;
        this.owner = owner;
        this.damage = damage;
        this.width = width;
        this.length = GameConfig.SCREEN_DIAGONAL;
        this.body = BodyFactory.createBody(owner.getWorld(), BodyType.DynamicBody, owner.getBody().getPosition().x + owner.getRadius(), owner.getBody().getPosition().y + owner.getRadius(), true, 0);
        this.body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, length / 2);
        BodyFactory.createFixture(body, shape, 0f, 0f, 0f, true);
    }
    
    public void setDirection(Vector2 direction) {
        angle = direction.angleRad();
        float angleForBody = angle + MathUtils.PI / 2;
    
        shipOffsetX = owner.getRadius() * MathUtils.cos(angle);
        shipOffsetY = owner.getRadius() * MathUtils.sin(angle);
        
        float beamOffsetX = length / 2 * MathUtils.cos(angle);
        float beamOffsetY = length / 2 * MathUtils.sin(angle);
        
        Vector2 beamStartPos = new Vector2(owner.getBody().getPosition())
                                   .add(shipOffsetX, shipOffsetY)
                                   .add(beamOffsetX, beamOffsetY);
    
        body.setTransform(beamStartPos, angleForBody);
    }
    @Override
    public void update(float delta) {
        if (owner instanceof PlayerShip && !((PlayerShip)owner).isShooting()) {
            markForRemoval();
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        Vector2 ownerPosition = owner.getBody().getPosition();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.identity();
        shapeRenderer.translate(ownerPosition.x + shipOffsetX, ownerPosition.y + shipOffsetY, 0);
        shapeRenderer.rotate(0, 0, 1, angle * MathUtils.radiansToDegrees);
        shapeRenderer.rect(0, -width / 2, length, width);
        shapeRenderer.identity();
        shapeRenderer.end();
    }
    
}
