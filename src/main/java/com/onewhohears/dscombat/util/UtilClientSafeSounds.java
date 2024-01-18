package com.onewhohears.dscombat.util;

import java.util.NoSuchElementException;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.sounds.DopplerSoundInstance;
import com.onewhohears.dscombat.client.sounds.PlaneMusicSoundInstance;
import com.onewhohears.dscombat.client.sounds.VehicleEngineSoundInstance;
import com.onewhohears.dscombat.data.aircraft.DSCPhysicsConstants;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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
	
	public static void tickPassengerSounds(EntityVehicle vehicle) {
		Minecraft m = Minecraft.getInstance();
		if (!vehicle.equals(m.player.getRootVehicle())) return;
 		// RWR WARNINGS
		if (vehicle.tickCount%4==0 && vehicle.radarSystem.isTrackedByMissile()) {
			playCockpitSound(ModSounds.MISSILE_WARNING.get(), 
				Config.CLIENT.missileWarningVol.get().floatValue(), 1f);
		} else if (vehicle.tickCount%8==0 && vehicle.radarSystem.isTrackedByRadar()) {
			playCockpitSound(ModSounds.GETTING_LOCKED.get(), 
				Config.CLIENT.rwrWarningVol.get().floatValue(), 1f);
		}
		// IR LOCK TONE
		if (vehicle.tickCount%10==0 && vehicle.shouldPlayIRTone())  {
			playCockpitSound(ModSounds.FOX2_TONE_1.get(), 
	    		Config.CLIENT.irTargetToneVol.get().floatValue(), 1f);
		}
		if (vehicle.getAircraftType().isPlane()) {
			// STALL
			if (vehicle.isStalling()) { if (vehicle.getStallTicks() % 24 == 1) {
				playCockpitSound(ModSounds.STALL_ALERT_GM1.get(), 1f, 1f);
			} }
			else if (vehicle.isAboutToStall()) { if (vehicle.getAboutToStallTicks() % 40 == 1) {
				playCockpitSound(ModSounds.STALL_WARNING_GM1.get(), 1f, 1f);
			} }
			// PULL UP
			if (vehicle.getDeltaMovement().y <= -DSCPhysicsConstants.COLLIDE_SPEED 
					&& vehicle.tickCount % 13 == 0
					&& UtilEntity.getDistFromGround(vehicle) / -vehicle.getDeltaMovement().y <= 80) {
				playCockpitSound(ModSounds.PULL_UP_GM1.get(), 1f, 1f);
			}
		}
		if (vehicle.getAircraftType().isAircraft) {
			// ENGINE FIRE
			if (vehicle.getEngineFireTicks() % 70 == 1) 
				playCockpitSound(ModSounds.ENGINE_FIRE_GM1.get(), 1f, 1f);
			// FUEL LEAK
			if (vehicle.getBingoTicks() % 150 <= 60 && vehicle.getFuelLeakTicks() % 30 == 1) 
				playCockpitSound(ModSounds.FUEL_LEAK_GM1.get(), 1f, 1f);
			// BINGO FUEL
			if (vehicle.getBingoTicks() % 160 <= 60 && vehicle.getBingoTicks() % 20 == 1) 
				playCockpitSound(ModSounds.BINGO_GM1.get(), 1f, 1f);
		}
		// TODO 8.1 data link notification lines
		// TODO 8.2 different bitchin betty voice actors
	}
	
	public static void playCockpitSound(SoundEvent sound, float pitch, float volume) {
		Minecraft m = Minecraft.getInstance();
		m.getSoundManager().play(forCockpit(sound, pitch, volume));
	}
	
	public static SimpleSoundInstance forCockpit(SoundEvent sound, float pitch, float volume) {
		return new SimpleSoundInstance(sound.getLocation(), SoundSource.PLAYERS, volume, pitch, 
				SoundInstance.createUnseededRandom(), false, 0, 
				SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
	}
	
}
