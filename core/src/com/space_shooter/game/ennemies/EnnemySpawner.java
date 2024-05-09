package com.space_shooter.game.ennemies;

import java.util.function.Function;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.core.GameContext;

public class EnnemySpawner {
    private long lastSpawnTime;
    private long nextSpawnDelay;
    private float minSpawnDelay = 1.0f; 
    private float maxSpawnDelay = 3.0f;

    class EnnemyFactoryEntry {
        String name;
        Function<Vector2, EnnemyShip> factory;
        float weight;
    
        EnnemyFactoryEntry(String name, Function<Vector2, EnnemyShip> factory, float weight) {
            this.name = name;
            this.factory = factory;
            this.weight = weight;
        }
    }
    
    Array<EnnemyFactoryEntry> ennemyFactories;

    public EnnemySpawner() {
        lastSpawnTime = TimeUtils.millis();
        ennemyFactories = new Array<>();
        // ennemyFactories.add(new EnnemyFactoryEntry("Kamikaze", position -> new KamikazeShip(position), 1.0f));
        ennemyFactories.add(new EnnemyFactoryEntry("Distance Shooter", position -> new DistanceShooterShip(position), 0.0f));
    }

    public void update(float delta) {
        if (TimeUtils.millis() - lastSpawnTime >  nextSpawnDelay) {
            int ennemiesLeft = GameContext.getInstance().getGamePlayManager().getEnnemiesLeft();
            if (ennemiesLeft < GameConfig.MAX_ENEMIES && ennemiesLeft >= 0) {
                spawnAlienShip();
                lastSpawnTime = TimeUtils.millis();
                nextSpawnDelay = (long) MathUtils.random(minSpawnDelay, maxSpawnDelay) * 1_000L;
            }
        }
    }

    private Function<Vector2, EnnemyShip> selectRandomFactory() {
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
    
    public void adjustDifficulty() {
        for (EnnemyFactoryEntry entry : ennemyFactories) {
            if (entry.name == "Distance Shooter") {
                entry.weight = Math.min(GameContext.getInstance().getGamePlayManager().getScore() * GameConfig.DIFFICULTY_FACTOR, 2);  // Ajustez en fonction de votre logique de jeu
            }
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
        
        adjustDifficulty();
        Function<Vector2, EnnemyShip> factory = selectRandomFactory();
        EnnemyShip newAlienShip = factory.apply(new Vector2(x, y));
        GameContext.getInstance().getGamePlayManager().notifyEnnemySpawned(newAlienShip);
    }
}
