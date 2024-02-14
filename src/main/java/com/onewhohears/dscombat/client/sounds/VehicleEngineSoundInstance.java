package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;

public class VehicleEngineSoundInstance extends DopplerSoundInstance {
	
	public final boolean isPassengerSound;
	
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
		EntityVehicle craft = (EntityVehicle)entity;
		if (craft.getCurrentThrottle() == 0) {
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
		float th = Math.abs(craft.getCurrentThrottle());
		if (th == 0) initVolume = 0;
		else initVolume = 0.2f + 0.8f*th;;
		initPitch = 0.6f + 0.4f*th;
	}

}
