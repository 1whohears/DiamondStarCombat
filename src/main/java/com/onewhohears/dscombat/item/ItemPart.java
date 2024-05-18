package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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
		if (group.getId() == ModItems.PARTS.getId()) {
			ItemStack test = new ItemStack(this);
			test.setTag(getDefaultPartStats().createFilledPartInstance("").writeNBT());
			items.add(test);
		}
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
		return UtilMCText.translatable(getDescriptionId()).setStyle(Style.EMPTY.withColor(0x55FF55));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		getDefaultPartStats().addToolTips(tips, isAdvanced);
	}

}
