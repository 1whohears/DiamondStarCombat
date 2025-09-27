package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.FuelTankExternalInstance;
import com.onewhohears.dscombat.data.parts.stats.FuelTankExternalStats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityFuelTank extends EntityPart<FuelTankExternalStats, FuelTankExternalInstance<FuelTankExternalStats>> {

	public EntityFuelTank(EntityType<?> type, Level level) {
		super(type, level, "light_external_fuel_tank");
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
		return PartType.EXTERNAL_FUEL_TANK;
	}

	@Override
	public boolean canGetHurt() {
		return true;
	}

}
