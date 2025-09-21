package com.onewhohears.dscombat.common.event;

import com.onewhohears.dscombat.DSCombatMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CommonModEventHandlersForge {

    @SubscribeEvent
    public static void loadModConfigEvent(ModConfigEvent.Loading event) {
        CommonEventHandlers.onReadConfig(event.getConfig());
    }

    @SubscribeEvent
    public static void reloadModConfigEvent(ModConfigEvent.Reloading event) {
        CommonEventHandlers.onReadConfig(event.getConfig());
    }
}
