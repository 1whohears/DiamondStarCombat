package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.RadarPartStats;
import com.onewhohears.dscombat.data.radar.RadarInstance;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.world.phys.Vec3;

public class RadarPartInstance<T extends RadarPartStats> extends PartInstance<T> {

	public RadarPartInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void setup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		RadarInstance<?> data = craft.radarSystem.get(getStats().getRadarId(), slotId);
		if (data == null) {
			data = RadarPresets.get().get(getStats().getRadarId()).createRadarInstance();
			if (data == null) return;
			data.setSlot(slotId);
			data.setPos(pos);
			craft.radarSystem.addRadar(data);
		}
	}
	
	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		RadarInstance<?> data = craft.radarSystem.get(getStats().getRadarId(), slotId);
		if (data == null) return false;
		return true;
	}
	
	@Override
	public void remove(String slotId) {
		super.serverRemove(slotId);
		if (getParent() == null) return;
		getParent().radarSystem.removeRadar(getStats().getRadarId(), slotId);
	}

}
