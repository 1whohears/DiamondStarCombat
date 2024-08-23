package com.onewhohears.dscombat.data.graph;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;

import net.minecraft.resources.ResourceLocation;

public class TurnRatesBySpeedGraph extends FFMultiGraph {

	public TurnRatesBySpeedGraph(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public JsonPresetType getType() {
		return GraphType.TURN_RATES_SPEED;
	}
	
	@Override
	public int getRows() {
		return 3;
	}
	
	public float getMaxRollRate(float speed) {
		return getLerpFloat(speed, 0);
	}
	
	public float getMaxPitchRate(float speed) {
		return getLerpFloat(speed, 1);
	}
	
	public float getMaxYawRate(float speed) {
		return getLerpFloat(speed, 2);
	}

}
