package com.onewhohears.dscombat.fabric;

import com.onewhohears.dscombat.DSCombatMod;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.ModInitializer;

public class DSCombatModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DSCombatMod.init();
        if (Platform.getEnvironment() == Env.CLIENT) {
            DSCombatMod.clientInit();
        }
    }
}
