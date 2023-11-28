package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilClientSafeSoundInstance;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;

public class VehicleSoundManager {
	
	public final EntityVehicle parent;
	
	public VehicleSoundManager(EntityVehicle parent) {
		this.parent = parent;
	}
	
	public void onTick() {
		if (parent.level.isClientSide) onClientTick();
		else onServerTick();
	}
	
	protected void onClientTick() {
		if (parent.isOperational()) UtilClientSafeSoundInstance.tickPassengerSounds(parent);
	}
	
	protected void onServerTick() {
	}
	
	public void onClientInit() {
		UtilClientSafeSoundInstance.nonPassengerVehicleEngineSound(parent, getNonPassengerEngineSound());
		UtilClientSafeSoundInstance.passengerVehicleEngineSound(parent, getPassengerEngineSound());
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
		UtilClientSafeSoundInstance.aircraftRadio(	parent, song);
	}
	
}
