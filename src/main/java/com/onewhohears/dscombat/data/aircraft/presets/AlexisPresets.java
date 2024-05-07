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

public class AlexisPresets {
	
	public static final AircraftPreset EMPTY_ALEXIS_PLANE = AircraftPreset.Builder
			.create(DSCombatMod.MODID, "alexis_plane_empty")
			.setSortFactor(10)
			.setAircraftType(AircraftType.PLANE)
			.setItem(ModItems.ALEXIS_PLANE.getId())
			.setMaxHealth(140f)
			.setMass(8500f)
			.setMaxSpeed(1.4f)
			.setStealth(0.9f)
			.setCrossSecArea(4f)
			.setIdleHeat(4f)
			.setBaseArmor(2f)
			.setTurnRadius(10f)
			.setMaxTurnRates(6f, 2.5f, 1.5f)
			.setTurnTorques(1.5f, 2f, 4f)
			.setThrottleRate(0.04f, 0.08f)
			.setPlaneWingArea(28f)
			.setFighterJetSounds(ModSounds.ALEXIS_EXT_AFTERBURNER_CLOSE, ModSounds.ALEXIS_EXT_AFTERBURNER_FAR,
					ModSounds.ALEXIS_EXT_RPM, ModSounds.ALEXIS_EXT_WIND_CLOSE, ModSounds.ALEXIS_EXT_WIND_FAR,
					ModSounds.ALEXIS_CP_RPM, ModSounds.ALEXIS_CP_AFTERBURNER, ModSounds.ALEXIS_CP_WIND_SLOW, 
					ModSounds.ALEXIS_CP_WIND_FAST)
			.setRotationalInertia(4, 8, 2)
			.setCrashExplosionRadius(5)
			.set3rdPersonCamDist(17)
			.setPlaneLiftAOAGraph(LiftKGraph.ALEXIS_PLANE_GRAPH)
			.setPlaneFlapDownAOABias(8)
			.setPlaneNoseCanAimDown(false)
			.setBaseTextureNum(2)
			.setLayerTextureNum(2)
			.setDefultPassengerSoundPack(PassengerSoundPack.ENG_NON_BINARY_GOOBER)
			.addIngredient(ModItems.FUSELAGE.getId(), 1)
			.addIngredient(ModItems.WING.getId(), 3)
			.addIngredient(ModItems.ADVANCED_COCKPIT.getId())
			.addIngredient(ModItems.WHEEL.getId(), 3)
			.addIngredient("minecraft:gold_ingot", 24)
			.addPilotSeatSlot(0, 0.1, 6.5)
			.addEmptySlot("left_wing_1", SlotType.WING, 2.3, -0.08, -0.1, 180)
			.addEmptySlot("left_wing_2", SlotType.WING, 3.6, -0.08, -0.7, 180)
			.addEmptySlot("left_wing_3", SlotType.WING, 4.95, -0.08, -1.1, 180)
			.addEmptySlot("left_wing_4", SlotType.WING, 6.05, 0.4, -1.5, -90)
			.addEmptySlot("right_wing_1", SlotType.WING, -2.3, -0.08, -0.1, 180)
			.addEmptySlot("right_wing_2", SlotType.WING, -3.6, -0.08, -0.7, 180)
			.addEmptySlot("right_wing_3", SlotType.WING, -4.95, -0.08, -1.1, 180)
			.addEmptySlot("right_wing_4", SlotType.WING, -6.05, 0.4, -1.5, 90)
			.addEmptySlot("frame_1", SlotType.FRAME, 0, -0.15, 8, 180)
			.addEmptySlot("internal_1", SlotType.PUSH_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_5", SlotType.ADVANCED_INTERNAL)
			.addEmptySlot("internal_6", SlotType.ADVANCED_INTERNAL)
			.addEntityScreen(EntityScreenIds.AIR_RADAR_SCREEN, 0.225, 0.798, 7.195, 0.15, 0.15)
			.addEntityScreen(EntityScreenIds.FUEL_SCREEN, -0.265, 0.948, 7.195, 0.07, 0.07)
			.addEntityScreen(EntityScreenIds.RWR_SCREEN, 0.19, 0.974, 7.195, 0.13, 0.13)
			.addEntityScreen(EntityScreenIds.GROUND_RADAR_SCREEN, -0.225, 0.798, 7.195, 0.15, 0.15)
			.addHUDScreen(0, 0.1, 6.5)
			.addAfterBurnerSmokePos(0,0.3,-6.5)
			.build();
	
	public static final AircraftPreset UNARMED_ALEXIS_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane_unarmed", EMPTY_ALEXIS_PLANE)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.TURBOFAN_F145.getId())
			.setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.TURBOFAN_F145.getId())
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.build();
	
	public static final AircraftPreset DEFAULT_ALEXIS_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane", UNARMED_ALEXIS_PLANE)
			// actual weapon costs: 63 copper, 88 iron, 130 gunpowder, 140 redstone
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("frame_1", ModItems.XM12.getId(), "20mm", true)
			.setSlotItem("internal_4", ModItems.AR2K.getId())
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.XM12.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 63)
			.addIngredient("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
	public static final AircraftPreset SUPPORT_ALEXIS_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane_support", UNARMED_ALEXIS_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
			.setSlotItem("left_wing_3", ModItems.GIMBAL_CAMERA.getId())
			.setSlotItem("left_wing_4", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm88g", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65g", true)
			.setSlotItem("right_wing_3", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("right_wing_4", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("frame_1", ModItems.XM12.getId(), "20mmhe", true)
			.setSlotItem("internal_4", ModItems.AR2K.getId())
			.setSlotItem("internal_5", ModItems.GR400.getId())
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.GR400.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 5)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.XM12.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.setDefaultBaseTexture(1)
			.build();
	
	public static final AircraftPreset SNIPER_ALEXIS_PLANE = AircraftPreset.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane_sniper", UNARMED_ALEXIS_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "meteor", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("left_wing_3", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "meteor", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("right_wing_3", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("frame_1", ModItems.XM12.getId(), "20mm", true)
			.setSlotItem("internal_4", ModItems.AR2K.getId())
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 4)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 1)
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.setDefaultBaseTexture(1)
			.build();
	
}
