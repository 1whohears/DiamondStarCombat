package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartData.PartType;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityRadar extends EntityPart {

	public EntityRadar(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	
	public EntityRadar(EntityType<?> type, Level level, String slotId, Vec3 pos) {
		super(type, level, slotId, pos);
	}

	@Override
	public boolean shouldRender() {
		return true;
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 65536;
	}

	@Override
	public PartType getPartType() {
		return PartType.EXTERNAL_RADAR;
	}

}
