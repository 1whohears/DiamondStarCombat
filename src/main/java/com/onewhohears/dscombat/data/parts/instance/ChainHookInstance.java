package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.ChainHookStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

public class ChainHookInstance<T extends ChainHookStats> extends PartInstance<T> {

	public ChainHookInstance(T stats) {
		super(stats);
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}

}