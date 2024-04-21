package com.onewhohears.dscombat.data.aircraft.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class JasonPresets {
	
	public static final AircraftPreset EMPTY_JASON_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "jason_plane_empty")
			.setSortFactor(2)
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.JASON_PLANE.getId())
			.setMaxHealth(60f)
			.setMass(1100f)
			.setMaxSpeed(0.8f)
			.setStealth(1f)
			.setCrossSecArea(3f)
			.setIdleHeat(1.5f)
			.setBaseArmor(0f)
			.setTurnRadius(10f)
			.setMaxTurnRates(5f, 3f, 2f)
			.setTurnTorques(2f, 2f, 3f)
			.setThrottleRate(0.04f, 0.07f)
			.setPlaneWingArea(15f)
			.setBasicEngineSounds(ModSounds.BIPLANE_1, ModSounds.BIPLANE_1)
			.setRotationalInertia(5, 9, 3)
			.setCrashExplosionRadius(3)
			.set3rdPersonCamDist(8)
			.setPlaneLiftAOAGraph(LiftKGraph.WOODEN_PLANE_GRAPH)
			.setPlaneFlapDownAOABias(8)
			.setPlaneNoseCanAimDown(false)
			.setBaseTextureNum(3)
			.setLayerTextureNum(3)
			.addIngredientTag("minecraft:planks", 30)
			.addIngredientTag("dscombat:aluminum_ingot", 3)
			.addIngredient(ModItems.WING.getId(), 2)
			.addIngredient(ModItems.COCKPIT.getId(), 1)
			.addIngredient(ModItems.WHEEL.getId(), 3)
			.addIngredient(ModItems.PROPELLER.getId(), 1)
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
