package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModSounds;

public class JamesPresets {

    public static final VehicleStats EMPTY_JAMES_PLANE = VehicleStats.Builder
            .createPlane(DSCombatMod.MODID, "james_wooden_plane_empty")
            .setAssetId("james_wooden_plane")
            .setSortFactor(1)
            .setItem(ModItems.JAMES_WOODEN_PLANE.getId())
            .setMaxHealth(60f)
            .setBaseArmor(0f)
            .setArmorDamageThreshold(0.5f)
            .setArmorAbsorbtionPercent(0)
            .setMass(1800)
            .setMaxSpeed(0.75f)
            .setStealth(1.0f)
            .setCrossSecArea(4f)
            .setIdleHeat(1f)
            .setTurnRadius(20f)
            .setTurnRateGraph("james_plane_turn_rates")
            .setMaxTurnRates(4f, 2.0f, 1.5f)
            .setTurnTorques(1.5f, 2.5f, 4.5f)
            .setThrottleRate(0.02f, 0.06f)
            .setPlaneWingArea(18)
            .setFuselageLiftArea(10)
            .setBasicEngineSounds(ModSounds.BIPLANE_1, ModSounds.BIPLANE_1)
            .setRotationalInertia(4, 7, 3)
            .setCrashExplosionRadius(3.5f)
            .set3rdPersonCamDist(8)
            .setPlaneLiftAOAGraph("wooden_plane")
            .setPlaneFlapDownAOABias(8)
            .setPlaneNoseCanAimDown(false)
            .addIngredientTag("minecraft:planks", 60)
            .addIngredient(ModItems.SEAT.getId())
            .addIngredient(ModItems.PROPELLER.getId())
            .addIngredient(ModItems.WHEEL.getId(), 3)
            .addPilotSeatSlot(0, -0.8, -0.25)
            .addEmptySlot("left_wing_1", SlotType.PYLON_LIGHT, 3, -1.1, 0, 180, "left_wing_bottom")
            .addEmptySlot("left_wing_2", SlotType.PYLON_LIGHT, 3, 0.8, 0.6, 180, "left_wing_top")
            .addEmptySlot("right_wing_1", SlotType.PYLON_LIGHT, -3, -1.1, 0, 180, "right_wing_bottom")
            .addEmptySlot("right_wing_2", SlotType.PYLON_LIGHT, -3, 0.8, 0.6, 180, "right_wing_top")
            .addEmptySlot("internal_1", SlotType.RADIAL_ENGINE)
            .addEmptySlot("internal_2", SlotType.INTERNAL)
            .addEmptySlot("internal_3", SlotType.INTERNAL)
            .addEmptySlot("internal_4", SlotType.INTERNAL)
            .addEmptySlot("internal_5", SlotType.INTERNAL)
            .setGroundXTilt(18)
            .setEntityMainHitboxSize(4.8f, 4.8f)
            .setRootHitboxNoCollide(true)
            .addRotableHitbox("base", 1.4, 1.4, 3.5, 0, -0.6, -0.7,
                    0, 0, false, false, true)
            .addRotableHitbox("tail", 0.8, 1, 3.8, 0, -0.3, -4.4,
                    20, 0, true, true, false)
            .addRotableHitbox("engine", 1.1, 1.1, 1.1, 0, -0.6, 1.6,
                    15, 0, true, true, false)
            .addRotableHitbox("left_wing_bottom", 4.9, 0.3, 1.3, 3.1, -1, -0.2,
                    15, 0, true, true, false)
            .addRotableHitbox("left_wing_top", 6, 0.3, 1.3, 3, 0.8, 0.4,
                    15, 0, true, true, false)
            .addRotableHitbox("right_wing_bottom", 4.9, 0.3, 1.3, -3.1, -1, -0.2,
                    15, 0, true, true, false)
            .addRotableHitbox("right_wing_top", 6, 0.3, 1.3, -3, 0.8, 0.4,
                    15, 0, true, true, false)
            .addRotableHitbox("tail_flaps", 2.5, 0.3, 1.7, 0, 0.15, -6,
                    10, 0, true, true, false)
            .setHitboxesControlPitch("tail_flaps")
            .setHitboxesControlRoll("left_wing_bottom", "left_wing_top", "right_wing_bottom", "right_wing_top")
            .setHitboxesControlYaw("tail_flaps", "tail")
            .setWingLiftHitboxNames("left_wing_bottom", "left_wing_top", "right_wing_bottom", "right_wing_top")
            .build();

    public static final VehicleStats DEFAULT_JAMES_PLANE = VehicleStats.Builder
            .createFromCopy(DSCombatMod.MODID, "james_wooden_plane", EMPTY_JAMES_PLANE)
            .setCraftable()
            .addIngredient(ModItems.CM_MANLY_52.getId())
            .addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
            .setSlotItem("internal_1", ModItems.CM_MANLY_52.getId())
            .setSlotItem("internal_2", ModItems.LIGHT_FUEL_TANK.getId(), true)
            .build();

}
