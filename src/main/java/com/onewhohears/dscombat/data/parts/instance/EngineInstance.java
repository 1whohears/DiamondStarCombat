package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.EngineStats;
import com.onewhohears.dscombat.data.parts.stats.EngineStats.EngineType;

public class EngineInstance<T extends EngineStats> extends PartInstance<T> {

	public EngineInstance(T stats) {
		super(stats);
	}
	
	public float getPushThrust() {
		if (isDamaged()) return 0;
		if (getStats().getEngineType() == EngineType.PUSH) return getStats().getThrust();
		return 0;
	}
	
	public float getSpinThrust() {
		if (isDamaged()) return 0;
		if (getStats().getEngineType() == EngineType.SPIN) return getStats().getThrust();
		return 0;
	}
	
	public float getEngineHeat() {
		if (isDamaged()) return getStats().getHeat() * 2;
		return getStats().getHeat();
	}
	
	public float getFuelPerTick() {
		if (isDamaged()) return 0;
		return getStats().getFuelPerTick();
	}
	
}
