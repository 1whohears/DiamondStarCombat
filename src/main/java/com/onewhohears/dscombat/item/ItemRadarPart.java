package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.ExternalRadarPartData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.RadarPartData;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRadarPart extends ItemPart {
	
	public final String preset;
	public final boolean external;
	public final String modelId;
	public final EntityDimensions size;
	
	public ItemRadarPart(float weight, String preset, SlotType[] compatibleSlots) {
		super(8, weight, compatibleSlots);
		this.preset = preset;
		this.external = false;
		this.modelId = "";
		this.size = null;
	}
	
	public ItemRadarPart(float weight, String preset, SlotType[] compatibleSlots, String modelId, EntityDimensions size) {
		super(4, weight, compatibleSlots);
		this.preset = preset;
		this.external = true;
		this.modelId = modelId;
		this.size = size;
	}

	@Override
	public PartData getPartData() {
		if (external) return new ExternalRadarPartData(weight, preset, ForgeRegistries.ITEMS.getKey(this), 
				compatibleSlots, modelId, size);
		return new RadarPartData(weight, preset, ForgeRegistries.ITEMS.getKey(this), compatibleSlots);
	}

}
