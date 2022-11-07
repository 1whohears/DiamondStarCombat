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
	
	public final int num;
	
	public ItemFuelTank(int num) {
		super(1);
		this.num = num;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		MutableComponent name = UtilMCText.simpleText(getDescriptionId()).append(" ")
			.append(" "+tag.getInt("fuel")+"/"+tag.getInt("max"));
		return name;	
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			if (num == 0) {
				ItemStack test = new ItemStack(ModItems.TEST_TANK.get());
				test.setTag(new FuelTankData(0.01f, 100f, 100f, ModItems.TEST_TANK.getId()).write());
				items.add(test);
			} else if (num == 1) {
				ItemStack test2 = new ItemStack(ModItems.TEST_TANK_2.get());
				test2.setTag(new FuelTankData(0.02f, 200f, 200f, ModItems.TEST_TANK_2.getId()).write());
				items.add(test2);
			}
		}
	}

}
