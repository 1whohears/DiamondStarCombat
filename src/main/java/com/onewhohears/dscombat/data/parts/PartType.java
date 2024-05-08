package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.jsonpreset.JsonPresetStats;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;

public abstract class PartType extends JsonPresetType {
	public static final Seat SEAT = Seat.INSTANCE;
	public static class Seat extends PartType {
		public static final String ID = "seat";
		public static final Seat INSTANCE = new Seat();
		public Seat() {
			super(ID, (key, data) -> null);
		}
		public Seat(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
			super(id, statsFactory);
		}
		@Override
		public boolean isSteat() {
			return true;
		}
	}
	public static final Turret TURRENT = Turret.INSTANCE;
	public static class Turret extends Seat {
		public static final String ID = "turret";
		public static final Turret INSTANCE = new Turret();
		public Turret() {
			super(ID, (key, data) -> null);
		}
	}
	public static final Weapon INTERNAL_WEAPON = Weapon.INSTANCE;
	public static class Weapon extends PartType {
		public static final String ID = "internal_weapon";
		public static final Weapon INSTANCE = new Weapon();
		public Weapon() {
			super(ID, (key, data) -> null);
		}
		public Weapon(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
			super(id, statsFactory);
		}
	}
	public static final ExternalWeapon EXTERNAL_WEAPON = ExternalWeapon.INSTANCE;
	public static class ExternalWeapon extends Weapon {
		public static final String ID = "external_weapon";
		public static final ExternalWeapon INSTANCE = new ExternalWeapon();
		public ExternalWeapon() {
			super(ID, (key, data) -> null);
		}
	}
	public static final Engine INTERNAL_ENGINE = Engine.INSTANCE;
	public static class Engine extends PartType {
		public static final String ID = "internal_engine";
		public static final Engine INSTANCE = new Engine();
		public Engine() {
			super(ID, (key, data) -> null);
		}
		public Engine(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
			super(id, statsFactory);
		}
	}
	public static final ExternalEngine EXTERNAL_ENGINE = ExternalEngine.INSTANCE;
	public static class ExternalEngine extends Engine {
		public static final String ID = "external_engine";
		public static final ExternalEngine INSTANCE = new ExternalEngine();
		public ExternalEngine() {
			super(ID, (key, data) -> null);
		}
	}
	public static final FuelTank FUEL_TANK = FuelTank.INSTANCE;
	public static class FuelTank extends PartType {
		public static final String ID = "fuel_tank";
		public static final FuelTank INSTANCE = new FuelTank();
		public FuelTank() {
			super(ID, (key, data) -> null);
		}
		public FuelTank(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
			super(id, statsFactory);
		}
	}
	public static final ExternalFuelTank EXTERNAL_FUEL_TANK = ExternalFuelTank.INSTANCE;
	public static class ExternalFuelTank extends FuelTank {
		public static final String ID = "external_fuel_tank";
		public static final ExternalFuelTank INSTANCE = new ExternalFuelTank();
		public ExternalFuelTank() {
			super(ID, (key, data) -> null);
		}
	}
	public static final Radar INTERNAL_RADAR = Radar.INSTANCE;
	public static class Radar extends PartType {
		public static final String ID = "internal_radar";
		public static final Radar INSTANCE = new Radar();
		public Radar() {
			super(ID, (key, data) -> null);
		}
		public Radar(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
			super(id, statsFactory);
		}
	}
	public static final ExternalRadar EXTERNAL_RADAR = ExternalRadar.INSTANCE;
	public static class ExternalRadar extends Radar {
		public static final String ID = "external_radar";
		public static final ExternalRadar INSTANCE = new ExternalRadar();
		public ExternalRadar() {
			super(ID, (key, data) -> null);
		}
	}
	public static final FlareDispenser FLARE_DISPENSER = FlareDispenser.INSTANCE;
	public static class FlareDispenser extends PartType {
		public static final String ID = "flare_dispenser";
		public static final FlareDispenser INSTANCE = new FlareDispenser();
		public FlareDispenser() {
			super(ID, (key, data) -> null);
		}
	}
	public static final ChaffDispenser CHAFF_DISPENSER = ChaffDispenser.INSTANCE;
	public static class ChaffDispenser extends PartType {
		public static final String ID = "chaff_dispenser";
		public static final ChaffDispenser INSTANCE = new ChaffDispenser();
		public ChaffDispenser() {
			super(ID, (key, data) -> null);
		}
	}
	public static final Buff BUFF = Buff.INSTANCE;
	public static class Buff extends PartType {
		public static final String ID = "buff";
		public static final Buff INSTANCE = new Buff();
		public Buff() {
			super(ID, (key, data) -> null);
		}
	}
	public static final Gimbal GIMBAL = Gimbal.INSTANCE;
	public static class Gimbal extends PartType {
		public static final String ID = "gimbal";
		public static final Gimbal INSTANCE = new Gimbal();
		public Gimbal() {
			super(ID, (key, data) -> null);
		}
	}
	public static final ChainHook CHAIN_HOOK = ChainHook.INSTANCE;
	public static class ChainHook extends PartType {
		public static final String ID = "chain_hook";
		public static final ChainHook INSTANCE = new ChainHook();
		public ChainHook() {
			super(ID, (key, data) -> null);
		}
	}
	public static final Storage INTERNAL_STORAGE = Storage.INSTANCE;
	public static class Storage extends PartType {
		public static final String ID = "internal_storage";
		public static final Storage INSTANCE = new Storage();
		public Storage() {
			super(ID, (key, data) -> null);
		}
		public Storage(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
			super(id, statsFactory);
		}
	}
	public static final ExternalStorage EXTERNAL_STORAGE = ExternalStorage.INSTANCE;
	public static class ExternalStorage extends PartType {
		public static final String ID = "external_storage";
		public static final ExternalStorage INSTANCE = new ExternalStorage();
		public ExternalStorage() {
			super(ID, (key, data) -> null);
		}
	}
	public PartType(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
		super(id, statsFactory);
	}
	public boolean isSteat() {
		return false;
	}
}
