package com.onewhohears.dscombat.common.container.slot;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PartItemSlot extends Slot {
	
	public final PartSlot data;
	
	public PartItemSlot(Container container, int slot, PartSlot data) {
		super(container, slot, data.getUIX(), data.getUIY());
		this.data = data;
	}
	
	@Override
	public boolean isActive() {
		return true;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		System.out.println("is "+stack+" compatible with tag "+stack.getOrCreateTag());
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.isEmpty()) return false;
		PartData part = UtilParse.parsePartFromCompound(tag);
		if (data.isCompatible(part)) return true;
		return false;
	}
	
	@Override
	public void setChanged() {
		super.setChanged();
	}
	
	@Override
	public int getMaxStackSize() {
		return 1;
	}
	
	@Override
	public boolean mayPickup(Player player) {
		return true;
	}

}
