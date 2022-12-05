package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.PosMissileData;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PositionMissile extends EntityMissile {
	
	public PositionMissile(EntityType<? extends PositionMissile> type, Level level) {
		super(type, level);
	}
	
	public PositionMissile(Level level, Entity owner, PosMissileData data) {
		super(level, owner, data);
	}
	
	@Override
	public void tickGuide() {
		if (tickCount < 20) return;
		this.guideToPosition();
	}

}
