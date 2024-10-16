package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.VehicleSoundManager.PassengerSoundPack;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class FelixPresets {
	
	public static final VehicleStats EMPTY_FELIX_PLANE = VehicleStats.Builder
			.createPlane(DSCombatMod.MODID, "felix_plane_empty")
			.setAssetId("felix_plane")
			.setSortFactor(7)
			.setItem(ModItems.FELIX_PLANE.getId())
			.setMaxHealth(100f)
			.setBaseArmor(20f)
			.setArmorDamageThreshold(2f)
			.setArmorAbsorbtionPercent(0.08f)
			.setMass(5200f)
			.setMaxSpeed(1.25f)
			.setStealth(0.95f)
			.setCrossSecArea(4f)
			.setIdleHeat(6f)
			.setTurnRadius(12f)
			.setTurnRateGraph("felix_plane_turn_rates")
			.setMaxTurnRates(5f, 2.15f, 1.15f)
			.setTurnTorques(1f, 2f, 4f)
			.setThrottleRate(0.04f, 0.08f)
			.setPlaneWingArea(23f)
			.setFuselageLiftArea(14)
			.setMaxAltitude(530)
			.setFighterJetSounds(ModSounds.ALEXIS_EXT_AFTERBURNER_CLOSE, ModSounds.ALEXIS_EXT_AFTERBURNER_FAR,
					ModSounds.ALEXIS_EXT_RPM, ModSounds.ALEXIS_EXT_WIND_CLOSE, ModSounds.ALEXIS_EXT_WIND_FAR,
					ModSounds.ALEXIS_CP_RPM, ModSounds.ALEXIS_CP_AFTERBURNER, ModSounds.ALEXIS_CP_WIND_SLOW, 
					ModSounds.ALEXIS_CP_WIND_FAST)
			.setRotationalInertia(4.5f, 9, 3)
			.setCrashExplosionRadius(5)
			.set3rdPersonCamDist(17)
			.setPlaneLiftAOAGraph("alexis_plane")
			.setPlaneFlapDownAOABias(8)
			.setPlaneNoseCanAimDown(false)
			.setBaseTextureNum(2)
			.setLayerTextureNum(3)
			.setDefultPassengerSoundPack(PassengerSoundPack.ENG_NON_BINARY_GOOBER)
			.addIngredient(ModItems.FUSELAGE.getId(), 1)
			.addIngredient(ModItems.WING.getId(), 2)
			.addIngredient(ModItems.ADVANCED_COCKPIT.getId())
			.addIngredient(ModItems.WHEEL.getId(), 3)
			.addPilotSeatSlot(0, -0.3, 4.7, true)
			.addEmptySlot("left_wing_1", SlotType.PYLON_LIGHT, 2.4, -0.64, -0.05, 180, "left_wing")
			.addEmptySlot("left_wing_2", SlotType.PYLON_LIGHT, 3.4, -0.64, -1.3, 180, "left_wing")
			.addEmptySlot("right_wing_1", SlotType.PYLON_LIGHT, -2.4, -0.64, -0.05, 180, "right_wing")
			.addEmptySlot("right_wing_2", SlotType.PYLON_LIGHT, -3.4, -0.64, -1.3, 180, "right_wing")
			.addEmptySlot("frame_1", SlotType.PYLON_MED, 0, -1.07, 1.2, 180)
			.addEmptySlot("nose_1", SlotType.PYLON_LIGHT, 0, -0.8, 3.6, 180)
			.addEmptySlot("internal_1", SlotType.PUSH_ENGINE, "engine")
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.TECH_INTERNAL)
			.addHUDScreen(0, -0.3, 4.7)
			.addAfterBurnerSmokePos(0,-0.1,-6)
			.setEntityMainHitboxSize(4, 4)
			.setRootHitboxNoCollide(true)
			.addRotableHitbox("fuselage", 1.3, 1.5, 11, 0, 0.1, 2.5, 
					0, 0, false, false, true)
			.addRotableHitbox("left_wing", 3.5, 0.3, 3.5, 2.5, -0.25, -0.7, 
					20, 20, true, true, false)
			.addRotableHitbox("right_wing", 3.5, 0.3, 3.5, -2.5, -0.25, -0.7, 
					20, 20, true, true, false)
			.addRotableHitbox("tail", 0.3, 2.5, 2, 0, 1.8, -5.3, 
					10, 10, true, true, false)
			.addRotableHitbox("left_elevator", 1.5, 0.3, 1.7, 1.5, 0, -6, 
					10, 10, true, true, false)
			.addRotableHitbox("right_elevator", 1.5, 0.3, 1.7, -1.5, 0, -6, 
					10, 10, true, true, false)
			.addRotableHitbox("engine", 1.1, 1.1, 2.8, 0, -0.1, -4.7, 
					10, 10, true, true, false)
			.setHitboxesControlPitch("left_elevator", "right_elevator")
			.setHitboxesControlRoll("left_wing", "right_wing")
			.setHitboxesControlYaw("tail")
			.setWingLiftHitboxNames("left_wing", "right_wing")
			.build();
	
	public static final VehicleStats UNARMED_FELIX_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "felix_plane_unarmed", EMPTY_FELIX_PLANE)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.TURBOFAN_F145.getId())
			.setSlotItem("internal_2", ModItems.LIGHT_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.TURBOFAN_F145.getId())
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.build();
	
	public static final VehicleStats DEFAULT_FELIX_PLANE = VehicleStats.Builder
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
	
	public static final VehicleStats AIR_SUPPORT_FELIX_PLANE = VehicleStats.Builder
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
