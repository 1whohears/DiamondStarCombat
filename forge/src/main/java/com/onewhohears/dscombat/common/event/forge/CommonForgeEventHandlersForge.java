package com.onewhohears.dscombat.common.event.forge;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.event.CommonEventHandlers;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CommonForgeEventHandlersForge {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        float amount = CommonEventHandlers.onLivingHurt(event.getEntity(), event.getSource(), event.getAmount());
        event.setAmount(amount);
    }
}
