package com.onewhohears.dscombat.common.event.fabric;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.event.CommonEventHandlers;
import net.minecraftforge.api.fml.event.config.ModConfigEvents;
import net.minecraftforge.fml.config.ModConfig;

public class CommonEventHandlersFabric {

    public static void init() {
        ModConfigEvents.loading(DSCombatMod.MODID).register(CommonEventHandlersFabric::onConfigLoad);
        ModConfigEvents.reloading(DSCombatMod.MODID).register(CommonEventHandlersFabric::onConfigReload);
    }

    public static void onConfigReload(ModConfig modConfig) {
        CommonEventHandlers.onReadConfig(modConfig);
    }

    public static void onConfigLoad(ModConfig modConfig) {
        CommonEventHandlers.onReadConfig(modConfig);
    }

}
