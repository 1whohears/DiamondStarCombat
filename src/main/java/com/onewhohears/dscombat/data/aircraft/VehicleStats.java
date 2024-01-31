package com.onewhohears.dscombat.data.aircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

public abstract class VehicleStats {
	
	// basic
	public float max_health = 10, max_speed = 0.1f, mass = 1000;
	// defense
	public float stealth = 1, cross_sec_area = 10, idleheat = 10, base_armor = 0;
	// turn
	public float turn_radius = 100, maxroll, maxpitch, maxyaw;
	public float rolltorque, pitchtorque, yawtorque;
	public float Ix = 4, Iy = 4, Iz = 4;
	// control
	public float throttleup = 0.01f, throttledown = 0.01f;
	public boolean negativeThrottle = false;
	// other 
	public float crashExplosionRadius, spinRate = Mth.PI;
	public double cameraDistance = 4;
	public int baseTextureVariants = 1, textureLayers = 0;
	
	protected VehicleStats() {
	}
	
	public void readPresetData(AircraftPreset acp) {
		CompoundTag nbt = acp.getDataAsNBT();
		max_health = nbt.getFloat("max_health");
		max_speed = nbt.getFloat("max_speed");
		mass = nbt.getFloat("mass");
		stealth = nbt.getFloat("stealth");
		cross_sec_area = nbt.getFloat("cross_sec_area");
		idleheat = nbt.getFloat("idleheat");
		base_armor = nbt.getFloat("base_armor");
		turn_radius = nbt.getFloat("turn_radius");
		maxroll = nbt.getFloat("maxroll");
		maxpitch = nbt.getFloat("maxpitch");
		maxyaw = nbt.getFloat("maxyaw");
		rolltorque = nbt.getFloat("rolltorque");
		pitchtorque = nbt.getFloat("pitchtorque");
		yawtorque = nbt.getFloat("yawtorque");
		throttleup = nbt.getFloat("throttleup");
		throttledown = nbt.getFloat("throttledown");
		
		crashExplosionRadius = nbt.getFloat("crashExplosionRadius");
		spinRate = nbt.getFloat("spinRate");
		cameraDistance = nbt.getDouble("cameraDistance");
		baseTextureVariants = nbt.getInt("baseTextureVariants");
		textureLayers = nbt.getInt("textureLayers");
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
			CompoundTag nbt = acp.getDataAsNBT();
			wing_area = nbt.getFloat("wing_area");
			
			flapsAOABias = nbt.getFloat("flapsAOABias");
			canAimDown = nbt.getBoolean("canAimDown");
			//liftKGraph = nbt.getString("liftKGraph");
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
			CompoundTag nbt = acp.getDataAsNBT();
			accForward = nbt.getFloat("accForward");
			accSide = nbt.getFloat("accSide");
			
			heliLiftFactor = nbt.getFloat("heliLiftFactor");
			alwaysLandingGear = nbt.getBoolean("alwaysLandingGear");
		}
	}
	
	public static class CarStats extends VehicleStats {
		public boolean isTank = false;
		public CarStats() {
		}
		public void readPresetData(AircraftPreset acp) {
			super.readPresetData(acp);
			CompoundTag nbt = acp.getDataAsNBT();
			
			isTank = nbt.getBoolean("isTank");
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
