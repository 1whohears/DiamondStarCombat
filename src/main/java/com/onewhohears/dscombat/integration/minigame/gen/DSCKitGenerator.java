package com.onewhohears.dscombat.integration.minigame.gen;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.minigames.data.kits.GameKit;
import com.onewhohears.minigames.data.kits.MiniGameKitsGenerator;
import com.onewhohears.minigames.data.shops.MiniGameShopsManager;

import net.minecraft.data.DataGenerator;

import javax.annotation.Nullable;

public class DSCKitGenerator extends MiniGameKitsGenerator {
	
	public static void register(DataGenerator generator) {
		generator.addProvider(true, new DSCKitGenerator(generator));
	}
	
	protected DSCKitGenerator(DataGenerator generator) {
		super(generator);
	}

	public static JsonObject createTaczGunItemJson(String GunId, int GunCurrentAmmoCount, String GunFireMode,
												   @Nullable String AttachmentSTOCK, @Nullable String AttachmentMUZZLE,
												   @Nullable String AttachmentSCOPE) {
		JsonObject json = new JsonObject();
		json.addProperty("GunId", GunId);
		json.addProperty("HasBulletInBarrel", true);
		json.addProperty("GunCurrentAmmoCount", GunCurrentAmmoCount);
		json.addProperty("GunFireMode", GunFireMode);
		if (AttachmentSTOCK != null) json.add("AttachmentSTOCK", createTaczAttachmentJson(AttachmentSTOCK));
		if (AttachmentMUZZLE != null) json.add("AttachmentMUZZLE", createTaczAttachmentJson(AttachmentMUZZLE));
		if (AttachmentSCOPE != null) json.add("AttachmentSCOPE", createTaczAttachmentJson(AttachmentSCOPE));
		return json;
	}

	public static JsonObject createTaczAttachmentJson(String AttachmentId) {
		JsonObject json = new JsonObject();
		json.addProperty("id", "tacz:attachment");
		JsonObject tag = new JsonObject();
		tag.addProperty("AttachmentId", AttachmentId);
		json.add("tag", tag);
		json.addProperty("Count", true);
		return json;
	}

	public static JsonObject createTaczAmmoBox(int AmmoCount, int Level, String AmmoId) {
		JsonObject json = new JsonObject();
		json.addProperty("AmmoCount", AmmoCount);
		json.addProperty("Level", Level);
		json.addProperty("AmmoId", AmmoId);
		return json;
	}

	public static JsonObject createTaczAmmo(String AmmoId) {
		JsonObject json = new JsonObject();
		json.addProperty("AmmoId", AmmoId);
		return json;
	}

	public static JsonObject createTaczGunItemJson(String GunId, int GunCurrentAmmoCount, String GunFireMode) {
		return createTaczGunItemJson(GunId, GunCurrentAmmoCount, GunFireMode,
				null, null, null);
	}

