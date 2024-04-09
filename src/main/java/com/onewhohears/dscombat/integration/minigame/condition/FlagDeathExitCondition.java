package com.onewhohears.dscombat.integration.minigame.condition;

import com.onewhohears.dscombat.integration.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.condition.PhaseExitCondition;
import com.onewhohears.minigames.minigame.phase.GamePhase;

import net.minecraft.server.MinecraftServer;

public class FlagDeathExitCondition extends PhaseExitCondition<VillageDefenseData> {

	public FlagDeathExitCondition() {
		super("village_defense_flag_death", "village_defense_buy");
	}

	@Override
	public boolean shouldExit(MinecraftServer server, GamePhase<VillageDefenseData> currentPhase) {
		// TODO 3.8.1.1 flag death exit condition
		return false;
	}
	
	@Override
	public void onExit(MinecraftServer server, GamePhase<VillageDefenseData> currentPhase) {
		super.onExit(server, currentPhase);
		// TODO 3.8.1.2 announce flag died and give attackers a point
		//currentPhase.getGameData().getAttackers().getScore();
	}

}
