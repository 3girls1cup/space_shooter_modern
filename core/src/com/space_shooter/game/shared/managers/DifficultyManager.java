package com.space_shooter.game.shared.managers;

import com.space_shooter.game.core.GameConfig;
import com.space_shooter.game.core.GameContext;

public abstract class DifficultyManager {

   public static void update(int score) {
      float baseMultiplier = score * GameConfig.DIFFICULTY_FACTOR;
      increasePlayerTeleportDistance(score);
      increaseEnnemySpawnerDifficulty(baseMultiplier);
      increaseWallSpawnerDifficulty(baseMultiplier);
   }

   private static void increasePlayerTeleportDistance(int score) {
      GameContext.getInstance().getPlayer().increaseTeleportDistance(score / 500);
   }

   private static void increaseEnnemySpawnerDifficulty(float baseMultiplier) {
      float delayDivider = Math.min(1 + baseMultiplier, 2);
      float healthMultiplier = Math.min(1 + baseMultiplier, 6);
      float weightAddition = baseMultiplier;

      GameContext.getInstance().getEnnemySpawner().adjustDifficulty(delayDivider, healthMultiplier, weightAddition);
   }

   private static void increaseWallSpawnerDifficulty(float baseMultiplier) {
      float delayDivider = Math.min(1 + baseMultiplier, 3);
      float speedMultiplier = Math.min(1 + baseMultiplier, 3);

      GameContext.getInstance().getWallSpawner().adjustDifficulty(delayDivider, speedMultiplier);
   }
}
