package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.RadarExternalStats;

public class RadarExternalInstance<T extends RadarExternalStats> extends RadarPartInstance<T> {

	public RadarExternalInstance(T stats) {
		super(stats);
	}

}
