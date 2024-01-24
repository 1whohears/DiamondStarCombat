package com.onewhohears.dscombat.minigame.agent;

import com.onewhohears.dscombat.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.agent.TeamAgent;

public class DefendTeamAgent extends TeamAgent<VillageDefenseData> {

	public DefendTeamAgent(String teamName, VillageDefenseData gameData) {
		super(teamName, gameData);
	}
	
	@Override
	public boolean canUseKit(String kit) {
		return super.canUseKit(kit);
	}
	
	@Override
	public boolean canOpenShop(String shop) {
		if (shop.equals("attack")) return false;
		return super.canOpenShop(shop);
	}

}
