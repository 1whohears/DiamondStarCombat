package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.EngineStats;

public class EngineInstance<T extends EngineStats> extends PartInstance<T> {

	public EngineInstance(T stats) {
		super(stats);
	}

}
