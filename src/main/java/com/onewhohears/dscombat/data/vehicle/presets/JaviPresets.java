package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.LiftKGraph;
import com.onewhohears.dscombat.data.vehicle.VehicleSoundManager.PassengerSoundPack;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class JaviPresets {
	
	public static final VehicleStats EMPTY_JAVI_PLANE = VehicleStats.Builder
			.createPlane(DSCombatMod.MODID, "javi_plane_empty")
			.setAssetId("javi_plane")
			.setSortFactor(8)
			.setItem(ModItems.JAVI_PLANE.getId())
			.setMaxHealth(150f)
			.setBaseArmor(100f)
			.setArmorDamageThreshold(7f)
			.setArmorAbsorbtionPercent(0.30f)
			.setMass(11300f)
			.setMaxSpeed(1.05f)
			.setStealth(1.0f)
			.setCrossSecArea(7f)
			.setIdleHeat(5f)
			.setTurnRadius(12f)
			.setMaxTurnRates(4f, 2.1f, 1.1f)
			.setTurnTorques(1.5f, 2.5f, 4.5f)
			.setThrottleRate(0.04f, 0.08f)
			.setPlaneWingArea(47f)
			.setFighterJetSounds(ModSounds.ALEXIS_EXT_AFTERBURNER_CLOSE, ModSounds.ALEXIS_EXT_AFTERBURNER_FAR,
					ModSounds.ALEXIS_EXT_RPM, ModSounds.ALEXIS_EXT_WIND_CLOSE, ModSounds.ALEXIS_EXT_WIND_FAR,
					ModSounds.ALEXIS_CP_RPM, ModSounds.ALEXIS_CP_AFTERBURNER, ModSounds.ALEXIS_CP_WIND_SLOW, 
					ModSounds.ALEXIS_CP_WIND_FAST)
			.setRotationalInertia(6, 10, 4)
			.setCrashExplosionRadius(5)
			.set3rdPersonCamDist(16)
			.setPlaneLiftAOAGraph(LiftKGraph.JAVI_PLANE_GRAPH)
			.setPlaneFlapDownAOABias(10)
			.setPlaneNoseCanAimDown(true)
			.setBaseTextureNum(2)
			.setLayerTextureNum(2)
			.setDefultPassengerSoundPack(PassengerSoundPack.ENG_NON_BINARY_GOOBER)
			.addIngredient(ModItems.FUSELAGE.getId(), 1)
			.addIngredient(ModItems.LARGE_WING.getId(), 2)
			.addIngredient(ModItems.ADVANCED_COCKPIT.getId(), 1)
			.addIngredient(ModItems.WHEEL.getId(), 3)
			.addIngredient("minecraft:gold_ingot", 10)
			.addPilotSeatSlot(0, 0.5, 6.34375)
			.addEmptySlot("left_wing_1", SlotType.PYLON_HEAVY, 1.96875, -0.28125, 1, 180, "left_wing")
			.addEmptySlot("left_wing_2", SlotType.PYLON_HEAVY, 4.25, -0.28125, 1, 180, "left_wing")
			.addEmptySlot("left_wing_3", SlotType.PYLON_MED, 5.625, -0.28125, 1, 180, "left_wing") 
			.addEmptySlot("left_wing_4", SlotType.PYLON_MED, 6.96875, -0.28125, 1, 180, "left_wing") 
			.addEmptySlot("right_wing_1", SlotType.PYLON_HEAVY, -1.96875, -0.28125, 1, 180, "right_wing")
			.addEmptySlot("right_wing_2", SlotType.PYLON_HEAVY, -4.25, -0.28125, 1, 180, "right_wing")
			.addEmptySlot("right_wing_3", SlotType.PYLON_MED, -5.625, -0.28125, 1, 180, "right_wing") 
			.addEmptySlot("right_wing_4", SlotType.PYLON_MED, -6.96875, -0.28125, 1, 180, "right_wing") 
			.addEmptySlot("internal_gun", SlotType.INTERNAL_GUN, 0, -0.34375, 8.65625, 180)
			.setSlotOnlyCompatible("internal_gun", "gau_avenger")
			.addEmptySlot("frame_1", SlotType.PYLON_HEAVY, 0, -0.71875, 1.03125, 180)
			.addEmptySlot("internal_1", SlotType.PUSH_ENGINE, "left_engine")
			.addEmptySlot("internal_2", SlotType.PUSH_ENGINE, "right_engine")
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_5", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_6", SlotType.TECH_INTERNAL)
			.addAfterBurnerSmokePos(1.85,1.4,-4.4)
			.addAfterBurnerSmokePos(-1.85,1.4,-4.4)
			.setEntityMainHitboxSize(4, 4)
			.setRootHitboxNoCollide(true)
			.addRotableHitbox("fuselage", 1.5, 1.5, 14, 0, 0.5, 1, 
					0, 0, false, false, true)
			.addRotableHitbox("left_wing", 8.8, 0.3, 2.5, 5.3, 0.35, 0.25, 
					25, 25, true, true, false)
			.addRotableHitbox("right_wing", 8.8, 0.3, 2.5, -5.3, 0.35, 0.25, 
					25, 25, true, true, false)
			.addRotableHitbox("tail", 6.9, 0.2, 1.9, 0, 0.6, -6.85, 
					10, 10, true, true, false)
			.addRotableHitbox("left_engine", 1.5, 1.5, 3.2, 1.85, 1.25, -3, 
					20, 20, true, true, false)
			.addRotableHitbox("right_engine", 1.5, 1.5, 3.2, -1.85, 1.25, -3, 
					20, 20, true, true, false)
			.build();
	
	public static final VehicleStats UNARMED_JAVI_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "javi_plane_unarmed", EMPTY_JAVI_PLANE)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.TURBOFAN_F25.getId())
			.setSlotItem("internal_2", ModItems.TURBOFAN_F25.getId())
			.setSlotItem("internal_3", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_gun", "gau_avenger", "30mmhe", false)
			.addIngredient(ModItems.INTERNAL_GUN.getId())
			.addIngredient(ModItems.TURBOFAN_F25.getId(), 2)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.build();
	
	public static final VehicleStats DEFAULT_JAVI_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "javi_plane", UNARMED_JAVI_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("left_wing_3", ModItems.GIMBAL_CAMERA.getId())
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm88g", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
			.setSlotItem("internal_gun", "gau_avenger", "30mmhe", true)
			.setSlotItem("frame_1", ModItems.BOMB_RACK.getId(), "anm57", true)
			.setSlotItem("internal_5", ModItems.GR400.getId())
			.setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.GR400.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.BOMB_RACK.getId())
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredientTag("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
	public static final VehicleStats BOMBER_JAVI_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "javi_plane_bomber", UNARMED_JAVI_PLANE)
			.setSlotItem("left_wing_1", ModItems.BOMB_RACK.getId(), "anm30", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.setSlotItem("left_wing_3", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("left_wing_4", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9l", true)
			.setSlotItem("right_wing_1", ModItems.BOMB_RACK.getId(), "anm64", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65g", true)
			.setSlotItem("right_wing_3", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
			.setSlotItem("right_wing_4", ModItems.GIMBAL_CAMERA.getId())
			.setSlotItem("internal_gun", "gau_avenger", "30mmhe", true)
			.setSlotItem("frame_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm88g", true)
			.setSlotItem("internal_5", ModItems.GPR20.getId())
			.setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.GPR20.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 4)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.BOMB_RACK.getId(), 2)
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.setDefaultBaseTexture(1)
			.build();
	
	public static final VehicleStats TRUCK_JAVI_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "javi_plane_truck", UNARMED_JAVI_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7mh", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7f", true)
			.setSlotItem("left_wing_3", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("left_wing_4", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7mh", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7f", true)
			.setSlotItem("right_wing_3", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("right_wing_4", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("internal_gun", "gau_avenger", "30mmhe", true)
			.setSlotItem("frame_1", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
			.setSlotItem("internal_5", ModItems.AR2K.getId())
			.setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 8)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 1)
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.setDefaultBaseTexture(1)
			.build();
	
}
