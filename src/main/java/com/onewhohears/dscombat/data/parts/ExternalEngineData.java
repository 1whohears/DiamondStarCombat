package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ExternalEngineData extends EngineData {

	public ExternalEngineData(float weight, float thrust, float heat, float fuelRate, ResourceLocation itemid) {
		super(weight, thrust, heat, fuelRate, itemid);
	}
	
	public ExternalEngineData(float weight, float thrust, float heat, float fuelRate, String itemid) {
		this(weight, thrust, heat, fuelRate, new ResourceLocation(DSCombatMod.MODID, itemid));
	}
	
	public ExternalEngineData(CompoundTag tag) {
		super(tag);
	}
	
	public ExternalEngineData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public PartType getType() {
		return PartType.EXTERNAL_ENGINE;
	}
	
	@Override
	public SlotType[] getCompatibleSlots() {
		return INTERNAL;
	}

}
