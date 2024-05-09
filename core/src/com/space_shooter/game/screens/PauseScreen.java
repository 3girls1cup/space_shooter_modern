package com.space_shooter.game.screens;

import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.space_shooter.game.core.GameAssets;
import com.space_shooter.game.core.SpaceShooter;
import com.space_shooter.game.shared.utils.VisualDebugger;

public class PauseScreen implements Screen {
    private Stage stage;
    private SpaceShooter game;
    private GameScreen gameScreen;
    private Skin skin;
    private boolean isMusicEnabled = true;
    private boolean isSoundEnabled = true;
    private boolean isDebugBodyEnabled;
    private boolean isDebugDistanceShooterEnabled;

    public PauseScreen(SpaceShooter game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.stage = new Stage(new ScreenViewport());
        this.skin = GameAssets.getInstance().getSkinInstance(GameAssets.SKIN_SCI_FI);
        isDebugBodyEnabled = VisualDebugger.getInstance().isDebuggingBodyOutline();
        isDebugDistanceShooterEnabled = VisualDebugger.getInstance().isDebuggingDistanceShooter();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        stage.clear();
        createMainTable();
    }

    private void createMainTable() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.add(createInstructionsTable()).expand().fill();
        mainTable.add(createButtonsTable()).expand().fill();
        stage.addActor(mainTable);
    }

    private Table createInstructionsTable() {
        Table table = new Table();
        String[] infos = {
            "Esc - Pause",
            "A - Aller à gauche",
            "D - Aller à droite",
            "W - Aller en haut",
            "S - Aller en bas",
            "E - Changer d'arme",
            "Clic gauche - Tirer",
            "Clic droit - Se teleporter"
        };
        for (String info : infos) {
            addInstruction(table, info, Color.WHITE);
        }
        return table;
    }

    private void addInstruction(Table table, String instruction, Color color) {
        String[] parts = instruction.split(" - ", 2);
        if (parts.length == 2) {
            Label label = new Label(parts[0], skin);
            label.setColor(Color.WHITE);
            table.add(label).center().pad(10);
            label = new Label(parts[1], skin);
            label.setColor(Color.RED);
            table.add(label).center().pad(10).row();
        }
        }

    private Table createButtonsTable() {
        Table table = new Table();

        table.add(createButton("Resume", new ClickListener () {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(gameScreen);
            }
        })).size(200, 80).pad(10).row();
        table.add(createButton("Quit",  new ClickListener () {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        })).size(200, 80).pad(10).row();
        table.add(createButton("Reset", new ClickListener () {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        })).size(200, 80).pad(10).row();
        table.add(createButton("Settings", new ClickListener () {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSettingsTable();
            }
        })).size(200, 80).pad(10).row();
        return table;
    }


    private void showSettingsTable() {
        stage.clear();
        Table settingsTable = new Table();
        settingsTable.setFillParent(true);
        settingsTable.add(createSettingsTable()).expand().fill();
        stage.addActor(settingsTable);
    }


    private TextButton createButton(String text, ClickListener listener) {
        TextButton button = new TextButton(text, skin);
        button.addListener(listener);
        return button;
    }

    private Table createSettingsTable() {
        Table table = new Table(skin);
        HashMap<String, Boolean> settings = new HashMap<>();
        settings.put("Music", isMusicEnabled);
        settings.put("Sound", isSoundEnabled);
        settings.put("Debug Body", isDebugBodyEnabled);
        settings.put("Debug Distance Shooter", isDebugDistanceShooterEnabled);

        String[] orderedSettings = {"Music", "Sound", "Debug Body", "Debug Distance Shooter"};
        for (String setting : orderedSettings) {
            table.add(createButton(setting, settings.get(setting))).pad(10).row();
        }
        
        table.add(createButton("Back", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                show();
            }
        })).pad(10).row();
        return table;
        }

        private TextButton createButton(String label, boolean initialState) {
        TextButton button = new TextButton(label + ": " + (initialState ? "On" : "Off"), skin);
        button.getLabel().setFontScale(0.5f * Gdx.graphics.getWidth() / 800); // Adjust font scale
        button.setColor(!initialState ? Color.RED : Color.GREEN);
        button.getLabelCell().pad(10);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            boolean currentState = button.getText().toString().endsWith("On");
            button.setText(label + ": " + (!currentState ? "On" : "Off"));
            button.setColor(currentState ? Color.RED : Color.GREEN);
            }
        });
        return button;
        }

        private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(gameScreen);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        handleInput(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        float scale = Math.min(width, height) / 800f;
        stage.getActors().forEach(actor -> {
            if (actor instanceof TextButton) {
                ((TextButton) actor).getLabel().setFontScale(scale);
            } else if (actor instanceof Label) {
                ((Label) actor).setFontScale(scale);
            }
        });
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
