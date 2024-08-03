package com.onewhohears.dscombat.data.graph;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetStats;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public abstract class Graph<K extends Number, V extends Number> extends JsonPresetStats {
	
	private final int size;
	
	public Graph(ResourceLocation key, JsonObject json) {
		super(key, json);
		size = UtilParse.getIntSafe(json, "size", 0);
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
		int floorI = getFloorIndex(key);
		int ceilI = getCeilIndex(key);
		float floorValue = values[floorI].floatValue();
		if (floorI == ceilI) return floorValue;
		float floorKey = getKeys()[floorI].floatValue();
		float ceilKey = getKeys()[ceilI].floatValue();
		float ceilValue = values[ceilI].floatValue();
		float d = key - floorKey;
		return d*(ceilValue-floorValue)/(ceilKey-floorKey) + floorValue;
	}
	
	public float getLerpFloat(float key) {
		return getLerpFloat(key, getValues());
	}
	
}
