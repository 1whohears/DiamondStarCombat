package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class RadarPartData extends PartData {
	
	private final String radarId;
	
	public RadarPartData(float weight, String preset, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
		this.radarId = preset;
	}

	@Override
	public PartType getType() {
		return PartType.INTERNAL_RADAR;
	}
	
	@Override
	public void setup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		RadarData data = craft.radarSystem.get(radarId, slotId);
		if (data == null) {
			data = RadarPresets.get().getPreset(radarId);
			if (data == null) return;
			data.setSlot(slotId);
			data.setPos(pos);
			craft.radarSystem.addRadar(data);
		}
	}
	
	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		RadarData data = craft.radarSystem.get(radarId, slotId);
		if (data == null) return false;
		return true;
	}
	
	@Override
	public void remove(String slotId) {
		super.serverRemove(slotId);
		if (getParent() == null) return;
		getParent().radarSystem.removeRadar(radarId, slotId);
	}
	
	@Override
	public void tick(String slotId) {
		super.tick(slotId);
		/*RadarData data = this.getParent().radarSystem.get(radarId, slotId);
		if (data != null) {
			
		}*/
	}
	
	@Override
	public void clientTick(String slotId) {
		super.clientTick(slotId);
		//this.tick(slotId);
	}

}
