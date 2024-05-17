package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemPart extends Item {
	
	public final String defaultPartId;
	
	public ItemPart(int stackSize) {
		this(partProps(stackSize));
	}
	
	public ItemPart(Properties props) {
		super(props);
		this.defaultPartId = UtilItem.getItemKey(this).getPath();
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
			test.setTag(getFilledNbt());
			items.add(test);
		}
	}
	
	public CompoundTag getNbt() {
		return getPartData().writeNBT();
	}
	
	public CompoundTag getFilledNbt() {
		return getFilledPartData("").writeNBT();
	}
	
	public PartInstance<?> getPartData() {
		return getPartStats().createPartInstance();
	}
	
	public PartInstance<?> getFilledPartData(String param) {
		PartInstance<?> part = getPartData();
		part.setFilled(param);
		return part;
	}
	
	public PartStats getPartStats() {
		return PartPresets.get().get(defaultPartId);
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		stack.setTag(getNbt());
		return stack;
	}
	
	@Override
	public Component getName(ItemStack stack) {
		return UtilMCText.translatable(getDescriptionId()).setStyle(Style.EMPTY.withColor(0x55FF55));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		getPartStats().addToolTips(tips, isAdvanced);
	}

}
