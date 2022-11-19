package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.RadarPartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemRadarPart extends ItemPart {
	
	public final String preset;
	
	public ItemRadarPart(float weight, String preset, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
		this.preset = preset;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		/*CompoundTag tag = stack.getOrCreateTag();
		MutableComponent name = UtilMCText.simpleText(getDescriptionId())
			.append(" "+tag.getInt("fuel")+"/"+tag.getInt("max"));
		return name;*/
		return super.getName(stack);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack test = new ItemStack(this);
			test.setTag(new RadarPartData(weight, preset, getIdPart(), compatibleSlots).write());
			items.add(test);
		}
	}

}
