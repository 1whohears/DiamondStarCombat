package com.onewhohears.dscombat.data.aircraft.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.init.ModItems;

public class BroncoPresets {
	
	public static final AircraftPreset EMPTY_BRONCO_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "bronco_plane_empty")
			.setSortFactor(5)
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.BRONCO_PLANE.getId())
			.setMaxHealth(70f)
			.setMass(15f)
			.setMaxSpeed(1.05f)
			.setStealth(1f)
			.setCrossSecArea(5f)
			.setIdleHeat(3f)
			.setBaseArmor(1f)
			.setTurnRadius(8f)
			.setMaxTurnRates(2.5f, 1.2f, 0.9f)
			.setTurnTorques(1.5f, 2f, 3f)
			.setThrottleRate(0.03f, 0.07f)
			.setPlaneWingArea(16f)
			.addIngredient(ModItems.FUSELAGE.getId(), 1)
			.addIngredient(ModItems.WING.getId(), 3)
			.addIngredient(ModItems.COCKPIT.getId(), 2)
			.addIngredient(ModItems.WHEEL.getId(), 3)
			.addIngredient(ModItems.PROPELLER.getId(), 2)
			.addIngredient("minecraft:green_dye", 6)
			.addIngredient("minecraft:gold_ingot", 4)
			.addPilotSeatSlot(0, -0.44, 2.5)
			.addSeatSlot("seat2", SlotType.LIGHT_TURRET, 0, -0.31, 1.125)
			.addEmptySlot("nose_1", SlotType.FRAME, 0, -0.9357, 4.0625, 180)
			.addEmptySlot("frame_1", SlotType.FRAME, 0.5, -1.25, -0.8125, 180)
			.addEmptySlot("frame_2", SlotType.FRAME, 1.125, -1.375, -0.8125, 180)
			.addEmptySlot("frame_3", SlotType.FRAME, -0.5, -1.25, -0.8125, 180)
			.addEmptySlot("frame_4", SlotType.FRAME, -1.125, -1.375, -0.8125, 180)
			.addEmptySlot("left_wing_1", SlotType.WING, 4.875, 0.125, -0.9375, 180)
			.addEmptySlot("right_wing_1", SlotType.WING, -4.875, 0.125, -0.9375, 180)
			.addEmptySlot("internal_1", SlotType.RADIAL_ENGINE)
			.addEmptySlot("internal_2", SlotType.RADIAL_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.addEmptySlot("internal_5", SlotType.ADVANCED_INTERNAL)
			.build();
	
	public static final AircraftPreset UNARMED_BRONCO_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "bronco_plane_unarmed", EMPTY_BRONCO_PLANE)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.CM_MANLY_52.getId())
			.setSlotItem("internal_2", ModItems.CM_MANLY_52.getId())
			.setSlotItem("internal_3", ModItems.LIGHT_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.CM_MANLY_52.getId(), 2)
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_BRONCO_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "bronco_plane", UNARMED_BRONCO_PLANE)
			.setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.setSlotItem("left_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("right_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm65l", true)
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.build();
	
}
