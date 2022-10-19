package com.onewhohears.dscombat.common.container.slot;

import com.onewhohears.dscombat.item.ItemPart;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PartItemSlot extends Slot {
	
	public PartItemSlot(Container container, int slot, int x, int y) {
		super(container, slot, x, y);
	}
	
	@Override
	public boolean isActive() {
		return true;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof ItemPart;
	}
	
	@Override
	public void setChanged() {
		
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
