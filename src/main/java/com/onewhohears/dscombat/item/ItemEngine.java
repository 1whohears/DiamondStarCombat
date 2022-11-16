package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.EngineData;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemEngine extends ItemPart {
	
	public final float thrust, heat, fuelRate;
	
	public ItemEngine(float weight, float thrust, float heat, float fuelRate) {
		super(1, weight);
		this.thrust = thrust;
		this.heat = heat;
		this.fuelRate = fuelRate;
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack test = new ItemStack(this);
			test.setTag(new EngineData(weight, thrust, heat, fuelRate, getIdPart()).write());
			items.add(test);
		}
	}

}
