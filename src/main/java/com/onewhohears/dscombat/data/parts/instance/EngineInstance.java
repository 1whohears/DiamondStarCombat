package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.EngineStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

public class EngineInstance<T extends EngineStats> extends PartInstance<T> {

	public EngineInstance(T stats) {
		super(stats);
	}
	
	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}

}
