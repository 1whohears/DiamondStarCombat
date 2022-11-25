package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.TrackMissileData;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class TrackEntityMissile extends EntityMissile {

	public TrackEntityMissile(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	public TrackEntityMissile(Level level, Entity owner, TrackMissileData data) {
		super(level, owner, data);
	}

	@Override
	public void tickGuide() {
		if (tickCount < 20) return;
		this.guideToTarget();
	}

}
