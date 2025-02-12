package com.space_shooter.game.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.space_shooter.game.screens.MainMenuScreen;

public class SpaceShooter extends Game {
	public SpriteBatch batch;

	@Override
	public void create() {
		GameAssets.getInstance();
		AudioManager.getInstance().playBackgroundMusic();
		batch = new SpriteBatch();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
