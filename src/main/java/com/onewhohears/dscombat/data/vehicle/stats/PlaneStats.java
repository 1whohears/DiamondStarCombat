package com.onewhohears.dscombat.data.vehicle.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.vehicle.LiftKGraph;
import com.onewhohears.dscombat.data.vehicle.VehicleType;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.resources.ResourceLocation;

public class PlaneStats extends VehicleStats {
	
	public final float wing_area, flapsAOABias, fuselage_lift_area;
	public final LiftKGraph liftKGraph;
	public final boolean canAimDown;
	
	public PlaneStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		JsonObject plane = UtilParse.getJsonSafe(UtilParse.getJsonSafe(json,"stats"), "plane");
		wing_area = UtilParse.getFloatSafe(plane, "wing_area", 10);
		flapsAOABias = UtilParse.getFloatSafe(plane, "flapsAOABias", 8);
		canAimDown = UtilParse.getBooleanSafe(plane, "canAimDown", false);
		liftKGraph = LiftKGraph.getGraphById(UtilParse.getStringSafe(plane, "liftKGraph", LiftKGraph.WOODEN_PLANE_GRAPH.id));
		fuselage_lift_area = UtilParse.getFloatSafe(plane, "fuselage_lift_area", 0);
	}

	@Override
	public JsonPresetType getType() {
		return VehicleType.PLANE;
	}
	
	@Override 
	public PlaneStats asPlane() { 
		return this; 
	}
	
	@Override
	public boolean isAircraft() {
		return true;
	}
    
	@Override
    public boolean flipPitchThrottle() {
    	return false;
    }
    
	@Override
    public boolean ignoreInvertY() {
    	return false;
    }
	
	@Override
	public boolean isPlane() {
		return true;
	}

}
