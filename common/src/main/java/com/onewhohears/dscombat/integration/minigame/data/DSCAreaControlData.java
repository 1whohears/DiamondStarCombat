package com.onewhohears.dscombat.integration.minigame.data;

import com.onewhohears.dscombat.integration.minigame.phase.general_dsc.DSCBuyPhase;
import com.onewhohears.minigames.minigame.data.AreaControlData;
import com.onewhohears.minigames.minigame.param.MiniGameParamTypes;
import com.onewhohears.minigames.minigame.phase.areacontrol.AreaControlAttackPhase;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.BuyAttackAttackEndPhase;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.BuyAttackEndPhase;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.BuyAttackSetupPhase;

public class DSCAreaControlData extends AreaControlData {

	public static DSCAreaControlData createAreaControlMatch(String instanceId, String gameTypeId) {
		DSCAreaControlData game = new DSCAreaControlData(instanceId, gameTypeId);
		game.setPhases(new BuyAttackSetupPhase<>(game),
				new DSCBuyPhase<>(game),
				new AreaControlAttackPhase<>(game),
				new BuyAttackAttackEndPhase<>(game),
				new BuyAttackEndPhase<>(game));
		game.setParam(MiniGameParamTypes.CAN_ADD_PLAYERS, false);
		game.setParam(MiniGameParamTypes.CAN_ADD_TEAMS, true);
		game.setParam(MiniGameParamTypes.REQUIRE_SET_SPAWN, true);
		game.setParam(MiniGameParamTypes.USE_WORLD_BORDER, false);
		game.setParam(MiniGameParamTypes.DEFAULT_LIVES, 1000);
		game.setParam(MiniGameParamTypes.RESPAWN_TICKS, 200);
		game.setParam(MiniGameParamTypes.ROUNDS_TO_WIN, 10);
		game.getAllowedPoiTypes().add("area_control");
		game.addKits("soldier", "scout", "demoman", "heavy", "sniper");
		game.getParam(MiniGameParamTypes.ATTACKER_SHOPS).add("vehicle_attacker");
		game.getParam(MiniGameParamTypes.ATTACKER_SHOPS).add("military_misc");
		game.getParam(MiniGameParamTypes.DEFENDER_SHOPS).add("vehicle_defender");
		game.getParam(MiniGameParamTypes.DEFENDER_SHOPS).add("military_misc");
		game.setParam(MiniGameParamTypes.BUY_RADIUS, 50);
		return game;
	}

	public DSCAreaControlData(String instanceId, String gameTypeId) {
		super(instanceId, gameTypeId);
	}

}
