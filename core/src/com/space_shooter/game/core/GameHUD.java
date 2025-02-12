package com.space_shooter.game.core;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.space_shooter.game.weapons.Weapon;

public class GameHUD {
    private static final float PROGRESS_BAR_WIDTH_FACTOR = 0.3f;
    public Stage stage;
    private Integer health;
    private Map<String, Label> weaponAmmoLabels;
    private Map<String, Cell<Image>> weaponIcons;
    private Label scoreLabel;
    private static Label.LabelStyle labelStyle;
    private static BitmapFont font;
    private ProgressBar healthBar;
    private Cell<ProgressBar> healthBarCell;
    private Table table;

    public GameHUD() {
        this.health = GameContext.getInstance().getPlayer().getHealth();
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        labelStyle = new Label.LabelStyle(font, Color.WHITE);
        table = new Table();
        table.top();
        table.setFillParent(true);
        table.row();
        initializeHealthLabel(table);
        table.row();
        initializeScoreLabel(table);
        initializeWeaponIcons(table, GameContext.getInstance().getPlayer().getWeaponManager().getWeaponsMap());
        stage.addActor(table);
    }

    private void initializeHealthLabel(Table table) {
        healthBar = new ProgressBar(0, health, 1, false,
                GameAssets.getInstance().getSkinInstance(GameAssets.SKIN_SCI_FI));
        healthBar.setAnimateDuration(0.01f);
        healthBar.setValue(health);

        healthBarCell = table.add(healthBar).padTop(30).padRight(15).right();
    }

    private void initializeScoreLabel(Table table) {
        scoreLabel = new Label(String.format("Score: %03d", 0), labelStyle);
        table.add(scoreLabel).padTop(15).padRight(15).right();
    }

    private void initializeWeaponIcons(Table table, Map<String, Weapon> playerWeapons) {
        weaponIcons = new HashMap<>();
        weaponAmmoLabels = new HashMap<>();
        table.row();
        table.add().expandX().expandY().bottom();
        table.row();
        Table centerTable = new Table();
        for (Weapon weapon : playerWeapons.values()) {
            Image weaponIcon = new Image(GameAssets.getInstance().getTextureInstance(weapon.getIconPath()));
            weaponIcon.setSize(1, 1);
            Cell<Image> cell = centerTable.add(weaponIcon).size(40, 40).padBottom(20);
            weaponIcons.put(weapon.getName(), cell);
            addWeaponAmmoLabel(weapon, centerTable);
        }
        centerTable.padRight(-30);
        table.add(centerTable).expandX().center();
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
            table.add(labelContainer).padBottom(30).padRight(15);
        } else {
            table.add(ammoLabel).padBottom(20).padLeft(-20).padRight(15);
        }
    }

    public void updateScore(int score) {
        scoreLabel.setText(String.format("Score: %03d", score));
    }

    public void updateWeaponAmmo(String weaponName, int ammo) {
        weaponAmmoLabels.get(weaponName).setText(String.format("%d", ammo));
    }

    public void notifyWeaponSwitched(String weaponName) {
        for (Map.Entry<String, Cell<Image>> entry : weaponIcons.entrySet()) {
            Image weaponIcon = (Image) entry.getValue().getActor();
            if (entry.getKey().equals(weaponName)) {
                weaponIcon.setColor(Color.WHITE);
                weaponIcon.setVisible(true);
            } else {
                weaponIcon.setColor(Color.GRAY);
                weaponIcon.setVisible(true);
            }
        }
    }

    public void updateHealthBar(int health) {
        healthBar.setValue(health);
    }

    private void resizeAllHUDElements() {
        float maxScreenWidth = 1920;
        float minScreenWidth = 640;
        float widthRatio = (Gdx.graphics.getWidth() - minScreenWidth) / (maxScreenWidth - minScreenWidth);
        widthRatio = Math.max(0, Math.min(widthRatio, 1));

        resizeHealthBar(widthRatio);
        resizeScoreSize(widthRatio);
        resizeWeaponAmmoSize(widthRatio);
        resizeWeaponIconSize(widthRatio);
    }

    private void resizeHealthBar(float widthRatio) {
        float width = Gdx.graphics.getWidth() * PROGRESS_BAR_WIDTH_FACTOR;
        float healthBarHeight = interpolateSize(36, 72, widthRatio);
        float knobHeight = interpolateSize(12.5f, 50, widthRatio);

        ProgressBar.ProgressBarStyle style = healthBar.getStyle();
        style.background.setMinHeight(healthBarHeight);
        style.knobBefore.setMinHeight(knobHeight);
        style.knob.setMinHeight(knobHeight);
        healthBar.setStyle(style);

        if (healthBarCell != null) {
            healthBarCell.size(width, healthBarHeight);
            table.invalidateHierarchy();
        }
    }

    private void resizeScoreSize(float widthRatio) {
        float scoreFontSize = interpolateSize(12, 24, widthRatio);
        scoreLabel.setStyle(new Label.LabelStyle(font, Color.WHITE));
        scoreLabel.setFontScale(scoreFontSize / 12);
    }

    private void resizeWeaponAmmoSize(float widthRatio) {
        float ammoFontSize = interpolateSize(12, 24, widthRatio);
        for (Label ammoLabel : weaponAmmoLabels.values()) {
            ammoLabel.setStyle(new Label.LabelStyle(font, Color.WHITE));
            ammoLabel.setFontScale(ammoFontSize / 12);
        }
    }

    private void resizeWeaponIconSize(float widthRatio) {
        float iconSize = interpolateSize(40, 80, widthRatio);
        for (Cell<Image> weaponIcon : weaponIcons.values()) {
            weaponIcon.size(iconSize, iconSize);
        }
    }

    private float interpolateSize(float minSize, float maxSize, float ratio) {
        return minSize + (maxSize - minSize) * ratio;
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        resizeAllHUDElements();
    }

    public void dispose() {
        stage.dispose();
    }
}
