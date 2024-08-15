package com.onewhohears.dscombat.client.event.forgebus;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.onewholibs.common.event.OnSyncBoolGameRuleEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void onSyncBoolGameRuleEvent(OnSyncBoolGameRuleEvent event) {
        if (event.getId().equals(DSCGameRules.DISABLE_ELYTRA_FLYING.getId())) {
            DSCClientInputs.disable3rdPersonVehicle = event.getBool();
        }
    }

}
