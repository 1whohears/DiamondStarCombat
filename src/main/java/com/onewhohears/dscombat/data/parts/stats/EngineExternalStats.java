package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.EngineExternalInstance;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class EngineExternalStats extends EngineStats {
	
	public EngineExternalStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.EXTERNAL_ENGINE;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new EngineExternalInstance<>(this);
	}
	
	@Override
	public EntityType<?> getDefaultExternalEntity() {
		return ModEntities.CFM56.get();
	}
	
	@Override
	public float getExternalEntityDefaultHealth() {
		return 20;
	}
	
}
