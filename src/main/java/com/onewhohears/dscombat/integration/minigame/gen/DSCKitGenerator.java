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
				.addItem("tacz:modern_kinetic_gun", 1, false,
						createTaczGunItemJson("tacz:ak47", 30, "AUTO",
								"tacz:stock_tactical_ar",
								"tacz:muzzle_silencer_phantom_s1",
								"tacz:sight_coyote"),
						true, false, true, "", "GunId")
				.addItem("tacz:modern_kinetic_gun", 1, false,
						createTaczGunItemJson("tacz:p320", 12, "SEMI"),
						true, false, true, "", "GunId")
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
				.addItem("tacz:ammo_box", 1, false,
						createTaczAmmoBox(360,1,"tacz:762x39"),
						true, true, true, "AmmoCount", "AmmoId")
				.addItem("tacz:ammo_box", 1, false,
						createTaczAmmoBox(180,0,"tacz:45acp"),
						true, true, true, "AmmoCount", "AmmoId")
				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "scout")
				.addItem("tacz:modern_kinetic_gun", 1, false,
						createTaczGunItemJson("tacz:m870", 5, "SEMI"),
						true, false, true, "", "GunId")
				.addItem("tacz:modern_kinetic_gun", 1, false,
						createTaczGunItemJson("tacz:deagle", 7, "SEMI"),
						true, false, true, "", "GunId")
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
				.addItem("tacz:ammo_box", 1, false,
						createTaczAmmoBox(108,0,"tacz:12g"),
						true, true, true, "AmmoCount", "AmmoId")
				.addItem("tacz:ammo_box", 1, false,
						createTaczAmmoBox(144,0,"tacz:50ae"),
						true, true, true, "AmmoCount", "AmmoId")
				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "demoman")
				.addItem("tacz:modern_kinetic_gun", 1, false,
						createTaczGunItemJson("tacz:rpg7", 1, "SEMI"),
						true, false, true, "", "GunId")
				.addItem("tacz:modern_kinetic_gun", 1, false,
						createTaczGunItemJson("tacz:ump45", 25, "AUTO"),
						true, false, true, "", "GunId")
				.addItemKeep("minecraft:iron_axe", true)
				.addItemKeep("minecraft:iron_pickaxe", true)
				.addItemKeep("minecraft:iron_shovel", true)
				.addItemRefill("minecraft:bread", 32)
				.addItemKeep("minecraft:leather_helmet", true)
				.addItemKeep("minecraft:leather_chestplate", true)
				.addItemKeep("minecraft:leather_leggings", true)
				.addItemKeep("minecraft:leather_boots", true)
				.addItemRefill("dscombat:parachute")
				.addItemRefill("tacz:ammo", 6, createTaczAmmo("tacz:rpg_rocket"))
				.addItem("tacz:ammo_box", 1, false,
						createTaczAmmoBox(180,0,"tacz:45acp"),
						true, true, true, "AmmoCount", "AmmoId")
				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "heavy")
				.addItem("tacz:modern_kinetic_gun", 1, false,
						createTaczGunItemJson("tacz:m249", 75, "AUTO"),
						true, false, true, "", "GunId")
				.addItem("tacz:modern_kinetic_gun", 1, false,
						createTaczGunItemJson("tacz:glock_17", 17, "SEMI"),
						true, false, true, "", "GunId")
				.addItemKeep("minecraft:iron_axe", true)
				.addItemKeep("minecraft:iron_pickaxe", true)
				.addItemKeep("minecraft:iron_shovel", true)
				.addItemRefill("minecraft:bread", 32)
				.addItemKeep("minecraft:iron_helmet", true)
				.addItemKeep("minecraft:diamond_chestplate", true)
				.addItemKeep("minecraft:iron_leggings", true)
				.addItemKeep("minecraft:iron_boots", true)
				.addItemRefill("dscombat:parachute")
				.addItem("tacz:ammo_box", 1, false,
						createTaczAmmoBox(180,0,"tacz:556x45"),
						true, true, true, "AmmoCount", "AmmoId")
				.addItem("tacz:ammo_box", 1, false,
						createTaczAmmoBox(180,0,"tacz:9mm"),
						true, true, true, "AmmoCount", "AmmoId")
				.build());
		addPresetToGenerate(GameKit.Builder.create(DSCombatMod.MODID, "sniper")
				.addItem("tacz:modern_kinetic_gun", 1, false,
						createTaczGunItemJson("tacz:ai_awp", 5, "SEMI",
								null, null, "tacz:scope_standard_8x"),
						true, false, true, "", "GunId")
				.addItem("tacz:modern_kinetic_gun", 1, false,
						createTaczGunItemJson("tacz:m1911", 15, "AUTO"),
						true, false, true, "", "GunId")
				.addItemKeep("minecraft:iron_axe", true)
				.addItemKeep("minecraft:iron_pickaxe", true)
				.addItemKeep("minecraft:iron_shovel", true)
				.addItemRefill("minecraft:bread", 32)
				.addItemKeep("minecraft:leather_helmet", true)
				.addItemKeep("minecraft:leather_chestplate", true)
				.addItemKeep("minecraft:leather_leggings", true)
				.addItemKeep("minecraft:leather_boots", true)
				.addItemRefill("dscombat:parachute")
				.addItem("tacz:ammo_box", 1, false,
						createTaczAmmoBox(90,0,"tacz:338"),
						true, true, true, "AmmoCount", "AmmoId")
				.addItem("tacz:ammo_box", 1, false,
						createTaczAmmoBox(180,0,"tacz:45acp"),
						true, true, true, "AmmoCount", "AmmoId")
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
