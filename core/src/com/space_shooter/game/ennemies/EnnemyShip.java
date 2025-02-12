package com.space_shooter.game.ennemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.shared.entities.BattleShip;
import com.space_shooter.game.shared.entities.DrawnEntity;

public abstract class EnnemyShip extends BattleShip {
    protected int scoreValue;

    public EnnemyShip(Texture texture, Vector2 spawnPosition, String fileName, int health) {
        setStaticSprite(texture);
        addBodyToWorld(spawnPosition, fileName);
        this.health = health;
        this.teleportDistance = 20f;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    @Override
    public void kill() {
        super.kill();
        GameContext.getInstance().getGamePlayManager().notifyEnnemyKilled(this);
    }

    @Override
    public void onCollision(DrawnEntity other) {
        super.onCollision(other);
    }
}
