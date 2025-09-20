package com.onewhohears.dscombat.fabric.client;

import com.onewhohears.dscombat.client.event.DSCEntityRenderers;
import net.fabricmc.api.ClientModInitializer;

public class DSCombatModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DSCEntityRenderers.register();
    }
}
