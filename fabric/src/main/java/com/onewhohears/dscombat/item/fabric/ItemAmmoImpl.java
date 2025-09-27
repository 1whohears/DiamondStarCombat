package com.onewhohears.dscombat.item.fabric;

import com.onewhohears.dscombat.item.ItemAmmo;

public class ItemAmmoImpl {
    public static ItemAmmo create(int stackSize, String defaultWeaponId) {
        return new ItemAmmo(stackSize, defaultWeaponId);
    }
}
