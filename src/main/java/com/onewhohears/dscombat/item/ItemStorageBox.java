package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemStorageBox extends ItemPart {
	
	public ItemStorageBox(int stackSize) {
		super(stackSize);
	}
	
	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
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
