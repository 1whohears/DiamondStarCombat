package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.parts.PartType;

import com.onewhohears.dscombat.data.parts.instance.RadarPartInstance;
import com.onewhohears.dscombat.data.parts.stats.RadarPartStats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityRadar extends EntityPart<RadarPartStats, RadarPartInstance<RadarPartStats>> {

	public EntityRadar(EntityType<?> entityType, Level level) {
		super(entityType, level, "air_scan_a");
	}

	@Override
	public boolean shouldRender() {
		return true;
	}
	
	@Override
	protected double getClientRenderDistance() {
		return Config.CLIENT.renderRadarDistance.get();
	}

	@Override
	public PartType getPartType() {
		return PartType.EXTERNAL_RADAR;
	}

	@Override
	public boolean canGetHurt() {
		return true;
	}

}
