package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.VehicleSoundManager;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class CarPresets {
	
	public static final VehicleStats DEFAULT_ORANGE_TESLA = VehicleStats.Builder
			.createCar(DSCombatMod.MODID, "orange_tesla")
			.setAssetId("orange_tesla")
			.setSortFactor(10)
			.setItem(ModItems.VEHICLE.getId())
			.setCraftable()
			.setMaxHealth(40)
			.setBaseArmor(0)
			.setArmorDamageThreshold(1)
			.setArmorAbsorbtionPercent(0.01f)
			.setMass(3000f)
			.setMaxSpeed(0.6f)
			.setStealth(1.0f)
			.setCrossSecArea(5.375f)
			.setIdleHeat(4f)
			.setTurnRadius(7f)
			.setMaxTurnRates(0f, 0f, 4f)
			.setTurnTorques(0f, 0f, 1f)
			.setThrottleRate(0.07f, 0.07f)
			.setBasicEngineSounds(ModSounds.ORANGE_TESLA, ModSounds.ORANGE_TESLA)
			.setRotationalInertia(8, 12, 8)
			.setCrashExplosionRadius(2)
			.set3rdPersonCamDist(4)
			.setCanNegativeThrottle(true)
			.addPilotSeatSlot(0.5, 0.45, 0.3)
			.addSeatSlot("seat1", -0.5, 0.45, 0.3)
			.addSeatSlot("seat2", 0.5, 0.45, -0.85)
			.addSeatSlot("seat3", -0.5, 0.45, -0.85)
			.addItemSlot("internal_1", SlotType.SPIN_ENGINE, ModItems.C6_ENGINE.getId())
			.addItemSlot("internal_2", SlotType.INTERNAL, ModItems.LIGHT_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.SEAT.getId(), 4)
			.addIngredient(ModItems.C6_ENGINE.getId())
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.addIngredient(ModItems.WHEEL.getId(), 4)
			.addIngredientTag("dscombat:aluminum_ingot", 10)
			.addIngredient("minecraft:orange_dye", 4)
			.setEntityMainHitboxSize(2.5f, 2.15f)
			.build();
	
	public static final VehicleStats EMPTY_AXCEL_TRUCK = VehicleStats.Builder
			.createCar(DSCombatMod.MODID, "axcel_truck_empty")
			.setAssetId("axcel_truck")
			.setSortFactor(5)
			.setItem(ModItems.VEHICLE.getId())
			.setMaxHealth(40)
			.setBaseArmor(40)
			.setArmorDamageThreshold(4)
			.setArmorAbsorbtionPercent(0.20f)
			.setMass(6500f)
			.setMaxSpeed(0.6f)
			.setStealth(1.0f)
			.setCrossSecArea(7.5f)
			.setIdleHeat(6f)
			.setTurnRadius(11f)
			.setMaxTurnRates(0f, 0f, 4f)
			.setTurnTorques(0f, 0f, 1f)
			.setThrottleRate(0.05f, 0.05f)
			.setBasicEngineSounds(ModSounds.TANK_1, ModSounds.TANK_1)
			.setRotationalInertia(8, 12, 8)
			.setCrashExplosionRadius(3)
			.set3rdPersonCamDist(7)
			.setCarIsTank(false)
			.setCanNegativeThrottle(true)
			.addPilotSeatSlot(0.5, 0.9, 1)
			.addSeatSlot("seat2", -0.5, 0.9, 1)
			.addIngredient(ModItems.SEAT.getId(), 2)
			.addIngredient(ModItems.WHEEL.getId(), 6)
			.addIngredientTag("dscombat:aluminum_ingot", 24)
			.addEmptySlot("cargo_bed_1", SlotType.MOUNT_HEAVY, 0, 1, -2.75, 0, "cargo_bed")
			.addEmptySlot("frame_1", SlotType.MOUNT_TECH, 0, 2.95, 1, 0)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL, "cargo_bed")
			.addEmptySlot("internal_3", SlotType.INTERNAL, "cargo_bed")
			.addEmptySlot("internal_4", SlotType.TECH_INTERNAL)
			.setEntityMainHitboxSize(2.5f, 3)
			.setRootHitboxNoCollide(true)
			.addRotableHitbox("cage", 2, 2.25, 1.875, 0, 1.6875, 0.9375,
					0, 0, false, false, true)
			.addRotableHitbox("cargo_bed", 2, 0.5, 3.875, 0, 0.75, -1.9375,
					30, 30, false, true, false)
			.build();
	
	public static final VehicleStats UNARMED_AXCEL_TRUCK = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "axcel_truck_unarmed", EMPTY_AXCEL_TRUCK)
			.setCraftable()
			.addIngredient(ModItems.C12_ENGINE.getId())
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.build();
	
	public static final VehicleStats DEFAULT_AXCEL_TRUCK = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "axcel_truck", UNARMED_AXCEL_TRUCK)
			.addIngredient(ModItems.SAM_LAUNCHER.getId())
			.addIngredient(ModItems.AIR_SCAN_A.getId())
			.setSlotItem("frame_1", ModItems.AXCEL_TRUCK_RADAR.getId())
			.setSlotItem("cargo_bed_1", ModItems.SAM_LAUNCHER.getId(), "pac3", true)
			.build();

	public static final VehicleStats EMPTY_ERIC_TRUCK = VehicleStats.Builder
			.createCar(DSCombatMod.MODID, "eric_truck_empty")
			.setAssetId("eric_truck")
			.setSortFactor(6)
			.setItem(ModItems.VEHICLE.getId())
			.setMaxHealth(100)
			.setBaseArmor(150)
			.setArmorDamageThreshold(6)
			.setArmorAbsorbtionPercent(0.4f)
			.setMass(9600f)
			.setMaxSpeed(0.5f)
			.setStealth(0.9f)
			.setCrossSecArea(9f)
			.setIdleHeat(8f)
			.setTurnRadius(14f)
			.setMaxTurnRates(0f, 0f, 3f)
			.setTurnTorques(0f, 0f, 1f)
			.setThrottleRate(0.05f, 0.05f)
			.setBasicEngineSounds(ModSounds.TANK_1, ModSounds.TANK_1)
			.setBaseTextureNum(3)
			.setLayerTextureNum(3)
			.setDefultPassengerSoundPack(VehicleSoundManager.PassengerSoundPack.ENG_MALE_1)
			.setRotationalInertia(9, 14, 10)
			.setCrashExplosionRadius(3.5f)
			.set3rdPersonCamDist(10)
			.setCarIsTank(false)
			.setCanNegativeThrottle(true)
			.addPilotSeatSlot(1.05, 1.4, 1.1)
			.addSeatSlot("seat2", 0, 1.4, 0.7)
			.addSeatSlot("seat3", -1.05, 1.4, 1.1)
			.addIngredient(ModItems.SEAT.getId(), 3)
			.addIngredient(ModItems.LARGE_WHEEL.getId(), 8)
			.addIngredientTag("dscombat:aluminum_ingot", 40)
			.addIngredient("minecraft:gold_ingot", 10)
			.addEmptySlot("cargo_bed_1", SlotType.MOUNT_SUPER_HEAVY, 0, 3, -5.1, 0, "tech_platform")
			.addEmptySlot("frame_1", SlotType.MOUNT_TECH, 0, 3.4, 1, 0)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL, "cargo_bed")
			.addEmptySlot("internal_3", SlotType.INTERNAL, "cargo_bed")
			.addEmptySlot("internal_4", SlotType.INTERNAL, "cargo_bed")
			.addEmptySlot("internal_5", SlotType.INTERNAL, "cargo_bed")
			.addEmptySlot("internal_6", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_7", SlotType.TECH_INTERNAL)
			.addEmptySlot("hitch_1", SlotType.EXTERNAL_TOUGH, 0, 1.25, -7.4, 180, "cargo_bed")
			.setSlotOnlyCompatible("hitch_1", "chain_hook")
			.setEntityMainHitboxSize(3.2f, 3.2f)
			.setRootHitboxNoCollide(true)
			.addRotableHitbox("cage", 3.625, 2.25, 3.625, 0, 2.3125, 0.9375,
					0, 0, false, false, true)
			.addRotableHitbox("cargo_bed", 2.125, 0.375, 7.1875, 0, 1.3125, -4.53125,
					50, 50, false, true, false)
			.addRotableHitbox("tech_platform", 3.625, 0.9375, 5.8125, 0, 2.59375, -4.65625,
					40, 40, true, true, false)
			.addRotableHitbox("left_cargo_platform", 1.1875, 0.4375, 6.625, 1.5938, 1.6563, -4.625,
					20, 20, true, true, false)
			.addRotableHitbox("right_cargo_platform", 1.1875, 0.4375, 6.625, -1.5938, 1.6563, -4.625,
					20, 20, true, true, false)
			.addRotableHitbox("left_cage_platform", 0.9375, 0.125, 2.5625, 1.9688, 1.125, -0.0313,
					15, 15, true, true, false)
			.addRotableHitbox("right_cage_platform", 0.9375, 0.125, 2.5625, -1.9688, 1.125, -0.0313,
					15, 15, true, true, false)
			.addRotableHitbox("left_step", 0.5, 0.125, 0.6875, 2.5, 0.625, -0.5313,
					10, 10, true, true, false)
			.addRotableHitbox("right_step", 0.5, 0.125, 0.6875, -2.5, 0.625, -0.5313,
					10, 10, true, true, false)
			.addRotableHitbox("back_step", 2.25, 0.125, 0.6875, 0, 0.6875, -8.3438,
					10, 10, true, true, false)
			.build();

	public static final VehicleStats UNARMED_ERIC_TRUCK = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "eric_truck_unarmed", EMPTY_ERIC_TRUCK)
			.setCraftable()
			.addIngredient(ModItems.C12_ENGINE.getId())
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.addIngredient(ModItems.CHAIN_HOOK.getId())
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("hitch_1", ModItems.CHAIN_HOOK.getId())
			.build();

	public static final VehicleStats DEFAULT_ERIC_TRUCK = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "eric_truck", UNARMED_ERIC_TRUCK)
			.addIngredient(ModItems.MLRS.getId())
			.addIngredient(ModItems.AIR_SCAN_A.getId())
			.setSlotItem("frame_1", ModItems.AIR_SCAN_B.getId())
			.setSlotItem("cargo_bed_1", ModItems.MLRS.getId(), "pac3", true)
			.build();
}
