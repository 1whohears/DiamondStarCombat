package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.BuffStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.world.phys.Vec3;

public class BuffInstance<T extends BuffStats> extends PartInstance<T> {

	public BuffInstance(T stats) {
		super(stats);
	}
	
	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}
	
	@Override
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		switch (getStats().getBuffType()) {
		case DATA_LINK:
			getParent().radarSystem.dataLink = true;
			break;
		case NIGHT_VISION_HUD:
			getParent().nightVisionHud = true;
			break;
		case RADIO:
			getParent().hasRadio = true;
			break;
		case ARMOR:
			break;
		}
	}
	
	@Override
	public void serverRemove(String slotId) {
		super.serverRemove(slotId);
		if (getParent() == null) return;
		switch (getStats().getBuffType()) {
		case DATA_LINK:
			getParent().radarSystem.dataLink = false;
			break;
		case NIGHT_VISION_HUD:
			getParent().nightVisionHud = false;
			break;
		case RADIO:
			getParent().hasRadio = false;
			break;
		case ARMOR:
			break;
		}
	}

}
