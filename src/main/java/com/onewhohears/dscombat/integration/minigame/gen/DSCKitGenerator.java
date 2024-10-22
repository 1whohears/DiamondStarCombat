package com.onewhohears.dscombat.integration.minigame.gen;

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
	protected void registerPresets() {
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "scout")

				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "soldier")

				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "demoman")

				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "heavy")

				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "sniper")

				.build());
	}
	
	@Override
	public String getName() {
		return DSCombatMod.MODID+":"+MiniGameShopsManager.KIND;
	}

}
