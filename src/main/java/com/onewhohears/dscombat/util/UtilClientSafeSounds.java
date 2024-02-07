package com.onewhohears.dscombat.util;

import java.util.NoSuchElementException;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.sounds.DopplerSoundInstance;
import com.onewhohears.dscombat.client.sounds.PlaneMusicSoundInstance;
import com.onewhohears.dscombat.client.sounds.VehicleEngineSoundInstance;
import com.onewhohears.dscombat.data.aircraft.DSCPhysicsConstants;
import com.onewhohears.dscombat.data.aircraft.VehicleSoundManager.PassengerSoundPack;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilClientSafeSounds {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public static void dopplerSound(Entity entity, SoundEvent sound, 
			float initVolume, float initPitch, float velSound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new DopplerSoundInstance(sound, 
				p, entity, initVolume, initPitch, velSound));
	}
	
	public static void nonPassengerVehicleEngineSound(EntityVehicle plane, SoundEvent sound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleEngineSoundInstance(sound, 
				p, plane, 10F, false));
	}
	
	public static void passengerVehicleEngineSound(EntityVehicle plane, SoundEvent sound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleEngineSoundInstance(sound, 
				p, plane, 10F, true));
	}
	
	public static void aircraftRadio(EntityVehicle plane, SoundEvent sound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new PlaneMusicSoundInstance(sound, 
				p, plane, 40F));
	}
	
	public static void aircraftRadio(EntityVehicle plane, String sound) {
		try {
			SoundEvent se = ForgeRegistries.SOUND_EVENTS.getDelegate(new ResourceLocation(sound)).get().get();
			aircraftRadio(plane, se);
		} catch (NoSuchElementException e) {
			LOGGER.error("ERROR: "+sound+" does not exist!");
		}
	}
	
	public static void tickPassengerSounds(EntityVehicle vehicle, PassengerSoundPack passengerSoundPack) {
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
				playCockpitSound(passengerSoundPack.stallAlert, 1f, 1f);
			} }
			else if (vehicle.isAboutToStall()) { if (vehicle.getAboutToStallTicks() % 40 == 1) {
				playCockpitSound(passengerSoundPack.stallWarn, 1f, 1f);
			} }
			// PULL UP
			if (vehicle.getDeltaMovement().y <= -DSCPhysicsConstants.COLLIDE_SPEED 
					&& vehicle.tickCount % 13 == 0
					&& UtilEntity.getDistFromGround(vehicle) / -vehicle.getDeltaMovement().y <= 80) {
				playCockpitSound(passengerSoundPack.pullUp, 1f, 1f);
			}
		}
		if (vehicle.getAircraftType().isAircraft) {
			// ENGINE FIRE
			if (vehicle.getEngineFireTicks() % 70 == 1) 
				playCockpitSound(passengerSoundPack.engineFire, 1f, 1f);
			// FUEL LEAK
			if (vehicle.getBingoTicks() % 150 <= 60 && vehicle.getFuelLeakTicks() % 30 == 1) 
				playCockpitSound(passengerSoundPack.fuelLeak, 1f, 1f);
			// BINGO FUEL
			if (vehicle.getBingoTicks() % 160 <= 60 && vehicle.getBingoTicks() % 20 == 1) 
				playCockpitSound(passengerSoundPack.bingoFuel, 1f, 1f);
		}
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
