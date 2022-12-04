package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.FuelTankData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class ItemFuelTank extends ItemPart {
	
	public final float fuel, max;
	
	public ItemFuelTank(float weight, float fuel, float max, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
		this.fuel = fuel;
		this.max = max;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		MutableComponent name = Component.translatable(getDescriptionId())
			.append(" "+tag.getInt("fuel")+"/"+tag.getInt("max"));
		return name;	
	}

	@Override
	public PartData getPartData() {
		return new FuelTankData(weight, fuel, max, getIdPart(), compatibleSlots);
	}
	
	@Override
	public PartData getFilledPartData(String param) {
		return new FuelTankData(weight, max, max, getIdPart(), compatibleSlots);
	}

}
