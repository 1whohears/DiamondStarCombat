package com.onewhohears.dscombat.item.fabric;

import com.onewhohears.dscombat.item.ItemWeaponPart;

public class ItemWeaponPartImpl {
    public static ItemWeaponPart create(int stackSize, String defaultPresetId) {
        return new ItemWeaponPart(stackSize, defaultPresetId);
    }
}
