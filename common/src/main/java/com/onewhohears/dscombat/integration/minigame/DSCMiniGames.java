package com.onewhohears.dscombat.integration.minigame;

import com.onewhohears.dscombat.integration.minigame.data.DSCAreaControlData;
import com.onewhohears.dscombat.integration.minigame.data.DogFightData;
import com.onewhohears.dscombat.integration.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.MiniGameManager;

public class DSCMiniGames {
	
	/**
	 * register all games here
	 */
	public static void registerGames() {
		MiniGameManager.registerGame("simple_dog_fight", DogFightData::createSimpleDogFight);
		MiniGameManager.registerGame("village_defense_vehicle_only", VillageDefenseData::createVillageDefenseVehicle);
		MiniGameManager.registerGame("dsc_area_control", DSCAreaControlData::createAreaControlMatch);
	}
	
}
