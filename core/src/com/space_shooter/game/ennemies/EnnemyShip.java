package com.space_shooter.game.ennemies;

import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.shared.entities.BattleShip;
import com.space_shooter.game.shared.entities.DrawnEntity;
import com.space_shooter.game.walls.Wall;

public class EnnemyShip extends BattleShip {
    protected int scoreValue;

    public EnnemyShip() {
        super();
        this.teleportDistance = 10f;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    @Override
    public void died() {
        super.died();
        GameContext.getInstance().getGamePlayManager().notifyEnnemyKilled(this);
    }

    @Override
    public void onCollision(DrawnEntity entity) {
        if (entity instanceof Wall) {
            startTeleportationAnimation(body.getLinearVelocity());
        }
    }
}
