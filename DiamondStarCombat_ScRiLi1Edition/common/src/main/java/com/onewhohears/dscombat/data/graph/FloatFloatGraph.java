package com.onewhohears.dscombat.data.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;

import net.minecraft.resources.ResourceLocation;

public class FloatFloatGraph extends Graph<Float, Float> {
	
	private final Float[] keys, values;
	private final boolean overrideSize;
	
	public FloatFloatGraph(ResourceLocation key, JsonObject json) {
		super(key, json);
		if (json.has("map")) {
			overrideSize = true;
			JsonArray mapJA = json.get("map").getAsJsonArray();
			keys = new Float[mapJA.size()];
			values = new Float[mapJA.size()];
			for (int i = 0; i < mapJA.size(); ++i) {
				JsonObject entry = mapJA.get(i).getAsJsonObject();
				keys[i] = entry.get("key").getAsFloat();
				values[i] = entry.get("value").getAsFloat();
			}
		} else {
			overrideSize = false;
			keys = new Float[getSize()];
			values = new Float[getSize()];
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

	@Override
	public int getSize() {
		if (overrideSize) return keys.length;
		return super.getSize();
	}

}
