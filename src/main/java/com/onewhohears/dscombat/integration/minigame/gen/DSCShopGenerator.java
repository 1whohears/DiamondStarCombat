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
		JsonObject felix_plane_support = new JsonObject();
		felix_plane_support.addProperty("preset", "felix_plane_support");
		JsonObject javi_plane_bomber = new JsonObject();
		javi_plane_bomber.addProperty("preset", "javi_plane_bomber");
		JsonObject alexis_plane_support = new JsonObject();
		alexis_plane_support.addProperty("preset", "alexis_plane_support");
		addPresetToGenerate(GameShop.Builder.create(DSCombatMod.MODID, "attacker")
				.addProduct("dscombat:bronco_plane", "minigames:money", 8)
				.addProduct("dscombat:noah_chopper", "minigames:money", 13)
				.addProduct("dscombat:felix_plane", "minigames:money", 10)
				.addProduct("dscombat:felix_plane", felix_plane_support, "minigames:money", 15)
				.addProduct("dscombat:javi_plane", "minigames:money", 20)
				.addProduct("dscombat:javi_plane", javi_plane_bomber, "minigames:money", 31)
				.addProduct("dscombat:alexis_plane", "minigames:money", 16)
				.addProduct("dscombat:alexis_plane", alexis_plane_support, "minigames:money", 34)
				.build());
		JsonObject javi_plane_truck = new JsonObject();
		javi_plane_truck.addProperty("preset", "javi_plane_truck");
		JsonObject alexis_plane_sniper = new JsonObject();
		alexis_plane_sniper.addProperty("preset", "alexis_plane_sniper");
		addPresetToGenerate(GameShop.Builder.create(DSCombatMod.MODID, "defender")
				.addProduct("dscombat:small_roller", "minigames:money", 8)
				.addProduct("dscombat:mrbudger_tank", "minigames:money", 11)
				.addProduct("dscombat:axcel_truck", "minigames:money", 15)
				.addProduct("dscombat:jason_plane", "minigames:money", 7)
				.addProduct("dscombat:felix_plane", "minigames:money", 16)
				.addProduct("dscombat:noah_chopper", "minigames:money", 18)
				.addProduct("dscombat:javi_plane", javi_plane_truck, "minigames:money", 29)
				.addProduct("dscombat:alexis_plane", "minigames:money", 31)
				.addProduct("dscombat:alexis_plane", alexis_plane_sniper, "minigames:money", 45)
				.build());
	}
	
	@Override
	public String getName() {
		return DSCombatMod.MODID+":"+MiniGameShopsManager.KIND;
	}

}
