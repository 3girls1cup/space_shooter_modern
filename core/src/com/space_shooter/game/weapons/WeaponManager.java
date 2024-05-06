package com.space_shooter.game.weapons;


import java.util.HashMap;
import java.util.Map;

import com.space_shooter.game.core.GameConstants;
import com.space_shooter.game.core.GameContext;
import com.space_shooter.game.player.PlayerShip;
import com.space_shooter.game.shared.entities.BattleShip;

public class WeaponManager {
    protected Map<String, Weapon> weapons = new HashMap<>();
    protected String currentWeaponName;
    protected BattleShip owner;

    public WeaponManager(BattleShip owner) {
        this.owner = owner;
        initializeWeapons();
    }

    protected void initializeWeapons() {
        addWeapon(new LaserWeapon(GameConstants.LASER_NAME, 1, 500, 100, true, this));
    }

    public void addWeapon(Weapon weapon) {
        weapons.put(weapon.getName(), weapon);
        currentWeaponName = weapon.getName();
    }

    public Weapon getWeapon(String name) {
        return weapons.get(name);
    }

    public Map<String, Weapon> getWeaponsMap() {
        return weapons;
    }

    public void switchWeapon(String weaponName) {
        if (weapons.containsKey(weaponName)) {
            currentWeaponName = weaponName;
        }
        notifySwitchWeapon();
    }

    private void notifySwitchWeapon() {
        if (owner instanceof PlayerShip) {
            GameContext.getInstance().getGameHUD().notifyWeaponSwitched(currentWeaponName);
        }
    }

    public void switchToNextWeapon() {
        if (weapons.isEmpty()) {
            return;
        }
        String[] weaponNames = weapons.keySet().toArray(new String[0]);
        int currentIndex = 0;
        for (int i = 0; i < weaponNames.length; i++) {
            if (weaponNames[i].equals(currentWeaponName)) {
                currentIndex = i;
                break;
            }
        }
        currentIndex = (currentIndex + 1) % weaponNames.length;
        currentWeaponName = weaponNames[currentIndex];
        notifySwitchWeapon();
    }

    public Weapon getCurrentWeapon() {
        return weapons.get(currentWeaponName);
    }

    public BattleShip getOwner() {
        return owner;
    }
}


