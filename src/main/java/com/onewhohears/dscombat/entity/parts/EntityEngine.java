package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilParticles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityEngine extends EntityPart {
	
	public EntityEngine(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	@Override
	public void tick() {
		super.tick();
		EntityVehicle vehicle = getParentVehicle();
		if (level.isClientSide && vehicle != null) {
			Vec3 dir = vehicle.getLookAngle().scale(-vehicle.getCurrentThrottle()*0.4);
			UtilParticles.afterBurner(vehicle, getBoundingBox().getCenter(), dir);
		}
	}
	
	@Override
	public boolean shouldRender() {
		return true;
	}
	
	@Override
	protected double getClientRenderDistance() {
		return Config.CLIENT.renderEngineDistance.get();
	}

	@Override
	public PartType getPartType() {
		return PartType.EXTERNAL_ENGINE;
	}

	@Override
	public boolean canGetHurt() {
		return true;
	}

}
