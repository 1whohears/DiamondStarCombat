package com.onewhohears.dscombat.minigame.phase.villagedefense;

import com.onewhohears.dscombat.minigame.condition.FlagDeathExitCondition;
import com.onewhohears.dscombat.minigame.condition.TeamDeathExitCondition;
import com.onewhohears.dscombat.minigame.data.VillageDefenseData;
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
