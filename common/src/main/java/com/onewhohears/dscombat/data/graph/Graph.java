package com.onewhohears.dscombat.data.graph;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetStats;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;

public abstract class Graph<K extends Number, V extends Number> extends JsonPresetStats {
	
	private final int size;
	private final boolean mirror_negative_keys, invert_mirrored_values;

	public Graph(ResourceLocation key, JsonObject json) {
		super(key, json);
		size = UtilParse.getIntSafe(json, "size", 0);
		mirror_negative_keys = UtilParse.getBooleanSafe(json, "mirror_negative_keys", false);
		invert_mirrored_values = UtilParse.getBooleanSafe(json, "invert_mirrored_values", false);
	}
	
	public int getSize() {
		return size;
	}
	
	public abstract K[] getKeys();
	public abstract V[] getValues();
	public abstract boolean isFloatLessThan(float a, K b);
	
	public int getFloorIndex(float key) {
		if (isFloatLessThan(key, getKeys()[0])) return 0;
		for (int i = 0; i < getSize()-1; ++i) 
			if (isFloatLessThan(key, getKeys()[i+1])) 
				return i;
		return getSize()-1;
	}
	
	public int getCeilIndex(float key) {
		if (isFloatLessThan(key, getKeys()[0])) return 0;
		for (int i = 0; i < getSize()-1; ++i) 
			if (isFloatLessThan(key, getKeys()[i+1])) 
				return i+1;
		return getSize()-1;
	}
	
	public V getFloor(float key) {
		return getFloor(key, getValues());
	}
	
	public V getCeil(float key) {
		return getCeil(key, getValues());
	}
	
	protected V getFloor(float key, V[] values) {
		return values[getFloorIndex(key)];
	}
	
	protected V getCeil(float key, V[] values) {
		return values[getCeilIndex(key)];
	}
	
	protected float getLerpFloat(float key, V[] values) {
		boolean mirroredKey = false;
		if (isMirrorNegativeKeys() && key < 0) {
			key *= -1;
			mirroredKey = true;
		}
		int floorI = getFloorIndex(key);
		int ceilI = getCeilIndex(key);
		float floorValue = values[floorI].floatValue();
		if (floorI == ceilI) return floorValue;
		float floorKey = getKeys()[floorI].floatValue();
		float ceilKey = getKeys()[ceilI].floatValue();
		float ceilValue = values[ceilI].floatValue();
		if (mirroredKey) {
			key *= -1;
			// FIXME this logic currently requires a point to be defined at zero
			float tempKey = floorKey;
			floorKey = -ceilKey;
			ceilKey = -tempKey;
			float tempValue = floorValue;
			floorValue = ceilValue;
			ceilValue = tempValue;
			if (isInvertMirroredValues()) {
				floorValue *= -1;
				ceilValue *= -1;
			}
		}
		float d = key - floorKey;
		return d*(ceilValue-floorValue)/(ceilKey-floorKey) + floorValue;
	}
	
	public float getLerpFloat(float key) {
		return getLerpFloat(key, getValues());
	}

	public boolean isMirrorNegativeKeys() {
		return mirror_negative_keys;
	}

	public boolean isInvertMirroredValues() {
		return invert_mirrored_values;
	}

}
