package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.EngineData;
import com.onewhohears.dscombat.data.parts.ExternalEngineData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;

public class ItemEngine extends ItemPart {
	
	public final float thrust, heat, fuelRate;
	public final boolean external;
	
	public ItemEngine(float weight, float thrust, float heat, float fuelRate, boolean external, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
		this.thrust = thrust;
		this.heat = heat;
		this.fuelRate = fuelRate;
		this.external = external;
	}

	@Override
	public PartData getPartData() {
		if (external) return new ExternalEngineData(weight, thrust, heat, fuelRate, getIdPart(), compatibleSlots);
		return new EngineData(weight, thrust, heat, fuelRate, getIdPart(), compatibleSlots);
	}

}
