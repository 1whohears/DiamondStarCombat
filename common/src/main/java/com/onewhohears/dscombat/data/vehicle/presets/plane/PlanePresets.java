package com.onewhohears.dscombat.data.vehicle.presets.plane;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenIds;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.physics.LiftSurfaceData;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;
import net.minecraft.world.phys.Vec3;

public class PlanePresets {
	
	public static final VehicleStats EMPTY_WOODEN_PLANE = VehicleStats.Builder
			.createPlane(DSCombatMod.MODID, "wooden_plane_empty")
			.setAssetId("wooden_plane")
			.setSortFactor(0)
			.setItem(ModItems.VEHICLE.getId())
			.setMaxHealth(40f)
			.setBaseArmor(0f)
			.setArmorDamageThreshold(0.5f)
			.setArmorAbsorbtionPercent(0)
			.setStealth(1.0f)
			.setCrossSecArea(2f)
			.setIdleHeat(1f)
			.setTurnRadius(16f)

			.setMass(500)
			.setPlaneLiftAOAGraph("jason_lift_aoa")
			.setDragAOAGraph("jason_drag_aoa")
			.setPlaneFlapDownAOABias(10)
			.setTurnRateGraph("wooden_plane_turn_rates")
			.setMaxTurnRates(5f, 3.0f, 2.0f)
			.setThrottleRate(0.02f, 0.06f)
			.setRotationalInertia(1000, 10000f, 11000f)
			.setPlaneWingArea(8)
			.setFuselageLiftArea(4)
			.setMaxAltitude(300)
			.setDragArea(0.1f)
			.setPushEngineOverrideStats(14000, 5f, 0.003f)
			.setPlaneSpeeds(8, 8, 5.0f)
			.setBreakDeAcc(0.032f, 0.016f)
			.setUseSpeedScales(true, true)
			.setHasTurnAssist(false)
			// wings
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					10, 6, new Vec3(1.5, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.LEFT_FLAP, "jason_lift_aoa",
					"jason_drag_aoa", 0.8f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					10, 6, new Vec3(-1.5, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.RIGHT_FLAP, "jason_lift_aoa",
					"jason_drag_aoa", 0.8f))
			// elevators
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					20, 2, new Vec3(0.4, 0, -2.0), 0, 0, 0,
					LiftSurfaceData.InputType.ELEVATOR, "jason_lift_aoa",
					"jason_drag_aoa", 0.4f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					20, 2, new Vec3(-0.4, 0, -2.0), 0, 0, 0,
					LiftSurfaceData.InputType.ELEVATOR, "jason_lift_aoa",
					"jason_drag_aoa", 0.4f))
			// tail
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					4, 4, new Vec3(0, 0, -2.0), 0, 0, 90,
					LiftSurfaceData.InputType.STABILIZER, "jason_lift_aoa",
					"jason_drag_aoa", 0.6f))
			// nose to counter elevators and tail
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					0, 4, new Vec3(0, 0, 2.0), 0, 0, 0,
					LiftSurfaceData.InputType.NONE, "jason_lift_aoa",
					"jason_drag_aoa", 0.4f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					0, 4, new Vec3(0, 0, 2.0), 0, 0, 90,
					LiftSurfaceData.InputType.NONE, "jason_lift_aoa",
					"jason_drag_aoa", 0.6f))
			// fuselage
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", true,
					0, 6, new Vec3(0, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.NONE, "jason_lift_aoa",
					"jason_drag_aoa", 0.8f))

			.setBasicEngineSounds(ModSounds.BIPLANE_1, ModSounds.BIPLANE_1)
			.setCrashExplosionRadius(3)
			.set3rdPersonCamDist(4)
			.setPlaneNoseCanAimDown(false)
			.addIngredientTag("minecraft:planks", 20)
			.addIngredient(ModItems.SEAT.getId())
			.addIngredient(ModItems.PROPELLER.getId())
			.addIngredient(ModItems.WHEEL.getId(), 2)
			.addPilotSeatSlot(0, -0.4, 0)
			.addEmptySlot("left_wing_1", SlotType.PYLON_LIGHT, 1.5, 0, 0, 180)
			.addEmptySlot("right_wing_1", SlotType.PYLON_LIGHT, -1.5, 0, 0, 180)
			.addEmptySlot("internal_1", SlotType.RADIAL_ENGINE)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.setEntityMainHitboxSize(1.7f, 1.7f)
			.build();
	
	public static final VehicleStats DEFAULT_WOODEN_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "wooden_plane", EMPTY_WOODEN_PLANE)
			.setCraftable()
			.addIngredient(ModItems.CM_MANLY_52.getId())
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.setSlotItem("internal_1", ModItems.CM_MANLY_52.getId())
			.setSlotItem("internal_2", ModItems.LIGHT_FUEL_TANK.getId(), true)
			.build();
	
	public static final VehicleStats BOMBER_WOODEN_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "wooden_plane_bomber", DEFAULT_WOODEN_PLANE)
			.setSlotItem("left_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("right_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.addIngredient("minecraft:tnt", 12)
			.addIngredient("dscombat:aluminum_ingot", 36)
			.addIngredient(ModItems.LIGHT_MISSILE_RACK.getId(), 2)
			.build();
	
	public static final VehicleStats FIGHTER_WOODEN_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "wooden_plane_fighter", DEFAULT_WOODEN_PLANE)
			.setSlotItem("left_wing_1", ModItems.XM12.getId(), "10mm", true)
			.setSlotItem("right_wing_1", ModItems.XM12.getId(), "10mm", true)
			.addIngredient("minecraft:copper_ingot", 64)
			.addIngredient(ModItems.XM12.getId(), 2)
			.build();
	
	public static final VehicleStats EMPTY_E3SENTRY_PLANE = VehicleStats.Builder
			.createPlane(DSCombatMod.MODID, "e3sentry_plane_empty")
			.setAssetId("e3sentry_plane")
			.setSortFactor(15)
			.setItem(ModItems.VEHICLE.getId())
			.setMaxHealth(200)
			.setBaseArmor(100)
			.setArmorDamageThreshold(3f)
			.setArmorAbsorbtionPercent(0.05f)
			.setStealth(1.3f)
			.setCrossSecArea(16f)
			.setIdleHeat(20f)
			.setTurnRadius(30f)
			.setTurnRateGraph("e3sentry_turn_rates")

			.setMass(83915f)
			.setPlaneLiftAOAGraph("e3sentry_lift_aoa")
			.setDragAOAGraph("e3sentry_drag_aoa")
			.setPlaneFlapDownAOABias(10)
			.setTurnRateGraph("e3sentry_turn_rates")
			.setMaxTurnRates(3f, 2.0f, 2.0f)
			.setRotationalInertia(24000f, 200000f, 210000f)
			.setThrottleRate(0.01f, 0.04f)
			.setPlaneWingArea(283)
			.setFuselageLiftArea(80)
			.setMaxAltitude(440)
			.setDragArea(1.70f)
			.setPushEngineOverrideStats(96000, 20f, 0.006f)
			.setPlaneSpeeds(11.86f, 11.86f, 8.0f)
			.setBreakDeAcc(0.016f, 0.010f)
			.setUseSpeedScales(true, true)
			.setHasTurnAssist(true)
			// wings
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					10, 141.5, new Vec3(12, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.LEFT_FLAP, "e3sentry_lift_aoa",
					"e3sentry_drag_aoa", 0.8f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					10, 141.5, new Vec3(-12, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.RIGHT_FLAP, "e3sentry_lift_aoa",
					"e3sentry_drag_aoa", 0.8f))
			// elevators
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					10, 20, new Vec3(4, 0, -15), 0, 0, 0,
					LiftSurfaceData.InputType.ELEVATOR, "e3sentry_lift_aoa",
					"e3sentry_drag_aoa", 0.4f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					10, 20, new Vec3(-4, 0, -15), 0, 0, 0,
					LiftSurfaceData.InputType.ELEVATOR, "e3sentry_lift_aoa",
					"e3sentry_drag_aoa", 0.4f))
			// tail
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					4, 30, new Vec3(0, 0, -14), 0, 0, 90,
					LiftSurfaceData.InputType.STABILIZER, "alexis_plane",
					"e3sentry_drag_aoa", 0.6f))
			// nose to counter elevators and tail
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					0, 40, new Vec3(0, 0, 15), 0, 0, 0,
					LiftSurfaceData.InputType.NONE, "e3sentry_lift_aoa",
					"e3sentry_drag_aoa", 0.4f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					0, 60, new Vec3(0, 0, 14), 0, 0, 90,
					LiftSurfaceData.InputType.NONE, "alexis_plane",
					"e3sentry_drag_aoa", 0.6f))
			// fuselage
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", true,
					0, 80, new Vec3(0, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.NONE, "e3sentry_lift_aoa",
					"e3sentry_drag_aoa", 0.8f))

			.setBasicEngineSounds(ModSounds.JET_1, ModSounds.JET_1)
			.setCrashExplosionRadius(8)
			.set3rdPersonCamDist(12)
			.setPlaneNoseCanAimDown(false)
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
			.addEmptySlot("left_wing_1", SlotType.PYLON_HEAVY, 4, -0.6, 0.5, 180)
			.addEmptySlot("right_wing_1", SlotType.PYLON_HEAVY, -4, -0.6, 0.5, 180)
			.addEmptySlot("left_wing_2", SlotType.PYLON_HEAVY, 6,-0.6, 0.5, 180)
			.addEmptySlot("right_wing_2", SlotType.PYLON_HEAVY, -6, -0.6, 0.5, 180)
			.addEmptySlot("frame_1", SlotType.MOUNT_HEAVY, 0, 0.8, -1.3, 0)
			.addEmptySlot("internal_1", SlotType.INTERNAL)
			.addEmptySlot("internal_2", SlotType.INTERNAL)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.addEmptySlot("internal_5", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_6", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_7", SlotType.TECH_INTERNAL)
			.addEmptySlot("internal_8", SlotType.TECH_INTERNAL)
			.addEntityScreen(EntityScreenIds.AIR_RADAR_SCREEN, 0, -0.85, 5.49, 0.7, 0.7)
			.addEntityScreen(EntityScreenIds.FUEL_SCREEN, 0.59, -0.59, 5.49, 0.15, 0.15)
			.addEntityScreen(EntityScreenIds.RWR_SCREEN, 0.83, -0.66, 5.49, 0.3, 0.3)
			.setEntityMainHitboxSize(4, 4)
			.build();
	
	public static final VehicleStats DEFAULT_E3SENTRY_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "e3sentry_plane", EMPTY_E3SENTRY_PLANE)
			.setCraftable()
			.addIngredient(ModItems.CFM56.getId(), 4)
			.addIngredient(ModItems.HEAVY_FUEL_TANK.getId(), 2)
			.addIngredient(ModItems.AR20K.getId())
			.addIngredient(ModItems.DATA_LINK.getId())
			.setSlotItem("left_wing_1", ModItems.CFM56.getId())
			.setSlotItem("right_wing_1", ModItems.CFM56.getId())
			.setSlotItem("left_wing_2", ModItems.CFM56.getId())
			.setSlotItem("right_wing_2", ModItems.CFM56.getId())
			.setSlotItem("frame_1", ModItems.AR20K.getId())
			.setSlotItem("internal_1", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
			.setSlotItem("internal_5", ModItems.DATA_LINK.getId())
			.build();
	
}
