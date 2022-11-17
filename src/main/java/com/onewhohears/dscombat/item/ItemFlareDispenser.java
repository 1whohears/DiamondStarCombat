package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.FlareDispenserData;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemFlareDispenser extends ItemPart {
	
	public final int flares, max, age;
	public final float heat;
	
	public ItemFlareDispenser(float weight, int flares, int max, float heat, int age) {
		super(1, weight);
		this.flares = flares;
		this.max = max;
		this.heat = heat;
		this.age = age;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		MutableComponent name = UtilMCText.simpleText(getDescriptionId())
			.append(" "+tag.getInt("flares")+"/"+tag.getInt("max"));
		return name;	
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack test = new ItemStack(this);
			test.setTag(new FlareDispenserData(weight, flares, max, heat, age, getIdPart()).write());
			items.add(test);
		}
	}

}
