package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.SeatStats;

public class SeatInstance<T extends SeatStats> extends PartInstance<T> {

	public SeatInstance(T stats) {
		super(stats);
	}

}
