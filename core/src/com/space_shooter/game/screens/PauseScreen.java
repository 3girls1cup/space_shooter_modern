package com.space_shooter.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.space_shooter.game.core.SpaceShooter;

public class PauseScreen implements Screen {
    private Stage stage;
    private SpaceShooter game; // Assurez-vous que SpaceShooter est votre classe de jeu principale
    private GameScreen gameScreen; // Assurez-vous que GameScreen est importé
    private Skin skin; // Assurez-vous que skin est initialisé quelque part

    public PauseScreen(SpaceShooter game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("./assets/skin/star-soldier-ui.json")); // Mettez le chemin correct vers votre fichier skin
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Button resumeButton = new TextButton("Resume", skin);
        Button quitButton = new TextButton("Quit", skin);
        Button resetButton = new TextButton("Reset", skin);
        Button infoButton = new TextButton("Info", skin);

        // Ajout des écouteurs pour les boutons
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(gameScreen); // Réutiliser l'écran de jeu existant
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game)); // Recréer un nouvel écran de jeu
            }
        });

        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Afficher les informations
            }
        });

        // Ajout des boutons à la table avec la taille spécifiée
        table.add(resumeButton).size(200, 80).pad(10).row();
        table.add(quitButton).size(200, 80).pad(10).row();
        table.add(resetButton).size(200, 80).pad(10).row();
        table.add(infoButton).size(200, 80).pad(10).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Effacer l'écran
        stage.act(delta); // Mettre à jour le stage
        stage.draw(); // Dessiner le stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose(); // Libérer les ressources du stage
        skin.dispose(); // Libérer les ressources du skin
    }
}
