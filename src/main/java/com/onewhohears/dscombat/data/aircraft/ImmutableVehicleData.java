package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.data.aircraft.presets.AlexisPresets;
import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.data.aircraft.presets.CarPresets;
import com.onewhohears.dscombat.data.aircraft.presets.HeliPresets;
import com.onewhohears.dscombat.data.aircraft.presets.JaviPresets;
import com.onewhohears.dscombat.data.aircraft.presets.PlanePresets;
import com.onewhohears.dscombat.data.aircraft.presets.SubPresets;
import com.onewhohears.dscombat.data.aircraft.presets.TankPresets;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraftforge.registries.RegistryObject;

public class ImmutableVehicleData {
	
	public static final ImmutableVehicleData JAVI_PLANE_DATA = Builder.create(JaviPresets.DEFAULT_JAVI_PLANE)
			.setInteralEngineSound(ModSounds.JET_1)
			.setExternalEngineSound(ModSounds.JET_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(6, 10, 4)
			.setCrashExplosionRadius(5)
			.setCameraDistance(9)
			.setSpinRate(0)
			.setLiftKGraph(LiftKGraph.JAVI_PLANE_GRAPH)
			.setFlapsAOABias(10f)
			.setCanAimDown(true)
			.build();
	
	public static final ImmutableVehicleData ALEXIS_PLANE_DATA = Builder.create(AlexisPresets.DEFAULT_ALEXIS_PLANE)
			.setInteralEngineSound(ModSounds.JET_1)
			.setExternalEngineSound(ModSounds.JET_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(4, 8, 2)
			.setCrashExplosionRadius(5)
			.setCameraDistance(17)
			.setSpinRate(0)
			.setLiftKGraph(LiftKGraph.ALEXIS_PLANE_GRAPH)
			.setFlapsAOABias(8f)
			.setCanAimDown(false)
			.build();
	
	public static final ImmutableVehicleData WOODEN_PLANE_DATA = Builder.create(PlanePresets.DEFAULT_WOODEN_PLANE)
			.setInteralEngineSound(ModSounds.BIPLANE_1)
			.setExternalEngineSound(ModSounds.BIPLANE_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(4, 7, 3)
			.setCrashExplosionRadius(3)
			.setCameraDistance(4)
			.setSpinRate(Mth.PI)
			.setLiftKGraph(LiftKGraph.WOODEN_PLANE_GRAPH)
			.setFlapsAOABias(8f)
			.setCanAimDown(false)
			.build();
	
	public static final ImmutableVehicleData E3SENTRY_PLANE_DATA = Builder.create(PlanePresets.DEFAULT_E3SENTRY_PLANE)
			.setInteralEngineSound(ModSounds.JET_1)
			.setExternalEngineSound(ModSounds.JET_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(10, 12, 8)
			.setCrashExplosionRadius(8)
			.setCameraDistance(12)
			.setSpinRate(Mth.PI*0.01f)
			.setLiftKGraph(LiftKGraph.E3SENTRY_PLANE_GRAPH)
			.setFlapsAOABias(10f)
			.setCanAimDown(false)
			.build();
	
	public static final ImmutableVehicleData NOAH_CHOPPER_DATA = Builder.create(HeliPresets.DEFAULT_NOAH_CHOPPER)
			.setInteralEngineSound(ModSounds.HELI_1)
			.setExternalEngineSound(ModSounds.HELI_1)
			.setNegativeThrottle(false)
			.setRotationalInertia(8, 6, 4)
			.setCrashExplosionRadius(4)
			.setCameraDistance(6)
			.setSpinRate(3.141f)
			.setAlwaysLandingGear(true)
			.setHeliLiftFactor(2.75f)
			.build();
	
	public static final ImmutableVehicleData MRBUDGER_TANK_DATA = Builder.create(TankPresets.DEFAULT_MRBUDGER_TANK)
			.setInteralEngineSound(ModSounds.TANK_1)
			.setExternalEngineSound(ModSounds.TANK_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(8, 12, 8)
			.setCrashExplosionRadius(3)
			.setCameraDistance(4)
			.setSpinRate(1.5f)
			.setIsTank(true)
			.build();
	
	public static final ImmutableVehicleData SMALL_ROLLER_DATA = Builder.create(TankPresets.DEFAULT_SMALL_ROLLER)
			.setInteralEngineSound(ModSounds.TANK_1)
			.setExternalEngineSound(ModSounds.TANK_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(8, 12, 8)
			.setCrashExplosionRadius(1)
			.setCameraDistance(4)
			.setSpinRate(1.5f)
			.setIsTank(true)
			.build();
	
	public static final ImmutableVehicleData ORANGE_TESLA_DATA = Builder.create(CarPresets.DEFAULT_ORANGE_TESLA)
			.setInteralEngineSound(ModSounds.ORANGE_TESLA)
			.setExternalEngineSound(ModSounds.ORANGE_TESLA)
			.setNegativeThrottle(true)
			.setRotationalInertia(8, 12, 8)
			.setCrashExplosionRadius(2)
			.setCameraDistance(4)
			.setSpinRate(1.5f)
			.setIsTank(false)
			.build();
	
	public static final ImmutableVehicleData AXCEL_TRUCK_DATA = Builder.create(CarPresets.DEFAULT_AXCEL_TRUCK)
			.setInteralEngineSound(ModSounds.TANK_1)
			.setExternalEngineSound(ModSounds.TANK_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(8, 12, 8)
			.setCrashExplosionRadius(3)
			.setCameraDistance(7)
			.setSpinRate(1.5f)
			.setIsTank(false)
			.build();
	
	public static final ImmutableVehicleData NATHA_BOAT_DATA = Builder.create(BoatPresets.DEFAULT_NATHAN_BOAT)
			.setInteralEngineSound(ModSounds.BOAT_1)
			.setExternalEngineSound(ModSounds.BOAT_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(6, 10, 4)
			.setCrashExplosionRadius(2)
			.setCameraDistance(4)
			.setSpinRate(3.141f)
			.build();
	
	public static final ImmutableVehicleData GRONK_BATTLESHIP_DATA = Builder.create(BoatPresets.DEFAULT_GRONK_BATTLESHIP)
			.setInteralEngineSound(ModSounds.BOAT_1)
			.setExternalEngineSound(ModSounds.BOAT_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(6, 10, 4)
			.setCrashExplosionRadius(8)
			.setCameraDistance(30)
			.setSpinRate(3.141f)
			.build();
	
	public static final ImmutableVehicleData DESTROYER_DATA = Builder.create(BoatPresets.DEFAULT_DESTROYER)
			.setInteralEngineSound(ModSounds.BOAT_1)
			.setExternalEngineSound(ModSounds.BOAT_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(6, 10, 4)
			.setCrashExplosionRadius(8)
			.setCameraDistance(22)
			.setSpinRate(3.141f)
			.build();
	
	public static final ImmutableVehicleData CRUISER_DATA = Builder.create(BoatPresets.DEFAULT_NATHAN_BOAT)
			.setInteralEngineSound(ModSounds.BOAT_1)
			.setExternalEngineSound(ModSounds.BOAT_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(6, 10, 4)
			.setCrashExplosionRadius(8)
			.setCameraDistance(26)
			.setSpinRate(3.141f)
			.build();
	
	public static final ImmutableVehicleData CORVETTE_DATA = Builder.create(BoatPresets.DEFAULT_CORVETTE)
			.setInteralEngineSound(ModSounds.BOAT_1)
			.setExternalEngineSound(ModSounds.BOAT_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(6, 10, 4)
			.setCrashExplosionRadius(8)
			.setCameraDistance(13)
			.setSpinRate(3.141f)
			.build();
	
	public static final ImmutableVehicleData AIRCRAFT_CARRIER_DATA = Builder.create(BoatPresets.DEFAULT_AIRCRAFT_CARRIER)
			.setInteralEngineSound(ModSounds.BOAT_1)
			.setExternalEngineSound(ModSounds.BOAT_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(8, 10, 8)
			.setCrashExplosionRadius(10)
			.setCameraDistance(34)
			.setSpinRate(3.141f)
			.build();
	
	public static final ImmutableVehicleData ANDOLF_SUB_DATA = Builder.create(SubPresets.DEFAULT_ANDOLF_SUB)
			.setInteralEngineSound(ModSounds.SUB_1)
			.setExternalEngineSound(ModSounds.SUB_1)
			.setNegativeThrottle(true)
			.setRotationalInertia(6, 10, 3)
			.setCrashExplosionRadius(6)
			.setCameraDistance(12)
			.setSpinRate(3.141f)
			.build();
	
	public final AircraftPreset defaultPreset;
	public final RegistryObject<SoundEvent> externalEngineSound;
	public final RegistryObject<SoundEvent> internalEngineSound;
	public final boolean negativeThrottle;
	public final float Ix, Iy, Iz;
	public final float crashExplosionRadius;
	public final double cameraDistance;
	/**
	 * VEHICLES WITH A SPINNY THING
	 */
	public final float spinRate;
	/**
	 * PLANES ONLY
	 */
	public final LiftKGraph liftKGraph;
	/**
	 * PLANES ONLY
	 */
	public final float flapsAOABias;
	/**
	 * PLANES ONLY
	 */
	public final boolean canAimDown;
	/**
	 * HELI ONLY
	 */
	public final boolean alwaysLandingGear;
	/**
	 * HELI ONLY
	 */
	public final float heliLiftFactor;
	/**
	 * GROUND VEHICLE ONLY
	 */
	public final boolean isTank;
	
	public ImmutableVehicleData(AircraftPreset defaultPreset, 
			RegistryObject<SoundEvent> externalEngineSound,
			RegistryObject<SoundEvent> internalEngineSound,
			boolean negativeThrottle, float Ix, float Iy, float Iz,
			float crashExplosionRadius, double cameraDistance, float spinRate, 
			LiftKGraph liftKGraph, float flapsAOABias, boolean canAimDown,
			boolean alwaysLandingGear, float heliLiftFactor, 
			boolean isTank) {
		this.defaultPreset = defaultPreset;
		this.externalEngineSound = externalEngineSound;
		this.internalEngineSound = internalEngineSound;
		this.negativeThrottle = negativeThrottle;
		this.Ix = Ix; this.Iy = Iy; this.Iz = Iz;
		this.crashExplosionRadius = crashExplosionRadius;
		this.cameraDistance = cameraDistance;
		this.spinRate = spinRate;
		this.liftKGraph = liftKGraph;
		this.flapsAOABias = flapsAOABias;
		this.canAimDown = canAimDown;
		this.alwaysLandingGear = alwaysLandingGear;
		this.heliLiftFactor = heliLiftFactor;
		this.isTank = isTank;
	}
	
	public static class Builder {
		
		private final AircraftPreset defaultPreset;
		private RegistryObject<SoundEvent> externalEngineSound = ModSounds.BIPLANE_1;
		private RegistryObject<SoundEvent> internalEngineSound = ModSounds.BIPLANE_1;
		private boolean negativeThrottle = false;
		private float Ix = 4, Iy = 4, Iz = 4;
		private float crashExplosionRadius = 3;
		private double cameraDistance = 4;
		private float spinRate = Mth.PI;
		private LiftKGraph liftKGraph = LiftKGraph.WOODEN_PLANE_GRAPH;
		private float flapsAOABias = 8;
		private boolean canAimDown = false;
		private boolean alwaysLandingGear = false;
		private float heliLiftFactor = 1f;
		private boolean isTank = false;
		
		public static Builder create(AircraftPreset defaultPreset) {
			return new Builder(defaultPreset);
		}
		
		public ImmutableVehicleData build() {
			return new ImmutableVehicleData(defaultPreset, externalEngineSound, internalEngineSound,
					negativeThrottle, Ix, Iy, Iz, crashExplosionRadius, cameraDistance, spinRate, 
					liftKGraph, flapsAOABias, canAimDown, alwaysLandingGear, heliLiftFactor, isTank);
		}
		
		public Builder setExternalEngineSound(RegistryObject<SoundEvent> sound) {
			this.externalEngineSound = sound;
			return this;
		}
		
		public Builder setInteralEngineSound(RegistryObject<SoundEvent> sound) {
			this.internalEngineSound = sound;
			return this;
		}
		
		public Builder setNegativeThrottle(boolean negative) {
			this.negativeThrottle = negative;
			return this;
		}
		
		public Builder setRotationalInertia(float Ix, float Iy, float Iz) {
			this.Ix = Ix;
			this.Iy = Iy;
			this.Iz = Iz;
			return this;
		}
		
		public Builder setCrashExplosionRadius(float radius) {
			this.crashExplosionRadius = radius;
			return this;
		}
		
		public Builder setSpinRate(float spinRate) {
			this.spinRate = spinRate;
			return this;
		}
		
		/**
		 * PLANES ONLY
		 */
		public Builder setLiftKGraph(LiftKGraph graph) {
			this.liftKGraph = graph;
			return this;
		}
		
		/**
		 * PLANES ONLY
		 */
		public Builder setFlapsAOABias(float bias) {
			this.flapsAOABias = bias;
			return this;
		}
		
		/**
		 * PLANES ONLY
		 */
		public Builder setCanAimDown(boolean canAimDown) {
			this.canAimDown = canAimDown;
			return this;
		}
		
		public Builder setCameraDistance(double distance) {
			this.cameraDistance = distance;
			return this;
		}
		
		/**
		 * HELI ONLY
		 */
		public Builder setAlwaysLandingGear(boolean gear) {
			this.alwaysLandingGear = gear;
			return this;
		}
		
		/**
		 * HELI ONLY
		 */
		public Builder setHeliLiftFactor(float factor) {
			this.heliLiftFactor = factor;
			return this;
		}
		
		/**
		 * GROUND VEHICLE ONLY
		 */
		public Builder setIsTank(boolean isTank) {
			this.isTank = isTank;
			return this;
		}
		
		protected Builder(AircraftPreset defaultPreset) {
			this.defaultPreset = defaultPreset;
		}
		
	}
	
}
