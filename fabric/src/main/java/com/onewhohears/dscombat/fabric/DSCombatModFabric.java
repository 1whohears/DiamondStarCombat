package com.onewhohears.dscombat.fabric;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.event.ClientEventHandlersFabric;
import com.onewhohears.dscombat.common.event.CommonEventHandlersFabric;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.ModInitializer;

public class DSCombatModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DSCombatMod.init();
        CommonEventHandlersFabric.init();
        if (Platform.getEnvironment() == Env.CLIENT) {
            DSCombatMod.clientInit();
            ClientEventHandlersFabric.init();
        }
    }
}
