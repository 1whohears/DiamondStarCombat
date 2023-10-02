package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;

public class EntityEngine extends EntityVehiclePart {
	
	public EntityEngine(EntityAircraft parent, String modelId, EntityDimensions size, String slotId, Vec3 pos, float z_rot) {
		super(parent, modelId, size, slotId, pos, z_rot);
		setHealth(10);
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
