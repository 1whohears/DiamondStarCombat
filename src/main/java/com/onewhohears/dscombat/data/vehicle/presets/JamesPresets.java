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
            .setMaxHealth(40f)
            .setBaseArmor(0f)
            .setArmorDamageThreshold(0.5f)
            .setArmorAbsorbtionPercent(0)
            .setMass(500f)
            .setMaxSpeed(0.7f)
            .setStealth(1.0f)
            .setCrossSecArea(2f)
            .setIdleHeat(1f)
            .setTurnRadius(16f)
            .setMaxTurnRates(5f, 3.0f, 2.0f)
            .setTurnTorques(1.5f, 2.5f, 4.5f)
            .setThrottleRate(0.02f, 0.06f)
            .setPlaneWingArea(8f)
            .setFuselageLiftArea(4)
            .setBasicEngineSounds(ModSounds.BIPLANE_1, ModSounds.BIPLANE_1)
            .setRotationalInertia(4, 7, 3)
            .setCrashExplosionRadius(3)
            .set3rdPersonCamDist(4)
            .setPlaneLiftAOAGraph("wooden_plane")
            .setPlaneFlapDownAOABias(8)
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

    public static final VehicleStats DEFAULT_JAMES_PLANE = VehicleStats.Builder
            .createFromCopy(DSCombatMod.MODID, "james_wooden_plane", EMPTY_JAMES_PLANE)
            .setCraftable()
            .addIngredient(ModItems.CM_MANLY_52.getId())
            .addIngredient(ModItems.LIGHT_FUEL_TANK.getId())
            .setSlotItem("internal_1", ModItems.CM_MANLY_52.getId())
            .setSlotItem("internal_2", ModItems.LIGHT_FUEL_TANK.getId(), true)
            .build();

}
