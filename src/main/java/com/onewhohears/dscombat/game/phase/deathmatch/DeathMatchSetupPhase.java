package com.onewhohears.dscombat.game.phase.deathmatch;

import com.onewhohears.dscombat.game.data.DeathMatchData;
import com.onewhohears.dscombat.game.phase.SetupPhase;

import net.minecraft.server.MinecraftServer;

public class DeathMatchSetupPhase<T extends DeathMatchData> extends SetupPhase<T> {

	public DeathMatchSetupPhase(T gameData) {
		super("death_match_setup", gameData);
		forceAdventureMode = true;
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
