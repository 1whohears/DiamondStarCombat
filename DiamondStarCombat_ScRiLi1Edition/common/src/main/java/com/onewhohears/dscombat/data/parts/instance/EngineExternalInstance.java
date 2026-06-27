package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.EngineExternalStats;

public class EngineExternalInstance<T extends EngineExternalStats> extends EngineInstance<T> {

	public EngineExternalInstance(T stats) {
		super(stats);
	}

}
