package com.onewhohears.dscombat.fabric;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.event.fabric.ClientEventHandlersFabric;
import com.onewhohears.dscombat.common.event.fabric.CommonEventHandlersFabric;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.ModInitializer;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

public class DSCombatModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DSCombatMod.init();
        CommonEventHandlersFabric.init();
        if (Platform.getEnvironment() == Env.CLIENT) {
            DSCombatMod.clientInit();
            ClientEventHandlersFabric.init();
        }
        ModLoadingContext.registerConfig(MODID, ModConfig.Type.CLIENT, Config.clientSpec);
        ModLoadingContext.registerConfig(MODID, ModConfig.Type.COMMON, Config.commonSpec);
        ModLoadingContext.registerConfig(MODID, ModConfig.Type.SERVER, Config.serverSpec);
    }
}
