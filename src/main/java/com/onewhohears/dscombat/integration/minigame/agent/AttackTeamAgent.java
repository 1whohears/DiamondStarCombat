package com.onewhohears.dscombat.integration.minigame.agent;

import com.onewhohears.dscombat.integration.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.agent.TeamAgent;

public class AttackTeamAgent extends TeamAgent<VillageDefenseData> {

	public AttackTeamAgent(String teamName, VillageDefenseData gameData) {
		super(teamName, gameData);
	}
	
	@Override
	public boolean canUseKit(String kit) {
		return super.canUseKit(kit);
	}
	
	@Override
	public boolean canOpenShop(String shop) {
		if (shop.equals("defend")) return false;
		return super.canOpenShop(shop);
	}

}
