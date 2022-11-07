package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.EngineData;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemEngine extends ItemPart {
	
	public final int num;
	
	public ItemEngine(int num) {
		super(1);
		this.num = num;
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			if (num == 0) {
				ItemStack test = new ItemStack(ModItems.TEST_ENGINE.get());
				test.setTag(new EngineData(0.01f, 0.04f, 4f, 0.001f, 
						ModItems.TEST_ENGINE.getId()).write());
				items.add(test);
			}
		}
	}

}
