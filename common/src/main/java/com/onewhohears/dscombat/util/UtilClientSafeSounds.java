package com.onewhohears.dscombat.util;

import java.util.NoSuchElementException;

import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.client.sounds.AfterBurnerSoundInstance;
import com.onewhohears.dscombat.client.sounds.DopplerSoundInstance;
import com.onewhohears.dscombat.client.sounds.PlaneMusicSoundInstance;
import com.onewhohears.dscombat.client.sounds.VehicleEngineSoundInstance;
import com.onewhohears.dscombat.client.sounds.VehicleWindSoundInstance;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class UtilClientSafeSounds {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public static void dopplerSound(Entity entity, SoundEvent sound, 
			float initVolume, float initPitch, float velSound, boolean delayed) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		int delay = 0;
		if (delayed) delay = (int)(p.distanceTo(entity) / velSound);
		m.getSoundManager().playDelayed(new DopplerSoundInstance(sound, 
				p, entity, initVolume, initPitch, velSound), delay);
	}
	
	public static void nonPassengerVehicleEngineSound(EntityVehicle plane, SoundEvent sound, double range) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleEngineSoundInstance(sound, 
				p, plane, DSCPhyCons.getVelSound(), false, range, 0));
	}
	
	public static void nonPassengerVehicleEngineSound(EntityVehicle plane, SoundEvent sound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleEngineSoundInstance(sound, 
				p, plane, DSCPhyCons.getVelSound(), false));
	}
	
	public static void passengerVehicleEngineSound(EntityVehicle plane, SoundEvent sound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleEngineSoundInstance(sound, 
				p, plane, DSCPhyCons.getVelSound(), true));
	}
	
	public static void nonPassengerAfterBurnerSound(EntityVehicle plane, SoundEvent sound, double range, float minDist) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new AfterBurnerSoundInstance(sound, 
				p, plane, DSCPhyCons.getVelSound(), false, range, minDist));
	}
	
	public static void passengerAfterBurnerSound(EntityVehicle plane, SoundEvent sound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new AfterBurnerSoundInstance(sound, 
				p, plane, DSCPhyCons.getVelSound(), true));
	}
	
	public static void nonPassengerWindSound(EntityVehicle plane, SoundEvent sound, double range, float minDist) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleWindSoundInstance(sound, 
				p, plane, DSCPhyCons.getVelSound(), false, range, minDist, 0));
	}
	
	public static void passengerWindSound(EntityVehicle plane, SoundEvent sound, double minSpeed) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new VehicleWindSoundInstance(sound, 
				p, plane, DSCPhyCons.getVelSound(), true, minSpeed));
	}
	
	public static void aircraftRadio(EntityVehicle plane, SoundEvent sound) {
		Minecraft m = Minecraft.getInstance();
		LocalPlayer p = m.player;
		if (p == null) return;
		m.getSoundManager().play(new PlaneMusicSoundInstance(sound, 
				p, plane, DSCPhyCons.getVelSound()));
	}
	
	public static void aircraftRadio(EntityVehicle plane, String sound) {
		try {
			SoundEvent se = UtilSound.getSoundById(sound, SoundEvents.MUSIC_DISC_BLOCKS);
			aircraftRadio(plane, se);
		} catch (NoSuchElementException e) {
			LOGGER.error("ERROR: "+sound+" does not exist!");
		}
	}
	
	public static void playCockpitSound(RegistrySupplier<SoundEvent> sound, float pitch, float volume) {
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
		Minecraft m = Minecraft.getInstance();
		final int vehicleId = m.player.getRootVehicle().getId();
		return new SimpleSoundInstance(sound.getLocation(), SoundSource.PLAYERS, volume, pitch, 
				SoundInstance.createUnseededRandom(), false, 0, 
				SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true) {
			@Override
			public float getVolume() {
				if (m.player != null && m.player.isPassenger() && m.player.getRootVehicle().getId() == vehicleId)
					return super.getVolume();
				return 0;
			}
		};
	}

	public static boolean isClientRidingVehicle(EntityVehicle vehicle) {
		Minecraft m = Minecraft.getInstance();
		if (m.player == null) return false;
		return m.player.getRootVehicle().equals(vehicle);
	}

	public static boolean missileSonicBoom(@NotNull EntityMissile<?> missile) {
		return entitySonicBoom(missile, missile.getStats().getMass());
	}

	public static boolean vehicleSonicBoom(@NotNull EntityVehicle vehicle) {
		return entitySonicBoom(vehicle, vehicle.getStats().mass);
	}

	public static boolean entitySonicBoom(@NotNull Entity entity, float mass) {
		if (UtilGeometry.isZero(entity.getDeltaMovement())) return false;
		Minecraft m = Minecraft.getInstance();
		if (m.player == null) return false;
		float size = mass / 8573f;
		float pitch = getSonicBoomPitch(m.player, entity, DSCPhyCons.getVelSound(), size);
		if (pitch <= 0) return false;
		Vec3 diff = entity.position().subtract(m.player.position());
		double distance = diff.length();
		double scale = Math.max(1, Math.min(32, distance * 0.08)); // 32 / 400
		Vec3 pos = m.player.position().add(diff.normalize().scale(scale));
		float volume = Math.max(0f, Math.min(1f, 400f / (float) distance));
		m.level.playLocalSound(pos.x(), pos.y(), pos.z(), ModSounds.SONIC_BOOM,
				SoundSource.PLAYERS, volume, pitch, false);
		//System.out.println("played sonic boom pitch "+pitch);
		return true;
	}

	public static float getSonicBoomPitch(@NotNull Entity observer, @NotNull Entity aircraft,
										  double speedOfSound, double aircraftSize) {
		Vec3 aircraftPos = aircraft.position();
		Vec3 observerPos = observer.position();

		Vec3 velocity = aircraft.getDeltaMovement();
		double aircraftSpeed = velocity.length();
		if (aircraftSpeed <= speedOfSound) {
			//System.out.println("NO BOOM sub sonic "+aircraftSpeed+" "+speedOfSound);
			return -1;
		}

		Vec3 toObserver = observerPos.subtract(aircraftPos);
		if (toObserver.dot(velocity) >= 0) {
			//System.out.println("NO BOOM aircraft in front");
			return -1;
		}

        /*double machConeAngle = Math.asin(speedOfSound / aircraftSpeed);
        double cosPhi = -toObserver.normalize().dot(velocity.normalize());
        if (cosPhi < Math.cos(machConeAngle)) {
            System.out.println("NO BOOM not in cone "+cosPhi+" goal "+machConeAngle);
            return -1;
        }*/

		double normalizedSize = Math.max(aircraftSize, 0.01);
		double pitch = 1.0 / normalizedSize;

		pitch = Math.max(0.9, Math.min(pitch, 4.0));
		return (float) pitch;
	}
	
}
