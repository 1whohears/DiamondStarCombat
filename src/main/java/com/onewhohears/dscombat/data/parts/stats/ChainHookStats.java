package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.ChainHookInstance;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class ChainHookStats extends PartStats {

	public ChainHookStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.CHAIN_HOOK;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new ChainHookInstance<>(this);
	}
	
	@Override
	public EntityType<?> getDefaultExternalEntity() {
		return ModEntities.CHAIN_HOOK.get();
	}
	
	@Override
	public float getExternalEntityDefaultHealth() {
		return 50;
	}

}
