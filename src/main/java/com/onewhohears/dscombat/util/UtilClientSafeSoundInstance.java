package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.client.sounds.DopplerOnPlayerSoundInstance;
import com.onewhohears.dscombat.client.sounds.PlaneEngineOnPlayerSoundInstance;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;

public class UtilClientSafeSoundInstance {
	
	public static void dopplerSound(Minecraft m, Entity entity, SoundEvent sound, float initVolume, float initPitch, float velSound) {
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new DopplerOnPlayerSoundInstance(sound, 
				p, entity, initVolume, initPitch, velSound));
	}
	
	public static void aircraftEngineSound(Minecraft m, EntityAircraft plane, SoundEvent sound) {
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new PlaneEngineOnPlayerSoundInstance(sound, 
				p, plane, 10F));
	}
	
}
