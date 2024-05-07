package com.onewhohears.dscombat.data.aircraft;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.aircraft.stats.VehicleStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilClientSafeSounds;
import com.onewhohears.dscombat.util.UtilSound;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;

public class VehicleSoundManager {
	private static final RandomSource RANDOM_SRC = RandomSource.create();
	
	public final EntityVehicle parent;
	
	private VehicleLoopingSounds loopManager;
	private PassengerSoundPack passengerSoundPack = PassengerSoundPack.NO_VOICE;
	
	public VehicleSoundManager(EntityVehicle parent) {
		this.parent = parent;
	}
	
	public void loadSounds(VehicleStats acp) {
		CompoundTag sounds = acp.getDataAsNBT().getCompound("sounds");
		String loopSoundType = sounds.getString("loopSoundType");
		loopManager = VehicleLoopingSounds.getByType(loopSoundType, parent);
		loopManager.loadPreset(sounds);
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
		if (this.parent.level.isClientSide || !this.parent.isOperational()) return;

		float volume = Mth.clamp(amount * 0.5F, 0.12F,1.88F);
		// keep this clamped close to 1.0F since audio duration noticeably changes for larger values
		float pitch = Mth.clamp(1.0F + ((RANDOM_SRC.nextFloat() - 0.5F) / 2.94F), 0.83F, 1.17F);

		final SoundEvent forBroadcast = soundForHurt(source);

		UtilSound.sendDelayedSound(
				forBroadcast,
				parent.position(), 160, parent.level.dimension(), volume, pitch
		);
	}
	
	public void onRadioSongUpdate(String song) {
		if (!parent.level.isClientSide) return;
		if (song.isEmpty()) return;
		UtilClientSafeSounds.aircraftRadio(parent, song);
	}

	/*
		HELPER METHODS BEGIN HERE
	 */

	/**
	 * made this since we want compatibility... conceivably we're going to run into inappropriate SFX being played
	 * for some arbitrary <code>DamageSource</code> in the future. Since some mods don't actually implement
	 * <code>DamageSource</code> properly (e.g. CGM bullets return false for #isProjectile), this manual
	 * handling is necessary...
	 * @author kawaiicakes
	 * @param source the <code>DamageSource</code> hurting the vehicle.
	 * @return the appropriate-sounding <code>SoundEvent</code>.
	 */
	// TODO: make vehicles have a "material" associated similarly to blocks for the purpose of sound interactions
	public static SoundEvent soundForHurt(DamageSource source) {
		final SoundEvent toReturn;

		if (source.isProjectile() || source.msgId.equals("bullet")) {
			toReturn = ModSounds.VEHICLE_HURT_PROJECTILE_METAL;
		} else {
			// TODO: continue this and also create more SoundEvents (for fire, explosions, etc)
			toReturn = ModSounds.VEHICLE_HURT_COLLISION_METAL;
		}

		return toReturn;
	}
	
	public enum PassengerSoundPack {
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
		PassengerSoundPack(String id,
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
