package com.space_shooter.game.core;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.space_shooter.game.weapons.Weapon;

public class GameHUD {
    public Stage stage;
    private Integer health;
    private Map<String, Label> weaponAmmoLabels;
    private Map<String, Image> weaponIcons;
    private Label scoreLabel;
    private static Label.LabelStyle labelStyle;
    private static BitmapFont font;
    private Array<Image> heartIcons;

    public GameHUD() {
        this.health = GameContext.getInstance().getPlayer().getHealth();
        stage = new Stage(new ScreenViewport());
        heartIcons = new Array<Image>();
        font = new BitmapFont();
        labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        initializeScoreLabel(table);
        initializeHealthLabel(table);
        initializeWeaponIcons(table, GameContext.getInstance().getPlayer().getWeaponManager().getWeaponsMap());
        stage.addActor(table);
    }

    private void initializeHealthLabel(Table table) {
        for (int i = 0; i < 5; i++) {
            Image heartIcon = new Image(GameAssets.getInstance().getTextureInstance(GameAssets.HEART_ICON));
            heartIcon.setSize(1, 1);
            heartIcons.add(heartIcon);
            table.add(heartIcon).width(20).height(20).padTop(15).padRight(2);  
        }
        table.padRight(13);
    }

    private void initializeScoreLabel(Table table) {
        scoreLabel = new Label(String.format("Score: %03d", 0), labelStyle);
        table.add(scoreLabel).padTop(15).padLeft(15).expandX().left();
    }

    private void initializeWeaponIcons(Table table, Map<String, Weapon> playerWeapons) {
        weaponIcons = new HashMap<>();
        weaponAmmoLabels = new HashMap<>();
        table.row();
        table.add().expandX().expandY().bottom();
        table.row();
        for (Weapon weapon : playerWeapons.values()) {
            Image weaponIcon = new Image(GameAssets.getInstance().getTextureInstance(weapon.getIconPath()));
            weaponIcon.setSize(1, 1);
            weaponIcons.put(weapon.getName(), weaponIcon);

            table.add(weaponIcon).width(20).height(20).padBottom(15).padRight(2).right();
            addWeaponAmmoLabel(weapon, table);
        }
        notifyWeaponSwitched(GameContext.getInstance().getPlayer().getWeaponManager().getCurrentWeapon().getName());
    }



    private void addWeaponAmmoLabel(Weapon weapon, Table table) {
        int ammo = weapon.getAmmo();
        String weaponName = weapon.getName();
        Label ammoLabel = new Label(ammo == -1 ? "8" : String.format("%d", ammo), labelStyle);
        weaponAmmoLabels.put(weaponName, ammoLabel);

        if (ammo == -1) {
            Container<Label> labelContainer = new Container<>(ammoLabel);
            labelContainer.setTransform(true);
            labelContainer.setOrigin(Align.center);
            labelContainer.rotateBy(90);
            labelContainer.center();
            table.add(labelContainer).padBottom(15).padRight(15);
        } else {
            table.add(ammoLabel).padBottom(15).padRight(15);
        }
    }

    public void updateScore(int score) {
        scoreLabel.setText(String.format("Score: %03d", score));
    }

    public void updateHealth() {
        this.health = GameContext.getInstance().getPlayer().getHealth();
        
        for (int i = 0; i < heartIcons.size; i++) {
            heartIcons.get(i).setVisible(i < health);
        }
    }

    public void updateWeaponAmmo(String weaponName, int ammo) {
        weaponAmmoLabels.get(weaponName).setText(String.format("%d", ammo));
    }

    public void notifyWeaponSwitched(String weaponName) {
        for (Map.Entry<String, Image> entry : weaponIcons.entrySet()) {
            Image weaponIcon = entry.getValue();
            if (entry.getKey().equals(weaponName)) {
                weaponIcon.setColor(Color.WHITE);
                weaponIcon.setVisible(true);
            } else {
                weaponIcon.setColor(Color.GRAY);
                weaponIcon.setVisible(true);
            }
        }
    }    

    // On devrait utiliser un observer pour mettre à jour le HUD au lieu de le faire à chaque frame
    public void update() {
        updateHealth();
    }

    public void resize(int width, int height) {
        // stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}
