package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;

public class PlaneEngineOnPlayerSoundInstance extends DopplerOnPlayerSoundInstance {

	public PlaneEngineOnPlayerSoundInstance(SoundEvent sound, LocalPlayer player, EntityAircraft entity, float velSound) {
		super(sound, player, entity, 1.0f, 1.0f, velSound);
	}
	
	@Override
	public void tick() {
		EntityAircraft craft = (EntityAircraft)this.entity;
		float th = Math.abs(craft.getCurrentThrottle());
		if (th == 0) initVolume = th;
		else initVolume = 0.4f + 0.6f*th;;
		initPitch = 0.5f + 0.5f*th;
		if (craft.isVehicleOf(player)) {
			this.volume = initVolume;
			this.pitch = initPitch;
			this.x = entity.getX();
			this.y = entity.getY();
			this.z = entity.getZ();
		} else super.tick();
	}

}
