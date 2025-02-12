package com.space_shooter.game.ennemies;

import java.util.function.BiFunction;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.core.GameContext;

public class EnnemySpawner {
    private long lastSpawnTime;
    private long nextSpawnDelay;
    private float minSpawnDelay = 5.0f;
    private float maxSpawnDelay = 10.0f;
    private int ennemyHealth = GameConstants.ENNEMY_BASE_HEALTH;
    private Array<EnnemyFactoryEntry> ennemyFactories;

    class EnnemyFactoryEntry {
        String name;
        BiFunction<Vector2, Integer, EnnemyShip> factory;
        float weight;

        EnnemyFactoryEntry(String name, BiFunction<Vector2, Integer, EnnemyShip> factory, float weight) {
            this.name = name;
            this.factory = factory;
            this.weight = weight;
        }
    }

    public EnnemySpawner() {
        lastSpawnTime = TimeUtils.millis();
        ennemyFactories = new Array<>();
        ennemyFactories.add(
                new EnnemyFactoryEntry("Kamikaze", (position, health) -> new KamikazeShip(position, health), 1.0f));
        ennemyFactories.add(new EnnemyFactoryEntry("Distance Shooter",
                (position, health) -> new DistanceShooterShip(position, health), 0.0f));
    }

    public void update(float delta) {
        if (TimeUtils.millis() - lastSpawnTime > nextSpawnDelay) {
            int ennemiesLeft = GameContext.getInstance().getGamePlayManager().getEnnemiesLeft();
            if (ennemiesLeft < GameConfig.MAX_ENEMIES) {
                spawnAlienShip();
                lastSpawnTime = TimeUtils.millis();
                nextSpawnDelay = (long) MathUtils.random(minSpawnDelay, maxSpawnDelay) * 1_000L;
            }
        }
    }

    private BiFunction<Vector2, Integer, EnnemyShip> selectRandomFactory() {
        float totalWeight = 0f;
        for (EnnemyFactoryEntry entry : ennemyFactories) {
            totalWeight += entry.weight;
        }

        float random = MathUtils.random(0, totalWeight);
        float currentSum = 0;
        for (EnnemyFactoryEntry entry : ennemyFactories) {
            currentSum += entry.weight;
            if (currentSum >= random) {
                return entry.factory;
            }
        }

        return ennemyFactories.peek().factory;
    }

    public void adjustDifficulty(float delayDivider, float healthMultiplier, float weightAddition) {
        minSpawnDelay /= delayDivider;
        maxSpawnDelay /= delayDivider;
        ennemyHealth *= healthMultiplier;

        for (EnnemyFactoryEntry entry : ennemyFactories) {
            entry.weight += weightAddition;
        }
    }

    private void spawnAlienShip() {
        float x = 0, y = 0;
        int side = MathUtils.random(0, 2);

        switch (side) {
            case 0:
                x = MathUtils.random(0, GameConfig.WORLD_WIDTH);
                y = GameConfig.WORLD_HEIGHT + 10;
                break;
            case 1:
                x = MathUtils.random(0, GameConfig.WORLD_WIDTH);
                y = -10;
                break;
            case 2:
                x = GameConfig.WORLD_WIDTH + 10;
                y = MathUtils.random(0, GameConfig.WORLD_HEIGHT);
                break;
        }

        BiFunction<Vector2, Integer, EnnemyShip> factory = selectRandomFactory();
        EnnemyShip newAlienShip = factory.apply(new Vector2(x, y), ennemyHealth);
        GameContext.getInstance().getGamePlayManager().notifyEnnemySpawned(newAlienShip);
    }
}
