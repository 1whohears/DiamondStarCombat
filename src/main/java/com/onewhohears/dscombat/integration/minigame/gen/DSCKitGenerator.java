package com.onewhohears.dscombat.integration.minigame.gen;

import com.google.gson.JsonObject;
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
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "dogfight_alexis")
				.addItem("dscombat:vehicle", vehicleItem("alexis_plane"))
				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "dogfight_felix")
				.addItem("dscombat:vehicle", vehicleItem("felix_plane"))
				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "dogfight_javi")
				.addItem("dscombat:vehicle", vehicleItem("javi_plane"))
				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "dogfight_eden")
				.addItem("dscombat:vehicle", vehicleItem("eden_plane"))
				.build());
	}

	public static JsonObject vehicleItem(String preset) {
		JsonObject json = new JsonObject();
		json.addProperty("preset", preset);
		return json;
	}
	
	@Override
	public String getName() {
		return DSCombatMod.MODID+":"+MiniGameShopsManager.KIND;
	}

}
