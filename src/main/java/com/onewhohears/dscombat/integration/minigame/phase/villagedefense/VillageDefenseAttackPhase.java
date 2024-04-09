package com.onewhohears.dscombat.integration.minigame.phase.villagedefense;

import com.onewhohears.dscombat.integration.minigame.condition.FlagDeathExitCondition;
import com.onewhohears.dscombat.integration.minigame.condition.TeamDeathExitCondition;
import com.onewhohears.dscombat.integration.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.condition.TimeoutPhaseExitCondition;
import com.onewhohears.minigames.minigame.phase.deathmatch.DeathMatchPlayPhase;

import net.minecraft.server.MinecraftServer;

public class VillageDefenseAttackPhase extends DeathMatchPlayPhase<VillageDefenseData> {

	public VillageDefenseAttackPhase(VillageDefenseData gameData) {
		super("village_defense_attack", gameData, 
			new FlagDeathExitCondition(), new TeamDeathExitCondition(),
			new TimeoutPhaseExitCondition<>("village_defense_attack_timeout", 
				"village_defense_buy", gameData.getAttackTime(), 
				"info.dscombat.attack_phase_end"));
	}
	
	@Override
	public void tickPhase(MinecraftServer server) {
		super.tickPhase(server);
		// TODO 3.3.4 periodically say how much time is left in attack phase
	}
	
	@Override
	public void onReset(MinecraftServer server) {
		super.onReset(server);
	}
	
	@Override
	public void onStart(MinecraftServer server) {
		super.onStart(server);
		// TODO 3.8.3.2 tell teams current game info (attack phase start/score/time left)
	}
	
	@Override
	public void onStop(MinecraftServer server) {
		super.onStop(server);
	}

}
