package com.onewhohears.dscombat.client.sounds;

import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
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
		super(sound, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
		this.player = player;
		this.entity = entity;
		this.looping = true;
		this.delay = 0;
		this.volume = initVolume;
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
		//System.out.println("TICK MISSILE SOUND");
		// position
		this.x = entity.getX();
		this.y = entity.getY();
		this.z = entity.getZ();
		// volume
		float d2 = (float)player.distanceToSqr(entity);
		volume = initVolume - d2 * 0.00005f;
		// pitch
		Vec3 v = entity.position().subtract(player.position());
		Vec3 v3p = UtilGeometry.componentOfVecByAxis(player.getDeltaMovement(), v);
		Vec3 v3m = UtilGeometry.componentOfVecByAxis(entity.getDeltaMovement(), v);
		float vp = (float)v3p.length();
		float vm = (float)v3m.length();
		if (!sameDirection(v, v3p)) vp *= -1f;
		if (!sameDirection(v, v3m)) vm *= -1f;
		pitch = initPitch * ((velocitySound + vp)/(velocitySound + vm));
		/*System.out.println("vp = "+vp);
		System.out.println("vm = "+vm);
		System.out.println("new pitch = "+pitch);*/
	}
	
	private boolean sameDirection(Vec3 a, Vec3 b) {
		return Math.signum(a.x) == Math.signum(b.x) && Math.signum(a.y) == Math.signum(b.y) && Math.signum(a.z) == Math.signum(b.z);
	}

}
