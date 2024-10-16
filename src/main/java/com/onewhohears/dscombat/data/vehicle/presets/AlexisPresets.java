package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.VehicleSoundManager.PassengerSoundPack;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class AlexisPresets {
	
	public static final VehicleStats EMPTY_ALEXIS_PLANE = VehicleStats.Builder
			.createPlane(DSCombatMod.MODID, "alexis_plane_empty")
			.setAssetId("alexis_plane")
			.setSortFactor(10)
			.setItem(ModItems.ALEXIS_PLANE.getId())
			.setMaxHealth(100f)
			.setMass(8500f)
			.setMaxSpeed(1.4f)
			.setStealth(0.9f)
			.setCrossSecArea(4f)
			.setIdleHeat(4f)
			.setBaseArmor(40f)
			.setArmorDamageThreshold(2f)
			.setArmorAbsorbtionPercent(0.135f)
			.setTurnRadius(10f)
			.setMaxTurnRates(6f, 2.5f, 1.5f)
			.setTurnTorques(1.5f, 2f, 4f)
			.setThrottleRate(0.04f, 0.08f)
			.setPlaneWingArea(28f)
			.setFuselageLiftArea(18)
			.setMaxAltitude(630)
			.setFighterJetSounds(ModSounds.ALEXIS_EXT_AFTERBURNER_CLOSE, ModSounds.ALEXIS_EXT_AFTERBURNER_FAR,
					ModSounds.ALEXIS_EXT_RPM, ModSounds.ALEXIS_EXT_WIND_CLOSE, ModSounds.ALEXIS_EXT_WIND_FAR,
					ModSounds.ALEXIS_CP_RPM, ModSounds.ALEXIS_CP_AFTERBURNER, ModSounds.ALEXIS_CP_WIND_SLOW, 
					ModSounds.ALEXIS_CP_WIND_FAST)
			.setRotationalInertia(4, 8, 2)
			.setCrashExplosionRadius(5)
			.set3rdPersonCamDist(17)
			.setPlaneLiftAOAGraph("alexis_plane")
			.setTurnRateGraph("alexis_plane_turn_rates")
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
			.addPilotSeatSlot(0, 0.1, 6.5, true)
			.addEmptySlot("left_wing_1", SlotType.PYLON_MED, 2.3, -0.08, -0.1, 180, "left_wing")
			.addEmptySlot("left_wing_2", SlotType.PYLON_MED, 3.6, -0.08, -0.7, 180, "left_wing")
			.addEmptySlot("left_wing_3", SlotType.PYLON_LIGHT, 4.95, -0.08, -1.1, 180, "left_wing")
			.addEmptySlot("left_wing_4", SlotType.PYLON_LIGHT, 6.05, 0.4, -1.5, -90, "left_wing")
			.addEmptySlot("right_wing_1", SlotType.PYLON_MED, -2.3, -0.08, -0.1, 180, "right_wing")
			.addEmptySlot("right_wing_2", SlotType.PYLON_MED, -3.6, -0.08, -0.7, 180, "right_wing")
			.addEmptySlot("right_wing_3", SlotType.PYLON_LIGHT, -4.95, -0.08, -1.1, 180, "right_wing")
			.addEmptySlot("right_wing_4", SlotType.PYLON_LIGHT, -6.05, 0.4, -1.5, 90, "right_wing")
			.addEmptySlot("internal_gun", SlotType.INTERNAL_GUN, 0, -0.15, 8, 180, "nose")
			.setSlotOnlyCompatible("internal_gun", "m61a1_vulcan")
			.addEmptySlot("internal_1", SlotType.PUSH_ENGINE, "engine")
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.HIGH_TECH_INTERNAL)
			.addEmptySlot("internal_5", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_6", SlotType.TECH_INTERNAL)
			.addAfterBurnerSmokePos(0,0.3,-6.5)
			.setEntityMainHitboxSize(4, 4)
			.setRootHitboxNoCollide(true)
			.addRotableHitbox("fuselage", 1.7, 2, 11, 0, 0.5, 2, 
					0, 0, false, false, true)
			.addRotableHitbox("left_wing", 4.9, 0.3, 3.5, 3.5, 0.35, -0.7, 
					20, 20, true, true, false)
			.addRotableHitbox("right_wing", 4.9, 0.3, 3.5, -3.5, 0.35, -0.7, 
					20, 20, true, true, false)
			.addRotableHitbox("tail", 0.3, 3, 2, 0, 2.7, -5.3, 
					10, 10, true, true, false)
			.addRotableHitbox("left_elevator", 2.5, 0.3, 1.7, 2, 0.35, -6, 
					10, 10, true, true, false)
			.addRotableHitbox("right_elevator", 2.5, 0.3, 1.7, -2, 0.35, -6, 
					10, 10, true, true, false)
			.addRotableHitbox("engine", 1.4, 1.4, 2.9, 0, 0.3, -4.9,
					10, 10, true, true, false)
			.addRotableHitbox("nose", 1.2, 1, 2.1, 0, 0.3, 8.6,
					10, 10, true, true, false)
			.setHitboxesControlPitch("left_elevator", "right_elevator")
			.setHitboxesControlRoll("left_wing", "right_wing")
			.setHitboxesControlYaw("tail")
			.setWingLiftHitboxNames("left_wing", "right_wing")
			.build();
	
	public static final VehicleStats UNARMED_ALEXIS_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane_unarmed", EMPTY_ALEXIS_PLANE)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.TURBOFAN_F145.getId())
			.setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_gun", "m61a1_vulcan", "20mm", false)
			.addIngredient(ModItems.INTERNAL_GUN.getId())
			.addIngredient(ModItems.TURBOFAN_F145.getId())
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId())
			.build();
	
	public static final VehicleStats DEFAULT_ALEXIS_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane", UNARMED_ALEXIS_PLANE)
			// actual weapon costs: 63 copper, 88 iron, 130 gunpowder, 140 redstone
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("internal_gun", "m61a1_vulcan", "20mm", true)
			.setSlotItem("internal_4", ModItems.AR2K.getId())
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 63)
			.addIngredient("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.build();
	
	public static final VehicleStats SUPPORT_ALEXIS_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane_support", UNARMED_ALEXIS_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
			.setSlotItem("left_wing_3", ModItems.GIMBAL_CAMERA.getId())
			.setSlotItem("left_wing_4", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm88g", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65g", true)
			.setSlotItem("right_wing_3", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("right_wing_4", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("internal_gun", "m61a1_vulcan", "20mmhe", true)
			.setSlotItem("internal_4", ModItems.AR2K.getId())
			.setSlotItem("internal_5", ModItems.GR400.getId())
			.setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.addIngredient(ModItems.AR2K.getId())
			.addIngredient(ModItems.GR400.getId())
			.addIngredient(ModItems.HEAVY_MISSILE_RACK.getId(), 5)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient("dscombat:aluminum_ingot", 64)
			.addIngredient("minecraft:redstone", 64)
			.addIngredient("minecraft:gunpowder", 64)
			.setDefaultBaseTexture(1)
			.build();
	
	public static final VehicleStats SNIPER_ALEXIS_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "alexis_plane_sniper", UNARMED_ALEXIS_PLANE)
			.setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "meteor", true)
			.setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("left_wing_3", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
			.setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "meteor", true)
			.setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
			.setSlotItem("right_wing_3", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
			.setSlotItem("internal_gun", "m61a1_vulcan", "20mm", true)
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
