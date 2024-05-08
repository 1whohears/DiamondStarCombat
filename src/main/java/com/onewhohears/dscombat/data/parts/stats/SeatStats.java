package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.SeatInstance;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class SeatStats extends PartStats {

	public SeatStats(ResourceLocation key, JsonObject json) {
		super(key, json);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.SEAT;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new SeatInstance<>(this);
	}
	
	@Override
	public boolean isSeat() {
		return true;
	}
	
	@Override
	public EntityType<?> getDefaultExternalEntity() {
		return ModEntities.SEAT.get();
	}

}
