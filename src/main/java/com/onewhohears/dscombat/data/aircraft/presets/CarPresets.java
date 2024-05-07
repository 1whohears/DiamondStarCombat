package com.onewhohears.dscombat.data.aircraft.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class CarPresets {
	
	public static final AircraftPreset DEFAULT_ORANGE_TESLA = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "orange_tesla")
			.setSortFactor(10)
			.setAircraftType(AircraftType.CAR)
			.setItem(ModItems.ORANGE_TESLA.getId())
			.setCraftable()
			.setMaxHealth(40f)
			.setMass(3000f)
			.setMaxSpeed(0.6f)
			.setStealth(1.0f)
			.setCrossSecArea(5.375f)
			.setIdleHeat(4f)
			.setBaseArmor(1f)
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
			.build();
	
	public static final AircraftPreset EMPTY_AXCEL_TRUCK = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "axcel_truck_empty")
			.setSortFactor(5)
			.setAircraftType(AircraftType.CAR)
			.setItem(ModItems.AXCEL_TRUCK.getId())
			.setMaxHealth(80f)
			.setMass(6500f)
			.setMaxSpeed(0.5f)
			.setStealth(1.0f)
			.setCrossSecArea(7.5f)
			.setIdleHeat(6f)
			.setBaseArmor(8f)
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
			.addEmptySlot("cargo_bed_1", SlotType.HEAVY_TURRET, 0, 1, -2.75, 0)
			.addEmptySlot("frame_1", SlotType.ADVANCED_FRAME, 0, 2.95, 1, 0)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_4", SlotType.ADVANCED_INTERNAL)
			.addEntityScreen(EntityScreenIds.AIR_RADAR_SCREEN, 0, 1.35, 1.6, 0.6, 0.6)
			.build();
	
	public static final AircraftPreset UNARMED_AXCEL_TRUCK = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "axcel_truck_unarmed", EMPTY_AXCEL_TRUCK)
			.setCraftable()
			.addIngredient(ModItems.C12_ENGINE.getId())
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.build();
	
	public static final AircraftPreset DEFAULT_AXCEL_TRUCK = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "axcel_truck", UNARMED_AXCEL_TRUCK)
			.addIngredient(ModItems.SAM_LAUNCHER.getId())
			.addIngredient(ModItems.AIR_SCAN_A.getId())
			.setSlotItem("frame_1", ModItems.AXCEL_TRUCK_RADAR.getId())
			.setSlotItem("cargo_bed_1", ModItems.SAM_LAUNCHER.getId(), true)
			.build();
	
}
