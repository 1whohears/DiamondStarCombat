package com.onewhohears.dscombat.game.phase.deathmatch;

import com.onewhohears.dscombat.game.condition.NeverExitCondition;
import com.onewhohears.dscombat.game.data.DeathMatchData;
import com.onewhohears.dscombat.game.phase.GamePhase;

import net.minecraft.server.MinecraftServer;

public class DeathMatchEndPhase<T extends DeathMatchData> extends GamePhase<T> {
	
	public DeathMatchEndPhase(T gameData) {
		super("death_match_end", gameData, new NeverExitCondition<>());
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
