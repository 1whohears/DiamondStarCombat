package com.onewhohears.dscombat.data.vehicle.presets;

import com.onewhohears.dscombat.DSCombatMod;
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
            .lockSlot("pilot_seat")
            .lockSlot("radar_tower")
            .lockSlot("internal_1")
            .setEntityMainHitboxSize(1.5f, 2f)
            .build();

}
