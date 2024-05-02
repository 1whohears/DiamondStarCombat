package com.onewhohears.dscombat.data.aircraft.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.data.aircraft.VehicleSoundManager.PassengerSoundPack;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class FelixPresets {
	
	public static final AircraftPreset EMPTY_FELIX_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "felix_plane_empty")
			.setSortFactor(7)
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.FELIX_PLANE.getId())
			.setMaxHealth(120f)
			.setMass(5200f)
			.setMaxSpeed(1.25f)
			.setStealth(0.95f)
			.setCrossSecArea(4f)
			.setIdleHeat(6f)
			.setBaseArmor(2f)
			.setTurnRadius(12f)
			.setMaxTurnRates(5f, 2.15f, 1.15f)
			.setTurnTorques(1f, 2f, 4f)
			.setThrottleRate(0.04f, 0.08f)
			.setPlaneWingArea(23f)
			.setFighterJetSounds(ModSounds.ALEXIS_EXT_AFTERBURNER_CLOSE, ModSounds.ALEXIS_EXT_AFTERBURNER_FAR,
					ModSounds.ALEXIS_EXT_RPM, ModSounds.ALEXIS_EXT_WIND_CLOSE, ModSounds.ALEXIS_EXT_WIND_FAR,
					ModSounds.ALEXIS_CP_RPM, ModSounds.ALEXIS_CP_AFTERBURNER, ModSounds.ALEXIS_CP_WIND_SLOW, 
					ModSounds.ALEXIS_CP_WIND_FAST)
			.setRotationalInertia(4.5f, 9, 3)
			.setCrashExplosionRadius(5)
			.set3rdPersonCamDist(17)
			.setPlaneLiftAOAGraph(LiftKGraph.ALEXIS_PLANE_GRAPH)
			.setPlaneFlapDownAOABias(8)
			.setPlaneNoseCanAimDown(false)
			.setBaseTextureNum(2)
			.setLayerTextureNum(3)
			.setDefultPassengerSoundPack(PassengerSoundPack.ENG_NON_BINARY_GOOBER)
			.addIngredient(ModItems.FUSELAGE.getId(), 1)
			.addIngredient(ModItems.WING.getId(), 2)
			.addIngredient(ModItems.ADVANCED_COCKPIT.getId())
			.addIngredient(ModItems.WHEEL.getId(), 3)
			.addPilotSeatSlot(0, -0.3, 4.7)
			.addEmptySlot("left_wing_1", SlotType.WING, 2.4, -0.64, -0.05, 180)
			.addEmptySlot("left_wing_2", SlotType.WING, 3.4, -0.64, -1.3, 180)
			.addEmptySlot("right_wing_1", SlotType.WING, -2.4, -0.64, -0.05, 180)
			.addEmptySlot("right_wing_2", SlotType.WING, -3.4, -0.64, -1.3, 180)
			.addEmptySlot("frame_1", SlotType.FRAME, 0, -1.07, 1.2, 180)
			.addEmptySlot("nose_1", SlotType.FRAME, 0, -0.8, 3.6, 180)
			.addEmptySlot("internal_1", SlotType.PUSH_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.ADVANCED_INTERNAL)
			.addEntityScreen(EntityScreenIds.AIR_RADAR_SCREEN,0.294,0.434,5.425,0.195,0.195,10)
			.addEntityScreen(EntityScreenIds.FUEL_SCREEN,-0.39,0.135,5.155,0.1,0.1,10)
			.addEntityScreen(EntityScreenIds.GROUND_RADAR_SCREEN,-0.31,0.434,5.425,0.195,0.195,10)
			.addEntityScreen(EntityScreenIds.RWR_SCREEN,0.0775,0.505,5.44,0.11,0.11,10)
			.addHUDScreen(0, -0.3, 4.7)
			.addAfterBurnerSmokePos(0,-0.1,-6)
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
	
	public static final AircraftPreset AIR_SUPPORT_FELIX_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "felix_plane_support", UNARMED_FELIX_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
			.setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9l", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65g", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("frame_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65g", true)
			.setSlotItem("nose_1", ModItems.XM12.getId(), "15mm", true)
			.setSlotItem("internal_4", ModItems.GR200.getId())
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.setDefaultBaseTexture(1)
			.build();
	
}
