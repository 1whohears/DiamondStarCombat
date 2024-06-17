package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemPart extends Item {
	
	public ItemPart(int stackSize) {
		this(partProps(stackSize));
	}
	
	public ItemPart(Properties props) {
		super(props);
	}
	
	public static Properties partProps(int stackSize) {
		return new Item.Properties().tab(ModItems.PARTS).stacksTo(stackSize);
	}
	
	public static Properties itemProps(int stackSize) {
		return new Item.Properties().tab(ModItems.DSC_ITEMS).stacksTo(stackSize);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() != getCreativeTab().getId() && group.getId() != CreativeModeTab.TAB_SEARCH.getId()) return;
		String itemId = UtilItem.getItemKeyString(this);
		for (int i = 0; i < PartPresets.get().getNum(); ++i) {
			PartStats stats = PartPresets.get().getAll()[i];
			if (!stats.getItemId().equals(itemId)) continue;
			fillItemCategory(stats, items);
		}
	}
	
	protected void fillItemCategory(PartStats stats, NonNullList<ItemStack> items) {
		items.add(stats.createFilledPartInstance("").getNewItemStack());
	}
	
	public CreativeModeTab getCreativeTab() {
		return ModItems.PARTS;
	}
	
	public PartStats getDefaultPartStats() {
		return PartPresets.get().get(getDefaultPartPresetId());
	}
	
	public String getDefaultPartPresetId() {
		return toString();
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		stack.setTag(getDefaultPartStats().createPartInstance().writeNBT());
		return stack;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		return getPartInstance(stack).getItemName();
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		getPartInstance(stack).addToolTips(tips, isAdvanced);
	}
	
	public PartInstance<?> getPartInstance(ItemStack stack) {
		return UtilParse.parsePartFromItem(stack, getDefaultPartPresetId());
	}

}
