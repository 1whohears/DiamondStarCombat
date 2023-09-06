package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.TrackMissileData;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

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
		guideToTarget();
		if (!level.isClientSide && tickCount % 10 == 0 && target instanceof EntityAircraft plane) {
			plane.trackedByMissile(position());
		}
	}

}
