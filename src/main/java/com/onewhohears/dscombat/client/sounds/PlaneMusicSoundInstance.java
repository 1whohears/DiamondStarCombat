package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class PlaneMusicSoundInstance extends DopplerSoundInstance {
	
	public final String song;
	
	public PlaneMusicSoundInstance(SoundEvent sound, LocalPlayer player, EntityAircraft entity, float velSound) {
		super(sound, player, entity, SoundSource.RECORDS, 1f, 1f, velSound, 0.000000f);
		song = sound.getLocation().toString();
		//System.out.println("NEW SOUND INSTANCE: "+song);
	}
	
	@Override
	public void tick() {
		EntityAircraft craft = (EntityAircraft)entity;
		if (!craft.getRadioSong().equals(song)) {
			stop();
			//System.out.println("SOUND INSTANCE STOP: old = "+song+" new = "+craft.getRadioSong()+" "+craft.hasRadio);
			return;
		}
		if (craft.isVehicleOf(player)) {
			this.volume = initVolume;
			this.pitch = initPitch;
			this.x = player.getX();
			this.y = player.getY();
			this.z = player.getZ();
		} else super.tick();
	}

}