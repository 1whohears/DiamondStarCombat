package com.onewhohears.dscombat.integration.distant_players;

import com.onewhohears.distant_players.client.core.ExtraInfoManager;
import com.onewhohears.dscombat.init.ModEntities;

public class DSCDistantPlayers {

    public static void register() {
        ExtraInfoManager.register(ModEntities.BOAT.get(), DSCExtraRenderInfo::new);
        ExtraInfoManager.register(ModEntities.CAR.get(), DSCExtraRenderInfo::new);
        ExtraInfoManager.register(ModEntities.PLANE.get(), DSCExtraRenderInfo::new);
        ExtraInfoManager.register(ModEntities.HELICOPTER.get(), DSCExtraRenderInfo::new);
        ExtraInfoManager.register(ModEntities.SUBMARINE.get(), DSCExtraRenderInfo::new);
        ExtraInfoManager.register(ModEntities.STATIONARY.get(), DSCExtraRenderInfo::new);
    }

}
