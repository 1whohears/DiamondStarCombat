package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.ChaffDispenserStats;

public class ChaffDispenserInstance<T extends ChaffDispenserStats> extends PartInstance<T> {

	public ChaffDispenserInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void setFilled(String param) {
		super.setFilled(param);
	}

}
