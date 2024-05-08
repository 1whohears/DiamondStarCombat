package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.GimbalInstance;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class GimbalStats extends PartStats {

	public GimbalStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.GIMBAL;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new GimbalInstance<>(this);
	}
	
	@Override
	public EntityType<?> getDefaultExternalEntity() {
		return ModEntities.GIMBAL_CAMERA.get();
	}
	
	@Override
	public float getExternalEntityDefaultHealth() {
		return 5;
	}

}
