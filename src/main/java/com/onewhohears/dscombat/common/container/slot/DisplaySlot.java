package com.onewhohears.dscombat.common.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DisplaySlot extends Slot {

	public DisplaySlot(Container container, int slot, int pX, int pY) {
		super(container, slot, pX, pY);
	}
	
	@Override
	public void setChanged() {
		
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean mayPickup(Player player) {
		return false;
	}

}
