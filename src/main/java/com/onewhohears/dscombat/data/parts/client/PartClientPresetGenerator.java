package com.onewhohears.dscombat.data.parts.client;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.phys.Vec3;

public class PartClientPresetGenerator extends JsonPresetGenerator<PartClientStats<?>> {

    public static final Vec3 ONE = new Vec3(1, 1, 1);

    @Override
    protected void registerPresets() {
        addPresetToGenerate(PartClientStats.Builder.createTurret("minigun_turret")
                .setHardCodedModel("minigun_turret")
                .setItemModelOverrides(0.8f, ONE, new Vec3(0.2, 0, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("heavy_tank_turret")
                .setHardCodedModel("heavy_tank_turret")
                .setSimpleModelId("autoloaderturret")
                .setItemModelOverrides(1, ONE, new Vec3(0, -0.1, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("steve_up_smash")
                .setHardCodedModel("steve_up_smash")
                .setItemModelOverrides(0.75f, ONE, new Vec3(0.05, 0.1, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("sam_launcher")
                .setHardCodedModel("sam_launcher")
                .setSimpleModelId("samlauncherv3")
                .setItemModelOverrides(0.75f, ONE, new Vec3(0.1, 0, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("mls")
                .setHardCodedModel("mls")
                .setItemModelOverrides(0.7f, ONE, new Vec3(0.2, 0.1, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("torpedo_tubes")
                .setHardCodedModel("torpedo_tubes")
                .setItemModelOverrides(0.75f, ONE, new Vec3(0.1, 0, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("aa_turret")
                .setHardCodedModel("aa_turret")
                .setItemModelOverrides(0.8f, ONE, new Vec3(0.2, 0, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("ciws")
                .setHardCodedModel("ciws")
                .setItemModelOverrides(0.7f, ONE, new Vec3(-1, 0, 0), new Vec3(60, 180, 0))
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("mark7_cannon")
                .setHardCodedModel("mark7")
                .setSimpleModelId("naval_cannon")
                .setItemModelOverrides(0.8f, ONE, new Vec3(-1, -0.1, 0), new Vec3(60, 180, 0))
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("mark45_cannon")
                .setHardCodedModel("mark45")
                .setSimpleModelId("naval_gun_v2")
                .setItemModelOverrides(0.8f, ONE, new Vec3(-1, -0.1, 0), new Vec3(60, 180, 0))
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("mlrs")
                .setHardCodedModel("mlrs")
                .setItemModelOverrides(0.8f, ONE, new Vec3(0.2, 0, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("artillery_cannon")
                .setHardCodedModel("artillery_cannon")
                .setItemModelOverrides(0.95f, ONE, new Vec3(0.2, 0, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createRadar("air_scan_a")
                .setHardCodedModel("air_scan_a")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createRadar("air_scan_b")
                .setHardCodedModel("air_scan_b")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createRadar("survey_all_a")
                .setHardCodedModel("survey_all_a")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createRadar("survey_all_b")
                .setHardCodedModel("survey_all_b")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createStandard("xm12")
                .setItemModelOverrides(0.8f, ONE, new Vec3(0.3, 0, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createWeaponRack("light_missile_rack")
                .setWeaponRackStats(4, 0.2f, 0.3f)
                .setItemModelOverrides(0.8f, ONE, new Vec3(0, 0.2, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createWeaponRack("heavy_missile_rack")
                .setWeaponRackStats(3, 0.3f, 0.5f)
                .setItemModelOverrides(0.8f, ONE, new Vec3(0, 0.2, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createWeaponRack("bomb_rack")
                .setHardCodedModel("bomb_rack")
                .setItemModelOverrides(0.5f, ONE, new Vec3(0.3, 0.3, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createStandard("adl")
                .setItemModelOverrides(0.7f, ONE, new Vec3(0.2, 0, 0), Vec3.ZERO)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createStandard("vls")
                .setItemModelOverrides(0.6f, ONE, new Vec3(0.2, 0.1, 0), Vec3.ZERO)
                .setHardCodedModel("vls")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createStandard("cfm56")
                .build());
    }

    public PartClientPresetGenerator(DataGenerator output) {
        super(output, "part_client", DataGenerator.Target.RESOURCE_PACK);
    }

    @Override
    public String getName() {
        return "Part Client: " + DSCombatMod.MODID;
    }
}
