package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartType;

import com.onewhohears.dscombat.data.parts.instance.GimbalInstance;
import com.onewhohears.dscombat.data.parts.stats.GimbalStats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityGimbal extends EntityPart<GimbalStats, GimbalInstance<GimbalStats>> {
	
	public EntityGimbal(EntityType<?> type, Level level) {
		super(type, level, "gimbal_camera");
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
