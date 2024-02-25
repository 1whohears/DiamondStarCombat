package com.onewhohears.dscombat.common.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class StorageBoxContainer extends SimpleContainer {
	
	public StorageBoxContainer(int size) {
		super(size);
	}
	
	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return canAddItem(stack);
	}
	
	@Override
	public boolean canAddItem(ItemStack stack) {
		if (!super.canAddItem(stack)) return false;
		return stack.getItem().canFitInsideContainerItems();
	}
	
	@Override
	public void fromTag(ListTag nbt) {
		for(int i = 0; i < nbt.size() && i < getContainerSize(); ++i) {
			ItemStack itemstack = ItemStack.of(nbt.getCompound(i));
			setItem(i, itemstack);
		}
	}
	
	@Override
	public ListTag createTag() {
		ListTag listtag = new ListTag();
		for(int i = 0; i < getContainerSize(); ++i) {
			ItemStack itemstack = getItem(i);
			listtag.add(itemstack.save(new CompoundTag()));
		}
		return listtag;
	}
	
}
