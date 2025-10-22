package com.onewhohears.dscombat.integration.minigame.gen;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.minigames.data.shops.GameShop;
import com.onewhohears.minigames.data.shops.MiniGameShopsGenerator;
import com.onewhohears.minigames.data.shops.MiniGameShopsManager;
import net.minecraft.data.PackOutput;

import static com.onewhohears.dscombat.integration.minigame.gen.DSCKitGenerator.createTaczGunItemJson;

public class DSCShopGenerator extends MiniGameShopsGenerator {
	
	public DSCShopGenerator(PackOutput output) {
		super(output);
	}

	@Override
	protected void registerPresets() {
		addPresetToGenerate(GameShop.Builder.create(DSCombatMod.MODID, "vehicle_attacker")
				.addProduct("dscombat:big_gas_can", "minigames:money", 4)
				.addProduct("dscombat:vehicle", vehicleItem("bronco_plane_shitter"), "minigames:money", 8)
				.addProduct("dscombat:vehicle", vehicleItem("krait_chopper_door_knocker"), "minigames:money", 12)
				.addProduct("dscombat:vehicle", vehicleItem("krait_chopper_brawler"), "minigames:money", 12)
				.addProduct("dscombat:vehicle", vehicleItem("javi_plane_close_air"), "minigames:money", 18)
				.addProduct("dscombat:vehicle", vehicleItem("javi_plane_heavy_bomber"), "minigames:money", 24)
				.addProduct("dscombat:vehicle", vehicleItem("alexis_plane_escort"), "minigames:money", 24)
				.addProduct("dscombat:vehicle", vehicleItem("alexis_plane_infiltrator"), "minigames:money", 32)
				.build());
		addPresetToGenerate(GameShop.Builder.create(DSCombatMod.MODID, "vehicle_defender")
				.addProduct("dscombat:big_gas_can", "minigames:money", 4)
				.addProduct("dscombat:vehicle", vehicleItem("small_roller"), "minigames:money", 8)
				.addProduct("dscombat:vehicle", vehicleItem("mrbudger_tank"), "minigames:money", 18)
				.addProduct("dscombat:vehicle", vehicleItem("eric_truck"), "minigames:money", 32)
				.addProduct("dscombat:vehicle", vehicleItem("krait_chopper_door_knocker"), "minigames:money", 12)
				.addProduct("dscombat:vehicle", vehicleItem("krait_chopper_brawler"), "minigames:money", 12)
				.addProduct("dscombat:vehicle", vehicleItem("felix_plane_defender"), "minigames:money", 18)
				.addProduct("dscombat:vehicle", vehicleItem("eden_plane_interceptor"), "minigames:money", 24)
				.build());
		JsonObject healthPotionNbt = new JsonObject();
		healthPotionNbt.addProperty("Potion", "minecraft:strong_healing");
		addPresetToGenerate(GameShop.Builder.create(DSCombatMod.MODID, "military_misc")
				.addProduct("tacz:modern_kinetic_gun", createTaczGunItemJson("yeet:m33a1",
						6, "SEMI"), "minigames:money", 5)
				.addProduct("tacz:modern_kinetic_gun", createTaczGunItemJson("yeet:shg43at",
						2, "SEMI"), "minigames:money", 5)
				.addProduct("tacz:modern_kinetic_gun", createTaczGunItemJson("yeet:m18",
						3, "SEMI"), "minigames:money", 3)
				.addProduct("dscombat:thick_wrench", "minigames:money", 2)
				.addProduct("dscombat:big_gas_can", "minigames:money", 2)
				.addProduct("dscombat:chain_hook", "minigames:money", 1)
				.addProduct("minecraft:chain", "minigames:money", 1)
				.addProduct("dscombat:gpr100", "minigames:money", 10)
				.addProduct("dscombat:spraycan", "minigames:money", 1)
				.addProduct("minecraft:splash_potion", healthPotionNbt, "minigames:money", 4)
				.addProduct("minecraft:cobblestone", 2, "minigames:money", 2)
				.addProduct("minecraft:oak_planks", 16, "minigames:money", 2)
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
