package com.onewhohears.dscombat.entity.weapon;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityBullet extends EntityAbstractWeapon {
	
	public EntityBullet(EntityType<? extends EntityBullet> type, Level level) {
		super(type, level);
	}
	
	public EntityBullet(Level level, Entity owner) {
		super(level, owner);
	}

}
