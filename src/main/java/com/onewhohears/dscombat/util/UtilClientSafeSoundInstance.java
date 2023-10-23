package com.onewhohears.dscombat.util;

import java.util.NoSuchElementException;

import com.onewhohears.dscombat.client.sounds.DopplerSoundInstance;
import com.onewhohears.dscombat.client.sounds.VehicleEngineSoundInstance;
import com.onewhohears.dscombat.client.sounds.PlaneMusicSoundInstance;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilClientSafeSoundInstance {
	
	public static void dopplerSound(Minecraft m, Entity entity, SoundEvent sound, 
			float initVolume, float initPitch, float velSound) {
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new DopplerSoundInstance(sound, 
				p, entity, initVolume, initPitch, velSound));
	}
	
	public static void nonPassengerVehicleEngineSound(Minecraft m, EntityVehicle plane, SoundEvent sound) {
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleEngineSoundInstance(sound, 
				p, plane, 10F, false));
	}
	
	public static void passengerVehicleEngineSound(Minecraft m, EntityVehicle plane, SoundEvent sound) {
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleEngineSoundInstance(sound, 
				p, plane, 10F, true));
	}
	
	public static void aircraftRadio(Minecraft m, EntityVehicle plane, SoundEvent sound) {
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new PlaneMusicSoundInstance(sound, 
				p, plane, 40F));
	}
	
	public static void aircraftRadio(Minecraft m, EntityVehicle plane, String sound) {
		try {
			SoundEvent se = ForgeRegistries.SOUND_EVENTS.getDelegate(new ResourceLocation(sound)).get().get();
			aircraftRadio(m, plane, se);
		} catch (NoSuchElementException e) {
			System.out.println("ERROR: "+sound+" does not exist!");
		}
	}
	
}
