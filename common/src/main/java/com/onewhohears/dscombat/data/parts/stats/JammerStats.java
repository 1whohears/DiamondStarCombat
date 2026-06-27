package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.JammerInstance;

import net.minecraft.resources.ResourceLocation;

/**
 * Stats for the Electronic Countermeasures (ECM/Jammer) part.
 * Protects the vehicle and nearby allies from guided missiles.
 */
public class JammerStats extends PartStats {

    /** Radius in blocks within which missiles lose their target */
    private final float jamRadius;
    /** 0.0-1.0: chance per check to break missile lock (1.0 = always jams) */
    private final float jamStrength;

    public JammerStats(ResourceLocation key, JsonObject json) {
        super(key, json);
        this.jamRadius = UtilParse.getFloatSafe(json, "jam_radius", 48f);
        this.jamStrength = UtilParse.getFloatSafe(json, "jam_strength", 1.0f);
    }

    public float getJamRadius() {
        return jamRadius;
    }

    public float getJamStrength() {
        return jamStrength;
    }

    @Override
    public JsonPresetType getType() {
        return PartType.JAMMER;
    }

    @Override
    public JsonPresetInstance<?> createPresetInstance() {
        return new JammerInstance<>(this);
    }
}
