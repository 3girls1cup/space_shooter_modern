package com.space_shooter.game.shared.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {
    private int frame_cols;
    private int frame_rows;
    private Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)
	private Texture sheet;
    private Sprite sprite;

    public Animator(Texture sheet, int frame_cols, int frame_rows, float frameDuration, Sprite sprite) {
        this.sheet = sheet;
        this.frame_cols = frame_cols;
        this.frame_rows = frame_rows;
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / frame_cols, sheet.getHeight() / frame_rows);
        TextureRegion[] frames = new TextureRegion[frame_cols * frame_rows];
        int index = 0;
        for (int i = 0; i < frame_rows; i++) {
            for (int j = 0; j < frame_cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(frameDuration, frames);
    }
}
