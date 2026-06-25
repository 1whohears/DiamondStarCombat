package com.onewhohears.dscombat.data.vehicle;

import java.util.HashMap;
import java.util.Map;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.client.util.UtilClientSafeSounds;
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
	protected float prevYaw = 0;
	protected int lastTankTurnSoundTime = 0; // Cooldown for tank turn sound
	protected static final int TANK_TURN_SOUND_COOLDOWN = 20; // Ticks between sound triggers (1 second)
	protected int soundCooldown = 0; // Cooldown to prevent rapid sound creation
	protected static final int SOUND_COOLDOWN_TICKS = 10; // Minimum ticks between sound creations
	
	protected VehicleLoopingSounds(EntityVehicle parent) {
		this.parent = parent;
	}
	public void baseTick() {
		// Decrease cooldown timer
		if (soundCooldown > 0) soundCooldown--;
		
		// Only trigger sounds if cooldown has expired
		if (soundCooldown <= 0) {
			if (prevThrottle == 0 && parent.getCurrentThrottle() != 0) {
				onThrottleReset();
				soundCooldown = SOUND_COOLDOWN_TICKS; // Set cooldown after creating sound
			}
			if (prevVelSqr <= 0.01 && parent.getDeltaMovement().lengthSqr() > 0.01) {
				onVelReset();
				soundCooldown = SOUND_COOLDOWN_TICKS; // Set cooldown after creating sound
			}
			// Check if tank is turning in place (has fuel and is turning)
			if (isTankTurningInPlace()) {
				onTankTurning();
				soundCooldown = SOUND_COOLDOWN_TICKS; // Set cooldown after creating sound
			}
		}
		
		tick();
		prevThrottle = parent.getCurrentThrottle();
		prevVelSqr = parent.getDeltaMovement().lengthSqr();
		prevYaw = parent.inputs.yaw;
	}

	protected boolean isTankTurningInPlace() {
		// Only check on client side
		if (!parent.isClientSide()) return false;
		// Check if this is a car/ground vehicle
		if (parent.getVehicleType() != VehicleType.CAR) return false;
		// Check if it's specifically a tank
		if (parent.getStats().asCar() == null || !parent.getStats().asCar().isTank) return false;
		// Check if tank has fuel
		if (parent.getCurrentFuel() <= 0) return false;
		// Check if tank is operational
		if (!parent.isOperational()) return false;
		// IMPORTANT: Only trigger when tank is NOT moving (throttle is 0)
		// This prevents duplicate sound when tank is already moving forward/backward
		if (parent.getCurrentThrottle() != 0) return false;
		// Check cooldown - prevent sound from triggering too frequently
		int currentTime = parent.tickCount;
		if (currentTime - lastTankTurnSoundTime < TANK_TURN_SOUND_COOLDOWN) return false;
		// Check if tank is turning (yaw input changed from 0 to non-zero)
		// This triggers sound when player starts turning from stationary position
		if (prevYaw == 0 && parent.inputs.yaw != 0) {
			lastTankTurnSoundTime = currentTime; // Update cooldown timer
			return true;
		}
		return false;
	}

	public abstract void loadPreset(CompoundTag sounds);
	protected abstract void tick();
	protected abstract void onThrottleReset();
	protected abstract void onVelReset();
	protected abstract void onTankTurning();
    protected SoundEvent getSoundById(String id, SoundEvent alt) {
        return UtilSound.getSoundById(id, alt, parent.getWorld().registryAccess());
    }

	public interface VehicleLoopingSoundsFactory {
		VehicleLoopingSounds create(EntityVehicle parent);
	}

	public static class BasicLooper extends VehicleLoopingSounds {
		protected SoundEvent nonPassengerEngine = ModSounds.BIPLANE_1;
		protected SoundEvent passengerEngine = ModSounds.BIPLANE_1;
		protected SoundEvent nonPassengerIdle = null;
		protected SoundEvent passengerIdle = null;
		protected int lastIdleSoundTime = 0;
		protected static final int IDLE_SOUND_INTERVAL = 100; // Play idle sound every 5 seconds (100 ticks)
		
		protected BasicLooper(EntityVehicle parent) {
			super(parent);
		}
		@Override
		public void loadPreset(CompoundTag sounds) {
			nonPassengerEngine = getSoundById(sounds.getString("nonPassengerEngine"), nonPassengerEngine);
			passengerEngine = getSoundById(sounds.getString("passengerEngine"), passengerEngine);
			// Load idle sounds if specified
			if (sounds.contains("nonPassengerIdle")) {
				nonPassengerIdle = getSoundById(sounds.getString("nonPassengerIdle"), null);
			}
			if (sounds.contains("passengerIdle")) {
				passengerIdle = getSoundById(sounds.getString("passengerIdle"), null);
			}
		}
		@Override
		protected void tick() {
			// Play idle sound periodically when vehicle is stationary with fuel
			if (shouldPlayIdleSound()) {
				playIdleSound();
				lastIdleSoundTime = parent.tickCount;
			}
		}
		
		protected boolean shouldPlayIdleSound() {
			// Only on client side
			if (!parent.isClientSide()) return false;
			// Only play if idle sounds are configured
			if (nonPassengerIdle == null && passengerIdle == null) return false;
			// Check if vehicle is stationary (no throttle, minimal movement)
			if (parent.getCurrentThrottle() != 0) return false;
			// More lenient movement check - allow very small movements
			if (parent.getDeltaMovement().lengthSqr() > 0.001) return false;
			// Check if vehicle has fuel and is operational
			if (parent.getCurrentFuel() <= 0) return false;
			if (!parent.isOperational()) return false;
			// Check if enough time has passed since last idle sound
			int currentTime = parent.tickCount;
			if (currentTime - lastIdleSoundTime < IDLE_SOUND_INTERVAL) return false;
			return true;
		}
		
		protected void playIdleSound() {
			if (!parent.isClientSide()) return;
			UtilClientSafeSounds.vehicleIdleSound(parent, nonPassengerIdle, passengerIdle);
		}
		
		@Override
		protected void onThrottleReset() {
			UtilClientSafeSounds.nonPassengerVehicleEngineSound(parent, nonPassengerEngine);
			UtilClientSafeSounds.passengerVehicleEngineSound(parent, passengerEngine);
		}
		@Override
		protected void onVelReset() {
		}
		@Override
		protected void onTankTurning() {
			// Play engine sound when tank turns in place
			UtilClientSafeSounds.nonPassengerVehicleEngineSound(parent, nonPassengerEngine);
			UtilClientSafeSounds.passengerVehicleEngineSound(parent, passengerEngine);
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
			externalAfterBurnerClose = getSoundById(sounds.getString("externalAfterBurnerClose"), externalAfterBurnerClose);
			externalAfterBurnerFar = getSoundById(sounds.getString("externalAfterBurnerFar"), externalAfterBurnerFar);
			externalRPM = getSoundById(sounds.getString("externalRPM"), externalRPM);
			externalWindClose = getSoundById(sounds.getString("externalWindClose"), externalWindClose);
			externalWindFar = getSoundById(sounds.getString("externalWindFar"), externalWindFar);
			cockpitRPM = getSoundById(sounds.getString("cockpitRPM"), cockpitRPM);
			cockpitAfterBurner = getSoundById(sounds.getString("cockpitAfterBurner"), cockpitAfterBurner);
			cockpitWindSlow = getSoundById(sounds.getString("cockpitWindSlow"), cockpitWindSlow);
			cockpitWindFast = getSoundById(sounds.getString("cockpitWindFast"), cockpitWindFast);
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
		@Override
		protected void onTankTurning() {
			// Fighter jets don't turn in place like tanks, so do nothing
		}
	}

	static {
		addVehicleLoopSoundManager("basic", (parent) -> new BasicLooper(parent));
		addVehicleLoopSoundManager("fighter_jet", (parent) -> new FighterJetLooper(parent));
	}

}
