package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.GimbalStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

public class GimbalInstance<T extends GimbalStats> extends PartInstance<T> {

	public GimbalInstance(T stats) {
		super(stats);
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}

}
