package com.onewhohears.dscombat.minigame.gen;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.minigames.data.kits.GameKit;
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
		registerKit(GameKit.Builder.create(DSCombatMod.MODID, "scout")
				
				.build());
		registerKit(GameKit.Builder.create(DSCombatMod.MODID, "soldier")
				
				.build());
		registerKit(GameKit.Builder.create(DSCombatMod.MODID, "demoman")
				
				.build());
		registerKit(GameKit.Builder.create(DSCombatMod.MODID, "heavy")
				
				.build());
		registerKit(GameKit.Builder.create(DSCombatMod.MODID, "sniper")
				
				.build());
	}
	
	@Override
	public String getName() {
		return DSCombatMod.MODID+":"+MiniGameShopsManager.KIND;
	}

}
