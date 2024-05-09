package com.space_shooter.game.core;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.space_shooter.game.ennemies.EnnemySpawner;
import com.space_shooter.game.player.PlayerShip;
import com.space_shooter.game.screens.GameScreen;
import com.space_shooter.game.shared.managers.GamePlayManager;
import com.space_shooter.game.walls.WallSpawner;

public class GameContext {
    private static GameContext instance;
    
    private PlayerShip playerShip;
    private EnnemySpawner ennemySpawner;
    private WallSpawner wallSpawner;
    private GamePlayManager gamePlayManager;
    private GameScreen gameScreen;
    private GameHUD gameHUD;
    
    private GameContext() {}
    
    public static GameContext getInstance() {
        if (instance == null) {
            instance = new GameContext();
        }
        return instance;
    }
    
    public void initialize(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        playerShip = new PlayerShip();
        ennemySpawner = new EnnemySpawner();
        wallSpawner = new WallSpawner();
        gamePlayManager = new GamePlayManager();
        gameHUD = new GameHUD();
    }
    
    public World getWorld() {
        return gameScreen.getWorld();
    }

    public PlayerShip getPlayer() {
        return playerShip;
    }

    public EnnemySpawner getEnnemySpawner() {
        return ennemySpawner;
    }

    public WallSpawner getWallSpawner() {
        return wallSpawner;
    }

    public GamePlayManager getGamePlayManager() {
        return gamePlayManager;
    }

    public GameHUD getGameHUD() {
        return gameHUD;
    }

    public OrthographicCamera getCamera() {
        return gameScreen.getCamera();
    }

    public Array<Body> getBodies() {
        return gameScreen.getBodies();
    }

    public Viewport getViewport() {
        return gameScreen.getViewport();
    }

    public void dispose() {
        gameHUD.dispose();
    }

    public ShapeRenderer getShapeRenderer() {
        return gameScreen.getShapeRenderer();
    }
}