package com.onewhohears.dscombat.data.vehicle.client;

import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetStats;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;

public abstract class VehicleClientType extends JsonPresetType {
	public static final Standard STANDARD = Standard.INSTANCE;
	public static class Standard extends VehicleClientType {
		public static final String ID = "standard";
		public static final Standard INSTANCE = new Standard();
		public Standard() {
			super(ID, (key, data) -> new VehicleClientStats(key, data));
		}
	}
	public VehicleClientType(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
		super(id, statsFactory);
	}
}
