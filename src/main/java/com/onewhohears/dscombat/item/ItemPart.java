package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
	
	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		stack.setTag(getNbt());
		return stack;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		MutableComponent c = Component.literal("Compatible: ");
		for (int i = 0; i < compatibleSlots.length; ++i) c.append(compatibleSlots[i].toString()+" ");
		tips.add(c);
		tips.add(Component.literal("Mass: "+weight));
	}

}
