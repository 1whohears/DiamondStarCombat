package com.onewhohears.dscombat.data.aircraft.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.VehicleSoundManager.PassengerSoundPack;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class PlanePresets {
	
	public static final AircraftPreset EMPTY_WOODEN_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "wooden_plane_empty")
			.setSortFactor(0)
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.WOODEN_PLANE.getId())
			.setMaxHealth(40f)
			.setMass(500f)
			.setMaxSpeed(0.7f)
			.setStealth(1.0f)
			.setCrossSecArea(2f)
			.setIdleHeat(1f)
			.setBaseArmor(0f)
			.setTurnRadius(16f)
			.setMaxTurnRates(5f, 3.0f, 2.0f)
			.setTurnTorques(1.5f, 2.5f, 4.5f)
			.setThrottleRate(0.02f, 0.06f)
			.setPlaneWingArea(8f)
			.setBasicEngineSounds(ModSounds.BIPLANE_1, ModSounds.BIPLANE_1)
			.setRotationalInertia(4, 7, 3)
			.setCrashExplosionRadius(3)
			.set3rdPersonCamDist(4)
			.setPlaneLiftAOAGraph(LiftKGraph.WOODEN_PLANE_GRAPH)
			.setPlaneFlapDownAOABias(8)
			.setPlaneNoseCanAimDown(false)
			.addIngredientTag("minecraft:planks", 20)
			.addIngredient(ModItems.SEAT.getId())
			.addIngredient(ModItems.PROPELLER.getId())
			.addIngredient(ModItems.WHEEL.getId(), 2)
			.addPilotSeatSlot(0, -0.4, 0)
			.addEmptySlot("left_wing_1", SlotType.WING, 1.5, 0, 0, 180)
			.addEmptySlot("right_wing_1", SlotType.WING, -1.5, 0, 0, 180)
			.addEmptySlot("internal_1", SlotType.RADIAL_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.build();
	
	public static final AircraftPreset DEFAULT_WOODEN_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "wooden_plane", EMPTY_WOODEN_PLANE)
			.setCraftable()
			.addIngredient(ModItems.CM_MANLY_52.getId())
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.CM_MANLY_52.getId())
			.setSlotItem("internal_2", ModItems.LIGHT_FUEL_TANK.getId(), true)
			.build();
	
	public static final AircraftPreset BOMBER_WOODEN_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "wooden_plane_bomber", DEFAULT_WOODEN_PLANE)
			.setSlotItem("left_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("right_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.addIngredient("minecraft:tnt", 12)
			.addIngredient("dscombat:aluminum_ingot", 36)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.build();
	
	public static final AircraftPreset FIGHTER_WOODEN_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "wooden_plane_fighter", DEFAULT_WOODEN_PLANE)
			.setSlotItem("left_wing_1", ModItems.XM12.getId(), "20mm", true)
			.setSlotItem("right_wing_1", ModItems.XM12.getId(), "20mm", true)
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient(ModItems.XM12.getId(), 2)
			.build();
	
	public static final AircraftPreset EMPTY_E3SENTRY_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "e3sentry_plane_empty")
			.setSortFactor(15)
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.E3SENTRY_PLANE.getId())
			.setMaxHealth(400f)
			.setMass(83900f)
			.setMaxSpeed(1.0f)
			.setStealth(1.3f)
			.setCrossSecArea(16f)
			.setIdleHeat(20f)
			.setBaseArmor(4f)
			.setTurnRadius(30f)
			.setMaxTurnRates(3f, 2.0f, 2.0f)
			.setTurnTorques(2.0f, 2.0f, 2.0f)
			.setThrottleRate(0.01f, 0.04f)
			.setPlaneWingArea(283f)
			.setBasicEngineSounds(ModSounds.JET_1, ModSounds.JET_1)
			.setRotationalInertia(10, 12, 8)
			.setCrashExplosionRadius(8)
			.set3rdPersonCamDist(12)
			.setPlaneLiftAOAGraph(LiftKGraph.E3SENTRY_PLANE_GRAPH)
			.setPlaneFlapDownAOABias(10)
			.setPlaneNoseCanAimDown(false)
			.setDefultPassengerSoundPack(PassengerSoundPack.ENG_MALE_1)
			.addIngredient(ModItems.ADVANCED_COCKPIT.getId())
			.addIngredient(ModItems.SEAT.getId(), 11)
			.addIngredient(ModItems.LARGE_FUSELAGE.getId(), 2)
			.addIngredient(ModItems.LARGE_WING.getId(), 2)
			.addIngredient(ModItems.WHEEL.getId(), 6)
			.addIngredient("minecraft:light_gray_dye", 8)
			.addIngredient("minecraft:gold_ingot", 25)
			.addPilotSeatSlot(0.5, -1.35, 4.7)
			.addSeatSlot("seat1", -0.5, -1.35, 4.7)
			.addSeatSlot("seat2", 0.5, -1.35, 3)
			.addSeatSlot("seat3", 0.5, -1.35, 1.5)
			.addSeatSlot("seat4", 0.5, -1.35, 0)
			.addSeatSlot("seat5", 0.5, -1.35, -1.5)
			.addSeatSlot("seat6", 0.5, -1.35, -3)
			.addSeatSlot("seat7", -0.5, -1.35, 3)
			.addSeatSlot("seat8", -0.5, -1.35, 1.5)
			.addSeatSlot("seat9", -0.5, -1.35, 0)
			.addSeatSlot("seat10", -0.5, -1.35, -1.5)
			.addSeatSlot("seat11", -0.5, -1.35, -3)
			.addEmptySlot("left_wing_1", SlotType.WING, 4, -0.6, 0.5, 180)
			.addEmptySlot("right_wing_1", SlotType.WING, -4, -0.6, 0.5, 180)
			.addEmptySlot("left_wing_2", SlotType.WING, 6,-0.6, 0.5, 180)
			.addEmptySlot("right_wing_2", SlotType.WING, -6, -0.6, 0.5, 180)
			.addEmptySlot("frame_1", SlotType.HEAVY_FRAME, 0, 0.8, -1.3, 0)
			.addEmptySlot("internal_1", SlotType.INTERNAL)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.addEmptySlot("internal_5", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_6", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_7", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_8", SlotType.ADVANCED_INTERNAL)
			.addEntityScreen(EntityScreenIds.AIR_RADAR_SCREEN, 0, -0.85, 5.49, 0.7, 0.7)
			.addEntityScreen(EntityScreenIds.FUEL_SCREEN, 0.59, -0.59, 5.49, 0.15, 0.15)
			.addEntityScreen(EntityScreenIds.RWR_SCREEN, 0.83, -0.66, 5.49, 0.3, 0.3)
			.build();
	
	public static final AircraftPreset DEFAULT_E3SENTRY_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "e3sentry_plane", EMPTY_E3SENTRY_PLANE)
			.setCraftable()
			.addIngredient(ModItems.CFM56.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId(), 2)
			.addIngredient(ModItems.AR20K.getId())
			.addIngredient(ModItems.DATA_LINK.getId())
			.setSlotItem("left_wing_1", ModItems.CFM56.getId())
			.setSlotItem("right_wing_1", ModItems.CFM56.getId())
			.setSlotItem("frame_1", ModItems.AR20K.getId())
			.setSlotItem("internal_1", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_5", ModItems.DATA_LINK.getId())
			.build();
	
}
