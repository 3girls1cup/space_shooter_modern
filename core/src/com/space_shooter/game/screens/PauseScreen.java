package com.space_shooter.game.screens;

import java.util.function.Function;

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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.space_shooter.game.core.AudioManager;
import com.space_shooter.game.core.GameAssets;
import com.space_shooter.game.core.SpaceShooter;
import com.space_shooter.game.shared.utils.VisualDebugger;

public class PauseScreen implements Screen {
    private Stage stage = new Stage(new ScreenViewport());
    private SpaceShooter game;
    private GameScreen gameScreen;
    private Skin skin = GameAssets.getInstance().getSkinInstance(GameAssets.SKIN_SCI_FI);
    private boolean isMusicEnabled = true;
    private boolean isSoundEnabled = true;
    private boolean isDebugBodyEnabled = VisualDebugger.getInstance().isDebuggingBodyOutline();
    private boolean isDebugDistanceShooterEnabled = VisualDebugger.getInstance().isDebuggingDistanceShooter();

    public class ButtonData {
        public float originalWidth;
        public float originalHeight;

        public ButtonData(float width, float height) {
            this.originalWidth = width;
            this.originalHeight = height;
        }
    }

    public PauseScreen(SpaceShooter game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        Gdx.input.setInputProcessor(stage);
    }

    class Settings {
        String name;
        Function<Boolean, Void> action;
        boolean state;

        public Settings(String name, boolean state, Function<Boolean, Void> action) {
            this.name = name;
            this.action = action;
            this.state = state;
        }
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
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private Table createInstructionsTable() {
        Table table = new Table();
        String[] infos = {
                "Esc - Pause",
                "A - Aller A gauche",
                "D - Aller A droite",
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

        table.add(createButton("Resume", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(gameScreen);
            }
        })).size(200, 80).pad(10).row();
        table.add(createButton("Quit", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        })).size(200, 80).pad(10).row();
        table.add(createButton("Reset", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        })).size(200, 80).pad(10).row();
        table.add(createButton("Settings", new ClickListener() {
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
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private TextButton createButton(String text, ClickListener listener) {
        TextButton button = new TextButton(text, skin);
        button.setUserObject(new ButtonData(220, 80));
        button.addListener(listener);
        return button;
    }

    private Table createSettingsTable() {
        Table table = new Table(skin);

        Array<Settings> settings = new Array<>();
        settings.add(new Settings("Music", isMusicEnabled, (Boolean state) -> {
            isMusicEnabled = state;
            AudioManager.getInstance().setVolumeSoundtrack(isMusicEnabled ? 1 : 0);
            return null;
        }));
        settings.add(new Settings("Sound", isSoundEnabled, (Boolean state) -> {
            isSoundEnabled = state;
            AudioManager.getInstance().setVolumeSoundEffects(isSoundEnabled ? 1 : 0);
            return null;
        }));
        settings.add(new Settings("Debug Body", isDebugBodyEnabled, (Boolean state) -> {
            isDebugBodyEnabled = state;
            VisualDebugger.getInstance().setDebugBodyOutline(isDebugBodyEnabled);
            return null;
        }));
        settings.add(new Settings("Debug Distance Shooter", isDebugDistanceShooterEnabled, (Boolean state) -> {
            isDebugDistanceShooterEnabled = state;
            VisualDebugger.getInstance().setDebugDistanceShooter(isDebugDistanceShooterEnabled);
            return null;
        }));

        for (Settings setting : settings) {
            table.add(createButton(setting)).pad(10).row();
        }

        table.add(createButton("Back", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                show();
            }
        })).pad(10).row();
        return table;
    }

    private TextButton createButton(Settings setting) {
        TextButton button = new TextButton(setting.name + ": " + (setting.state ? "On" : "Off"), skin);
        button.setUserObject(new ButtonData(500, 80));
        button.getLabel().setFontScale(0.5f * Gdx.graphics.getWidth() / 800);
        button.setColor(setting.state ? Color.GREEN : Color.RED);
        button.getLabelCell().pad(10);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setting.state = !setting.state;
                button.setText(setting.name + ": " + (setting.state ? "On" : "Off"));
                button.setColor(setting.state ? Color.GREEN : Color.RED);
                setting.action.apply(setting.state);
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
        float scale = Math.min(width, height) / 640f;
        Array<Actor> actors = stage.getActors();
        System.out.println(scale);
        for (Actor actor : actors) {
            if (actor instanceof Table) {
                Table table = (Table) actor;
                resizeAllCells(table, scale);
            }
        }
    }

    private void resizeAllCells(Table table, float scale) {
        for (Cell<?> cell : table.getCells()) {
            if (cell.getActor() instanceof TextButton) {
                TextButton button = (TextButton) cell.getActor();
                ButtonData data = (ButtonData) button.getUserObject();
                cell.size(data.originalWidth * scale, data.originalHeight * scale);
                button.getLabel().setFontScale(1f * scale);
            } else if (cell.getActor() instanceof Table) {
                resizeAllCells((Table) cell.getActor(), scale);
            } else if (cell.getActor() instanceof Label) {
                Label label = (Label) cell.getActor();
                label.setFontScale(1f * scale);
            }
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

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