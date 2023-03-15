package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.TorpedoData;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TorpedoMissile extends TrackEntityMissile {
	
	public TorpedoMissile(EntityType<? extends TorpedoMissile> type, Level level) {
		super(type, level);
	}
	
	public TorpedoMissile(Level level, Entity owner, TorpedoData data) {
		super(level, owner, data);
	}
	
	@Override
	protected void motion() {
		if (isInWater()) super.motion();
		Vec3 cm = getDeltaMovement();
		cm = cm.add(0, -0.05, 0);
		setDeltaMovement(cm);
	}
	
}
