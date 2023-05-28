package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.BulletData;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityBomb extends EntityBullet {

	public EntityBomb(EntityType<? extends EntityBomb> type, Level level) {
		super(type, level);
	}
	
	public EntityBomb(Level level, Entity owner, BulletData data) {
		super(level, owner, data);
	}
	
	@Override
	protected void tickSetMove() {
		setDeltaMovement(getDeltaMovement().add(0, -0.05, 0));
	}

}
