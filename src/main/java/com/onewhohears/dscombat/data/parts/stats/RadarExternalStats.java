package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.RadarExternalInstance;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class RadarExternalStats extends RadarPartStats {

	public RadarExternalStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.EXTERNAL_RADAR;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new RadarExternalInstance<>(this);
	}
	
	@Override
	public EntityType<?> getDefaultExternalEntity() {
		return ModEntities.SURVEY_ALL_A.get();
	}
	
	@Override
	public float getExternalEntityDefaultHealth() {
		return 15;
	}

}
