package com.space_shooter.game.shared.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.shared.entities.DrawnEntity;

public class VisualDebugger {
    private static VisualDebugger instance;
    private boolean debugBodyOutline = false;
    private boolean debugDistanceShooter = false;

    private VisualDebugger() {
    }

    public static VisualDebugger getInstance() {
        if (instance == null) {
            instance = new VisualDebugger();
        }
        return instance;
    }

    public void setDebugBodyOutline(boolean debugBodyOutline) {
        this.debugBodyOutline = debugBodyOutline;
    }

    public void setDebugDistanceShooter(boolean debugDistanceShooter) {
        this.debugDistanceShooter = debugDistanceShooter;
    }

    public boolean isDebuggingBodyOutline() {
        return debugBodyOutline;
    }

    public boolean isDebuggingDistanceShooter() {
        return debugDistanceShooter;
    }

    public void drawDistanceShooterDebug(SpriteBatch batch, Vector2 bodyPosition, Vector2 targetPosition, Vector2 playerPosition, Vector2 snapshotPos, Vector2 maxAngle, Vector2 minAngle, float SAFE_DISTANCE) {
        
        if (!debugDistanceShooter) return;

        ShapeRenderer shapeRenderer = GameContext.getInstance().getShapeRenderer();
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        drawLine(shapeRenderer, bodyPosition, targetPosition, Color.RED);
        drawLine(shapeRenderer,snapshotPos, maxAngle, Color.GREEN);
        drawLine(shapeRenderer, snapshotPos, minAngle, Color.GREEN);
        drawCircle(shapeRenderer, playerPosition, SAFE_DISTANCE, Color.YELLOW, ShapeRenderer.ShapeType.Line);
        batch.begin();
    }

    public void drawBodyOutline(ShapeRenderer shapeRenderer, SpriteBatch batch, Body body, float delta) {

        if (!debugBodyOutline) return;

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        for (Fixture fixture : body.getFixtureList()) {
            if (fixture.getShape().getType() == Shape.Type.Polygon) {
                PolygonShape polygon = (PolygonShape) fixture.getShape();
                float[] vertices = new float[polygon.getVertexCount() * 2];
                for (int i = 0; i < polygon.getVertexCount(); i++) {
                    Vector2 vertex = new Vector2();
                    polygon.getVertex(i, vertex);
                    // Transform local coordinates to world coordinates
                    vertex = body.getWorldPoint(vertex);
                    vertices[i * 2] = vertex.x;
                    vertices[i * 2 + 1] = vertex.y;
                }
                shapeRenderer.polygon(vertices);
            }
        }
        shapeRenderer.end();

        DrawnEntity entity = (DrawnEntity) body.getUserData();
        if (entity == null || entity.getSprite() == null) {
            batch.begin();
            return;
        }
        
        drawCircle(shapeRenderer, body.getWorldCenter(), 0.4f, Color.YELLOW, ShapeRenderer.ShapeType.Filled);
        
        batch.begin();
    }

    private static void drawLine(ShapeRenderer shapeRenderer, Vector2 fromPosition, Vector2 toPosition, Color color) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color);
        shapeRenderer.line(fromPosition, toPosition);
        shapeRenderer.end();
    }

    private static void drawCircle(ShapeRenderer shapeRenderer, Vector2 position, float radius, Color color, ShapeRenderer.ShapeType shapeType) {
        shapeRenderer.begin(shapeType);
        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x, position.y, radius);
        shapeRenderer.end();
    }
}
