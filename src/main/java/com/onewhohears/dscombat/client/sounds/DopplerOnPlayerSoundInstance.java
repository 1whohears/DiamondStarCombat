package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class DopplerOnPlayerSoundInstance extends AbstractTickableSoundInstance {
	
	protected final LocalPlayer player;
	protected final Entity entity;
	protected final float velocitySound;
	protected float initVolume;
	protected float initPitch;
	
	public DopplerOnPlayerSoundInstance(SoundEvent sound, LocalPlayer player, Entity entity, float initVolume, float initPitch, float velSound) {
		super(sound, SoundSource.PLAYERS);
		this.player = player;
		this.entity = entity;
		this.looping = true;
		this.delay = 0;
		this.volume = 0.001f; // when missile enters client world from far a way the sound is loud at first without this
		this.initVolume = initVolume;
		this.pitch = initPitch;
		this.initPitch = initPitch;
		this.velocitySound = velSound;
	}

	@Override
	public void tick() {
		if (player.isRemoved() || entity.isRemoved()) {
			this.stop();
			return;
		}
		// position
		this.x = entity.getX();
		this.y = entity.getY();
		this.z = entity.getZ();
		// volume
		float d2 = (float)player.distanceToSqr(entity);
		volume = initVolume - d2 * 0.000025f;
		// pitch
		Vec3 v = entity.position().subtract(player.position());
		float vp = (float)UtilGeometry.vecCompMagDirByAxis(player.getDeltaMovement(), v);
		float vm = (float)UtilGeometry.vecCompMagDirByAxis(entity.getDeltaMovement(), v);
		pitch = initPitch * ((velocitySound + vp)/(velocitySound + vm));
	}

}
