package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;

public class AfterBurnerSoundInstance extends VehicleEngineSoundInstance {
	
	public AfterBurnerSoundInstance(SoundEvent sound, LocalPlayer player, EntityVehicle entity, float velSound, boolean isPassengerSound, double range, float minDist) {
		super(sound, player, entity, velSound, isPassengerSound, range, minDist);
	}
	
	public AfterBurnerSoundInstance(SoundEvent sound, LocalPlayer player, EntityVehicle entity, float velSound, boolean isPassengerSound) {
		this(sound, player, entity, velSound, isPassengerSound, 128, 0);
	}
	
	@Override
	protected void calcVolPitch(EntityVehicle craft) {
		float th = Math.abs(craft.getCurrentThrottle());
		th -= 0.5f; th *= 2;
		if (th <= 0) initVolume = 0;
		else initVolume = th;
		initPitch = 0.9f + 0.1f*th;
	}

}
