package com.onewhohears.dscombat.data.vehicle.presets.ground_vehicle;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;

public class StationaryPresets {

    public static final VehicleStats EWR4000 = VehicleStats.Builder
            .createStationary(DSCombatMod.MODID, "ewr4000")
            .setAssetId("ewr4000")
            .setItem(ModItems.EWR4000.getId()) // this cant be set to VEHICLE because EWR4000 has a custom recipe
            .setMaxHealth(20)
            .setBaseArmor(2)
            .setArmorDamageThreshold(1)
            .setArmorAbsorbtionPercent(0.01f)
            .setMass(2000f)
            .setMaxSpeed(0f)
            .setStealth(1.0f)
            .setCrossSecArea(5.375f)
            .setIdleHeat(8f)
            .setRotationalInertia(8, 12, 8)
            .setCrashExplosionRadius(1)
            .set3rdPersonCamDist(4)
            .setIsStationaryRadar(true)
            .addPilotSeatSlot(0, 0.0, -1.5)
            .addEmptySlot("radar_tower", SlotType.TECH_INTERNAL, 0, 2.5, 0)
            .setSlotItem("radar_tower", "ewr4000")
            .addItemSlot("internal_1", SlotType.TECH_INTERNAL, ModItems.DATA_LINK.getId())
            .lockSlot(PartSlot.PILOT_SLOT_NAME)
            .lockSlot("radar_tower")
            .lockSlot("internal_1")
            .setEntityMainHitboxSize(1.5f, 2f)
            .build();

    public static final VehicleStats TURRET_PLATFORM = VehicleStats.Builder
            .createStationary(DSCombatMod.MODID, "turret_platform")
            .setAssetId("turret_platform")
            .setItem(ModItems.VEHICLE.getId())
            .setMaxHealth(100000)
            .setBaseArmor(0)
            .setArmorDamageThreshold(1)
            .setArmorAbsorbtionPercent(0.01f)
            .setMass(2000f)
            .setMaxSpeed(0f)
            .setStealth(1.0f)
            .setCrossSecArea(5.375f)
            .setIdleHeat(8f)
            .setRotationalInertia(100, 100, 100)
            .setCrashExplosionRadius(1)
            .set3rdPersonCamDist(4)
            .addSeatSlot(PartSlot.COPILOT_SLOT_NAME, SlotType.MOUNT_SUPER_HEAVY, 0, 0.125, 0)
            .setEntityMainHitboxSize(1f, 0.125f)
            .build();

    public static final VehicleStats TECH_TURRET_PLATFORM = VehicleStats.Builder
            .createFromCopy(DSCombatMod.MODID, "tech_turret_platform", TURRET_PLATFORM)
            .addEmptySlot("internal_1", SlotType.TECH_INTERNAL, 0, 0.125, 0)
            .addEmptySlot("internal_2", SlotType.TECH_INTERNAL, 0, 0.125, 0)
            .build();

}