	@Override
	protected void registerPresets() {
		JsonObject healthPotionNbt = new JsonObject();
		healthPotionNbt.addProperty("Potion", "minecraft:strong_healing");
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "soldier")
				.addItemKeep("tacz:modern_kinetic_gun", createTaczGunItemJson("tacz:ak47",
						30, "AUTO", "tacz:stock_tactical_ar",
						"tacz:muzzle_silencer_phantom_s1", "tacz:sight_coyote"))
				.addItemKeep("tacz:modern_kinetic_gun", createTaczGunItemJson("tacz:p320",
						12, "SEMI"))
				.addItemKeep("minecraft:iron_axe", true)
				.addItemKeep("minecraft:iron_pickaxe", true)
				.addItemKeep("minecraft:iron_shovel", true)
				.addItemRefill("minecraft:cooked_beef", 32)
				.addItemRefill("minecraft:splash_potion", 1, healthPotionNbt)
				.addItemKeep("minecraft:iron_helmet", true)
				.addItemKeep("minecraft:iron_chestplate", true)
				.addItemKeep("minecraft:iron_leggings", true)
				.addItemKeep("minecraft:iron_boots", true)
				.addItemRefill("dscombat:parachute")
				.addItemRefill("tacz:ammo_box", createTaczAmmoBox(360,1,"tacz:762x39"), "AmmoCount")
				.addItemRefill("tacz:ammo_box", createTaczAmmoBox(180,0,"tacz:9mm"), "AmmoCount")
				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "scout")
				.addItemKeep("tacz:modern_kinetic_gun", createTaczGunItemJson("tacz:m870",
						5, "SEMI"))
				.addItemKeep("tacz:modern_kinetic_gun", createTaczGunItemJson("tacz:deagle",
						7, "SEMI"))
				.addItemKeep("minecraft:iron_axe", true)
				.addItemKeep("minecraft:iron_pickaxe", true)
				.addItemKeep("minecraft:iron_shovel", true)
				.addItemRefill("minecraft:cooked_beef", 32)
				.addItemRefill("minecraft:splash_potion", 3, healthPotionNbt)
				.addItemKeep("minecraft:iron_helmet", true)
				.addItemKeep("minecraft:iron_chestplate", true)
				.addItemKeep("minecraft:iron_leggings", true)
				.addItemKeep("minecraft:iron_boots", true)
				.addItemRefill("dscombat:parachute")
				.addItemRefill("tacz:ammo_box", createTaczAmmoBox(108,0,"tacz:12g"), "AmmoCount")
				.addItemRefill("tacz:ammo_box", createTaczAmmoBox(144,0,"tacz:50ae"), "AmmoCount")
				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "demoman")
				.addItemKeep("tacz:modern_kinetic_gun", createTaczGunItemJson("tacz:rpg7",
						1, "SEMI"))
				.addItemKeep("tacz:modern_kinetic_gun", createTaczGunItemJson("tacz:ump45",
						25, "AUTO"))
				.addItemKeep("minecraft:iron_axe", true)
				.addItemKeep("minecraft:iron_pickaxe", true)
				.addItemKeep("minecraft:iron_shovel", true)
				.addItemRefill("minecraft:bread", 32)
				.addItemKeep("minecraft:leather_helmet", true)
				.addItemKeep("minecraft:leather_chestplate", true)
				.addItemKeep("minecraft:leather_leggings", true)
				.addItemKeep("minecraft:leather_boots", true)
				.addItemRefill("dscombat:parachute")
				.addItemRefill("tacz:ammo", 6, createTaczAmmo("tacz:rpg_rocket"), "AmmoCount")
				.addItemRefill("tacz:ammo_box", createTaczAmmoBox(180,0,"tacz:45acp"), "AmmoCount")
				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "heavy")
				.addItemKeep("tacz:modern_kinetic_gun", createTaczGunItemJson("tacz:m249",
						75, "AUTO"))
				.addItemKeep("tacz:modern_kinetic_gun", createTaczGunItemJson("tacz:glock_17",
						17, "SEMI"))
				.addItemKeep("minecraft:iron_axe", true)
				.addItemKeep("minecraft:iron_pickaxe", true)
				.addItemKeep("minecraft:iron_shovel", true)
				.addItemRefill("minecraft:bread", 32)
				.addItemKeep("minecraft:iron_helmet", true)
				.addItemKeep("minecraft:diamond_chestplate", true)
				.addItemKeep("minecraft:iron_leggings", true)
				.addItemKeep("minecraft:iron_boots", true)
				.addItemRefill("dscombat:parachute")
				.addItemRefill("tacz:ammo_box", createTaczAmmoBox(180,0,"tacz:556x45"), "AmmoCount")
				.addItemRefill("tacz:ammo_box", createTaczAmmoBox(180,0,"tacz:9mm"), "AmmoCount")
				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "sniper")
				.addItemKeep("tacz:modern_kinetic_gun", createTaczGunItemJson("tacz:ai_awp",
						5, "SEMI", null, null,
						"tacz:scope_standard_8x"))
				.addItemKeep("tacz:modern_kinetic_gun", createTaczGunItemJson("tacz:tec9",
						15, "AUTO"))
				.addItemKeep("minecraft:iron_axe", true)
				.addItemKeep("minecraft:iron_pickaxe", true)
				.addItemKeep("minecraft:iron_shovel", true)
				.addItemRefill("minecraft:bread", 32)
				.addItemKeep("minecraft:leather_helmet", true)
				.addItemKeep("minecraft:leather_chestplate", true)
				.addItemKeep("minecraft:leather_leggings", true)
				.addItemKeep("minecraft:leather_boots", true)
				.addItemRefill("dscombat:parachute")
				.addItemRefill("tacz:ammo_box", createTaczAmmoBox(90,0,"tacz:338"), "AmmoCount")
				.addItemRefill("tacz:ammo_box", createTaczAmmoBox(180,0,"tacz:9mm"), "AmmoCount")
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
