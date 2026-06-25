package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.onewholibs.util.math.UtilGeometry;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DopplerSoundInstance extends AbstractTickableSoundInstance {

	protected static final float MIN_VOL = 0.001f;

	protected final LocalPlayer player;
	protected final Entity entity;
	protected final float velocitySound;
	protected final float volDecreaseRate;
	protected final float minDistSqr;
	protected float initVolume;
	protected float initPitch;
	
	public DopplerSoundInstance(SoundEvent sound, LocalPlayer player, Entity entity, float initVolume, float initPitch, float velSound) {
		this(sound, player, entity, SoundSource.PLAYERS, initVolume, initPitch, velSound, 160, 0);
	}
	
	public DopplerSoundInstance(SoundEvent sound, LocalPlayer player, Entity entity, float initVolume, float initPitch, float velSound, double range) {
		this(sound, player, entity, SoundSource.PLAYERS, initVolume, initPitch, velSound, range, 0);
	}
	
	public DopplerSoundInstance(SoundEvent sound, LocalPlayer player, Entity entity, float initVolume, float initPitch, float velSound, double range, float minDist) {
		this(sound, player, entity, SoundSource.PLAYERS, initVolume, initPitch, velSound, range, minDist);
	}
	
	public DopplerSoundInstance(SoundEvent sound, LocalPlayer player, Entity entity, SoundSource soundSource, 
			float initVolume, float initPitch, float velSound, double range, float minDist) {
		super(sound, soundSource, SoundInstance.createUnseededRandom());
		this.player = player;
		this.entity = entity;
		this.looping = true;
		this.delay = 0;
		this.volume = MIN_VOL; // when missile enters client world from far a way the sound is loud at first without this
		this.initVolume = initVolume;
		this.pitch = initPitch;
		this.initPitch = initPitch;
		this.velocitySound = velSound;
		// Changed: use linear attenuation rate instead of quadratic
		this.volDecreaseRate = (float) (1.0 / range); // Linear instead of 1/(range*range)
		this.minDistSqr = minDist*minDist;
	}

	@Override
	public void tick() {
		if (entity.isRemoved()) {
			stop();
			return;
		}
		// position
		x = entity.getX();
		y = entity.getY();
		z = entity.getZ();
		
		// Cache distance calculation - used multiple times
		float d2 = (float)player.distanceToSqr(entity);
		
		// volume - linear distance attenuation
		float minLess = minDistSqr - 400;
		
		if (d2 <= minLess) {
			volume = MIN_VOL;
		} else if (d2 > minLess && d2 < minDistSqr) {
			volume = Math.max(MIN_VOL, initVolume * (d2 - minLess) *  0.01f);
		} else {
			// Linear attenuation: volume decreases with distance, not distance squared
			// Use cached sqrt calculation
			float distance = (float)Math.sqrt(d2);
			volume = Math.max(MIN_VOL, initVolume * (1.0f - distance * volDecreaseRate));
		}
		
		// pitch - Doppler effect calculation
		Vec3 dPos = entity.position().subtract(player.position());
		float velPlayer = (float)UtilGeometry.vecCompMagDirByAxis(player.getDeltaMovement(), dPos);
		float velEntity = (float)UtilGeometry.vecCompMagDirByAxis(entity.getDeltaMovement(), dPos);
		
		// Cache velocity squared calculation
		double entityVelSqr = entity.getDeltaMovement().lengthSqr();
		double velSoundSqr = velocitySound * velocitySound;
		
		pitch = initPitch * ((velocitySound + velPlayer)/(velocitySound + velEntity));
		
		// if traveling faster than the speed of sound and towards the player
		if (entityVelSqr > velSoundSqr && velEntity <= 0) volume = MIN_VOL;
	}

}
