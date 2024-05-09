package com.space_shooter.game.shared.managers;

import java.util.HashMap;

import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.ennemies.EnnemyShip;

public class GamePlayManager {
    private int score;
    private int enemiesKilled;
    private int enemiesSpawned;
    private HashMap<String, Integer> enemies;
    public GamePlayManager() {
        this.score = 0;
        this.enemiesKilled = 0;
        this.enemiesSpawned = 0;
        this.enemies = new HashMap<String, Integer>();
    }

    public void notifyEnnemySpawned(EnnemyShip enemyType) {
        if (enemies.containsKey(enemyType.getClass().getSimpleName())) {
            enemies.put(enemyType.getClass().getSimpleName(), enemies.get(enemyType.getClass().getSimpleName()) + 1);
        } else {
            enemies.put(enemyType.getClass().getSimpleName(), 1);
        }
        enemiesSpawned++;
    }

    public void notifyEnnemyKilled(EnnemyShip enemyType) {
        if (enemies.containsKey(enemyType.getClass().getSimpleName())) {
            enemies.put(enemyType.getClass().getSimpleName(), enemies.get(enemyType.getClass().getSimpleName()) - 1);
        }
        enemiesKilled++;
        DifficultyManager.getInstance().update(enemyType.getScoreValue());
        addScore(enemyType.getScoreValue());
        GameContext.getInstance().getGameHUD().updateScore(score);
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getEnnemiesLeft() {
        return enemiesSpawned - enemiesKilled;
    }

    public int getScore() {
        return score;
    }
}
