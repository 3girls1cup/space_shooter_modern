package com.space_shooter.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.space_shooter.game.core.GameAssets;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.core.SpaceShooter;

public class EndGameScreen implements Screen {
    private Stage stage = new Stage(new ScreenViewport());
    private SpaceShooter game;
    private Skin skin = GameAssets.getInstance().getSkinInstance(GameAssets.SKIN_SCI_FI);
    private Color textColor = Color.WHITE;

    public EndGameScreen(SpaceShooter game) {
        this.game = game;
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        stage.clear();
        createTable();
    }

    private void createTable() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label gameOverLabel = new Label("Game Over", skin);
        gameOverLabel.setColor(textColor);
        table.add(gameOverLabel).expandX().padTop(10f).row();

        Label scoreLabel = new Label("Score: " + GameContext.getInstance().getGamePlayManager().getScore(), skin);
        scoreLabel.setColor(textColor);
        table.row();
        table.add(scoreLabel).expandX().padTop(10f);

        TextButton playAgainButton = new TextButton("Play Again", skin);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        table.row();
        table.add(playAgainButton).expandX().padTop(10f).row();

        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table.row();
        table.add(mainMenuButton).expandX().padTop(10f).row();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
