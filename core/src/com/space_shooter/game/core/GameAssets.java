package com.space_shooter.game.core;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;


public class GameAssets {
    public static String HEART_ICON = "heart_icon.png";
    public static String BACKGROUND = "background.jpg";
    public static String PLAYER_SHIP = "player_ship.png";
    public static String CROSSHAIR_ICON = "crosshair.png";
    public static String LASER_ICON = "weapons/Laser.png";
    public static String BASIC_ICON = "weapons/Basic.png";
    private static GameAssets instance;
    private HashMap<String, Class<?>> assets;
    private AssetManager assetManager;

    private GameAssets() {
        assetManager = new AssetManager();
        assets = new HashMap<String, Class<?>>();
        assets.put(CROSSHAIR_ICON, Texture.class);
        assets.put(HEART_ICON, Texture.class);
        assets.put(BACKGROUND, Texture.class);
        assets.put(LASER_ICON, Texture.class);
        assets.put(BASIC_ICON, Texture.class);
        assets.put(PLAYER_SHIP, Texture.class);

        loadAssets();
        assetManager.finishLoading();

        Pixmap pixmap = new Pixmap(Gdx.files.internal(CROSSHAIR_ICON));
        Cursor customCursor = Gdx.graphics.newCursor(pixmap, pixmap.getWidth() / 2, pixmap.getHeight() / 2);
        Gdx.graphics.setCursor(customCursor);
        pixmap.dispose();
    }

    private void loadAssets() {
        for (String asset : assets.keySet()) {
            assetManager.load(getAssetPath(asset), assets.get(asset));
        }
    }

    private String getAssetPath(String asset) {
        return "./assets/" + asset;
    }

    public Texture getTextureInstance(String asset) {
        if (assets.get(asset) == Texture.class) {
            return assetManager.get(getAssetPath(asset), Texture.class);
        }
        return null;
    }

    public Sound getSoundInstance(String asset) {
        if (assets.get(asset) == Sound.class) {
            return assetManager.get(getAssetPath(asset), Sound.class);
        }
        return null;
    }

    
    public static GameAssets getInstance() {
        if (instance == null) {
            instance = new GameAssets();
        }
        return instance;
    }
}
