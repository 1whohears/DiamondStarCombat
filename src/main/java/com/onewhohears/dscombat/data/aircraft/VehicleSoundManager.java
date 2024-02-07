package com.onewhohears.dscombat.data.aircraft;

import java.util.NoSuchElementException;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilClientSafeSounds;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.registries.ForgeRegistries;

public class VehicleSoundManager {
	
	public final EntityVehicle parent;
	private float prevThrottle = 0;
	
	private SoundEvent nonPassengerEngine = ModSounds.BIPLANE_1.get(), passengerEngine = ModSounds.BIPLANE_1.get();
	
	private PassengerSoundPack passengerSoundPack = PassengerSoundPack.NONE;
	
	public VehicleSoundManager(EntityVehicle parent) {
		this.parent = parent;
	}
	
	public void loadSounds(AircraftPreset acp) {
		CompoundTag sounds = acp.getDataAsNBT().getCompound("sounds");
		String nonPassengerEngineId = sounds.getString("nonPassengerEngine");
		nonPassengerEngine = getSoundById(nonPassengerEngineId, nonPassengerEngine);
		String passengerEngineId = sounds.getString("passengerEngine");
		passengerEngine = getSoundById(passengerEngineId, passengerEngine);
		if (sounds.contains("passengerSoundPack")) 
			passengerSoundPack = PassengerSoundPack.getById(sounds.getString("passengerSoundPack"));
	}
	
	public void read(CompoundTag nbt) {
		if (!nbt.contains("sounds")) return;
		CompoundTag sounds = nbt.getCompound("sounds");
		if (sounds.contains("passengerSoundPack")) 
			passengerSoundPack = PassengerSoundPack.getById(sounds.getString("passengerSoundPack"));
	}
	
	public void write(CompoundTag nbt) {
		CompoundTag sounds = new CompoundTag();
		sounds.putString("passengerSoundPack", passengerSoundPack.id);
		nbt.put("sounds", sounds);
	}
	
	public void read(FriendlyByteBuf buffer) {
		passengerSoundPack = PassengerSoundPack.getById(buffer.readUtf());
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(passengerSoundPack.id);
	}
	
	public void onTick() {
		if (parent.level.isClientSide) onClientTick();
		else onServerTick();
	}
	
	protected void onClientTick() {
		if (parent.isOperational()) UtilClientSafeSounds.tickPassengerSounds(parent, passengerSoundPack);
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
		return nonPassengerEngine;
	}
	
	public SoundEvent getPassengerEngineSound() {
		return passengerEngine;
	}
	
	public PassengerSoundPack getPassengerSoundPack() {
		return passengerSoundPack;
	}
	
	public void playRadarLockSound() {
		UtilClientSafeSounds.playCockpitSound(passengerSoundPack.radarLock, 1f, 1f);
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
		UtilClientSafeSounds.aircraftRadio(parent, song);
	}
	
	public static enum PassengerSoundPack {
		NONE("none", 
				null, null, null, null,
				null, null, null, 
				null, null, null),
		ENG_MALE_FIGHT_JET_1("eng_male_fight_jet_1", 
				ModSounds.MISSILE_WARNING.get(), ModSounds.GETTING_LOCKED.get(), 
				ModSounds.FOX2_TONE_1.get(), ModSounds.LOCK_GM1.get(),
				ModSounds.ENGINE_FIRE_GM1.get(), ModSounds.FUEL_LEAK_GM1.get(), ModSounds.BINGO_GM1.get(), 
				ModSounds.STALL_ALERT_GM1.get(), ModSounds.STALL_WARNING_GM1.get(), ModSounds.PULL_UP_GM1.get()),
		ENG_NON_BINARY_GOOBER("eng_non_binary_jet_1", 
				ModSounds.MISSILE_WARNING.get(), ModSounds.GETTING_LOCKED.get(), 
				ModSounds.FOX2_TONE_1.get(), ModSounds.LOCK_NBG.get(),
				ModSounds.ENGINE_FIRE_NBG.get(), ModSounds.FUEL_LEAK_NBG.get(), ModSounds.BINGO_NBG.get(), 
				ModSounds.STALL_ALERT_NBG.get(), ModSounds.STALL_WARNING_NBG.get(), ModSounds.PULL_UP_NBG.get());
		public final String id;
		public final SoundEvent missileAlert, rwrWarn, irLockTone, radarLock;
		public final SoundEvent engineFire, fuelLeak, bingoFuel;
		public final SoundEvent stallAlert, stallWarn, pullUp;
		private PassengerSoundPack(String id, 
				SoundEvent missileAlert, SoundEvent rwrWarn, SoundEvent irLockTone, SoundEvent radarLock,
				SoundEvent engineFire, SoundEvent fuelLeak, SoundEvent bingoFuel,
				SoundEvent stallAlert, SoundEvent stallWarn, SoundEvent pullUp) {
			this.id = id;
			this.missileAlert = missileAlert;
			this.rwrWarn = rwrWarn;
			this.irLockTone = irLockTone;
			this.radarLock = radarLock;
			this.engineFire = engineFire;
			this.fuelLeak = fuelLeak;
			this.bingoFuel = bingoFuel;
			this.stallAlert = stallAlert;
			this.stallWarn = stallWarn;
			this.pullUp = pullUp;
		}
		public static PassengerSoundPack getById(String id) {
			for (PassengerSoundPack pack : values()) 
				if (pack.id.equals(id)) return pack;
			return NONE;
		}
	}
	
	public static SoundEvent getSoundById(String id, SoundEvent alt) {
		try {
			return ForgeRegistries.SOUND_EVENTS.getDelegate(new ResourceLocation(id)).get().get();
		} catch(NoSuchElementException e) {
			return alt;
		}
	}
	
}
