package com.space_shooter.game.shared.managers;

import com.space_shooter.game.core.GameContext;

public class DifficultyManager {
      private static DifficultyManager instance;
      
      private DifficultyManager() {
      }
      
      public static DifficultyManager getInstance() {
         if (instance == null) {
               instance = new DifficultyManager();
         }
         return instance;
      }

      public void update(int score) {
         increasePlayerTeleportDistance(score);
      }
      
      private void increasePlayerTeleportDistance(int scoreValue) {
         GameContext.getInstance().getPlayer().increaseTeleportDistance(scoreValue / 500);
      }
}
