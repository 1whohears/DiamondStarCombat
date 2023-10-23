package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;

public class VehicleEngineSoundInstance extends DopplerSoundInstance {
	
	public final boolean isPassengerSound;
	
	public VehicleEngineSoundInstance(SoundEvent sound, LocalPlayer player, EntityVehicle entity, float velSound, boolean isPassengerSound) {
		super(sound, player, entity, 1.0f, 1.0f, velSound);
		this.isPassengerSound = isPassengerSound;
	}
	
	@Override
	public void tick() {
		EntityVehicle craft = (EntityVehicle)entity;
		float th = Math.abs(craft.getCurrentThrottle());
		if (th == 0) initVolume = th;
		else initVolume = 0.4f + 0.6f*th;;
		initPitch = 0.5f + 0.5f*th;
		boolean isPassenger = craft.isVehicleOf(player);
		if (isPassengerSound && isPassenger) {
			this.volume = initVolume;
			this.pitch = initPitch;
			this.x = player.getX();
			this.y = player.getY();
			this.z = player.getZ();
		} else if (!isPassengerSound && !isPassenger) {
			super.tick();
		} else {
			this.volume = 0;
		}
	}

}
