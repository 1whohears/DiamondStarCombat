package com.onewhohears.dscombat.data.weapon.client;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.onewholibs.client.model.obj.customanims.CustomAnimsBuilder;
import com.onewhohears.onewholibs.client.model.obj.customanims.EntityModelTransform;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetGenerator;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

public class WeaponClientPresetGenerator extends JsonPresetGenerator<WeaponClientStats<?>> {

    @Override
    protected void registerPresets() {
        addPresetToGenerate(WeaponClientStats.Builder.createStandard("type91")
                .setCustomAnims(CustomAnimsBuilder.create()
                        .addContinuousRotPixelAnim("prop1", 0, 0, -60.5f,
                                EntityModelTransform.RotationAxis.Z, -50)
                        .addContinuousRotPixelAnim("prop2", 0, 0, -61.5f,
                                EntityModelTransform.RotationAxis.Z, 50)
                        .build()
                ).build()
        );
    }

    public WeaponClientPresetGenerator(PackOutput output) {
        super(output, "weapon_client", PackOutput.Target.RESOURCE_PACK);
    }

    @Override
    public @NotNull String getName() {
        return "Weapon Client: " + DSCombatMod.MODID;
    }
}
