package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.BuffData;
import com.onewhohears.dscombat.data.parts.BuffData.BuffType;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemBuffPart extends ItemPart {

	public final BuffType type;
	
	public ItemBuffPart(BuffType type, SlotType[] compatibleSlots) {
		super(1, 0, compatibleSlots);
		this.type = type;
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack test = new ItemStack(this);
			test.setTag(new BuffData(type, getIdPart(), compatibleSlots).write());
			items.add(test);
		}
	}

}
