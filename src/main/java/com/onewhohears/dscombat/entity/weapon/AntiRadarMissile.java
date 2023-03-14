package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.AntiRadarMissileData;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AntiRadarMissile extends EntityMissile {
	
	public AntiRadarMissile(EntityType<? extends AntiRadarMissile> type, Level level) {
		super(type, level);
	}
	
	public AntiRadarMissile(Level level, Entity owner, AntiRadarMissileData data) {
		super(level, owner, data);
	}
	
	@Override
	public void tickGuide() {
		// TODO Auto-generated method stub

	}

}
