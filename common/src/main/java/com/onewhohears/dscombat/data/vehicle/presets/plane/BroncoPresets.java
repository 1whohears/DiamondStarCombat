package com.onewhohears.dscombat.data.vehicle.presets.plane;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.physics.LiftSurfaceData;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;
import net.minecraft.world.phys.Vec3;

public class BroncoPresets {
	
	public static final VehicleStats EMPTY_BRONCO_PLANE = VehicleStats.Builder
			.createPlane(DSCombatMod.MODID, "bronco_plane_empty")
			.setAssetId("bronco_plane")
			.setSortFactor(4)
			.setItem(ModItems.VEHICLE.getId())
			.setMaxHealth(80f)
			.setBaseArmor(10f)
			.setArmorDamageThreshold(1f)
			.setArmorAbsorbtionPercent(0.05f)
			.setStealth(1f)
			.setCrossSecArea(5f)
			.setIdleHeat(3f)
			.setTurnRadius(8f)
			.setPlaneWingArea(27f)
			.setFuselageLiftArea(9)

			.setMass(3127)
			.setPlaneLiftAOAGraph("javi_lift_aoa")
			.setDragAOAGraph("javi_drag_aoa")
			.setPlaneFlapDownAOABias(18)
			.setTurnRateGraph("bronco_plane_turn_rates")
			.setMaxTurnRates(2.5f, 1.2f, 0.9f)
			.setThrottleRate(0.03f, 0.07f)
			.setRotationalInertia(7000, 60000f, 65000f)
			.setPlaneWingArea(27)
			.setFuselageLiftArea(9)
			.setMaxAltitude(455)
			.setDragArea(0.50f)
			.setPushEngineOverrideStats(12000, 5f, 0.003f)
			.setPlaneSpeeds(6.39f, 6.39f, 5.0f)
			.setBreakDeAcc(0.032f, 0.016f)
            .setMinDriveAcc(0.002f * 8f)
			.setUseSpeedScales(true, true)
			.setHasTurnAssist(true)
			// wings
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					18, 13.5, new Vec3(4, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.LEFT_FLAP, "javi_lift_aoa",
					"javi_drag_aoa", 0.8f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					18, 13.5, new Vec3(-4, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.RIGHT_FLAP, "javi_lift_aoa",
					"javi_drag_aoa", 0.8f))
			// elevators
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					20, 10, new Vec3(0, 0, -5.5), 0, 0, 0,
					LiftSurfaceData.InputType.ELEVATOR, "javi_lift_aoa",
					"javi_drag_aoa", 0.4f))
			// tail
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					3, 5, new Vec3(3, 0, -4), 0, 0, 90,
					LiftSurfaceData.InputType.STABILIZER, "javi_lift_aoa",
					"javi_drag_aoa", 0.6f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					3, 5, new Vec3(-3, 0, -4), 0, 0, 90,
					LiftSurfaceData.InputType.STABILIZER, "javi_lift_aoa",
					"javi_drag_aoa", 0.6f))
			// nose to counter elevators and tail
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					0, 10, new Vec3(0, 0, 5.5), 0, 0, 0,
					LiftSurfaceData.InputType.NONE, "javi_lift_aoa",
					"javi_drag_aoa", 0.4f))
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", false,
					0, 10, new Vec3(0, 0, 4), 0, 0, 90,
					LiftSurfaceData.InputType.NONE, "javi_lift_aoa",
					"javi_drag_aoa", 0.6f))
			// fuselage
			.addPhysicsComponent(LiftSurfaceData.createJsonData("NONE", true,
					0, 9, new Vec3(0, 0, 0), 0, 0, 0,
					LiftSurfaceData.InputType.NONE, "javi_lift_aoa",
					"javi_drag_aoa", 0.8f))

			.setBasicEngineSounds(ModSounds.BIPLANE_1, ModSounds.BIPLANE_1)
			.setCrashExplosionRadius(4)
			.set3rdPersonCamDist(14)
			.setPlaneNoseCanAimDown(false)
			.setBaseTextureNum(2)
			.setLayerTextureNum(2)
			.addIngredientTag("minecraft:planks", 40)
			.addIngredientTag("dscombat:aluminum_ingot", 6)
			.addIngredient(ModItems.WING.getId(), 3)
			.addIngredient(ModItems.COCKPIT.getId(), 1)
			.addIngredient(ModItems.SEAT.getId(), 1)
			.addIngredient(ModItems.WHEEL.getId(), 3)
			.addIngredient(ModItems.PROPELLER.getId(), 2)
			.addPilotSeatSlot(0, -0.74, 4.5, true)
			.addSeatSlot(PartSlot.COPILOT_SLOT_NAME, SlotType.MOUNT_LIGHT, 0, -0.61, 3.125, true)
			.addEmptySlot("nose_1", SlotType.PYLON_LIGHT, 0, -0.9357, 6.0625, 180)
			.addEmptySlot("frame_1", SlotType.PYLON_LIGHT, 0.5, -1.25, 1.1875, 180)
			.addEmptySlot("frame_2", SlotType.PYLON_LIGHT, 1.125, -1.375, 1.1875, 180)
			.addEmptySlot("frame_3", SlotType.PYLON_LIGHT, -0.5, -1.25, 1.1875, 180)
			.addEmptySlot("frame_4", SlotType.PYLON_LIGHT, -1.125, -1.375, 1.1875, 180)
			.addEmptySlot("left_wing_1", SlotType.PYLON_LIGHT, 4.875, 0.125, 1.0625, 180)
			.addEmptySlot("right_wing_1", SlotType.PYLON_LIGHT, -4.875, 0.125, 1.0625, 180)
			.addEmptySlot("internal_1", SlotType.RADIAL_ENGINE)
			.addEmptySlot("internal_2", SlotType.RADIAL_ENGINE)
			.addEmptySlot("internal_3", SlotType.INTERNAL)
			.addEmptySlot("internal_4", SlotType.INTERNAL)
			.addEmptySlot("internal_5", SlotType.TECH_INTERNAL)
			.setEntityMainHitboxSize(4, 4)
			.build();
	
	public static final VehicleStats UNARMED_BRONCO_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "bronco_plane_unarmed", EMPTY_BRONCO_PLANE)
			.setCraftable()
			.setSlotItem("internal_1", ModItems.CM_MANLY_52.getId())
			.setSlotItem("internal_2", ModItems.CM_MANLY_52.getId())
			.setSlotItem("internal_3", ModItems.LIGHT_FUEL_TANK.getId(), true)
			.addIngredient(ModItems.CM_MANLY_52.getId(), 2)
			.addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
			.build();
	
	public static final VehicleStats DEFAULT_BRONCO_PLANE = VehicleStats.Builder
			.createFromCopy(DSCombatMod.MODID, "bronco_plane", UNARMED_BRONCO_PLANE)
			.setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
			.setSlotItem("internal_5", ModItems.GR200.getId())
			.setSlotItem("left_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.setSlotItem("right_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
			.addIngredient(ModItems.BASIC_FLARE_DISPENSER.getId())
			.addIngredient(ModItems.GR200.getId())
			.build();
	
}
