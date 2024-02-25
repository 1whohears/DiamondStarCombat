package com.onewhohears.dscombat.minigame.condition;

import com.onewhohears.dscombat.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.condition.PhaseExitCondition;
import com.onewhohears.minigames.minigame.phase.GamePhase;

import net.minecraft.server.MinecraftServer;

public class ScoreWinExitCondition extends PhaseExitCondition<VillageDefenseData> {

	public ScoreWinExitCondition() {
		super("village_defense_score_win", "village_defense_end");
	}

	@Override
	public boolean shouldExit(MinecraftServer server, GamePhase<VillageDefenseData> currentPhase) {
		// TOD 3.8.4.1 score win exit condition
		return false;
	}
	
	@Override
	public void onExit(MinecraftServer server, GamePhase<VillageDefenseData> currentPhase) {
		super.onExit(server, currentPhase);
		// TODO 3.8.4.2 announce which team won
	}

}
