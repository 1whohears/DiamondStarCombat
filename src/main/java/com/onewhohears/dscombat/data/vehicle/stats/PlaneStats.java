package com.onewhohears.dscombat.data.vehicle.stats;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.graph.AoaLiftKGraph;
import com.onewhohears.dscombat.data.graph.StatGraphs;
import com.onewhohears.dscombat.data.graph.TurnRatesBySpeedGraph;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.vehicle.VehicleType;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;

public class PlaneStats extends VehicleStats {
	
	public final float wing_area, flapsAOABias, fuselage_lift_area;
	public final boolean canAimDown;
	public final String[] wingLiftHitboxNames;
	private final String wing_lift_k_graph_key, fuselage_lift_k_graph_key, turn_rates_graph_key;
	private AoaLiftKGraph wing_lift_k_graph, fuselage_lift_k_graph;
	private TurnRatesBySpeedGraph turn_rates_graph;
	
	public PlaneStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		JsonObject plane = UtilParse.getJsonSafe(UtilParse.getJsonSafe(json,"stats"), "plane");
		wing_area = UtilParse.getFloatSafe(plane, "wing_area", 10);
		flapsAOABias = UtilParse.getFloatSafe(plane, "flapsAOABias", 8);
		canAimDown = UtilParse.getBooleanSafe(plane, "canAimDown", false);
		fuselage_lift_area = UtilParse.getFloatSafe(plane, "fuselage_lift_area", 0);
		wing_lift_k_graph_key = UtilParse.getStringSafe(plane, "wing_lift_k_graph", "fuselage");
		fuselage_lift_k_graph_key = UtilParse.getStringSafe(plane, "fuselage_lift_k_graph", "fuselage");
		turn_rates_graph_key = UtilParse.getStringSafe(plane, "turn_rates_graph", "wooden_plane_turn_rates");
		wingLiftHitboxNames = UtilParse.getStringArraySafe(plane, "wing_lift_hitbox_names");
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

	public String getWingLiftKGraphKey() {
		return wing_lift_k_graph_key;
	}

	public String getFuselageLiftKGraphKey() {
		return fuselage_lift_k_graph_key;
	}

	public AoaLiftKGraph getWingLiftKGraph() {
		if (wing_lift_k_graph == null) 
			wing_lift_k_graph = StatGraphs.get().getAoaLiftKGraph(getWingLiftKGraphKey());
		return wing_lift_k_graph;
	}

	public AoaLiftKGraph getFuselageLiftKGraph() {
		if (fuselage_lift_k_graph == null) 
			fuselage_lift_k_graph = StatGraphs.get().getAoaLiftKGraph(getFuselageLiftKGraphKey());
		return fuselage_lift_k_graph;
	}

	public String getTurnRatesGraphKey() {
		return turn_rates_graph_key;
	}

	public TurnRatesBySpeedGraph getTurnRatesGraph() {
		if (turn_rates_graph == null)
			turn_rates_graph = StatGraphs.get().getTurnRateGraph(getTurnRatesGraphKey());
		return turn_rates_graph;
	}

}
