package com.onewhohears.dscombat.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityTestPlane extends EntityAbstractPlane {

	public EntityTestPlane(EntityType<? extends EntityAbstractPlane> entity, Level level) {
		super(entity, level);
		this.addSeat(new Vec3(0, 0, 0));
	}

}
