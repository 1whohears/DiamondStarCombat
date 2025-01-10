package com.onewhohears.dscombat.data.parts.client;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetGenerator;
import net.minecraft.data.DataGenerator;

public class PartClientPresetGenerator extends JsonPresetGenerator<PartClientStats<?>> {

    @Override
    protected void registerPresets() {
        addPresetToGenerate(PartClientStats.Builder.createTurret("minigun_turret")
                .setHardCodedModel("minigun_turret")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("heavy_tank_turret")
                .setHardCodedModel("heavy_tank_turret")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("steve_up_smash")
                .setHardCodedModel("steve_up_smash")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("sam_launcher")
                .setHardCodedModel("sam_launcher")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("mls")
                .setHardCodedModel("mls")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("torpedo_tubes")
                .setHardCodedModel("torpedo_tubes")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("aa_turret")
                .setHardCodedModel("aa_turret")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("ciws")
                .setHardCodedModel("ciws")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("mark7")
                .setHardCodedModel("mark7")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("mark45")
                .setHardCodedModel("mark45")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("mlrs")
                .setHardCodedModel("mlrs")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createTurret("artillery_cannon")
                .setHardCodedModel("artillery_cannon")
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
                .build());
        addPresetToGenerate(PartClientStats.Builder.createWeaponRack("light_missile_rack")
                .setWeaponRackStats(4, 0.2f, 0.3f)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createWeaponRack("heavy_missile_rack")
                .setWeaponRackStats(3, 0.3f, 0.5f)
                .build());
        addPresetToGenerate(PartClientStats.Builder.createWeaponRack("bomb_rack")
                .setHardCodedModel("bomb_rack")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createStandard("adl")
                .build());
        addPresetToGenerate(PartClientStats.Builder.createStandard("vls")
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
