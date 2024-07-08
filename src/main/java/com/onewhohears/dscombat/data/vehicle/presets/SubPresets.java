package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class SubPresets {
	
	public static final VehicleStats EMPTY_ANDOLF_SUB = VehicleStats.Builder
			.createSubmarine(DSCombatMod.MODID, "andolf_sub_empty")
			.setAssetId("andolf_sub")
			.setSortFactor(5)
			.setItem(ModItems.ANDOLF_SUB.getId())
			.setMaxHealth(100f)
			.setBaseArmor(400f)
			.setArmorDamageThreshold(10f)
			.setArmorAbsorbtionPercent(0.60f)
			.setMass(90000f)
			.setMaxSpeed(0.6f)
			.setStealth(0.05f)
			.setCrossSecArea(18f)
			.setIdleHeat(10f)
			.setTurnRadius(0f)
			.setMaxTurnRates(2f, 2f, 2f)
			.setTurnTorques(1f, 1f, 1f)
			.setThrottleRate(0.04f, 0.04f)
			.setBasicEngineSounds(ModSounds.SUB_1, ModSounds.SUB_1)
			.setRotationalInertia(6, 10, 3)
			.setCrashExplosionRadius(6)
			.set3rdPersonCamDist(12)
			.setCanNegativeThrottle(true)
			.addIngredient(ModItems.LARGE_PROPELLER.getId(), 1)
			.addIngredient(ModItems.LARGE_FUSELAGE.getId(), 2)
			.addIngredient(ModItems.SEAT.getId(), 6)
			.addIngredient("minecraft:gold_ingot", 10)
			.addIngredient("minecraft:glass", 4)
			.addPilotSeatSlot(0.6, -1.5, 2.8)
			.addSeatSlot("seat1", -0.6, -1.5, 2.8)
			.addSeatSlot("seat2", 0.6, -1.5, 1.6)
			.addSeatSlot("seat3", -0.6, -1.5, 1.6)
			.addSeatSlot("seat4", 0.6, -1.5, 0.4)
			.addSeatSlot("seat5", -0.6, -1.5, 0.4)
			.addEmptySlot("nose_1", SlotType.PYLON_MED, 0, -1.6, 3.5, 180)
			.addEmptySlot("frame_1", SlotType.PYLON_MED, 2, 0, 1, -90)
			.addEmptySlot("frame_2", SlotType.PYLON_MED, 2, 0, 0, -90)
			.addEmptySlot("frame_3", SlotType.PYLON_MED, 2, 0, -1, -90)
			.addEmptySlot("frame_4", SlotType.PYLON_MED, -2, 0, 1, 90)
			.addEmptySlot("frame_5", SlotType.PYLON_MED, -2, 0, 0, 90)
			.addEmptySlot("frame_6", SlotType.PYLON_MED, -2, 0, -1, 90)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_5", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_6", SlotType.TECH_INTERNAL)
			.setEntityMainHitboxSize(4.5f, 4)
			.build();
			
	public static final VehicleStats UNARMED_ANDOLF_SUB = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "andolf_sub_unarmed", EMPTY_ANDOLF_SUB)
			.setCraftable()
			.addIngredient(ModItems.C12_ENGINE.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.build();
	
	public static final VehicleStats DEFAULT_ANDOLF_SUB = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "andolf_sub", UNARMED_ANDOLF_SUB)
			.addIngredient(ModItems.WR1K.getId())
			.setSlotItem("internal_4", ModItems.WR1K.getId())
			.setSlotItem("nose_1", ModItems.XM12.getId(), "20mm", true)
			.setSlotItem("frame_1", ModItems.HEAVY_MISSILE_RACK.getId(), "mk13", true)
			.setSlotItem("frame_2", ModItems.HEAVY_MISSILE_RACK.getId(), "mk13", true)
			.setSlotItem("frame_3", ModItems.HEAVY_MISSILE_RACK.getId(), "mk13", true)
			.setSlotItem("frame_4", ModItems.HEAVY_MISSILE_RACK.getId(), "mk13", true)
			.setSlotItem("frame_5", ModItems.HEAVY_MISSILE_RACK.getId(), "mk13", true)
			.setSlotItem("frame_6", ModItems.HEAVY_MISSILE_RACK.getId(), "mk13", true)
			.addIngredient("minecraft:copper_ingot", 63)
			.addIngredientTag("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 6)
			.addIngredient(ModItems.XM12.getId(), 1)
			.build();
	
	public static final VehicleStats EMPTY_GOOGLE_SUB = VehicleStats.Builder
			.createSubmarine(DSCombatMod.MODID, "google_sub_empty")
			.setAssetId("google_sub")
			.setSortFactor(6)
			.setItem(ModItems.GOOGLE_SUB.getId())
			.setMaxHealth(200f)
			.setBaseArmor(800f)
			.setArmorDamageThreshold(10f)
			.setArmorAbsorbtionPercent(0.70f)
			.setMass(180000f)
			.setMaxSpeed(0.4f)
			.setStealth(0.05f)
			.setCrossSecArea(18f)
			.setIdleHeat(10f)
			.setTurnRadius(0f)
			.setMaxTurnRates(2f, 2f, 2f)
			.setTurnTorques(1f, 1f, 1f)
			.setThrottleRate(0.04f, 0.04f)
			.setBasicEngineSounds(ModSounds.SUB_1, ModSounds.SUB_1)
			.setRotationalInertia(6, 10, 3)
			.setCrashExplosionRadius(6)
			.set3rdPersonCamDist(30)
			.setCanNegativeThrottle(true)
			.addIngredient(ModItems.LARGE_PROPELLER.getId(), 1)
			.addIngredient(ModItems.LARGE_FUSELAGE.getId(), 5)
			.addIngredient(ModItems.SEAT.getId(), 8)
			.addIngredient("minecraft:gold_ingot", 20)
			.addPilotSeatSlot(0.6, -3.5, 10)
			.addSeatSlot("seat2", -0.6, -3.5, 10)
			.addSeatSlot("seat3", 0.6, -3.5, 9)
			.addSeatSlot("seat4", -0.6, -3.5, 9)
			.addSeatSlot("seat5", 0.6, -3.5, 8)
			.addSeatSlot("seat6", -0.6, -3.5, 8)
			.addSeatSlot("seat7", 0.6, -3.5, 7)
			.addSeatSlot("seat8", -0.6, -3.5, 7)
			.addEmptySlot("frame_1", SlotType.PYLON_MED, 0, 2.5, 15, 0)
			.addEmptySlot("frame_2", SlotType.PYLON_MED, -2.5, 0, 15, 90)
			.addEmptySlot("frame_3", SlotType.PYLON_MED, 0, -2.5, 15, 180)
			.addEmptySlot("frame_4", SlotType.PYLON_MED, 2.5, 0, 15, 270)
			.addEmptySlot("frame_5", SlotType.MOUNT_HEAVY, 0, 4, 10.5, 0)
			.addEmptySlot("frame_6", SlotType.MOUNT_HEAVY, 0, 4, -2, 0)
			.addEmptySlot("frame_7", SlotType.MOUNT_HEAVY, 0, 4, -7, 0)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.addEmptySlot("internal_5", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_6", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_7", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_8", SlotType.TECH_INTERNAL)
			.setBaseTextureNum(2)
			.setEntityMainHitboxSize(8, 8)
			.build();
			
	public static final VehicleStats UNARMED_GOOGLE_SUB = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "google_sub_unarmed", EMPTY_GOOGLE_SUB)
			.setCraftable()
			.addIngredient(ModItems.C12_ENGINE.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.build();
	
	public static final VehicleStats DEFAULT_GOOGLE_SUB = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "google_sub", UNARMED_GOOGLE_SUB)
			.addIngredient(ModItems.WR1K.getId())
			.setSlotItem("internal_4", ModItems.WR1K.getId())
			.setSlotItem("internal_5", ModItems.AR1K.getId())
			.setSlotItem("frame_1", ModItems.HEAVY_MISSILE_RACK.getId(), "mk13", true)
			.setSlotItem("frame_2", ModItems.HEAVY_MISSILE_RACK.getId(), "mk13", true)
			.setSlotItem("frame_3", ModItems.HEAVY_MISSILE_RACK.getId(), "mk13", true)
			.setSlotItem("frame_4", ModItems.HEAVY_MISSILE_RACK.getId(), "mk13", true)
			.setSlotItem("frame_5", ModItems.VLS.getId(), "bgm109", true)
			.setSlotItem("frame_6", ModItems.VLS.getId(), "bgm109", true)
			.setSlotItem("frame_7", ModItems.VLS.getId(), "bgm109", true)
			.addIngredient("minecraft:copper_ingot", 63)
			.addIngredient("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 4)
			.addIngredient(ModItems.VLS.getId(), 3)
			.build();
	
}
