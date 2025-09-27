package com.onewhohears.dscombat.item.fabric;

import com.onewhohears.dscombat.item.ItemExternalPart;

public class ItemExternalPartImpl {
    public static ItemExternalPart create(int stackSize, String defaultPresetId) {
        return new ItemExternalPart(stackSize, defaultPresetId);
    }
}
