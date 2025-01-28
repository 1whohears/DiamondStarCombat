package com.onewhohears.dscombat.util;

import java.util.NoSuchElementException;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.client.sounds.AfterBurnerSoundInstance;
import com.onewhohears.dscombat.client.sounds.DopplerSoundInstance;
import com.onewhohears.dscombat.client.sounds.PlaneMusicSoundInstance;
import com.onewhohears.dscombat.client.sounds.VehicleEngineSoundInstance;
import com.onewhohears.dscombat.client.sounds.VehicleWindSoundInstance;
import com.onewhohears.dscombat.data.vehicle.DSCPhyCons;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.RegistryObject;

public class UtilClientSafeSounds {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public static void dopplerSound(Entity entity, SoundEvent sound, 
			float initVolume, float initPitch, float velSound, boolean delayed) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		int delay = 0;
		if (delayed) delay = (int)(p.distanceTo(entity) / DSCPhyCons.VEL_SOUND);
		m.getSoundManager().playDelayed(new DopplerSoundInstance(sound, 
				p, entity, initVolume, initPitch, velSound), delay);
	}
	
	public static void nonPassengerVehicleEngineSound(EntityVehicle plane, SoundEvent sound, double range) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleEngineSoundInstance(sound, 
				p, plane, DSCPhyCons.VEL_SOUND, false, range, 0));
	}
	
	public static void nonPassengerVehicleEngineSound(EntityVehicle plane, SoundEvent sound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleEngineSoundInstance(sound, 
				p, plane, DSCPhyCons.VEL_SOUND, false));
	}
	
	public static void passengerVehicleEngineSound(EntityVehicle plane, SoundEvent sound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleEngineSoundInstance(sound, 
				p, plane, DSCPhyCons.VEL_SOUND, true));
	}
	
	public static void nonPassengerAfterBurnerSound(EntityVehicle plane, SoundEvent sound, double range, float minDist) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new AfterBurnerSoundInstance(sound, 
				p, plane, DSCPhyCons.VEL_SOUND, false, range, minDist));
	}
	
	public static void passengerAfterBurnerSound(EntityVehicle plane, SoundEvent sound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new AfterBurnerSoundInstance(sound, 
				p, plane, DSCPhyCons.VEL_SOUND, true));
	}
	
	public static void nonPassengerWindSound(EntityVehicle plane, SoundEvent sound, double range, float minDist) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleWindSoundInstance(sound, 
				p, plane, DSCPhyCons.VEL_SOUND, false, range, minDist, 0));
	}
	
	public static void passengerWindSound(EntityVehicle plane, SoundEvent sound, double minSpeed) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleWindSoundInstance(sound, 
				p, plane, DSCPhyCons.VEL_SOUND, true, minSpeed));
	}
	
	public static void aircraftRadio(EntityVehicle plane, SoundEvent sound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new PlaneMusicSoundInstance(sound, 
				p, plane, DSCPhyCons.VEL_SOUND));
	}
	
	public static void aircraftRadio(EntityVehicle plane, String sound) {
		try {
			SoundEvent se = UtilSound.getSoundById(sound, SoundEvents.MUSIC_DISC_BLOCKS);
			aircraftRadio(plane, se);
		} catch (NoSuchElementException e) {
			LOGGER.error("ERROR: "+sound+" does not exist!");
		}
	}
	
	public static void playCockpitSound(RegistryObject<SoundEvent> sound, float pitch, float volume) {
		if (sound == null) return;
		playCockpitSound(sound.get(), pitch, volume);
	}
	
	public static void playCockpitSound(SoundEvent sound, float pitch, float volume) {
		if (sound == null) return;
		Minecraft m = Minecraft.getInstance();
		m.getSoundManager().play(forCockpit(sound, pitch, volume));
	}
	
	private static long prevQueueTime = 0;
	private static int waitTime = 0;
	
	public static void queueCockpitSound(SoundEvent sound, float pitch, float volume, int length) {
		if (sound == null) return;
		Minecraft m = Minecraft.getInstance();
		long currentTime = m.level.getGameTime();
		long timeDiff = currentTime - prevQueueTime;
		waitTime -= timeDiff;
		if (waitTime < 0) waitTime = 0;
		m.getSoundManager().playDelayed(forCockpit(sound, pitch, volume), waitTime);
		waitTime += length;
		prevQueueTime = currentTime;
	}
	
	public static SimpleSoundInstance forCockpit(SoundEvent sound, float pitch, float volume) {
		if (sound == null) sound = SoundEvents.VILLAGER_YES;
		return new SimpleSoundInstance(sound.getLocation(), SoundSource.PLAYERS, volume, pitch, 
				SoundInstance.createUnseededRandom(), false, 0, 
				SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
	}

	public static boolean isClientRidingVehicle(EntityVehicle vehicle) {
		Minecraft m = Minecraft.getInstance();
		if (m.player == null) return false;
		return m.player.getRootVehicle().equals(vehicle);
	}
	
}
