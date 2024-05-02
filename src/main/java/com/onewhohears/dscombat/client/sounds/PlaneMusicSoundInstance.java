package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class PlaneMusicSoundInstance extends DopplerSoundInstance {
	
	public final String song;
	
	public PlaneMusicSoundInstance(SoundEvent sound, LocalPlayer player, EntityVehicle entity, float velSound) {
		super(sound, player, entity, SoundSource.RECORDS, 1f, 1f, velSound, 64, 0);
		song = sound.getLocation().toString();
		x = 0;
		y = 0;
		z = 0;
		attenuation = SoundInstance.Attenuation.NONE;
		relative = true;
	}
	
	@Override
	public void tick() {
		EntityVehicle craft = (EntityVehicle)entity;
		if (!craft.getRadioSong().equals(song)) {
			stop();
			return;
		}
		if (craft.isVehicleOf(player)) {
			this.volume = initVolume;
			this.pitch = initPitch;
			this.x = 0;
			this.y = 0;
			this.z = 0;
			this.attenuation = SoundInstance.Attenuation.NONE;
			this.relative = true;
		} else {
			this.attenuation = SoundInstance.Attenuation.LINEAR;
			this.relative = false;
			super.tick();
		}
	}

}
