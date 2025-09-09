package com.onewhohears.dscombat.data.parts.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.SeatInstance;
import com.onewhohears.dscombat.init.ModEntities;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public class SeatStats extends PartStats {

	private final Vec3 passenger_offset;

	public SeatStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		passenger_offset = UtilParse.readVec3(json, "passenger_offset");
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

	@Override
	public PartInstance<?> createFilledPartInstance(String param) {
		return super.createFilledPartInstance(param);
	}

	public Vec3 getPassengerOffsets() {
		return passenger_offset;
	}
}
