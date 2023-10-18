package com.onewhohears.dscombat.game.condition;

import com.onewhohears.dscombat.game.data.GameData;
import com.onewhohears.dscombat.game.phase.GamePhase;

import net.minecraft.server.MinecraftServer;

public abstract class PhaseExitCondition<D extends GameData> {
	
	private final String id;
	private final String nextPhaseId;
	
	protected PhaseExitCondition(String id, String nextPhaseId) {
		this.id = id;
		this.nextPhaseId = nextPhaseId;
	}
	
	public abstract boolean shouldExit(MinecraftServer server, GamePhase<D> currentPhase);
	
	public void onExit(MinecraftServer server, GamePhase<D> currentPhase) {
		currentPhase.onStop(server);
	}
	
	public String getId() {
		return id;
	}
	
	public String getNextPhaseId() {
		return nextPhaseId;
	}
	
}
