package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.SeatStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

public class SeatInstance<T extends SeatStats> extends PartInstance<T> {

	public SeatInstance(T stats) {
		super(stats);
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}

}
