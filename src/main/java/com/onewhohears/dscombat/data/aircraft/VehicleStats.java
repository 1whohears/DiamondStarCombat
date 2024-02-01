package com.onewhohears.dscombat.data.aircraft;

import net.minecraft.nbt.CompoundTag;

public abstract class VehicleStats {
	
	// basic
	public float max_health = 10, max_speed = 0.1f, mass = 1000;
	// defense
	public float stealth = 1, cross_sec_area = 10, idleheat = 10, base_armor = 0;
	// turn
	public float turn_radius = 100;
	public float maxroll, maxpitch, maxyaw;
	public float torqueroll, torquepitch, torqueyaw;
	public float Ix = 4, Iy = 4, Iz = 4;
	// control
	public float throttleup = 0.01f, throttledown = 0.01f;
	public boolean negativeThrottle = false;
	// other 
	public float crashExplosionRadius;
	public double cameraDistance = 4;
	public int baseTextureVariants = 1, textureLayers = 0;
	
	protected VehicleStats() {
	}
	
	public void readPresetData(AircraftPreset acp) {
		CompoundTag stats = acp.getDataAsNBT().getCompound("stats");
		max_health = stats.getFloat("max_health");
		max_speed = stats.getFloat("max_speed");
		mass = stats.getFloat("mass");
		stealth = stats.getFloat("stealth");
		cross_sec_area = stats.getFloat("cross_sec_area");
		idleheat = stats.getFloat("idleheat");
		base_armor = stats.getFloat("base_armor");
		throttleup = stats.getFloat("throttleup");
		throttledown = stats.getFloat("throttledown");
		turn_radius = stats.getFloat("turn_radius");
		maxroll = stats.getFloat("maxroll");
		maxpitch = stats.getFloat("maxpitch");
		maxyaw = stats.getFloat("maxyaw");
		torqueroll = stats.getFloat("torqueroll");
		torquepitch = stats.getFloat("torquepitch");
		torqueyaw = stats.getFloat("torqueyaw");
		Iz = stats.getFloat("inertiaroll");
		Ix = stats.getFloat("inertiapitch");
		Iy = stats.getFloat("inertiayaw");
		crashExplosionRadius = stats.getFloat("crashExplosionRadius");
		cameraDistance = stats.getFloat("cameraDistance");
		if (acp.getDataAsNBT().contains("textures")) {
			CompoundTag textures = acp.getDataAsNBT().getCompound("textures");
			if (textures.contains("baseTextureVariants")) baseTextureVariants = textures.getInt("baseTextureVariants");
			if (textures.contains("textureLayers")) textureLayers = textures.getInt("textureLayers");
		}
	}
	
	public static class PlaneStats extends VehicleStats {
		public float wing_area = 10;
		public LiftKGraph liftKGraph = LiftKGraph.WOODEN_PLANE_GRAPH;
		public float flapsAOABias = 8;
		public boolean canAimDown = false;
		public PlaneStats() {
		}
		public void readPresetData(AircraftPreset acp) {
			super.readPresetData(acp);
			CompoundTag plane = acp.getDataAsNBT().getCompound("stats").getCompound("plane");
			wing_area = plane.getFloat("wing_area");
			flapsAOABias = plane.getFloat("flapsAOABias");
			canAimDown = plane.getBoolean("canAimDown");
			liftKGraph = LiftKGraph.getGraphById(plane.getString("liftKGraph"));
		}
	}
	
	public static class HeliStats extends VehicleStats {
		public float accForward = 0.1f, accSide = 0.1f;
		public float heliLiftFactor = 1f;
		public boolean alwaysLandingGear = false;
		public HeliStats() {
		}
		public void readPresetData(AircraftPreset acp) {
			super.readPresetData(acp);
			CompoundTag heli = acp.getDataAsNBT().getCompound("stats").getCompound("heli");
			accForward = heli.getFloat("accForward");
			accSide = heli.getFloat("accSide");
			heliLiftFactor = heli.getFloat("heliLiftFactor");
			alwaysLandingGear = heli.getBoolean("alwaysLandingGear");
		}
	}
	
	public static class CarStats extends VehicleStats {
		public boolean isTank = false;
		public CarStats() {
		}
		public void readPresetData(AircraftPreset acp) {
			super.readPresetData(acp);
			CompoundTag car = acp.getDataAsNBT().getCompound("stats").getCompound("car");
			isTank = car.getBoolean("isTank");
		}
	}
	
	public static class BoatStats extends VehicleStats {
		public BoatStats() {
		}
		public void readPresetData(AircraftPreset acp) {
			super.readPresetData(acp);
		}
	}
	
	public static class SubStats extends VehicleStats {
		public SubStats() {
		}
		public void readPresetData(AircraftPreset acp) {
			super.readPresetData(acp);
		}
	}
	
}
