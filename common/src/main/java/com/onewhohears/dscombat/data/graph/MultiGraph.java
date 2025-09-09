package com.onewhohears.dscombat.data.graph;

import com.google.gson.JsonObject;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;

public abstract class MultiGraph<K extends Number, V extends Number> extends Graph<K, V> {
	
	private final int rows;
	
	public MultiGraph(ResourceLocation key, JsonObject json) {
		super(key, json);
		rows = UtilParse.getIntSafe(json, "rows", 1);
	}

	public int getRows() {
		return rows;
	}
	
	public abstract V[] getValues(int row);
	
}
