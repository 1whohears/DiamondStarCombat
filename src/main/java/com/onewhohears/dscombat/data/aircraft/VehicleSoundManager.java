package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilClientSafeSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;

public class VehicleSoundManager {
	
	public final EntityVehicle parent;
	private float prevThrottle = 0;
	
	public VehicleSoundManager(EntityVehicle parent) {
		this.parent = parent;
	}
	
	public void onTick() {
		if (parent.level.isClientSide) onClientTick();
		else onServerTick();
	}
	
	protected void onClientTick() {
		if (parent.isOperational()) UtilClientSafeSounds.tickPassengerSounds(parent);
		tickEngineSound();
	}
	
	private void tickEngineSound() {
		if (prevThrottle == 0 && parent.getCurrentThrottle() != 0) {
			UtilClientSafeSounds.nonPassengerVehicleEngineSound(parent, getNonPassengerEngineSound());
			UtilClientSafeSounds.passengerVehicleEngineSound(parent, getPassengerEngineSound());
		}
		prevThrottle = parent.getCurrentThrottle();
	}
	
	protected void onServerTick() {
	}
	
	public void onClientInit() {
	}
	
	public SoundEvent getNonPassengerEngineSound() {
		return parent.vehicleData.externalEngineSound.get();
	}
	
	public SoundEvent getPassengerEngineSound() {
		return parent.vehicleData.internalEngineSound.get();
	}
	
	public void onHurt(DamageSource source, float amount) {
		if (!parent.level.isClientSide && parent.isOperational()) {
			parent.level.playSound(null, parent.blockPosition(), 
					ModSounds.VEHICLE_HIT_1.get(), 
					SoundSource.PLAYERS, 
					0.5f, 1.0f);
		}
	}
	
	public void onRadioSongUpdate(String song) {
		if (!parent.level.isClientSide) return;
		if (song.isEmpty()) return;
		UtilClientSafeSounds.aircraftRadio(	parent, song);
	}
	
}
