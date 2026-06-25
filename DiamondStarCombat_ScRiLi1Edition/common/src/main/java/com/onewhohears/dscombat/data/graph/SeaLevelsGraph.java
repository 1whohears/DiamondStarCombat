package com.onewhohears.dscombat.data.graph;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;

public class SeaLevelsGraph extends FloatFloatGraph {

    private final int seaLevel;

    public SeaLevelsGraph(ResourceLocation key, JsonObject json) {
        super(key, json);
        seaLevel = UtilParse.getIntSafe(json, "seaLevel", 0);
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    @Override
    public JsonPresetType getType() {
        return GraphType.SEA_LEVELS;
    }
}
