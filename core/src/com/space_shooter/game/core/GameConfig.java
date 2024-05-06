package com.space_shooter.game.core;

public class GameConfig {
    public static final int WORLD_WIDTH = 160;
    public static final int WORLD_HEIGHT = 90;
    public static final float SCREEN_DIAGONAL = (float)Math.sqrt(Math.pow(WORLD_WIDTH, 2) + Math.pow(WORLD_HEIGHT, 2));
    public static final int MAX_ENEMIES = 10;
    // Paramétres de difficulté
    public static final float DIFFICULTY_FACTOR = 0.0002f;
}