package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.FlareDispenserData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemFlareDispenser extends ItemPart {
	
	public final int flares, max, age;
	public final float heat;
	
	public ItemFlareDispenser(float weight, int flares, int max, float heat, int age, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
		this.flares = flares;
		this.max = max;
		this.heat = heat;
		this.age = age;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		MutableComponent name = ((MutableComponent)super.getName(stack))
			.append(" "+tag.getInt("flares")+"/"+max);
		return name;	
	}

	@Override
	public PartData getPartData() {
		return new FlareDispenserData(weight, flares, max, heat, age, ForgeRegistries.ITEMS.getKey(this), compatibleSlots);
	}
	
	@Override
	public PartData getFilledPartData(String param) {
		return new FlareDispenserData(weight, max, max, heat, age, ForgeRegistries.ITEMS.getKey(this), compatibleSlots);
	}

}
