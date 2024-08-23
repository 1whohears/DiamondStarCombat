package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.RadarPartInstance;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;

public class RadarPartStats extends PartStats {
	
	private final String radar;
	
	public RadarPartStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		radar = UtilParse.getStringSafe(json, "radar", "");
	}

	@Override
	public JsonPresetType getType() {
		return PartType.INTERNAL_RADAR;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new RadarPartInstance<>(this);
	}
	
	public String getRadarId() {
		return radar;
	}

}
