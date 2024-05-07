package com.space_shooter.game.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.player.PlayerShip;
import com.space_shooter.game.shared.entities.BattleShip;

public abstract class Weapon {
    protected String name;
    protected int damage;
    protected int ammo;
    protected int fireRate;
    protected long lastFireTime = 0;
    protected boolean automatic;
    protected float projectileWidth;
    protected int projectileHeight;
    protected WeaponManager weaponManager;
    protected float projectileSpeed;
    protected Texture projectileTexture;
    protected Color projectileColor;
    protected String iconPath;

    public String getName() {
        return name;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public BattleShip getOwner() {
        return weaponManager.getOwner();
    }

    public void manageFireAllowance(Vector2 direction) {
        if (System.currentTimeMillis() - lastFireTime > fireRate) {
            if (getOwner() instanceof PlayerShip) {
                if (ammo > 0 || ammo == -1) {
                    if (ammo != -1) {
                        ammo--;
                        notifyAmmoChanges();
                    }
                } else {
                    // TODO : ajouter un son pour indiquer que le joueur n'a plus de munitions
                    weaponManager.switchWeapon("Basic");
                    weaponManager.getCurrentWeapon().manageFireAllowance(direction);
                    return;
                }
            }
            lastFireTime = System.currentTimeMillis();
            fire(direction);
        }
    }

    protected void notifyAmmoChanges() {
        GameContext.getInstance().getGameHUD().updateWeaponAmmo(name, ammo);
    }

    public void fire(Vector2 velocity) {
        
    }

    public int getAmmo() {
        return ammo;
    }

    public String getIconPath() {
        return "weapons/" + name + ".png";
    }
}
