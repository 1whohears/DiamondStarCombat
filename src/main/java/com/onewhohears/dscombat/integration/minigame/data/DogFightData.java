package com.onewhohears.dscombat.integration.minigame.data;

import com.onewhohears.dscombat.integration.minigame.phase.dog_fight.DogFightAttackPhase;
import com.onewhohears.minigames.minigame.data.BuyAttackData;
import com.onewhohears.minigames.minigame.param.MiniGameParamTypes;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.*;

public class DogFightData extends BuyAttackData {
	
	public static DogFightData createSimpleDogFight(String instanceId, String gameTypeId) {
		DogFightData game = new DogFightData(instanceId, gameTypeId);
		game.setPhases(new BuyAttackSetupPhase<>(game),
				new BuyAttackBuyPhase<>(game),
				new DogFightAttackPhase(game),
				new BuyAttackAttackEndPhase<>(game),
				new BuyAttackEndPhase<>(game));
		game.setParam(MiniGameParamTypes.BUY_TIME, 200);
		game.setParam(MiniGameParamTypes.ROUNDS_TO_WIN, 5);
		game.setParam(MiniGameParamTypes.REQUIRE_SET_SPAWN, true);
		game.addKits("dogfight_alexis", "dogfight_felix", "dogfight_javi", "dogfight_eden");
		return game;
	}
	
	protected DogFightData(String instanceId, String gameTypeId) {
		super(instanceId, gameTypeId);
	}
}
