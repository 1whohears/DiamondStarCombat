package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilClientSafeSounds;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;

public class VehicleSoundManager {
	
	public final EntityVehicle parent;
	
	private VehicleLoopingSounds loopManager;
	private PassengerSoundPack passengerSoundPack = PassengerSoundPack.NO_VOICE;
	
	public VehicleSoundManager(EntityVehicle parent) {
		this.parent = parent;
	}
	
	public void loadSounds(AircraftPreset acp) {
		CompoundTag sounds = acp.getDataAsNBT().getCompound("sounds");
		String loopSoundType = sounds.getString("loopSoundType");
		loopManager = VehicleLoopingSounds.getByType(loopSoundType, parent);
		loopManager.load(sounds);
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
		PassengerSoundPack selectedPack = Config.CLIENT.passengerSoundPack.get();
		if (selectedPack.isSameAsVehicle()) selectedPack = passengerSoundPack;
		UtilClientSafeSounds.tickPassengerSounds(parent, selectedPack);
		tickLoopingSounds();
	}
	
	protected void tickLoopingSounds() {
		if (loopManager != null) loopManager.baseTick();
	}
	
	protected void onServerTick() {
	}
	
	public void onClientInit() {
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
					ModSounds.VEHICLE_HIT_1, 
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
		SAME_AS_VEHICLE("same_as_vehicle", 
				null, null, null, null,
				null, null, null, 
				null, null, null),
		NO_VOICE("no_voice", 
				ModSounds.MISSILE_WARNING, ModSounds.GETTING_LOCKED, 
				ModSounds.FOX2_TONE_1, null,
				null, null, null, 
				null, null, null),
		ENG_NON_BINARY_GOOBER("eng_non_binary_goober", 
				ModSounds.MISSILE_WARNING, ModSounds.GETTING_LOCKED, 
				ModSounds.FOX2_TONE_1, ModSounds.LOCK_NBG,
				ModSounds.ENGINE_FIRE_NBG, ModSounds.FUEL_LEAK_NBG, ModSounds.BINGO_NBG, 
				ModSounds.STALL_ALERT_NBG, ModSounds.STALL_WARNING_NBG, ModSounds.PULL_UP_NBG),
		ENG_MALE_1("eng_male_1", 
				ModSounds.MISSILE_WARNING, ModSounds.GETTING_LOCKED, 
				ModSounds.FOX2_TONE_1, ModSounds.LOCK_GM1,
				ModSounds.ENGINE_FIRE_GM1, ModSounds.FUEL_LEAK_GM1, ModSounds.BINGO_GM1, 
				ModSounds.STALL_ALERT_GM1, ModSounds.STALL_WARNING_GM1, ModSounds.PULL_UP_GM1);
		public final String id;
		public final SoundEvent missileAlert, rwrWarn, irLockTone, radarLock;
		public final SoundEvent engineFire, fuelLeak, bingoFuel;
		public final SoundEvent stallAlert, stallWarn, pullUp;
		private PassengerSoundPack(String id, 
				SoundEvent missileAlert, SoundEvent rwrWarn, 
				SoundEvent irLockTone, SoundEvent radarLock,
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
		public boolean isSameAsVehicle() {
			return this == SAME_AS_VEHICLE;
		}
		public static PassengerSoundPack getById(String id) {
			for (PassengerSoundPack pack : values()) 
				if (pack.id.equals(id)) return pack;
			return NO_VOICE;
		}
	}
	
}
