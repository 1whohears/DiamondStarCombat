package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.RocketData;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityRocket extends EntityBullet {
	
	public Entity parent;
	public Entity target;
	public Vec3 targetPos;
	
	public EntityRocket(EntityType<? extends EntityBullet> type, Level level) {
		super(type, level);
	}
	
	public EntityRocket(Level level, Entity owner, RocketData data) {
		super(level, owner, data);
	}
	
	@Override
	public void tick() {
		if (!this.level.isClientSide) {
			RocketData data = (RocketData)this.getWeaponData();
			data.tickGuide(this);
			motionClamp();
			// TODO explode if close enough to the target
		}
		super.tick();
		if (this.level.isClientSide /*&& this.tickCount % 2 < 2*/) {
			// TODO why are particles not showing / client side not firing?
			System.out.println("making particle");
			level.addParticle(ParticleTypes.FIREWORK, 
					this.getX(), this.getY(), this.getZ(), 
					this.random.nextGaussian() * 0.05D, 
					-this.getDeltaMovement().y * 0.5D, 
					this.random.nextGaussian() * 0.05D);
		}
	}
	
	public void motionClamp() {
		Vec3 motion = getDeltaMovement();
		double vel = motion.length();
		double max = 1d;
		if (vel > max) motion = motion.scale(max / vel);
		setDeltaMovement(motion);
	}

}
