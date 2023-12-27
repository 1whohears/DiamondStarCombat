package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.UtilParticles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityEngine extends EntityPart {
	
	public EntityEngine(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	public EntityEngine(EntityType<?> type, Level level, String slotId, Vec3 pos) {
		super(type, level, slotId, pos);
		setHealth(10);
	}
	
	@Override
	public void tick() {
		super.tick();
		EntityVehicle vehicle = getParentVehicle();
		if (vehicle == null) return;
		Vec3 dir = vehicle.getLookAngle().scale(-vehicle.getCurrentThrottle()*0.4);
		UtilParticles.afterBurner(vehicle, getBoundingBox().getCenter(), dir);
	}
	
	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	public PartType getPartType() {
		return PartType.EXTERNAL_ENGINE;
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 65536;
	}

	@Override
	public boolean canGetHurt() {
		return true;
	}

}
