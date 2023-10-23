package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilClientSafeSoundInstance;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

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
		if (parent.isOperational()) 
			for (Player player : parent.getRidingPlayers()) 
				clientTickPassengerSounds(player);
	}
	
	protected void onServerTick() {
	}
	
	protected void clientTickPassengerSounds(Player player) {
		BlockPos pos = new BlockPos(player.position());
		// RWR WARNINGS
		if (parent.tickCount%4==0 && parent.radarSystem.isTrackedByMissile()) {
			parent.level.playSound(player, pos, 
    			ModSounds.MISSILE_WARNING.get(), SoundSource.PLAYERS, 
    			Config.CLIENT.rwrWarningVol.get().floatValue(), 1f);
		} else if (parent.tickCount%8==0 && parent.radarSystem.isTrackedByRadar()) {
			parent.level.playSound(player, pos, 
	    		ModSounds.GETTING_LOCKED.get(), SoundSource.PLAYERS, 
	    		Config.CLIENT.missileWarningVol.get().floatValue(), 1f);
    	}
		// IR LOCK TONE
		if (parent.tickCount%10==0 && parent.shouldPlayIRTone())  {
			parent.level.playSound(player, pos, 
	    			ModSounds.FOX2_TONE_1.get(), SoundSource.PLAYERS, 
	    			Config.CLIENT.irTargetToneVol.get().floatValue(), 1f);
		}
		// TODO 8.1 data link notification lines
		// TODO 8.2 bitchin betty
		// pull up
		// over g
		// aoa stall
		// gear still out
		// TODO 8.3 jester
	}
	
	public void onClientInit() {
		UtilClientSafeSoundInstance.nonPassengerVehicleEngineSound(Minecraft.getInstance(), 
				parent, getNonPassengerEngineSound());
		UtilClientSafeSoundInstance.passengerVehicleEngineSound(Minecraft.getInstance(), 
				parent, getPassengerEngineSound());
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
		UtilClientSafeSoundInstance.aircraftRadio(Minecraft.getInstance(), 
				parent, song);
	}
	
}
