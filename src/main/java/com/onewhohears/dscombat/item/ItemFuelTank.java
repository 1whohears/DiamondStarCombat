package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.FuelTankData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
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
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack test = new ItemStack(this);
			test.setTag(new FuelTankData(weight, fuel, max, getIdPart(), compatibleSlots).write());
			items.add(test);
		}
	}

}
