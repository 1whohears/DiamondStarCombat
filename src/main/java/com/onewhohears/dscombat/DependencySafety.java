package com.onewhohears.dscombat;

import com.onewhohears.dscombat.minigame.DSCMiniGames;

public class DependencySafety {
	
	public static void fmlCommonSetup() {
		if (DSCombatMod.minigamesLoaded) DSCMiniGames.registerGames();
	}
	
}
