package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class VehicleIdleSoundInstance extends AbstractTickableSoundInstance {
	
	private final EntityVehicle vehicle;
	private final LocalPlayer player;
	private final boolean isPassengerSound;
	private final float targetVolume;
	private float currentVolume = 0f;
	private int tickCount = 0;
	private static final int FADE_IN_TICKS = 10; // 0.5 seconds fade in
	private static final int MAX_DURATION_TICKS = 60; // 3 seconds max duration
	
	public VehicleIdleSoundInstance(SoundEvent sound, LocalPlayer player, EntityVehicle vehicle, 
			boolean isPassengerSound, float targetVolume) {
		super(sound, SoundSource.NEUTRAL, RandomSource.create());
		this.vehicle = vehicle;
		this.player = player;
		this.isPassengerSound = isPassengerSound;
		this.targetVolume = targetVolume;
		this.looping = false;
		this.delay = 0;
		this.volume = 0f; // Start at 0 for fade in
		this.pitch = 1.0f;
		
		if (isPassengerSound) {
			this.x = 0;
			this.y = 0;
			this.z = 0;
			this.attenuation = SoundInstance.Attenuation.NONE;
			this.relative = true;
		} else {
			this.x = vehicle.getX();
			this.y = vehicle.getY();
			this.z = vehicle.getZ();
			this.attenuation = SoundInstance.Attenuation.LINEAR;
			this.relative = false;
		}
	}
	
	@Override
	public void tick() {
		tickCount++;
		
		// Stop if vehicle is removed or max duration reached
		if (vehicle.isRemoved() || tickCount >= MAX_DURATION_TICKS) {
			stop();
			return;
		}
		
		// Fade in volume
		if (tickCount <= FADE_IN_TICKS) {
			currentVolume = targetVolume * ((float) tickCount / FADE_IN_TICKS);
			this.volume = currentVolume;
		} else {
			this.volume = targetVolume;
		}
		
		// Update position for non-passenger sounds
		if (!isPassengerSound) {
			this.x = vehicle.getX();
			this.y = vehicle.getY();
			this.z = vehicle.getZ();
		}
		
		// Check if player is still passenger (for passenger sounds)
		if (isPassengerSound && !vehicle.isVehicleOf(player)) {
			stop();
		}
	}
}
