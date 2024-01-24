package com.onewhohears.dscombat.minigame.gen;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.minigames.data.kits.MiniGameKitsGenerator;
import com.onewhohears.minigames.data.shops.MiniGameShopsManager;

import net.minecraft.data.DataGenerator;

public class DSCKitGenerator extends MiniGameKitsGenerator {
	
	public static void register(DataGenerator generator) {
		generator.addProvider(true, new DSCKitGenerator(generator));
	}
	
	protected DSCKitGenerator(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected void registerKits() {
		
	}
	
	@Override
	public String getName() {
		return DSCombatMod.MODID+":"+MiniGameShopsManager.KIND;
	}

}
