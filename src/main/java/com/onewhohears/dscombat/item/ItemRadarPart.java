package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.RadarPartData;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemRadarPart extends ItemPart {
	
	public ItemRadarPart(int num) {
		super(1, num);
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
			if (num == 0) {
				ItemStack test = new ItemStack(ModItems.TEST_AIR_RADAR.get());
				test.setTag(new RadarPartData(0.001f, "test_air", ModItems.TEST_AIR_RADAR.getId()).write());
				items.add(test);
			} else if (num == 1) {
				ItemStack test = new ItemStack(ModItems.TEST_GROUND_RADAR.get());
				test.setTag(new RadarPartData(0.001f, "test_ground", ModItems.TEST_GROUND_RADAR.getId()).write());
				items.add(test);
			}
		}
	}

}
