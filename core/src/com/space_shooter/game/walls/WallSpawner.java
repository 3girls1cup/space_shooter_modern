package com.space_shooter.game.walls;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class WallSpawner {
    private long lastSpawnTime;
    private float minSpawnDelay = 10.0f; 
    private float maxSpawnDelay = 100.0f;
    public boolean test = false;

    public WallSpawner() {
        lastSpawnTime = TimeUtils.nanoTime();
    }
    
    public void update(float delta) {
        if (TimeUtils.nanoTime() - lastSpawnTime > MathUtils.random(minSpawnDelay, maxSpawnDelay) * 1_000_000_000L) {
            spawnWall();
            lastSpawnTime = TimeUtils.nanoTime();
        }
    }

    private void spawnWall() {
        int randomAngle = MathUtils.random(0, 180);
        new Wall(1.0f, randomAngle, getRandomSpeed());
    }

    private float getRandomSpeed() { //TODO : ajuster à la difficulté
        return MathUtils.random(5.0f, 10.0f);
    }
}
