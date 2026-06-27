package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.ChainHookStats;

public class ChainHookInstance<T extends ChainHookStats> extends PartInstance<T> {

	public ChainHookInstance(T stats) {
		super(stats);
	}

}
