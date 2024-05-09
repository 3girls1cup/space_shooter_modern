package com.space_shooter.game.core;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.audio.Sound;


public class GameAssets {
    public static final String HEART_ICON = "heart_icon.png";
    public static final String BACKGROUND = "background.jpg";
    public static final String PLAYER_SHIP = "player_ship.png";
    public static final String ANIMATED_PLAYER_SHIP = "player_ship_animated.png";
    public static final String KAMIKAZE = "kamikaze.png";
    public static final String DISTANCE_SHOOTER = "distance_shooter.png";
    public static final String CROSSHAIR_ICON = "crosshair.png";
    public static final String LASER_ICON = "weapons/Laser.png";
    public static final String BASIC_ICON = "weapons/Basic.png";
    public static final String BASIC_WEAPON_PLAYER = "weapons/BasicPlayer.png";
    public static final String LASER_BEAM = "weapons/texture_laser.png";
    public static final String WALL = "wall/texture_wall.png";
    public static final String SKIN_SCI_FI = "skin/star-soldier-ui.json";
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
        assets.put(KAMIKAZE, Texture.class);
        assets.put(DISTANCE_SHOOTER, Texture.class);
        assets.put(BASIC_WEAPON_PLAYER, Texture.class);
        assets.put(LASER_BEAM, Texture.class);
        assets.put(WALL, Texture.class);
        assets.put(SKIN_SCI_FI, Skin.class);

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

    public Skin getSkinInstance(String asset) {
        if (assets.get(asset) == Skin.class) {
            return assetManager.get(getAssetPath(asset), Skin.class);
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
