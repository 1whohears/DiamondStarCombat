package com.onewhohears.dscombat.data.graph;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.jsonpreset.JsonPresetReloadListener;

public class StatGraphs extends JsonPresetReloadListener<Graph<?,?>> {
	
	private static StatGraphs instance;
	
	public static StatGraphs get() {
		if (instance == null) instance = new StatGraphs();
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private Graph<?,?>[] allPresets;
	private List<AoaLiftKGraph> aoaLiftKGraphs;
	private List<TurnRatesBySpeedGraph> turnRateGraphs;
	
	public StatGraphs() {
		super("stat_graph");
	}

	@Override
	public Graph<?,?>[] getAll() {
		if (allPresets == null) 
			allPresets = presetMap.values().toArray(new Graph[presetMap.size()]);
		return allPresets;
	}
	
	public List<AoaLiftKGraph> getAoaLiftKGraphs() {
		if (aoaLiftKGraphs == null) 
			aoaLiftKGraphs = getPresetsOfType(GraphType.AOALIFTK);
		return aoaLiftKGraphs;
	}
	
	public List<TurnRatesBySpeedGraph> getTurnRateGraphs() {
		if (turnRateGraphs == null) 
			turnRateGraphs = getPresetsOfType(GraphType.TURN_RATES_SPEED);
		return turnRateGraphs;
	}
	
	public AoaLiftKGraph getAoaLiftKGraph(String id) {
		for (AoaLiftKGraph g : getAoaLiftKGraphs()) 
			if (g.getId().equals(id)) 
				return g;
		return getAoaLiftKGraph("fuselage");
	}
	
	@Nullable
	public TurnRatesBySpeedGraph getTurnRateGraph(String id) {
		for (TurnRatesBySpeedGraph g : getTurnRateGraphs()) 
			if (g.getId().equals(id)) 
				return g;
		return null;
	}

	@Override
	protected void resetCache() {
		allPresets = null;
		aoaLiftKGraphs = null;
	}

	@Override
	public void registerDefaultPresetTypes() {
		addPresetType(GraphType.FLOATFLOAT);
		addPresetType(GraphType.AOALIFTK);
		addPresetType(GraphType.FLOATFLOAT_MULTI);
		addPresetType(GraphType.TURN_RATES_SPEED);
	}

}
