package com.onewhohears.dscombat.data.parts.client;

import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetStats;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;

public abstract class PartClientType extends JsonPresetType {
	public static final Standard STANDARD = Standard.INSTANCE;
	public static class Standard extends PartClientType {
		public static final String ID = "standard";
		public static final Standard INSTANCE = new Standard();
		public Standard() {
			super(ID, PartClientStats::new);
		}
	}
	public static final Turret TURRET = Turret.INSTANCE;
	public static class Turret extends PartClientType {
		public static final String ID = "turret";
		public static final Turret INSTANCE = new Turret();
		public Turret() {
			super(ID, TurretClientStats::new);
		}
	}
	public static final Radar RADAR = Radar.INSTANCE;
	public static class Radar extends PartClientType {
		public static final String ID = "radar";
		public static final Radar INSTANCE = new Radar();
		public Radar() {
			super(ID, RadarClientStats::new);
		}
	}
	public static final WeaponRack WEAPON_RACK = WeaponRack.INSTANCE;
	public static class WeaponRack extends PartClientType {
		public static final String ID = "weapon_rack";
		public static final WeaponRack INSTANCE = new WeaponRack();
		public WeaponRack() {
			super(ID, WeaponRackClientStats::new);
		}
	}
	public PartClientType(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
		super(id, statsFactory);
	}
}
