package com.onewhohears.dscombat.minigame.phase.villagedefense;

import com.onewhohears.dscombat.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.phase.deathmatch.DeathMatchEndPhase;

import net.minecraft.server.MinecraftServer;

public class VillageDefenseEndPhase extends DeathMatchEndPhase<VillageDefenseData> {

	public VillageDefenseEndPhase(VillageDefenseData gameData) {
		super("village_defense_end", gameData);
	}
	
	@Override
	public void tickPhase(MinecraftServer server) {
		super.tickPhase(server);
	}
	
	@Override
	public void onReset(MinecraftServer server) {
		super.onReset(server);
	}
	
	@Override
	public void onStart(MinecraftServer server) {
		super.onStart(server);
	}
	
	@Override
	public void onStop(MinecraftServer server) {
		super.onStop(server);
	}

}
