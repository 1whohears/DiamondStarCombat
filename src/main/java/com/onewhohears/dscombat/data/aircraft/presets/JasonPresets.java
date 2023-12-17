package com.onewhohears.dscombat.data.aircraft.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.init.ModItems;

public class JasonPresets {
	
	public static final AircraftPreset EMPTY_JASON_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "jason_plane_empty")
			.setSortFactor(5)
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.JASON_PLANE.getId())
			.setMaxHealth(60f)
			.setMass(9f)
			.setMaxSpeed(0.85f)
			.setStealth(1f)
			.setCrossSecArea(3f)
			.setIdleHeat(1f)
			.setBaseArmor(0f)
			.setTurnRadius(10f)
			.setMaxTurnRates(5f, 3f, 2f)
			.setTurnTorques(2f, 2f, 3f)
			.setThrottleRate(0.04f, 0.07f)
			.setPlaneWingArea(11f)
			.addIngredient(ModItems.FUSELAGE.getId(), 1)
			.addIngredient(ModItems.WING.getId(), 2)
			.addIngredient(ModItems.COCKPIT.getId(), 1)
			.addIngredient(ModItems.WHEEL.getId(), 3)
			.addIngredient(ModItems.PROPELLER.getId(), 1)
			.addIngredient("minecraft:blue_dye", 4)
			.addPilotSeatSlot(0, 0.3, -0.15)
			.addEmptySlot("frame_1", SlotType.FRAME, 0, -0.45, 1.9, 180)
			.addEmptySlot("left_wing_1", SlotType.WING, 3.4, -0.4, 1.1, 180)
			.addEmptySlot("right_wing_1", SlotType.WING, -3.4, -0.4, 1.1, 180)
			.addEmptySlot("internal_1", SlotType.RADIAL_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.build();
	
	public static final AircraftPreset UNARMED_JASON_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "jason_plane_unarmed", EMPTY_JASON_PLANE)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.ALLISON_V_1710.getId())
			.setSlotItem("internal_2", ModItems.LIGHT_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.ALLISON_V_1710.getId())
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_JASON_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "jason_plane", UNARMED_JASON_PLANE)
			.setSlotItem("left_wing_1", ModItems.XM12.getId(), "10mm", true)
			.setSlotItem("right_wing_1", ModItems.XM12.getId(), "10mm", true)
			.build();
	
}
