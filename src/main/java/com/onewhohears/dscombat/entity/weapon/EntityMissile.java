package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.MissileData;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityMissile extends EntityBullet {
	
	public Entity parent;
	public Entity target;
	public Vec3 targetPos;
	
	public EntityMissile(EntityType<? extends EntityMissile> type, Level level) {
		super(type, level);
	}
	
	public EntityMissile(Level level, Entity owner, MissileData data) {
		this(ModEntities.MISSILE.get(), level);
		this.setOwner(owner);
		this.setWeaponData(data);
	}
	
	@Override
	public void tick() {
		//System.out.println("rocket "+this.tickCount+" "+this.level);
		super.tick();
		if (!this.level.isClientSide) {
			MissileData data = (MissileData)this.getWeaponData();
			data.tickGuide(this);
			motionClamp();
			// TODO explode if close enough to the target
		}
		if (this.level.isClientSide && this.tickCount % 2 == 0) {
			Vec3 move = this.getDeltaMovement();
			level.addParticle(ParticleTypes.FIREWORK, 
					this.getX(), this.getY(), this.getZ(), 
					-move.x * 0.5D + random.nextGaussian() * 0.05D, 
					-move.y * 0.5D + random.nextGaussian() * 0.05D, 
					-move.y * 0.5D + random.nextGaussian() * 0.05D);
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
