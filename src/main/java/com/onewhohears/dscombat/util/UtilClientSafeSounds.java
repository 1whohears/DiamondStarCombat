package com.onewhohears.dscombat.util;

import java.util.NoSuchElementException;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.sounds.AfterBurnerSoundInstance;
import com.onewhohears.dscombat.client.sounds.DopplerSoundInstance;
import com.onewhohears.dscombat.client.sounds.PlaneMusicSoundInstance;
import com.onewhohears.dscombat.client.sounds.VehicleEngineSoundInstance;
import com.onewhohears.dscombat.client.sounds.VehicleWindSoundInstance;
import com.onewhohears.dscombat.data.aircraft.DSCPhyCons;
import com.onewhohears.dscombat.data.aircraft.VehicleSoundManager.PassengerSoundPack;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

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
	
	public static void tickPassengerSounds(EntityVehicle vehicle, PassengerSoundPack passengerSoundPack) {
		if (!vehicle.isOperational()) return;
		Minecraft m = Minecraft.getInstance();
		if (!vehicle.equals(m.player.getRootVehicle())) return;
 		// RWR WARNINGS
		if (vehicle.tickCount%4==0 && vehicle.radarSystem.isTrackedByMissile()) {
			playCockpitSound(passengerSoundPack.missileAlert, 
				1f, Config.CLIENT.missileWarningVol.get().floatValue());
		} else if (vehicle.tickCount%8==0 && vehicle.radarSystem.isTrackedByRadar()) {
			playCockpitSound(passengerSoundPack.rwrWarn, 
				1f, Config.CLIENT.rwrWarningVol.get().floatValue());
		}
		// IR LOCK TONE
		if (vehicle.tickCount%10==0 && vehicle.shouldPlayIRTone())  {
			playCockpitSound(passengerSoundPack.irLockTone, 
	    		1f, Config.CLIENT.irTargetToneVol.get().floatValue());
		}
		if (vehicle.getAircraftType().isPlane()) {
			// STALL
			if (vehicle.isStalling()) { if (vehicle.getStallTicks() % 24 == 1) {
				playCockpitSound(passengerSoundPack.stallAlert, 1f, 
					Config.CLIENT.cockpitVoiceLineVol.get().floatValue());
			} }
			else if (vehicle.isAboutToStall()) { if (vehicle.getAboutToStallTicks() % 40 == 1) {
				playCockpitSound(passengerSoundPack.stallWarn, 1f, 
					Config.CLIENT.cockpitVoiceLineVol.get().floatValue());
			} }
			// PULL UP
			if (vehicle.getDeltaMovement().y <= -DSCPhyCons.COLLIDE_SPEED 
					&& vehicle.tickCount % 13 == 0
					&& UtilEntity.getDistFromGround(vehicle) / -vehicle.getDeltaMovement().y <= 80) {
				playCockpitSound(passengerSoundPack.pullUp, 1f, 
					Config.CLIENT.cockpitVoiceLineVol.get().floatValue());
			}
		}
		if (vehicle.getAircraftType().isAircraft) {
			// ENGINE FIRE
			if (vehicle.getEngineFireTicks() % 70 == 1) 
				playCockpitSound(passengerSoundPack.engineFire, 1f, 
					Config.CLIENT.cockpitVoiceLineVol.get().floatValue());
			// FUEL LEAK
			if (vehicle.getBingoTicks() % 150 <= 60 && vehicle.getFuelLeakTicks() % 30 == 1) 
				playCockpitSound(passengerSoundPack.fuelLeak, 1f, 
					Config.CLIENT.cockpitVoiceLineVol.get().floatValue());
			// BINGO FUEL
			if (vehicle.getBingoTicks() % 160 <= 60 && vehicle.getBingoTicks() % 20 == 1) 
				playCockpitSound(passengerSoundPack.bingoFuel, 1f, 
					Config.CLIENT.cockpitVoiceLineVol.get().floatValue());
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
	
	public static SimpleSoundInstance forCockpit(SoundEvent sound, float pitch, float volume) {
		if (sound == null) sound = SoundEvents.VILLAGER_YES;
		return new SimpleSoundInstance(sound.getLocation(), SoundSource.PLAYERS, volume, pitch, 
				SoundInstance.createUnseededRandom(), false, 0, 
				SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
	}
	
}
