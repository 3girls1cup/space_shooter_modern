package com.space_shooter.game.weapons;

import com.badlogic.gdx.math.Vector2;
import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.player.PlayerShip;

public class LaserWeapon extends Weapon {
    private LaserBeam currentBeam;
    
    public LaserWeapon(String name, int damage, int ammo, int fireRate, boolean automatic,
            WeaponManager weaponManager) {
        this.name = name;
        this.damage = damage;
        this.ammo = ammo;
        this.fireRate = fireRate;
        this.automatic = automatic;
        this.weaponManager = weaponManager;
        setBeamWidth(GameConstants.LASER_BEAM_WIDTH);
    }

    public void setBeamWidth(float width) {
        this.projectileWidth = width;
    }

    @Override
    public void manageFireAllowance(Vector2 direction) {
        if (System.currentTimeMillis() - lastFireTime > GameConstants.LASER_BEAM_TIMEOUT) {
            if (getOwner() instanceof PlayerShip) {
                if (ammo > 0 || ammo == -1) {
                    if (ammo != -1) {
                        ammo--;
                        super.notifyAmmoChanges();
                    }
                } else {
                    // TODO : ajouter un son pour indiquer que le joueur n'a plus de munitions
                    weaponManager.switchWeapon("Basic");
                    weaponManager.getCurrentWeapon().manageFireAllowance(direction);
                    return;
                }
            }
            lastFireTime = System.currentTimeMillis();
            if (currentBeam == null || currentBeam.isMarkedForRemoval()) {
                fire(direction);
            }
        }
        if (currentBeam != null || !currentBeam.isMarkedForRemoval()) {
            currentBeam.setDirection(direction);
        }
    }

    @Override
    public void fire(Vector2 velocity) {
            currentBeam = new LaserBeam(projectileWidth, damage, getOwner());
    }


}
