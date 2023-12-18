package com.onewhohears.dscombat.minigame;

import com.onewhohears.dscombat.minigame.data.DogFightData;
import com.onewhohears.minigames.minigame.MiniGameManager;

public class DSCMiniGames {
	
	/**
	 * called in {@link net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent}
	 * register all games here
	 */
	public static void registerGames() {
		/*
		 * TODO 3.2 create and register the following DSC mini game modes
		 * easy/fair dog fight resets (both players tp to runway when an aircraft is destroyed)
		 * bomb the flag/s
		 */
		MiniGameManager.registerGame("simple_dog_fight", (instanceId, gameTypeId) -> 
			DogFightData.createSimpleFFADogFight(instanceId, gameTypeId));
	}
	
}
