package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class JasonPresets {
	
	public static final VehicleStats EMPTY_JASON_PLANE = VehicleStats.Builder
			.createPlane(DSCombatMod.MODID, "jason_plane_empty")
			.setAssetId("jason_plane")
			.setSortFactor(2)
			.setItem(ModItems.JASON_PLANE.getId())
			.setMaxHealth(60f)
			.setBaseArmor(10f)
			.setArmorDamageThreshold(1f)
			.setArmorAbsorbtionPercent(0.04f)
			.setMass(1100f)
			.setMaxSpeed(0.8f)
			.setStealth(1f)
			.setCrossSecArea(3f)
			.setIdleHeat(1.5f)
			.setTurnRadius(10f)
			.setMaxTurnRates(5f, 3f, 2f)
			.setTurnTorques(2f, 2f, 3f)
			.setThrottleRate(0.04f, 0.07f)
			.setPlaneWingArea(12f)
			.setFuselageLiftArea(6)
			.setBasicEngineSounds(ModSounds.BIPLANE_1, ModSounds.BIPLANE_1)
			.setRotationalInertia(5, 9, 3)
			.setCrashExplosionRadius(3)
			.set3rdPersonCamDist(8)
			.setPlaneLiftAOAGraph("wooden_plane")
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
			.addEmptySlot("frame_1", SlotType.PYLON_LIGHT, 0, -0.45, 1.9, 180)
			.addEmptySlot("left_wing_1", SlotType.PYLON_LIGHT, 3.4, -0.4, 1.1, 180)
			.addEmptySlot("right_wing_1", SlotType.PYLON_LIGHT, -3.4, -0.4, 1.1, 180)
			.addEmptySlot("left_wing_gun", SlotType.INTERNAL_GUN, 3.4, -0.4, 1.1, 0)
			.setSlotOnlyCompatible("left_wing_gun", "m2_browning")
			.addEmptySlot("right_wing_gun", SlotType.INTERNAL_GUN, -3.4, -0.4, 1.1, 0)
			.setSlotOnlyCompatible("right_wing_gun", "m2_browning")
			.addEmptySlot("internal_1", SlotType.RADIAL_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.setEntityMainHitboxSize(3.2f, 3.2f)
			.setGroundXTilt(13f)
			.build();
	
	public static final VehicleStats UNARMED_JASON_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "jason_plane_unarmed", EMPTY_JASON_PLANE)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.ALLISON_V_1710.getId())
			.setSlotItem("internal_2", ModItems.LIGHT_FUEL_TANK.getId(), true)
			.setSlotItem("left_wing_gun", "m2_browning", "10mm", false)
			.setSlotItem("right_wing_gun", "m2_browning", "10mm", false)
			.addIngredient(ModItems.INTERNAL_GUN.getId(), 2)
			.addIngredient(ModItems.ALLISON_V_1710.getId())
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.build();
	
	public static final VehicleStats DEFAULT_JASON_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "jason_plane", UNARMED_JASON_PLANE)
			.setSlotItem("left_wing_gun", "m2_browning", "10mm", true)
			.setSlotItem("right_wing_gun", "m2_browning", "10mm", true)
			.build();
	
}
