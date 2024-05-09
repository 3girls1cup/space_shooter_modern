package com.space_shooter.game.shared.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.space_shooter.game.core.GameConstants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class TeleportAnimation {
    private Body body;
    private float teleportAnimationTimer;
    private Vector2 teleportStart;
    private Vector2 teleportEnd;
    private Array<Sprite> teleportSprites;
    private boolean isTeleporting;
    private Sprite baseSprite;
    public TeleportAnimation(Sprite baseSprite, Body body) {
        this.body = body;
        this.teleportAnimationTimer = 0f;
        this.teleportStart = new Vector2();
        this.teleportEnd = new Vector2();
        new Vector2();
        this.teleportSprites = new Array<>();
        this.baseSprite = baseSprite;
        this.isTeleporting = false;

        for (int i = 0; i < GameConstants.NUM_TELEPORT_CIRCLES; i++) {
            Sprite tempSprite = new Sprite(baseSprite);
            tempSprite.setOriginCenter();
            tempSprite.setSize(baseSprite.getWidth(), baseSprite.getHeight());
            teleportSprites.add(tempSprite);
        }
    }

    public void startTeleportation(Vector2 start, Vector2 end) {
        this.teleportStart.set(start);
        this.teleportAnimationTimer = 0f;
        this.isTeleporting = true;
        this.teleportEnd.set(end);
    }

    public void update(float deltaTime) {
        if (!isTeleporting) return;

        teleportAnimationTimer += deltaTime;
        if (teleportAnimationTimer >= GameConstants.TELEPORT_ANIMATION_DURATION) {
            isTeleporting = false;
            float angle = body.getAngle();
            
            float cos = MathUtils.cos(angle);
            float sin = MathUtils.sin(angle);
    
            float bx = -body.getLocalCenter().x * cos + body.getLocalCenter().y * sin;
            float by = -body.getLocalCenter().x * sin - body.getLocalCenter().y * cos;

            body.setTransform(teleportEnd.add(bx, by), angle);
        }
    }

    public void render(SpriteBatch spriteBatch) {
        if (!isTeleporting) return;

        float progress = teleportAnimationTimer / GameConstants.TELEPORT_ANIMATION_DURATION;
        int numSprites = teleportSprites.size;
        float increment = 0.5f / (numSprites - 1);

        
        for (int i = 0; i < numSprites; i++) {
            Sprite tempSprite = teleportSprites.get(i);

            float t = (float) i / (numSprites - 1);
            Vector2 pos = new Vector2(teleportStart).lerp(teleportEnd, t);
            float midpoint = 0.5f;
            float scale = Math.max(Math.abs(t - midpoint) * 2f, 0.6f);
            float peakTime = increment * i;
            float endFadeTime = peakTime + 0.5f;

            float visibility = calculateOpacity(progress, peakTime, endFadeTime, i, numSprites);


            tempSprite.setRotation(baseSprite.getRotation());
            tempSprite.setScale(scale);
            tempSprite.setPosition(pos.x - tempSprite.getWidth() / 2, pos.y - tempSprite.getHeight() / 2);
            tempSprite.setColor(tempSprite.getColor().r, tempSprite.getColor().g, tempSprite.getColor().b, visibility);
    
            tempSprite.draw(spriteBatch);
        }
    }

    private float calculateOpacity(float currentProgress, float peakTime, float endFadeTime, int index, int numSprites) {
        float visibility = 0f;
        if (currentProgress < peakTime) {
            visibility = 0f;
        } else if (index == numSprites - 1) {
            visibility = 1f;
        } else {
            float t = (currentProgress - peakTime) / (endFadeTime - peakTime);
            float fade = 1f - t;
            visibility = fade;
        }
        return visibility;
    }

    public boolean isTeleporting() {
        return isTeleporting;
    }
}
