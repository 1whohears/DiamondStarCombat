package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.FuelTankData;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemFuelTank extends ItemPart {
	
	public final float fuel, max;
	
	public ItemFuelTank(float weight, float fuel, float max) {
		super(1, weight);
		this.fuel = fuel;
		this.max = max;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		MutableComponent name = UtilMCText.simpleText(getDescriptionId())
			.append(" "+tag.getInt("fuel")+"/"+tag.getInt("max"));
		return name;	
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack test = new ItemStack(this);
			test.setTag(new FuelTankData(weight, fuel, max, getIdPart()).write());
			items.add(test);
		}
	}

}
