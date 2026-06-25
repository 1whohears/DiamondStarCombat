package com.onewhohears.dscombat.client.sounds;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

/**
 * Sound instance for missile engines that provides smooth volume falloff
 * without Doppler pitch shifting for better audio quality
 */
public class MissileEngineSoundInstance extends AbstractTickableSoundInstance {

	protected static final float MIN_VOL = 0.001f;
	protected static final float MAX_RANGE = 400.0f; // Maximum hearing distance
	protected static final float CLOSE_RANGE = 32.0f; // Distance for full volume

	protected final LocalPlayer player;
	protected final Entity entity;
	protected final float baseVolume;
	protected final float basePitch;
	
	public MissileEngineSoundInstance(SoundEvent sound, LocalPlayer player, Entity entity, 
			float baseVolume, float basePitch) {
		super(sound, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
		this.player = player;
		this.entity = entity;
		this.looping = true;
		this.delay = 0;
		this.volume = MIN_VOL;
		this.baseVolume = baseVolume;
		this.pitch = basePitch;
		this.basePitch = basePitch;
		this.attenuation = SoundInstance.Attenuation.LINEAR;
		this.relative = false;
	}

	@Override
	public void tick() {
		if (entity.isRemoved()) {
			stop();
			return;
		}
		
		// Update position to follow missile
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		
		// Calculate distance-based volume with smooth falloff
		float distance = (float) player.distanceTo(entity);
		
		// Smooth volume curve: full volume up close, gradual falloff
		if (distance < CLOSE_RANGE) {
			// Full volume when close
			volume = baseVolume;
		} else if (distance < MAX_RANGE) {
			// Smooth square root falloff for natural sound attenuation (less aggressive than quadratic)
			float normalizedDist = (distance - CLOSE_RANGE) / (MAX_RANGE - CLOSE_RANGE);
			volume = baseVolume * (1.0f - (float)Math.sqrt(normalizedDist));
			volume = Math.max(MIN_VOL, volume);
		} else {
			// Too far, minimal volume
			volume = MIN_VOL;
		}
		
		// Keep pitch constant for clean sound (no Doppler effect)
		pitch = basePitch;
	}

}
