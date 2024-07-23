package com.onewhohears.dscombat.data.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;

import net.minecraft.resources.ResourceLocation;

public class FFMultiGraph extends MultiGraph<Float, Float> {
	
	private final Float[] keys;
	private final Float[][] values;
	
	public FFMultiGraph(ResourceLocation key, JsonObject json) {
		super(key, json);
		keys = new Float[getSize()];
		values = new Float[getRows()][getSize()];
		JsonArray keyJA = json.get("keys").getAsJsonArray();
		JsonArray valueJA = json.get("values").getAsJsonArray();
		for (int i = 0; i < getSize(); ++i) {
			keys[i] = keyJA.get(i).getAsFloat();
			for (int j = 0; j < getRows(); ++j) 
				values[j][i] = valueJA.get(j).getAsJsonArray().get(i).getAsFloat();
		}
	}

	@Override
	public Float[] getValues(int row) {
		if (row < 0) row = 0;
		else if (row >= values.length) row = values.length-1;
		return values[row];
	}

	@Override
	public Float[] getKeys() {
		return keys;
	}

	@Override
	public Float[] getValues() {
		return getValues(0);
	}
	
	public float getLerpFloat(float key, int row) {
		return getLerpFloat(key, getValues(row));
	}

	@Override
	public boolean isFloatLessThan(float a, Float b) {
		return a < b.floatValue();
	}

	@Override
	public JsonPresetType getType() {
		return GraphType.FLOATFLOAT_MULTI;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return null;
	}

}
