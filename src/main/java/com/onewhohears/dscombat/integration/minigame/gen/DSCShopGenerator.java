package com.onewhohears.dscombat.integration.minigame.gen;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.minigames.data.shops.GameShop;
import com.onewhohears.minigames.data.shops.MiniGameShopsGenerator;
import com.onewhohears.minigames.data.shops.MiniGameShopsManager;

import net.minecraft.data.DataGenerator;

public class DSCShopGenerator extends MiniGameShopsGenerator {
	
	public static void register(DataGenerator generator) {
		generator.addProvider(true, new DSCShopGenerator(generator));
	}
	
	protected DSCShopGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void registerPresets() {
		addPresetToGenerate(GameShop.Builder.create(DSCombatMod.MODID, "attacker")
				.addProduct("dscombat:vehicle", vehicleItem("bronco_plane"), "minigames:money", 8)
				.addProduct("dscombat:vehicle", vehicleItem("noah_chopper"), "minigames:money", 13)
				.addProduct("dscombat:vehicle", vehicleItem("felix_plane"), "minigames:money", 10)
				.addProduct("dscombat:vehicle", vehicleItem("felix_plane_support"), "minigames:money", 15)
				.addProduct("dscombat:vehicle", vehicleItem("javi_plane"), "minigames:money", 20)
				.addProduct("dscombat:vehicle", vehicleItem("javi_plane_bomber"), "minigames:money", 31)
				.addProduct("dscombat:vehicle", vehicleItem("alexis_plane"), "minigames:money", 16)
				.addProduct("dscombat:vehicle", vehicleItem("alexis_plane_support"), "minigames:money", 34)
				.build());
		addPresetToGenerate(GameShop.Builder.create(DSCombatMod.MODID, "defender")
				.addProduct("dscombat:vehicle", vehicleItem("small_roller"), "minigames:money", 8)
				.addProduct("dscombat:vehicle", vehicleItem("mrbudger_tank"), "minigames:money", 11)
				.addProduct("dscombat:vehicle", vehicleItem("axcel_truck"), "minigames:money", 15)
				.addProduct("dscombat:vehicle", vehicleItem("jason_plane"), "minigames:money", 7)
				.addProduct("dscombat:vehicle", vehicleItem("felix_plane"), "minigames:money", 16)
				.addProduct("dscombat:vehicle", vehicleItem("krait_chopper"), "minigames:money", 18)
				.addProduct("dscombat:vehicle", vehicleItem("javi_plane_truck"), "minigames:money", 29)
				.addProduct("dscombat:vehicle", vehicleItem("alexis_plane"), "minigames:money", 31)
				.addProduct("dscombat:vehicle", vehicleItem("alexis_plane_sniper"), "minigames:money", 45)
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
