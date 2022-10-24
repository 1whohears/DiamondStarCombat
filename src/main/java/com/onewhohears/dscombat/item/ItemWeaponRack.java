package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.WeaponRackData;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemWeaponRack extends ItemPart {

	public ItemWeaponRack() {
		super(1);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		ItemStack seat1 = new ItemStack(ModItems.SEAT.get());
		seat1.setTag(new WeaponRackData(0.01f).write());
		items.add(seat1);
	}

}
