package com.onewhohears.dscombat.game.condition;

import com.onewhohears.dscombat.game.data.DeathMatchData;
import com.onewhohears.dscombat.game.phase.GamePhase;

import net.minecraft.server.MinecraftServer;

public class DeathMatchEndCondition<D extends DeathMatchData> extends PhaseExitCondition<D> {

	public DeathMatchEndCondition() {
		super("death_match_end", "death_match_end");
	}

	@Override
	public boolean shouldExit(MinecraftServer server, GamePhase<D> currentPhase) {
		return currentPhase.getGameData().getLivingAgents().size() == 1;
	}

}
