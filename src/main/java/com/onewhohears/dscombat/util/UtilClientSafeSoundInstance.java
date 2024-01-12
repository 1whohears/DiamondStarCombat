package com.onewhohears.dscombat.util;

import java.util.NoSuchElementException;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.sounds.DopplerSoundInstance;
import com.onewhohears.dscombat.client.sounds.PlaneMusicSoundInstance;
import com.onewhohears.dscombat.client.sounds.VehicleEngineSoundInstance;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilClientSafeSoundInstance {
	
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
		Vec3 camPos = m.gameRenderer.getMainCamera().getPosition();
		if (!vehicle.equals(m.player.getRootVehicle())) return;
 		// RWR WARNINGS
		if (vehicle.tickCount%4==0 && vehicle.radarSystem.isTrackedByMissile()) {
			m.player.level.playLocalSound(camPos.x, camPos.y, camPos.z, 
				ModSounds.MISSILE_WARNING.get(), SoundSource.PLAYERS, 
				Config.CLIENT.missileWarningVol.get().floatValue(), 1f, false);
		} else if (vehicle.tickCount%8==0 && vehicle.radarSystem.isTrackedByRadar()) {
			m.player.level.playLocalSound(camPos.x, camPos.y, camPos.z, 
				ModSounds.GETTING_LOCKED.get(), SoundSource.PLAYERS, 
				Config.CLIENT.rwrWarningVol.get().floatValue(), 1f, false);
		}
		// IR LOCK TONE
		if (vehicle.tickCount%10==0 && vehicle.shouldPlayIRTone())  {
			m.player.level.playLocalSound(camPos.x, camPos.y, camPos.z, 
	    		ModSounds.FOX2_TONE_1.get(), SoundSource.PLAYERS, 
	    		Config.CLIENT.irTargetToneVol.get().floatValue(), 1f, false);
		}
		if (vehicle.getAircraftType().isPlane()) {
			if (vehicle.isStalling()) if (vehicle.getStallTicks() % 7 == 1) {
				m.player.level.playLocalSound(camPos.x, camPos.y, camPos.z, 
					ModSounds.STALL_ALERT.get(), SoundSource.PLAYERS, 
					0.5f, 1f, false);
			} else if (vehicle.isAboutToStall()) if (vehicle.getAboutToStallTicks() % 14 == 1) {
				m.player.level.playLocalSound(camPos.x, camPos.y, camPos.z, 
					ModSounds.STALL_ALERT.get(), SoundSource.PLAYERS, 
					0.5f, 1f, false);
			}
		}
		// TODO 8.1 data link notification lines
		// TODO 8.2 bitchin betty
		// pull up, over g, aoa stall, gear still out, engine fire, fuel leak, hydraulics failure 
		// TODO 8.3 jester
	}
	
}
