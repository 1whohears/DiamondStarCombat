package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DopplerSoundInstance extends AbstractTickableSoundInstance {
	
	protected final LocalPlayer player;
	protected final Entity entity;
	protected final float velocitySound;
	protected final float volDecreaseRate;
	protected final float minDistSqr;
	protected float initVolume;
	protected float initPitch;
	
	public DopplerSoundInstance(SoundEvent sound, LocalPlayer player, Entity entity, float initVolume, float initPitch, float velSound) {
		this(sound, player, entity, SoundSource.PLAYERS, initVolume, initPitch, velSound, 128, 0);
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
		this.volume = 0.001f; // when missile enters client world from far a way the sound is loud at first without this
		this.initVolume = initVolume;
		this.pitch = initPitch;
		this.initPitch = initPitch;
		this.velocitySound = velSound;
		this.volDecreaseRate = (float) (1/(range*range));
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
		// volume
		float minLess = minDistSqr - 400;
		float d2 = (float)player.distanceToSqr(entity);
		if (d2 <= minLess) {
			volume = 0f;
			return;
		} else if (d2 > minLess && d2 < minDistSqr) {
			volume = initVolume * (d2 - minLess) *  0.01f;
		} else volume = initVolume - d2 * volDecreaseRate;
		// pitch
		Vec3 v = entity.position().subtract(player.position());
		float vp = (float)UtilGeometry.vecCompMagDirByAxis(player.getDeltaMovement(), v);
		float vm = (float)UtilGeometry.vecCompMagDirByAxis(entity.getDeltaMovement(), v);
		pitch = initPitch * ((velocitySound + vp)/(velocitySound + vm));
	}

}
