package com.onewhohears.dscombat.data.vehicle;

import com.onewhohears.dscombat.data.jsonpreset.JsonPresetStats;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.vehicle.stats.BoatStats;
import com.onewhohears.dscombat.data.vehicle.stats.CarStats;
import com.onewhohears.dscombat.data.vehicle.stats.HeliStats;
import com.onewhohears.dscombat.data.vehicle.stats.PlaneStats;
import com.onewhohears.dscombat.data.vehicle.stats.SubmarineStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.world.entity.EntityType;

public abstract class VehicleType extends JsonPresetType {
	public static final Plane PLANE = Plane.INSTANCE;
	public static class Plane extends VehicleType {
		public static final String ID = "plane";
		public static final Plane INSTANCE = new Plane();
		public Plane() {
			super(ID, (key, data) -> new PlaneStats(key, data));
		}
		@Override
		public EntityType<? extends EntityVehicle> getEntityType() {
			return ModEntities.PLANE.get();
		}
	}
	public static final Helicopter HELICOPTER = Helicopter.INSTANCE;
	public static class Helicopter extends VehicleType {
		public static final String ID = "helicopter";
		public static final Helicopter INSTANCE = new Helicopter();
		public Helicopter() {
			super(ID, (key, data) -> new HeliStats(key, data));
		}
		@Override
		public EntityType<? extends EntityVehicle> getEntityType() {
			return ModEntities.HELICOPTER.get();
		}
	}
	public static final Car CAR = Car.INSTANCE;
	public static class Car extends VehicleType {
		public static final String ID = "car";
		public static final Car INSTANCE = new Car();
		public Car() {
			super(ID, (key, data) -> new CarStats(key, data));
		}
		@Override
		public EntityType<? extends EntityVehicle> getEntityType() {
			return ModEntities.CAR.get();
		}
	}
	public static final Boat BOAT = Boat.INSTANCE;
	public static class Boat extends VehicleType {
		public static final String ID = "boat";
		public static final Boat INSTANCE = new Boat();
		public Boat() {
			super(ID, (key, data) -> new BoatStats(key, data));
		}
		@Override
		public EntityType<? extends EntityVehicle> getEntityType() {
			return ModEntities.BOAT.get();
		}
	}
	public static final Submarine SUBMARINE = Submarine.INSTANCE;
	public static class Submarine extends VehicleType {
		public static final String ID = "submarine";
		public static final Submarine INSTANCE = new Submarine();
		public Submarine() {
			super(ID, (key, data) -> new SubmarineStats(key, data));
		}
		@Override
		public EntityType<? extends EntityVehicle> getEntityType() {
			return ModEntities.SUBMARINE.get();
		}
	}
	public VehicleType(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
		super(id, statsFactory);
	}
	public abstract EntityType<? extends EntityVehicle> getEntityType();
}
