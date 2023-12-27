package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartData.PartType;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityGimbal extends EntityPart {
	
	public EntityGimbal(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	public EntityGimbal(EntityType<?> entityType, Level level, String slotId, Vec3 pos) {
		super(entityType, level, slotId, pos);
		setHealth(2);
	}
	
	@Override
	public void tick() {
		super.tick();
		controlDirection();
	}
	
	protected void controlDirection() {
		Entity vehicle = getVehicle();
		if (vehicle == null) return;
		Entity controller = vehicle.getControllingPassenger();
		if (controller == null) return;
		setXRot(controller.getXRot());
		setYRot(controller.getYRot());
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	public PartType getPartType() {
		return PartType.GIMBAL;
	}

	@Override
	public boolean canGetHurt() {
		return true;
	}

}
