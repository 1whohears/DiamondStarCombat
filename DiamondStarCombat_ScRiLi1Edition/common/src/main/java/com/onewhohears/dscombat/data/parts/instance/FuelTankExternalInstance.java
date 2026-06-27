package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.FuelTankExternalStats;

public class FuelTankExternalInstance<T extends FuelTankExternalStats> extends FuelTankInstance<T> {

	public FuelTankExternalInstance(T stats) {
		super(stats);
	}

}
