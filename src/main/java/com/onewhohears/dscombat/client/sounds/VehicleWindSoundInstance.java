package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;

public class VehicleWindSoundInstance extends DopplerSoundInstance {
	
	public final boolean isPassengerSound;
	public final double minSpeedSqr;
	
	public VehicleWindSoundInstance(SoundEvent sound, LocalPlayer player, EntityVehicle entity, float velSound, 
			boolean isPassengerSound, double range, float minDist, double minSpeed) {
		super(sound, player, entity, 1.0f, 1.0f, velSound, range, minDist);
		this.isPassengerSound = isPassengerSound;
		this.minSpeedSqr = minSpeed * minSpeed;
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
	
	public VehicleWindSoundInstance(SoundEvent sound, LocalPlayer player, EntityVehicle entity, float velSound, 
			boolean isPassengerSound, double minSpeed) {
		this(sound, player, entity, velSound, isPassengerSound, 128, 0, minSpeed);	
	}
	
	@Override
	public void tick() {
		EntityVehicle craft = (EntityVehicle)entity;
		if (craft.getDeltaMovement().lengthSqr() <= 0.01) {
			stop();
			return;
		}
		calcVolPitch(craft);
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
	
	protected void calcVolPitch(EntityVehicle craft) {
		if (craft.onGround()) {
			initVolume = 0;
			initPitch = 1;
			return;
		}
		double speedSqr = craft.getDeltaMovement().lengthSqr() - minSpeedSqr;
		if (speedSqr <= 0) {
			initVolume = 0;
			return;
		}
		if (speedSqr > 1) {
			initVolume = 1;
			initPitch = 1;
			return;
		}
		initVolume = (float) speedSqr;
		initPitch = 1;
	}

}
