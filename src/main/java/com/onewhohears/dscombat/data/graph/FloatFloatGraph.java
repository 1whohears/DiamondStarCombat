package com.onewhohears.dscombat.data.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;

import net.minecraft.resources.ResourceLocation;

public class FloatFloatGraph extends Graph<Float, Float> {
	
	private final Float[] keys, values;
	
	public FloatFloatGraph(ResourceLocation key, JsonObject json) {
		super(key, json);
		keys = new Float[getSize()];
		values = new Float[getSize()];
		if (json.has("map")) {
			JsonArray mapJA = json.get("map").getAsJsonArray();
			for (int i = 0; i < getSize(); ++i) {
				JsonObject entry = mapJA.get(i).getAsJsonObject();
				keys[i] = entry.get("key").getAsFloat();
				values[i] = entry.get("value").getAsFloat();
			}
		} else {
			JsonArray keyJA = json.get("keys").getAsJsonArray();
			JsonArray valueJA = json.get("values").getAsJsonArray();
			for (int i = 0; i < getSize(); ++i) {
				keys[i] = keyJA.get(i).getAsFloat();
				values[i] = valueJA.get(i).getAsFloat();
			}
		}
	}

	@Override
	public Float[] getKeys() {
		return keys;
	}

	@Override
	public Float[] getValues() {
		return values;
	}

	@Override
	public boolean isFloatLessThan(float a, Float b) {
		return a < b;
	}

	@Override
	public JsonPresetType getType() {
		return GraphType.FLOATFLOAT;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return null;
	}

}
