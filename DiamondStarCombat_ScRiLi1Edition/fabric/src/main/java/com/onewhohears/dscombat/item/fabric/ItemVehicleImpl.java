package com.onewhohears.dscombat.item.fabric;

import com.onewhohears.dscombat.item.ItemVehicle;

public class ItemVehicleImpl {
    public static ItemVehicle create(String defaultPresetId) {
        return new ItemVehicle(defaultPresetId);
    }
}
