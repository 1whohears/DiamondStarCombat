package com.onewhohears.dscombat.client.sounds;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

/**
 * Sound instance for distant gunfire/cannon fire that can be heard from far away.
 * Creates a "cannonade" effect where shots echo across the battlefield.
 */
public class DistantGunfireSoundInstance extends AbstractTickableSoundInstance {
	
	protected static final float MIN_VOL = 0.001f;
	protected static final float MIN_DISTANCE = 80f; // Only play when far enough
	protected static final float MAX_DISTANCE = 800f; // Maximum hearing distance
	
	protected final LocalPlayer player;
	protected final Vec3 sourcePos;
	protected final float baseVolume;
	protected final float basePitch;
	protected int ticksAlive = 0;
	protected final int maxTicks;
	
	public DistantGunfireSoundInstance(SoundEvent sound, LocalPlayer player, Vec3 sourcePos, 
			float baseVolume, float basePitch, int durationTicks) {
		super(sound, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
		this.player = player;
		this.sourcePos = sourcePos;
		this.baseVolume = baseVolume;
		this.basePitch = basePitch;
		this.maxTicks = durationTicks;
		this.looping = false;
		this.delay = 0;
		
		// Set initial position
		this.x = sourcePos.x;
		this.y = sourcePos.y;
		this.z = sourcePos.z;
		
		// Calculate initial volume and pitch based on distance
		updateVolumeAndPitch();
	}
	
	@Override
	public void tick() {
		ticksAlive++;
		
		// Stop after duration
		if (ticksAlive >= maxTicks) {
			stop();
			return;
		}
		
		updateVolumeAndPitch();
	}
	
	protected void updateVolumeAndPitch() {
		float distance = (float) player.position().distanceTo(sourcePos);
		
		// Only play if within range and far enough
		if (distance < MIN_DISTANCE || distance > MAX_DISTANCE) {
			volume = MIN_VOL;
			return;
		}
		
		// Calculate volume falloff (linear from MIN_DISTANCE to MAX_DISTANCE)
		float distanceRatio = (distance - MIN_DISTANCE) / (MAX_DISTANCE - MIN_DISTANCE);
		volume = baseVolume * (1.0f - distanceRatio);
		volume = Math.max(MIN_VOL, Math.min(baseVolume, volume));
		
		// Slightly lower pitch for distant sounds
		pitch = basePitch * (0.85f + distanceRatio * 0.1f);
	}
}
