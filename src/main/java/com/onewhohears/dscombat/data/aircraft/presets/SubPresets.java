package com.onewhohears.dscombat.data.aircraft.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class SubPresets {
	
	public static final AircraftPreset EMPTY_ANDOLF_SUB = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "andolf_sub_empty")
			.setSortFactor(5)
			.setAircraftType(AircraftType.SUBMARINE)
			.setItem(ModItems.ANDOLF_SUB.getId())
			.setMaxHealth(800f)
			.setMass(180000f)
			.setMaxSpeed(0.5f)
			.setStealth(0.05f)
			.setCrossSecArea(18f)
			.setIdleHeat(10f)
			.setBaseArmor(20f)
			.setTurnRadius(0f)
			.setMaxTurnRates(2f, 2f, 2f)
			.setTurnTorques(1f, 1f, 1f)
			.setThrottleRate(0.04f, 0.04f)
			.setEngineSounds(ModSounds.SUB_1, ModSounds.SUB_1)
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
			.addEmptySlot("nose_1", SlotType.FRAME, 0, -1.6, 3.5, 180)
			.addEmptySlot("frame_1", SlotType.FRAME, 2, 0, 1, -90)
			.addEmptySlot("frame_2", SlotType.FRAME, 2, 0, 0, -90)
			.addEmptySlot("frame_3", SlotType.FRAME, 2, 0, -1, -90)
			.addEmptySlot("frame_4", SlotType.FRAME, -2, 0, 1, 90)
			.addEmptySlot("frame_5", SlotType.FRAME, -2, 0, 0, 90)
			.addEmptySlot("frame_6", SlotType.FRAME, -2, 0, -1, 90)
			.addEmptySlot("internal_1", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_2", SlotType.SPIN_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_5", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_6", SlotType.ADVANCED_INTERNAL)
			.build();
			
	public static final AircraftPreset UNARMED_ANDOLF_SUB = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "andolf_sub_unarmed", EMPTY_ANDOLF_SUB)
			.setCraftable()
			.addIngredient(ModItems.C12_ENGINE.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_2", ModItems.C12_ENGINE.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.build();
	
	public static final AircraftPreset DEFAULT_ANDOLF_SUB = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "andolf_sub", UNARMED_ANDOLF_SUB)
			.addIngredient(ModItems.WR1K.getId())
			.setSlotItem("internal_4", ModItems.WR1K.getId())
			.setSlotItem("nose_1", ModItems.XM12.getId(), "20mm", true)
			.setSlotItem("frame_1", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.setSlotItem("frame_2", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.setSlotItem("frame_3", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.setSlotItem("frame_4", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.setSlotItem("frame_5", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.setSlotItem("frame_6", ModItems.HEAVY_MISSILE_RACK.getId(), "torpedo1", true)
			.addIngredient("minecraft:copper_ingot", 63)
			.addIngredient("minecraft:iron_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 6)
			.addIngredient(ModItems.XM12.getId(), 1)
			.build();
	
}
