package com.space_shooter.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.core.SpaceShooter;
import com.space_shooter.game.shared.entities.DrawnEntity;
import com.space_shooter.game.shared.utils.GameContactListener;

public class GameScreen implements Screen {
    private OrthographicCamera camera;
    private Viewport viewport;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Array<Body> bodies;

    public GameScreen(SpaceShooter game) {
        this.batch = game.batch;
        this.backgroundTexture = new Texture("background.jpg");
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, 0), true);

        GameContext.getInstance().initialize(this);
        shapeRenderer = new ShapeRenderer();
        this.bodies = new Array<Body>();

        world.setContactListener(new GameContactListener());
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        GameContext.getInstance().getEnnemySpawner().update(delta);
        GameContext.getInstance().getWallSpawner().update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        
        camera.update();
        viewport.apply();

        
        batch.setProjectionMatrix(camera.combined); 
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight()); 
    
        shapeRenderer.setProjectionMatrix(camera.combined); 
        

        world.getBodies(this.bodies);

        for (Body body : this.bodies) {
            DrawnEntity entity = (DrawnEntity) body.getUserData();
            if (entity.isMarkedForRemoval()) {
                world.destroyBody(body);
                continue;
            }

            if (entity != null) {
                entity.update(delta);
                entity.render(batch);
                drawBodyOutline(body, delta);
            }
        }
        
        batch.end();


        world.step(1/60f, 6, 2);

        GameContext.getInstance().getGameHUD().stage.draw();
    }

    private void drawBodyOutline(Body body, float delta) {
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
        
        Sprite sprite = entity.getSprite();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Vector2 position = body.getWorldPoint(new Vector2(sprite.getWidth() / 2,sprite.getHeight()));
        shapeRenderer.setColor(Color.YELLOW);
        float screenX = position.x;
        float screenY = position.y;
        shapeRenderer.circle(screenX, screenY, 0.2f);
        shapeRenderer.end();
        batch.begin();
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(GameConfig.WORLD_WIDTH / 2, GameConfig.WORLD_HEIGHT / 2, 0);
    }
    
    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        shapeRenderer.dispose();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public World getWorld() {
        return world;
    }

    public Array<Body> getBodies() {
        return bodies;
    }
}
