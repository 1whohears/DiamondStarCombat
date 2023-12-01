package com.onewhohears.dscombat.data.aircraft.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.init.ModItems;

public class FelixPresets {
	
	public static final AircraftPreset EMPTY_FELIX_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "felix_plane_empty")
			.setSortFactor(10)
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.FELIX_PLANE.getId())
			.setMaxHealth(110f)
			.setMass(18f)
			.setMaxSpeed(1.4f)
			.setStealth(0.95f)
			.setCrossSecArea(5f)
			.setIdleHeat(6f)
			.setBaseArmor(2f)
			.setTurnRadius(12f)
			.setMaxTurnRates(5f, 2.15f, 1.15f)
			.setTurnTorques(1f, 2f, 4f)
			.setThrottleRate(0.04f, 0.08f)
			.setPlaneWingArea(15f)
			.addIngredient(ModItems.FUSELAGE.getId(), 1)
			.addIngredient(ModItems.WING.getId(), 2)
			.addIngredient(ModItems.COCKPIT.getId())
			.addIngredient(ModItems.WHEEL.getId(), 3)
			.addIngredient("minecraft:light_gray_dye", 5)
			.addIngredient("minecraft:gold_ingot", 5)
			.addPilotSeatSlot(0, 0.1, 4.5)
			.addEmptySlot("left_wing_1", SlotType.WING, 2.3, -0.08, -2.1, 180)
			.addEmptySlot("left_wing_2", SlotType.WING, 3.6, -0.08, -2.7, 180)
			.addEmptySlot("right_wing_1", SlotType.WING, -2.3, -0.08, -2.1, 180)
			.addEmptySlot("right_wing_2", SlotType.WING, -3.6, -0.08, -2.7, 180)
			.addEmptySlot("frame_1", SlotType.FRAME, 0, -0.15, 6, 180)
			.addEmptySlot("nose_1", SlotType.FRAME, 0, -0.15, 8, 180)
			.addEmptySlot("internal_1", SlotType.PUSH_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.ADVANCED_INTERNAL)
			.build();
	
	public static final AircraftPreset UNARMED_FELIX_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "felix_plane_unarmed", EMPTY_FELIX_PLANE)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.TURBOFAN_F145.getId())
			.setSlotItem("internal_2", ModItems.LIGHT_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.TURBOFAN_F145.getId())
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_FELIX_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "felix_plane", UNARMED_FELIX_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7f", true)
			.setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9l", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7mh", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9l", true)
			.setSlotItem("nose_1", ModItems.XM12.getId(), "15mm", true)
			.setSlotItem("internal_4", ModItems.AR500.getId())
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR500.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.XM12.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.build();
	
}
