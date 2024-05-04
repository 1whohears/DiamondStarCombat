package com.onewhohears.dscombat.data.radar;

import com.onewhohears.dscombat.data.jsonpreset.JsonPresetStats;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;

public abstract class RadarType extends JsonPresetType {
	public static final Standard STANDARD = Standard.INSTANCE;
	public static class Standard extends RadarType {
		public static final String ID = "standard";
		public static final Standard INSTANCE = new Standard();
		public Standard() {
			super(ID, (key, data) -> new RadarStats(key, data));
		}
	}
	public RadarType(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
		super(id, statsFactory);
	}

}
