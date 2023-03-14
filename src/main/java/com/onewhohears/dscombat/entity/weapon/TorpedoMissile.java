package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.TorpedoData;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class TorpedoMissile extends TrackEntityMissile {
	
	public TorpedoMissile(EntityType<? extends TorpedoMissile> type, Level level) {
		super(type, level);
	}
	
	public TorpedoMissile(Level level, Entity owner, TorpedoData data) {
		super(level, owner, data);
	}
	
}
