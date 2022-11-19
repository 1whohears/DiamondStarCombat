package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.EngineData;
import com.onewhohears.dscombat.data.parts.ExternalEngineData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemEngine extends ItemPart {
	
	public final float thrust, heat, fuelRate;
	public final boolean external;
	
	public ItemEngine(float weight, float thrust, float heat, float fuelRate, boolean external, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
		this.thrust = thrust;
		this.heat = heat;
		this.fuelRate = fuelRate;
		this.external = external;
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack test = new ItemStack(this);
			if (external) test.setTag(new ExternalEngineData(weight, thrust, heat, fuelRate, getIdPart(), compatibleSlots).write());
			else test.setTag(new EngineData(weight, thrust, heat, fuelRate, getIdPart(), compatibleSlots).write());
			items.add(test);
		}
	}

}
