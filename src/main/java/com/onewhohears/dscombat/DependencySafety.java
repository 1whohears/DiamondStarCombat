package com.onewhohears.dscombat;

import com.onewhohears.dscombat.integration.minigame.DSCMiniGames;
import com.onewhohears.dscombat.integration.minigame.gen.DSCKitGenerator;
import com.onewhohears.dscombat.integration.minigame.gen.DSCShopGenerator;

import net.minecraft.data.DataGenerator;

public class DependencySafety {
	
	public static void fmlCommonSetup() {
		if (DSCombatMod.minigamesLoaded) DSCMiniGames.registerGames();
	}
	
	public static void serverDataGen(DataGenerator generator) {
		if (DSCombatMod.minigamesLoaded) {
			DSCKitGenerator.register(generator);
			DSCShopGenerator.register(generator);
		}
	}
	
}
