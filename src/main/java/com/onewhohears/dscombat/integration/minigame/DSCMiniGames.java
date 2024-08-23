package com.onewhohears.dscombat.integration.minigame;

import com.onewhohears.dscombat.integration.minigame.data.DogFightData;
import com.onewhohears.dscombat.integration.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.MiniGameManager;

public class DSCMiniGames {
	
	/**
	 * called in {@link net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent}
	 * register all games here
	 */
	public static void registerGames() {
		/*
		 * TODO 3.2 create and register the following DSC mini game modes
		 * easy/fair dog fight resets (both players tp to runway when an vehicle is destroyed)
		 * bomb the flag/s
		 */
		MiniGameManager.registerGame("simple_dog_fight", (instanceId, gameTypeId) -> 
			DogFightData.createSimpleFFADogFight(instanceId, gameTypeId));
		MiniGameManager.registerGame("village_defense_3", (instanceId, gameTypeId) -> 
			VillageDefenseData.createVillageDefense(instanceId, gameTypeId, 3));
		MiniGameManager.registerGame("village_defense_5", (instanceId, gameTypeId) -> 
			VillageDefenseData.createVillageDefense(instanceId, gameTypeId, 5));
		MiniGameManager.registerGame("village_defense_10", (instanceId, gameTypeId) -> 
			VillageDefenseData.createVillageDefense(instanceId, gameTypeId, 10));
		MiniGameManager.registerGame("village_defense_extended", (instanceId, gameTypeId) -> 
			VillageDefenseData.createExtendedVillageDefense(instanceId, gameTypeId));
	}
	
}
