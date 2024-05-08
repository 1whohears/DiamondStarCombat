package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.ChaffDispenserStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

public class ChaffDispenserInstance<T extends ChaffDispenserStats> extends PartInstance<T> {

	public ChaffDispenserInstance(T stats) {
		super(stats);
	}
	
	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}

}
