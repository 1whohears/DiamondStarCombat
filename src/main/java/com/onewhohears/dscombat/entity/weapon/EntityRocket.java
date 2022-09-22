package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.RocketData;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityRocket extends EntityBullet {
	
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
			if (data.getGuidanceType() == RocketData.GuidanceType.OWNER_RADAR
					|| data.getTargetType() == RocketData.TargetType.POS) {
				data.guideToTarget(this, targetPos);
			} else if (data.getGuidanceType() == RocketData.GuidanceType.IR) {
				data.irGuidance(this);
			} else {
				data.guideToTarget(this, target);
			}
			motionClamp();
		} else {
			level.addParticle(ParticleTypes.SMOKE, 
					this.getX(), this.getY(), this.getZ(), 
					0.0D, 0.0D, 0.0D);
		}
		super.tick();
	}
	
	public void motionClamp() {
		Vec3 motion = getDeltaMovement();
		double vel = motion.length();
		double max = 4d;
		if (vel > max) motion = motion.scale(max / vel);
		setDeltaMovement(motion);
	}

}
