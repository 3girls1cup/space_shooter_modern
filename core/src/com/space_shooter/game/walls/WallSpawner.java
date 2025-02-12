package com.space_shooter.game.walls;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class WallSpawner {
    private long lastSpawnTime;
    private long nextSpawnDelay;
    private float minSpawnDelay = 5.0f;
    private float maxSpawnDelay = 10.0f;
    private float minWallSpeed = 6.0f;
    private float maxWallSpeed = 14.0f;

    public WallSpawner() {
        lastSpawnTime = TimeUtils.millis();
    }

    public void update(float delta) {
        if (TimeUtils.millis() - lastSpawnTime > nextSpawnDelay) {
            spawnWall();
            lastSpawnTime = TimeUtils.millis();
            nextSpawnDelay = (long) MathUtils.random(minSpawnDelay, maxSpawnDelay) * 1000L;
        }
    }

    private void spawnWall() {
        int randomAngle = MathUtils.random(0, 180);
        new Wall(3.0f, randomAngle, getRandomSpeed());
    }

    private float getRandomSpeed() {
        return MathUtils.random(minWallSpeed, maxWallSpeed);
    }

    public void adjustDifficulty(float delayDivider, float speedMultiplier) {
        minSpawnDelay /= delayDivider;
        maxSpawnDelay /= delayDivider;
        minWallSpeed *= speedMultiplier;
        maxWallSpeed *= speedMultiplier;
    }
}
