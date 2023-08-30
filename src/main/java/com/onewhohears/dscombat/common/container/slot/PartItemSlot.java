package com.onewhohears.dscombat.common.container.slot;

import com.onewhohears.dscombat.common.container.AircraftMenuContainer;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PartItemSlot extends Slot {
	
	public final AircraftMenuContainer menu;
	public final PartSlot data;
	
	public PartItemSlot(AircraftMenuContainer menu, int slot, PartSlot data, int x, int y) {
		super(menu.getPlaneInventory(), slot, x, y);
		this.menu = menu;
		this.data = data;
	}
	
	@Override
	public boolean isActive() {
		return true;
	}
	
	@Override
	public void setChanged() {
		//System.out.println("THIS CHANGED "+this);
		//super.setChanged();
	}
	
	@Override
	public int getMaxStackSize() {
		return 1;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		//System.out.println("is "+stack+" compatible with tag "+stack.getOrCreateTag());
		PartData part = UtilParse.parsePartFromItem(stack);
		if (part == null) return false;
		if (data.isCompatible(part)) return true;
		return false;
	}
	
	@Override
	public boolean mayPickup(Player player) {
		if (!(data.isPilotSlot() && data.isNormalSeat()))
			return mayPlace(menu.getCarried());
		return !data.isLocked();
	}

}
