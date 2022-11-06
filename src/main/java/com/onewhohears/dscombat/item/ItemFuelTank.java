package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.FuelTankData;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemFuelTank extends ItemPart {

	public ItemFuelTank() {
		super(1);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack test = new ItemStack(ModItems.TEST_TANK.get());
			test.setTag(new FuelTankData(0.01f, 100f, 100f).write());
			items.add(test);
			
			ItemStack test2 = new ItemStack(ModItems.TEST_TANK_2.get());
			test2.setTag(new FuelTankData(0.02f, 200f, 200f).write());
			items.add(test2);
		}
	}

}
