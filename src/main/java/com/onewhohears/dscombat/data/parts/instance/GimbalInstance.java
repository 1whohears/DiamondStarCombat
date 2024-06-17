package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.GimbalStats;

public class GimbalInstance<T extends GimbalStats> extends PartInstance<T> {

	public GimbalInstance(T stats) {
		super(stats);
	}

}
