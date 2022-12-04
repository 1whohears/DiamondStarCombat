package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ItemPart extends Item {
	
	public final float weight;
	public final SlotType[] compatibleSlots;
	
	protected ItemPart(int stackSize, float weight, SlotType[] compatibleSlots) {
		this(partProps(stackSize), weight, compatibleSlots);
	}
	
	protected ItemPart(Properties props, float weight, SlotType[] compatibleSlots) {
		super(props);
		this.weight = weight;
		this.compatibleSlots = compatibleSlots;
	}
	
	public static Properties partProps(int stackSize) {
		return new Item.Properties().tab(ModItems.PARTS).stacksTo(stackSize);
	}
	
	public String getIdPart() {
		return getDescriptionId().split("\\.")[2];
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack test = new ItemStack(this);
			test.setTag(getNbt());
			items.add(test);
		}
	}
	
	@Override
	public void onCraftedBy(ItemStack stack, Level level, Player player) {
		//System.out.println("ON CRAFTED");
		//System.out.println("nbt = "+stack.getOrCreateTag());
		if (stack.getOrCreateTag().contains("type")) return;
		stack.setTag(getNbt());
	}
	
	public CompoundTag getNbt() {
		return getPartData().write();
	}
	
	public abstract PartData getPartData();
	
	public PartData getFilledPartData(String param) {
		return getPartData();
	}

}
