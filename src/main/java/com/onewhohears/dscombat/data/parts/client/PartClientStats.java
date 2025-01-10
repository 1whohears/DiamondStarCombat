package com.onewhohears.dscombat.data.parts.client;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.model.obj.HardCodedModelAnims;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.onewholibs.data.jsonpreset.CustomAnimStats;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PartClientStats<E extends EntityPart> extends CustomAnimStats<ObjPartModel<E>,E> {
    public PartClientStats(ResourceLocation key, JsonObject json) {
        super(key, json);
    }

    @Override
    protected ObjPartModel<E> createModel() {
        if (getJsonData().has("model_data")) {
            JsonObject model_data = getJsonData().get("model_data").getAsJsonObject();
            if (model_data.has("hard_coded_model_anims")) {
                return HardCodedModelAnims.getPartModel(model_data.get("hard_coded_model_anims").getAsString());
            }
        }
        return createNotHardCodedModel();
    }

    protected ObjPartModel<E> createNotHardCodedModel() {
        return new ObjPartModel<>(getModelId(), getCustomAnims(), getKeyframeAnimIds());
    }

    @Override
    public JsonPresetType getType() {
        return PartClientType.STANDARD;
    }

    @Override
    public @Nullable JsonPresetInstance<?> createPresetInstance() {
        return null;
    }

    public static class Builder extends CustomAnimStatsBuilder<Builder> {
        public Builder setHardCodedModel(String id) {
            getModelData().addProperty("hard_coded_model_anims", id);
            return this;
        }
        public Builder setWeaponRackStats(int maxAmmoNum, float dX, float dY) {
            setFloat("dX", dX);
            setFloat("dY", dY);
            return setInt("maxAmmoNum", maxAmmoNum);
        }
        public static Builder createStandard(String name) {
            return new Builder(name, PartClientType.STANDARD);
        }
        public static Builder createTurret(String name) {
            return new Builder(name, PartClientType.TURRET);
        }
        public static Builder createRadar(String name) {
            return new Builder(name, PartClientType.RADAR);
        }
        public static Builder createWeaponRack(String name) {
            return new Builder(name, PartClientType.WEAPON_RACK);
        }
        protected Builder(String name, JsonPresetType type) {
            super(DSCombatMod.MODID, name, type);
        }
    }
}
