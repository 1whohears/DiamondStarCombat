package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.parts.StorageBoxData;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemStorageBox extends ItemPart {
	
	public final int size;
	
	public ItemStorageBox(float weight, SlotType[] compatibleSlots, int size) {
		super(1, weight, compatibleSlots);
		this.size = size;
	}

	@Override
	public PartData getPartData() {
		return new StorageBoxData(weight, UtilItem.getItemKey(this), compatibleSlots, size);
	}
	
	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		tips.add(UtilMCText.literal("Total Slots: "+size).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		int items = 0;
		if (stack.getTag() != null && stack.getTag().contains("items")) 
			items = countItemsInNBT(stack.getTag().getList("items", 10));
		tips.add(UtilMCText.literal(items+" Items").setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}
	
	public static int countItemsInNBT(ListTag nbt) {
		int k = 0;
		for(int i = 0; i < nbt.size(); ++i) {
			ItemStack itemstack = ItemStack.of(nbt.getCompound(i));
			if (itemstack.isEmpty()) continue;
			k += itemstack.getCount();
		}
		return k;
	}
	
}
