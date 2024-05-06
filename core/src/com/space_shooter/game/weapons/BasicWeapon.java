package com.space_shooter.game.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class BasicWeapon extends Weapon {
    private float projectileRadius;
    public BasicWeapon(String name, float projectileSpeed, float projectileRadius, Color projectileColor, int damage, int ammo, int fireRate, boolean automatic,
            WeaponManager weaponManager) {
        this.name = name;
        this.projectileSpeed = projectileSpeed;
        this.projectileRadius = projectileRadius;
        this.projectileColor = projectileColor;
        this.damage = damage;
        this.ammo = ammo;
        this.fireRate = fireRate;
        this.automatic = automatic;
        this.weaponManager = weaponManager;
    }

    @Override
    public void fire(Vector2 velocity) {
            new BulletProjectile(velocity, damage, getOwner(), projectileRadius, projectileColor, projectileSpeed);
    }
}
