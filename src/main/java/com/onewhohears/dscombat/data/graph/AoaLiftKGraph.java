package com.onewhohears.dscombat.data.graph;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;

import net.minecraft.resources.ResourceLocation;

public class AoaLiftKGraph extends FloatFloatGraph {
	
	private final float criticalAOA, warnAOA;
	
	public AoaLiftKGraph(ResourceLocation key, JsonObject json) {
		super(key, json);
		criticalAOA = findCriticalAOA();
		warnAOA = criticalAOA*0.5f;
	}

	public float getCriticalAOA() {
		return criticalAOA;
	}

	public float getWarnAOA() {
		return warnAOA;
	}
	
	private float findCriticalAOA() {
		for (int i = 1; i < getSize()-1; ++i) {
			float aoaI = getKeys()[i].floatValue();
			if (aoaI < 0) continue;
			float liftI = getValues()[i].floatValue();
			float liftIB = getValues()[i-1].floatValue();
			float liftIF = getValues()[i+1].floatValue();
			if (liftIB < liftI && liftIF < liftI)
				return aoaI;
		}
		return 0;
	}
	
	@Override
	public JsonPresetType getType() {
		return GraphType.AOALIFTK;
	}

}
