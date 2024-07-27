package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class TankPresets {
	
	public static final VehicleStats EMPTY_MRBUDGER_TANK = VehicleStats.Builder
			.createCar(DSCombatMod.MODID, "mrbudger_tank_empty")
			.setAssetId("mrbudger_tank")
			.setSortFactor(4)
			.setItem(ModItems.MRBUDGER_TANK.getId())
			.setMaxHealth(200f)
			.setBaseArmor(300f)
			.setArmorDamageThreshold(8f)
			.setArmorAbsorbtionPercent(0.55f)
			.setMass(15000f)
			.setMaxSpeed(0.3f)
			.setStealth(1.0f)
			.setCrossSecArea(7.5f)
			.setIdleHeat(10f)
			.setTurnRadius(0f)
			.setMaxTurnRates(0f, 0f, 2f)
			.setTurnTorques(0f, 0f, 8f)
			.setThrottleRate(0.04f, 0.04f)
			.setBasicEngineSounds(ModSounds.TANK_1, ModSounds.TANK_1)
			.setRotationalInertia(8, 12, 8)
			.setCrashExplosionRadius(3)
			.set3rdPersonCamDist(4)
			.setCarIsTank(true)
			.setCanNegativeThrottle(true)
			.addIngredient(ModItems.SEAT.getId(), 4)
			.addIngredient(ModItems.TANK_TRACK.getId(), 4)
			.addIngredientTag("dscombat:aluminum_ingot", 30)
			.addIngredient("minecraft:gold_ingot", 5)
			.addSeatSlot(PartSlot.PILOT_SLOT_NAME, SlotType.MOUNT_HEAVY, 0, 1.4, 0)
			.addSeatSlot("seat1", SlotType.MOUNT_MED, 1, 1.4, 2)
			.addSeatSlot("seat2", SlotType.MOUNT_MED, -1, 1.4, 2)
			.addSeatSlot("seat3", SlotType.MOUNT_MED, 1, 1.4, -2)
			.addSeatSlot("seat4", SlotType.MOUNT_MED, -1, 1.4, -2)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.setEntityMainHitboxSize(3, 2.5f)
			.build();
	
	public static final VehicleStats UNARMED_MRBUDGER_TANK = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "mrbudger_tank_unarmed", EMPTY_MRBUDGER_TANK)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem(PartSlot.PILOT_SLOT_NAME, ModItems.HEAVY_TANK_TURRET.getId(), false)
			.addIngredient(ModItems.HEAVY_TANK_TURRET.getId())
			.addIngredient(ModItems.C12_ENGINE.getId(), 1)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.build();
	
	public static final VehicleStats DEFAULT_MRBUDGER_TANK = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "mrbudger_tank", UNARMED_MRBUDGER_TANK)
			.setSlotItem(PartSlot.PILOT_SLOT_NAME, ModItems.HEAVY_TANK_TURRET.getId(), true)
			.addIngredient(ModItems.B_120MMHE.getId(), 16)
			.build();
	
	public static final VehicleStats EMPTY_SMALL_ROLLER = VehicleStats.Builder
			.createCar(DSCombatMod.MODID, "small_roller_empty")
			.setAssetId("small_roller")
			.setSortFactor(1)
			.setItem(ModItems.SMALL_ROLLER.getId())
			.setMaxHealth(30f)
			.setBaseArmor(0f)
			.setArmorDamageThreshold(1f)
			.setArmorAbsorbtionPercent(0f)
			.setMass(600f)
			.setMaxSpeed(0.2f)
			.setStealth(1.0f)
			.setCrossSecArea(1.2f)
			.setIdleHeat(1f)
			.setTurnRadius(0f)
			.setMaxTurnRates(0f, 0f, 3f)
			.setTurnTorques(0f, 0f, 10f)
			.setThrottleRate(0.08f, 0.08f)
			.setBasicEngineSounds(ModSounds.TANK_1, ModSounds.TANK_1)
			.setRotationalInertia(8, 12, 8)
			.setCrashExplosionRadius(1)
			.set3rdPersonCamDist(4)
			.setCarIsTank(true)
			.setCanNegativeThrottle(true)
			.addIngredient(ModItems.SEAT.getId(), 1)
			.addIngredientTag("dscombat:aluminum_ingot", 5)
			.addIngredient(ModItems.TANK_TRACK.getId(), 2)
			.addSeatSlot(PartSlot.PILOT_SLOT_NAME, SlotType.MOUNT_MED, 0, 0.6, 0)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.setEntityMainHitboxSize(1.5f, 0.8f)
			.build();
	
	public static final VehicleStats UNARMED_SMALL_ROLLER = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "small_roller_unarmed", EMPTY_SMALL_ROLLER)
			.setCraftable()
			.addIngredient(ModItems.C6_ENGINE.getId())
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.C6_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.LIGHT_FUEL_TANK.getId(), true)
			.build();
	
	public static final VehicleStats DEFAULT_SMALL_ROLLER = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "small_roller", UNARMED_SMALL_ROLLER)
			.setSlotItem(PartSlot.PILOT_SLOT_NAME, ModItems.STEVE_UP_SMASH.getId(), "aim9p5", true)
			.addIngredient(ModItems.STEVE_UP_SMASH.getId())
			.addIngredient(ModItems.AIM9P5.getId(), 4)
			.build();
	
	public static final VehicleStats TANK_SMALL_ROLLER = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "small_roller_tank", UNARMED_SMALL_ROLLER)
			.setSlotItem(PartSlot.PILOT_SLOT_NAME, ModItems.HEAVY_TANK_TURRET.getId(), true)
			.addIngredient(ModItems.HEAVY_TANK_TURRET.getId())
			.addIngredient(ModItems.B_120MMHE.getId(), 16)
			.build();
	
}
