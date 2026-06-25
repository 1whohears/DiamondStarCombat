package com.onewhohears.dscombat.item.fabric;

import com.onewhohears.dscombat.item.ItemTurret;

public class ItemTurretImpl {
    public static ItemTurret create(int stackSize, String defaultPresetId) {
        return new ItemTurret(stackSize, defaultPresetId);
    }
}
