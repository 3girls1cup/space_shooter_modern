package com.space_shooter.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.space_shooter.game.core.SpaceShooter;


public class MainMenuScreen implements Screen {
    private SpaceShooter game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private BitmapFont font;

    public MainMenuScreen(SpaceShooter game) {
        this.game = game;
        this.batch = game.batch;
        this.backgroundTexture = new Texture("background.jpg");
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        this.font.getData().setScale(2);
    }

    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); 
        font.draw(batch, "Space Shooter", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2); 
        font.draw(batch, "Press any key to start the game", Gdx.graphics.getWidth() / 2 - 140, Gdx.graphics.getHeight() / 2 - 50); 
        batch.end();

        if (Gdx.input.isKeyPressed(-1)) {
            game.setScreen(new GameScreen(game)); 
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
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
        backgroundTexture.dispose(); 
        font.dispose(); 
    }
}
