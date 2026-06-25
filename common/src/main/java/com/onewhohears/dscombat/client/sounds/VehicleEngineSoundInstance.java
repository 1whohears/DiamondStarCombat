package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;

public class VehicleEngineSoundInstance extends DopplerSoundInstance {
	
	public final boolean isPassengerSound;
	private float tankTurnThrottle = 0f; // Virtual throttle for tank turning
	private static final float TURN_THROTTLE_FADE_SPEED = 0.05f; // Speed of fade in/out
	
	public VehicleEngineSoundInstance(SoundEvent sound, LocalPlayer player, EntityVehicle entity, float velSound, boolean isPassengerSound, double range, float minDist) {
		super(sound, player, entity, 1.0f, 1.0f, velSound, range, minDist);
		this.isPassengerSound = isPassengerSound;
		if (this.isPassengerSound) {
			this.x = 0;
			this.y = 0;
			this.z = 0;
			this.attenuation = SoundInstance.Attenuation.NONE;
			this.relative = true;
		} else {
			this.attenuation = SoundInstance.Attenuation.LINEAR;
			this.relative = false;
		}
	}
	
	public VehicleEngineSoundInstance(SoundEvent sound, LocalPlayer player, EntityVehicle entity, float velSound, boolean isPassengerSound) {
		this(sound, player, entity, velSound, isPassengerSound, 128, 0);	
	}
	
	@Override
	public void tick() {
		if (entity.isRemoved()) {
			stop();
			return;
		}
		EntityVehicle craft = (EntityVehicle)entity;
		
		// Update tank turn throttle smoothly
		updateTankTurnThrottle(craft);
		
		// Stop sound if throttle is 0 AND tank turn throttle is also 0
		if (craft.getCurrentThrottle() == 0 && tankTurnThrottle <= 0.01f) {
			stop();
			return;
		}
		
		// Calculate volume and pitch first
		calcVolPitch(craft);
		
		// Check if we're a passenger once
		boolean isPassenger = craft.isVehicleOf(player);
		
		if (isPassengerSound && isPassenger) {
			this.volume = initVolume;
			this.pitch = initPitch;
		} else if (!isPassengerSound && !isPassenger) {
			super.tick();
		} else {
			this.volume = 0;
		}
	}
	
	private void updateTankTurnThrottle(EntityVehicle craft) {
		if (isTankTurning(craft)) {
			// Fade in: increase tank turn throttle towards target
			float targetThrottle = Math.abs(craft.inputs.yaw) * 0.5f;
			tankTurnThrottle += TURN_THROTTLE_FADE_SPEED;
			if (tankTurnThrottle > targetThrottle) {
				tankTurnThrottle = targetThrottle;
			}
		} else {
			// Fade out: decrease tank turn throttle towards 0
			tankTurnThrottle -= TURN_THROTTLE_FADE_SPEED;
			if (tankTurnThrottle < 0) {
				tankTurnThrottle = 0;
			}
		}
	}
	
	private boolean isTankTurning(EntityVehicle craft) {
		// Check if this is a tank
		if (craft.getVehicleType() != com.onewhohears.dscombat.data.vehicle.VehicleType.CAR) return false;
		if (craft.getStats().asCar() == null || !craft.getStats().asCar().isTank) return false;
		// Check if tank is turning (has yaw input)
		return craft.inputs.yaw != 0 && craft.getCurrentFuel() > 0 && craft.isOperational();
	}
	
	protected void calcVolPitch(EntityVehicle craft) {
		float th = Math.abs(craft.getCurrentThrottle());
		// If tank is turning in place, use smooth tank turn throttle
		if (th == 0 && tankTurnThrottle > 0) {
			th = tankTurnThrottle;
		}
		if (th == 0) initVolume = 0;
		else initVolume = 0.2f + 0.8f*th;;
		initPitch = 0.6f + 0.4f*th;
	}

}
