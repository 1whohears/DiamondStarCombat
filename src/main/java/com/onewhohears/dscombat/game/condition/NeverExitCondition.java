package com.onewhohears.dscombat.game.condition;

import com.onewhohears.dscombat.game.data.GameData;
import com.onewhohears.dscombat.game.phase.GamePhase;

import net.minecraft.server.MinecraftServer;

public class NeverExitCondition<T extends GameData> extends PhaseExitCondition<T> {
	
	public NeverExitCondition() {
		super("never_exit", "");
	}

	@Override
	public boolean shouldExit(MinecraftServer server, GamePhase<T> currentPhase) {
		return false;
	}

}
