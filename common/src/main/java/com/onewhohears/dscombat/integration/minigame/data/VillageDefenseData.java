package com.onewhohears.dscombat.integration.minigame.data;

import com.onewhohears.dscombat.integration.minigame.phase.village_defense.VillageDefenseBuyPhase;
import com.onewhohears.minigames.minigame.data.KillFlagData;
import com.onewhohears.minigames.minigame.param.MiniGameParamTypes;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.BuyAttackAttackEndPhase;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.BuyAttackEndPhase;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.BuyAttackSetupPhase;
import com.onewhohears.minigames.minigame.phase.flag.KillFlagAttackPhase;

public class VillageDefenseData extends KillFlagData {
	
	public static VillageDefenseData createVillageDefenseVehicle(String instanceId, String gameTypeId) {
		VillageDefenseData game = new VillageDefenseData(instanceId, gameTypeId);
		game.setPhases(new BuyAttackSetupPhase<>(game),
				new VillageDefenseBuyPhase(game),
				new KillFlagAttackPhase<>(game),
				new BuyAttackAttackEndPhase<>(game),
				new BuyAttackEndPhase<>(game));
		game.addKits("soldier", "scout", "demoman", "heavy", "sniper");
		game.getParam(MiniGameParamTypes.ATTACKER_SHOPS).add("vehicle_attacker");
		game.getParam(MiniGameParamTypes.ATTACKER_SHOPS).add("military_misc");
		game.getParam(MiniGameParamTypes.DEFENDER_SHOPS).add("vehicle_defender");
		game.getParam(MiniGameParamTypes.DEFENDER_SHOPS).add("military_misc");
		game.setParam(MiniGameParamTypes.ROUNDS_TO_WIN, 5);
		game.setParam(MiniGameParamTypes.BUY_RADIUS, 50);
		game.setParam(MiniGameParamTypes.REQUIRE_SET_SPAWN, true);
		return game;
	}
	
	public VillageDefenseData(String instanceId, String gameTypeId) {
		super(instanceId, gameTypeId);
	}

}
