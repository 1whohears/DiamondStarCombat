package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.EngineStats;
import com.onewhohears.dscombat.data.parts.stats.EngineStats.EngineType;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;

public class EngineInstance<T extends EngineStats> extends PartInstance<T> {

	public EngineInstance(T stats) {
		super(stats);
	}
	
	public float getPushThrust(VehicleStats vehicleStats) {
		if (isDamaged()) return 0;
		if (getStats().getEngineType() == EngineType.PUSH) {
			if (vehicleStats.max_push_thrust_per_engine != -1) {
				if (getParent() != null && getParent().isUsingAfterburner())
					return vehicleStats.max_afterburner_push_thrust_per_engine;
				return vehicleStats.max_push_thrust_per_engine;
			}
			return getStats().getThrust();
		}
		return 0;
	}
	
	public float getSpinThrust(VehicleStats vehicleStats) {
		if (isDamaged()) return 0;
		if (getStats().getEngineType() == EngineType.SPIN) {
			if (vehicleStats.max_spin_thrust_per_engine != -1)
				return vehicleStats.max_spin_thrust_per_engine;
			return getStats().getThrust();
		}
		return 0;
	}
	
	public float getEngineHeat(VehicleStats vehicleStats) {
		float heat;
		if (vehicleStats.heat_per_engine != -1) heat = vehicleStats.heat_per_engine;
		else heat = getStats().getHeat();
		if (isDamaged()) heat *= 2;
		if (getParent() != null && getParent().isUsingAfterburner()) heat *= 10;
		return heat;
	}
	
	public float getFuelPerTick(VehicleStats vehicleStats) {
		if (isDamaged()) return 0;
		float fuel;
		if (vehicleStats.fuel_consume_per_engine != -1)
			fuel = vehicleStats.fuel_consume_per_engine;
		else fuel = getStats().getFuelPerTick();
		if (getParent() != null && getParent().isUsingAfterburner()) fuel *= 4;
		return fuel;
	}
	
}
