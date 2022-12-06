package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.TrackMissileData;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class TrackEntityMissile extends EntityMissile {

	public TrackEntityMissile(EntityType<? extends TrackEntityMissile> type, Level level) {
		super(type, level);
	}
	
	public TrackEntityMissile(Level level, Entity owner, TrackMissileData data) {
		super(level, owner, data);
	}

	@Override
	public void tickGuide() {
		if (tickCount < 20) return;
		//System.out.println("starting guide to target");
		this.guideToTarget();
		//System.out.println("starting cast");
		if (this.target instanceof EntityAbstractAircraft plane) {
			plane.trackedByMissile();
		}
	}

}
