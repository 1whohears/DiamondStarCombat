package com.onewhohears.dscombat.integration.minigame.phase.village_defense;

import com.onewhohears.dscombat.integration.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.phase.flag.KillFlagBuyPhase;
import net.minecraft.server.MinecraftServer;

public class VillageDefenseBuyPhase extends KillFlagBuyPhase<VillageDefenseData> {

    public VillageDefenseBuyPhase(VillageDefenseData gameData) {
        super(gameData);
    }

    @Override
    public void onStart(MinecraftServer server) {
        if (!getGameData().isFirstRound()) getGameData().givePlayersTheirVehicle(server);
        super.onStart(server);
    }
}
