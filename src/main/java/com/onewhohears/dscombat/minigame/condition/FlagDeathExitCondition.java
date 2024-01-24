package com.onewhohears.dscombat.minigame.condition;

import com.onewhohears.dscombat.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.condition.PhaseExitCondition;
import com.onewhohears.minigames.minigame.phase.GamePhase;

import net.minecraft.server.MinecraftServer;

public class FlagDeathExitCondition extends PhaseExitCondition<VillageDefenseData> {

	public FlagDeathExitCondition() {
		super("village_defense_flag_death", "village_defense_buy");
	}

	@Override
	public boolean shouldExit(MinecraftServer server, GamePhase<VillageDefenseData> currentPhase) {
		// TODO 3.8.1 flag death exit condition
		return false;
	}
	
	@Override
	public void onExit(MinecraftServer server, GamePhase<VillageDefenseData> currentPhase) {
		super.onExit(server, currentPhase);
	}

}
