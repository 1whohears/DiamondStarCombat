package com.onewhohears.dscombat.data.vehicle.presets.plane;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.physics.LiftSurfaceData;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;
import net.minecraft.world.phys.Vec3;

public class JasonPresets {
	
	public static final VehicleStats EMPTY_JASON_PLANE = VehicleStats.Builder
			.createPlane(DSCombatMod.MODID, "jason_plane_empty")
			.setAssetId("jason_plane")
			.setSortFactor(2)
			.setItem(ModItems.VEHICLE.getId())
			.setMaxHealth(60f)
			.setBaseArmor(10f)
			.setArmorDamageThreshold(1f)
			.setArmorAbsorbtionPercent(0.04f)
			.setStealth(1f)
			.setCrossSecArea(3f)
			.setIdleHeat(1.5f)
			.setTurnRadius(10f)

			.setMass(3463)
			.setPlaneLiftAOAGraph("jason_lift_aoa")
			.setDragAOAGraph("jason_drag_aoa")
			.setPlaneFlapDownAOABias(10)
			.setTurnRateGraph("jason_plane_turn_rates")
			.setMaxTurnRates(5f, 3f, 2f)
			.setThrottleRate(0.04f, 0.07f)
			.setRotationalInertia(3000, 30000f, 35000f)
			.setPlaneWingArea(21.8f)
			.setFuselageLiftArea(8)
			.setMaxAltitude(640)
			.setDragArea(0.35f)
			.setPushEngineOverrideStats(26000, 5f, 0.003f)
			.setPlaneSpeeds(9.86f, 9.86f, 4.15f)
			.setBreakDeAcc(0.032f, 0.016f)
			.setUseSpeedScales(true, true)
			.setHasTurnAssist(false)
			// wings
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					10, 10.9, new Vec3(2.5, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.LEFT_FLAP, "jason_lift_aoa",
					"jason_drag_aoa", 0.8f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					10, 10.9, new Vec3(-2.5, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.RIGHT_FLAP, "jason_lift_aoa",
					"jason_drag_aoa", 0.8f))
			// elevators
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					20, 2, new Vec3(1.2, 0, -4.0), 0, 0, 0,
					LiftSurfaceData.InputType.ELEVATOR, "jason_lift_aoa",
					"jason_drag_aoa", 0.4f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					20, 2, new Vec3(-1.2, 0, -4.0), 0, 0, 0,
					LiftSurfaceData.InputType.ELEVATOR, "jason_lift_aoa",
					"jason_drag_aoa", 0.4f))
			// tail
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					4, 5, new Vec3(0, 0, -4.0), 0, 0, 90,
					LiftSurfaceData.InputType.STABILIZER, "jason_lift_aoa",
					"jason_drag_aoa", 0.6f))
			// nose to counter elevators and tail
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					0, 4, new Vec3(0, 0, 4.0), 0, 0, 0,
					LiftSurfaceData.InputType.NONE, "jason_lift_aoa",
					"jason_drag_aoa", 0.4f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					0, 5, new Vec3(0, 0, 4.0), 0, 0, 90,
					LiftSurfaceData.InputType.NONE, "jason_lift_aoa",
					"jason_drag_aoa", 0.6f))
			// fuselage
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", true,
					0, 11, new Vec3(0, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.NONE, "jason_lift_aoa",
					"jason_drag_aoa", 0.8f))

			.setBasicEngineSounds(ModSounds.BIPLANE_1, ModSounds.BIPLANE_1)
			.setCrashExplosionRadius(3)
			.set3rdPersonCamDist(8)
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
