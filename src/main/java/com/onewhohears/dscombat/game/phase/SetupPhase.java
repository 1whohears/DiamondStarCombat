package com.onewhohears.dscombat.game.phase;

import com.onewhohears.dscombat.game.condition.NeverExitCondition;
import com.onewhohears.dscombat.game.condition.PhaseExitCondition;
import com.onewhohears.dscombat.game.data.GameData;

import net.minecraft.server.MinecraftServer;

public abstract class SetupPhase<T extends GameData> extends GamePhase<T> {

	protected SetupPhase(String id, T gameData) {
		this(id, gameData, new NeverExitCondition<T>());
	}
	
	@SafeVarargs
	protected SetupPhase(String id, T gameData, PhaseExitCondition<T>... exitConditions) {
		super(id, gameData, exitConditions);
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
	
	@Override
	public boolean isSetupPhase() {
		return true;
	}
	
}
