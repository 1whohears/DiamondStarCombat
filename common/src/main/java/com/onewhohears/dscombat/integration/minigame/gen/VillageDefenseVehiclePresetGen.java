package com.onewhohears.dscombat.integration.minigame.gen;

import com.onewhohears.dscombat.data.vehicle.VehiclePresetGenerator;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.init.ModItems;

import static com.onewhohears.dscombat.data.vehicle.presets.plane.AlexisPresets.UNARMED_ALEXIS_PLANE;
import static com.onewhohears.dscombat.data.vehicle.presets.plane.BroncoPresets.UNARMED_BRONCO_PLANE;
import static com.onewhohears.dscombat.data.vehicle.presets.plane.EdenPresets.UNARMED_EDEN_PLANE;
import static com.onewhohears.dscombat.data.vehicle.presets.plane.FelixPresets.UNARMED_FELIX_PLANE;
import static com.onewhohears.dscombat.data.vehicle.presets.plane.JaviPresets.UNARMED_JAVI_PLANE;
import static com.onewhohears.dscombat.data.vehicle.presets.helicopter.KraitChopperPresets.UNARMED_KRAIT_CHOPPER;

public class VillageDefenseVehiclePresetGen {

    public static void generate() {
        // ATTACKERS
        // bronco shitter
        VehiclePresetGenerator.INSTANCE.addPresetToGenerate(VehicleStats.Builder
                .createFromCopy("dscombat", "bronco_plane_shitter", UNARMED_BRONCO_PLANE)
                .setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
                .setSlotItem("internal_5", ModItems.GR200.getId())
                .setSlotItem("left_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
                .setSlotItem("right_wing_1", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
                .build());
        // javi close air support
        VehiclePresetGenerator.INSTANCE.addPresetToGenerate(VehicleStats.Builder
                .createFromCopy("dscombat", "javi_plane_close_air", UNARMED_JAVI_PLANE)
                .setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
                .setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
                .setSlotItem("left_wing_3", ModItems.GIMBAL_CAMERA.getId())
                .setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
                .setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
                .setSlotItem("internal_gun", "gau_avenger", "30mmhe", true)
                .setSlotItem("frame_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm88g", true)
                .setSlotItem("internal_5", ModItems.AR500.getId())
                .setSlotItem("internal_6", ModItems.GPR20.getId())
                .setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
                .build());
        // javi heavy bomber
        VehiclePresetGenerator.INSTANCE.addPresetToGenerate(VehicleStats.Builder
                .createFromCopy("dscombat", "javi_plane_heavy_bomber", UNARMED_JAVI_PLANE)
                .setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm88g", true)
                .setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
                .setSlotItem("left_wing_3", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
                .setSlotItem("left_wing_4", ModItems.GIMBAL_CAMERA.getId())
                .setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm88g", true)
                .setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
                .setSlotItem("right_wing_3", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
                .setSlotItem("right_wing_4", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
                .setSlotItem("internal_gun", "gau_avenger", "30mmhe", true)
                .setSlotItem("frame_1", ModItems.BOMB_RACK.getId(), "anm64", true)
                .setSlotItem("internal_5", ModItems.AR500.getId())
                .setSlotItem("internal_6", ModItems.GPR20.getId())
                .setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
                .setDefaultBaseTexture(1)
                .build());
        // alexis escort
        VehiclePresetGenerator.INSTANCE.addPresetToGenerate(VehicleStats.Builder
                .createFromCopy("dscombat", "alexis_plane_escort", UNARMED_ALEXIS_PLANE)
                .setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
                .setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
                .setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
                .setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
                .setSlotItem("internal_gun", "m61a1_vulcan", "20mm", true)
                .setSlotItem("internal_4", ModItems.AR2K.getId())
                .setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
                .build());
        // alexis infiltrator
        VehiclePresetGenerator.INSTANCE.addPresetToGenerate(VehicleStats.Builder
                .createFromCopy("dscombat", "alexis_plane_infiltrator", UNARMED_ALEXIS_PLANE)
                .setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm88g", true)
                .setSlotItem("left_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
                .setSlotItem("left_wing_3", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
                .setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
                .setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
                .setSlotItem("right_wing_3", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
                .setSlotItem("internal_gun", "m61a1_vulcan", "20mmhe", true)
                .setSlotItem("right_tech_mount", ModItems.GIMBAL_CAMERA.getId())
                .setSlotItem("internal_4", ModItems.AR2K.getId())
                .setSlotItem("internal_5", ModItems.GR400.getId())
                .setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
                .setDefaultBaseTexture(1)
                .build());
        // DEFENDERS
        // steve up smash (small_roller)
        // mr budger tank (mrbudger_tank)
        // eric truck SAM (eric_truck)
        // felix defender
        VehiclePresetGenerator.INSTANCE.addPresetToGenerate(VehicleStats.Builder
                .createFromCopy("dscombat", "felix_plane_defender", UNARMED_FELIX_PLANE)
                .setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7f", true)
                .setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
                .setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7mh", true)
                .setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
                .setSlotItem("nose_1", ModItems.XM12.getId(), "15mm", true)
                .setSlotItem("internal_4", ModItems.AR1K.getId())
                .setSlotItem("internal_3", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
                .setSlotItem("internal_2", ModItems.HEAVY_FUEL_TANK.getId(), true)
                .build());
        // eden interceptor
        VehiclePresetGenerator.INSTANCE.addPresetToGenerate(VehicleStats.Builder
                .createFromCopy("dscombat", "eden_plane_interceptor", UNARMED_EDEN_PLANE)
                .setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
                .setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9x", true)
                .setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120c", true)
                .setSlotItem("right_wing_2", ModItems.HEAVY_MISSILE_RACK.getId(), "aim120b", true)
                .setSlotItem("internal_gun", "gsh_30_1", "20mm", true)
                .setSlotItem("internal_4", ModItems.AR2K.getId())
                .setSlotItem("internal_5", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
                .setDefaultBaseTexture(1)
                .build());
        // BOTH
        // krait door knocker (air-to-ground)
        VehiclePresetGenerator.INSTANCE.addPresetToGenerate(VehicleStats.Builder
                .createFromCopy("dscombat", "krait_chopper_door_knocker", UNARMED_KRAIT_CHOPPER)
                .setSlotItem("nose_gun", ModItems.XM12.getId(), "20mmhe", true)
                .setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm84e", true)
                .setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "agm114k", true)
                .setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "agm65l", true)
                .setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9l", true)
                .setSlotItem("internal_5", ModItems.AR500.getId())
                .setSlotItem("internal_6", ModItems.GR400.getId())
                .setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
                .setDefaultBaseTexture(2)
                .build());
        // krait brawler (air-to-air)
        VehiclePresetGenerator.INSTANCE.addPresetToGenerate(VehicleStats.Builder
                .createFromCopy("dscombat", "krait_chopper_brawler", UNARMED_KRAIT_CHOPPER)
                .setSlotItem("nose_gun", ModItems.XM12.getId(), "20mmhe", true)
                .setSlotItem("left_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7f", true)
                .setSlotItem("left_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
                .setSlotItem("right_wing_1", ModItems.HEAVY_MISSILE_RACK.getId(), "aim7mh", true)
                .setSlotItem("right_wing_2", ModItems.LIGHT_MISSILE_RACK.getId(), "aim9p5", true)
                .setSlotItem("internal_5", ModItems.AR1K.getId())
                .setSlotItem("internal_6", ModItems.GR400.getId())
                .setSlotItem("internal_4", ModItems.BASIC_FLARE_DISPENSER.getId(), true)
                .setDefaultBaseTexture(3)
                .build());
    }

}
