package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.onewholibs.util.UtilItem;

import com.onewhohears.dscombat.util.UtilPresetParse;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemPart extends Item {

	protected final String defaultPresetId;

	public ItemPart(int stackSize, String defaultPresetId) {
		this(partProps(stackSize), defaultPresetId);
	}

	public ItemPart(Properties props, String defaultPresetId) {
		super(props);
		this.defaultPresetId = defaultPresetId;
	}

	public ItemPart(int stackSize) {
		super(partProps(stackSize));
		this.defaultPresetId = toString();
	}
	
	public ItemPart(Properties props) {
		super(props);
		this.defaultPresetId = toString();
	}
	
	public static Properties partProps(int stackSize) {
		return new Item.Properties().tab(ModItems.PARTS).stacksTo(stackSize);
	}
	
	public static Properties itemProps(int stackSize) {
		return new Item.Properties().tab(ModItems.DSC_ITEMS).stacksTo(stackSize);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
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

	/**
	 * DO NOT CALL BEFORE THE DATA GENERATORS HAVE BEEN RUN OR IT WILL BE NULL
	 */
	@Nullable
	public PartStats getDefaultPartStats() {
		return PartPresets.get().get(getDefaultPartPresetId());
	}
	
	public String getDefaultPartPresetId() {
		return defaultPresetId;
	}
	
	@Override
	public @NotNull ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		PartStats stats = getDefaultPartStats();
		if (stats != null) stack.setTag(stats.createPartInstance().writeNBT());
		return stack;
	}
	
	@Override
	public @NotNull Component getName(@NotNull ItemStack stack) {
		PartInstance<?> instance = getPartInstance(stack);
		if (instance != null) return instance.getItemName();
		return super.getName(stack);
	}
	
	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tips, @NotNull TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		PartInstance<?> instance = getPartInstance(stack);
		if (instance != null) instance.addToolTips(tips, isAdvanced);
	}

	@Nullable
	public PartInstance<?> getPartInstance(ItemStack stack) {
		return UtilPresetParse.parsePartFromItem(stack, getDefaultPartPresetId());
	}

}
