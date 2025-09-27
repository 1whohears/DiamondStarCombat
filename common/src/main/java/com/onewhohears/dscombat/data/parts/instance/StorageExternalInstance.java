package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.StorageExternalStats;

public class StorageExternalInstance<T extends StorageExternalStats> extends StorageInstance<T> {

	public StorageExternalInstance(T stats) {
		super(stats);
	}

}
