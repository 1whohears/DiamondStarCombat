package com.onewhohears.dscombat.data.parts.client;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel;
import com.onewhohears.dscombat.entity.parts.EntityRadar;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;

public class RadarClientStats extends PartClientStats<EntityRadar> {
    private final String large_model_id;
    public RadarClientStats(ResourceLocation key, JsonObject json) {
        super(key, json);
        large_model_id = UtilParse.getStringSafe(json, "large_model_id", "");
    }
    @Override
    protected ObjPartModel<EntityRadar> createNotHardCodedModel() {
        return new ObjRadarModel(getModelId(), getLargeModelId(), getCustomAnims(), getKeyframeAnimIds());
    }
    @Override
    public JsonPresetType getType() {
        return PartClientType.RADAR;
    }
    public String getLargeModelId() {
        return large_model_id;
    }
}
