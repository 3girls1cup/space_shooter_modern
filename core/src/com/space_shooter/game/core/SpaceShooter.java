package com.space_shooter.game.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.space_shooter.game.screens.GameScreen;

public class SpaceShooter extends Game {
	public SpriteBatch batch;
	
	@Override
	public void create () {
		GameAssets.getInstance();
		batch = new SpriteBatch();
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
