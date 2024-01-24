package com.onewhohears.dscombat.minigame.condition;

import com.onewhohears.dscombat.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.condition.PhaseExitCondition;
import com.onewhohears.minigames.minigame.phase.GamePhase;

import net.minecraft.server.MinecraftServer;

public class TeamDeathExitCondition extends PhaseExitCondition<VillageDefenseData> {

	public TeamDeathExitCondition() {
		super("village_defense_team_death", "village_defense_buy");
	}

	@Override
	public boolean shouldExit(MinecraftServer server, GamePhase<VillageDefenseData> currentPhase) {
		return currentPhase.getGameData().getDefenders().getLivingPlayerAgents().size() == 0;
	}
	
	@Override
	public void onExit(MinecraftServer server, GamePhase<VillageDefenseData> currentPhase) {
		super.onExit(server, currentPhase);
		// TODO 3.8.4.1 announce all defenders died
	}

}
