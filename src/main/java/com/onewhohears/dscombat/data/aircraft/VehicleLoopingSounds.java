package com.onewhohears.dscombat.data.aircraft;

import java.util.HashMap;
import java.util.Map;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilClientSafeSounds;
import com.onewhohears.dscombat.util.UtilSound;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;

public abstract class VehicleLoopingSounds {
	
	public static VehicleLoopingSounds getByType(String type, EntityVehicle parent) {
		if (loopFactories.containsKey(type)) return loopFactories.get(type).create(parent);
		return new BasicLooper(parent);
	}
	private static Map<String, VehicleLoopingSoundsFactory> loopFactories = new HashMap<>();
	public static void addVehicleLoopSoundManager(String type, VehicleLoopingSoundsFactory factory) {
		loopFactories.put(type, factory);
	}
	
	public final EntityVehicle parent;
	protected float prevThrottle = 0;
	protected double prevVelSqr = 0;
	protected VehicleLoopingSounds(EntityVehicle parent) {
		this.parent = parent;
	}
	public void baseTick() {
		if (prevThrottle == 0 && parent.getCurrentThrottle() != 0) onThrottleReset();
		if (prevVelSqr == 0 && parent.getDeltaMovement().lengthSqr() > 0) onVelReset();
		tick();
		prevThrottle = parent.getCurrentThrottle();
		prevVelSqr = parent.getDeltaMovement().lengthSqr();
	}
	public abstract void load(CompoundTag sounds);
	protected abstract void tick();
	protected abstract void onThrottleReset();
	protected abstract void onVelReset();
	
	public static interface VehicleLoopingSoundsFactory {
		VehicleLoopingSounds create(EntityVehicle parent);
	}
	
	public static class BasicLooper extends VehicleLoopingSounds {
		protected SoundEvent nonPassengerEngine = ModSounds.BIPLANE_1.get();
		protected SoundEvent passengerEngine = ModSounds.BIPLANE_1.get();
		protected BasicLooper(EntityVehicle parent) {
			super(parent);
		}
		@Override
		public void load(CompoundTag sounds) {
			String nonPassengerEngineId = sounds.getString("nonPassengerEngine");
			nonPassengerEngine = UtilSound.getSoundById(nonPassengerEngineId, nonPassengerEngine);
			String passengerEngineId = sounds.getString("passengerEngine");
			passengerEngine = UtilSound.getSoundById(passengerEngineId, passengerEngine);
		}
		@Override
		protected void tick() {
		}
		@Override
		protected void onThrottleReset() {
			UtilClientSafeSounds.nonPassengerVehicleEngineSound(parent, nonPassengerEngine);
			UtilClientSafeSounds.passengerVehicleEngineSound(parent, passengerEngine);
		}
		@Override
		protected void onVelReset() {
		}
	}
	
	public static class FighterJetLooper extends VehicleLoopingSounds {
		protected SoundEvent externalAfterBurnerNear, externalAfterBurnerFar;
		protected SoundEvent externalWindNear, externalWindFar;
		protected SoundEvent externalGroundedLowRPM;
		protected SoundEvent cockpitRPM, cockpitAfterBurner;
		protected SoundEvent cockpitWindSlow, cockpitWindFast;
		protected FighterJetLooper(EntityVehicle parent) {
			super(parent);
		}
		@Override
		public void load(CompoundTag sounds) {
			
		}
		@Override
		protected void tick() {
			
		}
		@Override
		protected void onThrottleReset() {
			
		}
		@Override
		protected void onVelReset() {
			
		}
	}
	
	static {
		addVehicleLoopSoundManager("basic", (parent) -> new BasicLooper(parent));
		addVehicleLoopSoundManager("fighter_jet", (parent) -> new FighterJetLooper(parent));
	}
	
}
