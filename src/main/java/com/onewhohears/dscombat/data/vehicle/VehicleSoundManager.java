package com.onewhohears.dscombat.data.vehicle;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.sound.PassengerSoundPack;
import com.onewhohears.dscombat.data.sound.VehiclePassengerSoundPacks;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
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

import javax.annotation.Nullable;

public class VehicleSoundManager {
	private static final RandomSource RANDOM_SRC = RandomSource.create();
	
	public final EntityVehicle parent;
	
	private VehicleLoopingSounds loopManager;
	
	public VehicleSoundManager(EntityVehicle parent) {
		this.parent = parent;
	}
	
	public void loadSounds(VehicleStats acp) {
		CompoundTag sounds = acp.getDataAsNBT().getCompound("sounds");
		String loopSoundType = sounds.getString("loopSoundType");
		loopManager = VehicleLoopingSounds.getByType(loopSoundType, parent);
		loopManager.loadPreset(sounds);
	}
	
	public void read(CompoundTag nbt) {
		if (!nbt.contains("sounds")) return;
		CompoundTag sounds = nbt.getCompound("sounds");
	}
	
	public void write(CompoundTag nbt) {
		CompoundTag sounds = new CompoundTag();

		nbt.put("sounds", sounds);
	}
	
	public void read(FriendlyByteBuf buffer) {

	}
	
	public void write(FriendlyByteBuf buffer) {

	}
	
	public void onTick() {
		if (parent.level.isClientSide) onClientTick();
		else onServerTick();
	}
	
	protected void onClientTick() {
		if (UtilClientSafeSounds.isClientRidingVehicle(parent)) {
			PassengerSoundPack pack = getPassengerSoundPack();
			if (pack != null) pack.clientTickPassengerSounds(parent);
		}
		tickLoopingSounds();
	}
	
	protected void tickLoopingSounds() {
		if (loopManager != null) loopManager.baseTick();
	}
	
	protected void onServerTick() {
	}
	
	public void onClientInit() {
	}

	@Nullable
	public PassengerSoundPack getPassengerSoundPack() {
		String selectedPack = Config.CLIENT.passengerSoundPack.get();
		return VehiclePassengerSoundPacks.get().get(selectedPack);
	}
	
	public void playPassengerRadarLockSound() {
		if (!parent.getLevel().isClientSide()) return;
		PassengerSoundPack pack = getPassengerSoundPack();
		if (pack != null) pack.playRadarLockSound();
	}

	public void playPassengerRadarFoundSound() {
		if (!parent.getLevel().isClientSide()) return;
		PassengerSoundPack pack = getPassengerSoundPack();
		if (pack != null) pack.playRadarFoundSound();
	}

	public void playPassengerFlareSound() {
		if (!parent.getLevel().isClientSide()) return;
		PassengerSoundPack pack = getPassengerSoundPack();
		if (pack != null) pack.playFlareSound();
	}

	public void playPassengerChaffSound() {
		if (!parent.getLevel().isClientSide()) return;
		PassengerSoundPack pack = getPassengerSoundPack();
		if (pack != null) pack.playChaffSound();
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
	
}
