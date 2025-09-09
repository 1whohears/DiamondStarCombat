package com.onewhohears.dscombat.data.vehicle;

import java.util.HashMap;
import java.util.Map;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
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
		if (prevVelSqr <= 0.01 && parent.getDeltaMovement().lengthSqr() > 0.01) onVelReset();
		tick();
		prevThrottle = parent.getCurrentThrottle();
		prevVelSqr = parent.getDeltaMovement().lengthSqr();
	}
	public abstract void loadPreset(CompoundTag sounds);
	protected abstract void tick();
	protected abstract void onThrottleReset();
	protected abstract void onVelReset();
	
	public static interface VehicleLoopingSoundsFactory {
		VehicleLoopingSounds create(EntityVehicle parent);
	}
	
	public static class BasicLooper extends VehicleLoopingSounds {
		protected SoundEvent nonPassengerEngine = ModSounds.BIPLANE_1;
		protected SoundEvent passengerEngine = ModSounds.BIPLANE_1;
		protected BasicLooper(EntityVehicle parent) {
			super(parent);
		}
		@Override
		public void loadPreset(CompoundTag sounds) {
			nonPassengerEngine = UtilSound.getSoundById(sounds.getString("nonPassengerEngine"), nonPassengerEngine);
			passengerEngine = UtilSound.getSoundById(sounds.getString("passengerEngine"), passengerEngine);
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
		protected SoundEvent externalAfterBurnerClose = ModSounds.ALEXIS_EXT_AFTERBURNER_CLOSE;
		protected SoundEvent externalAfterBurnerFar = ModSounds.ALEXIS_EXT_AFTERBURNER_FAR;
		protected SoundEvent externalRPM = ModSounds.ALEXIS_EXT_RPM;
		protected SoundEvent externalWindClose = ModSounds.ALEXIS_EXT_WIND_CLOSE;
		protected SoundEvent externalWindFar = ModSounds.ALEXIS_EXT_WIND_FAR;
		protected SoundEvent cockpitRPM = ModSounds.ALEXIS_CP_RPM;
		protected SoundEvent cockpitAfterBurner = ModSounds.ALEXIS_CP_AFTERBURNER;
		protected SoundEvent cockpitWindSlow = ModSounds.ALEXIS_CP_WIND_SLOW;
		protected SoundEvent cockpitWindFast = ModSounds.ALEXIS_CP_WIND_FAST;
		protected FighterJetLooper(EntityVehicle parent) {
			super(parent);
		}
		@Override
		public void loadPreset(CompoundTag sounds) {
			externalAfterBurnerClose = UtilSound.getSoundById(sounds.getString("externalAfterBurnerClose"), externalAfterBurnerClose);
			externalAfterBurnerFar = UtilSound.getSoundById(sounds.getString("externalAfterBurnerFar"), externalAfterBurnerFar);
			externalRPM = UtilSound.getSoundById(sounds.getString("externalRPM"), externalRPM);
			externalWindClose = UtilSound.getSoundById(sounds.getString("externalWindClose"), externalWindClose);
			externalWindFar = UtilSound.getSoundById(sounds.getString("externalWindFar"), externalWindFar);
			cockpitRPM = UtilSound.getSoundById(sounds.getString("cockpitRPM"), cockpitRPM);
			cockpitAfterBurner = UtilSound.getSoundById(sounds.getString("cockpitAfterBurner"), cockpitAfterBurner);
			cockpitWindSlow = UtilSound.getSoundById(sounds.getString("cockpitWindSlow"), cockpitWindSlow);
			cockpitWindFast = UtilSound.getSoundById(sounds.getString("cockpitWindFast"), cockpitWindFast);
		}
		@Override
		protected void tick() {
			
		}
		@Override
		protected void onThrottleReset() {
			UtilClientSafeSounds.nonPassengerVehicleEngineSound(parent, externalRPM, 90);
			UtilClientSafeSounds.nonPassengerAfterBurnerSound(parent, externalAfterBurnerClose, 90, 0);
			UtilClientSafeSounds.nonPassengerAfterBurnerSound(parent, externalAfterBurnerFar, 250, 80);
			
			UtilClientSafeSounds.passengerVehicleEngineSound(parent, cockpitRPM);
			UtilClientSafeSounds.passengerAfterBurnerSound(parent, cockpitAfterBurner);
			
		}
		@Override
		protected void onVelReset() {
			UtilClientSafeSounds.nonPassengerWindSound(parent, externalWindClose, 90, 0);
			UtilClientSafeSounds.nonPassengerWindSound(parent, externalWindFar, 250, 80);
			
			UtilClientSafeSounds.passengerWindSound(parent, cockpitWindSlow, 0);
			UtilClientSafeSounds.passengerWindSound(parent, cockpitWindFast, 1);
		}
	}
	
	static {
		addVehicleLoopSoundManager("basic", (parent) -> new BasicLooper(parent));
		addVehicleLoopSoundManager("fighter_jet", (parent) -> new FighterJetLooper(parent));
	}
	
}
