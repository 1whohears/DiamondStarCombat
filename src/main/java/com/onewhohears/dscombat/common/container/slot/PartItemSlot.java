package com.onewhohears.dscombat.common.container.slot;

import com.onewhohears.dscombat.common.container.menu.VehicleContainerMenu;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;

import com.onewhohears.dscombat.util.UtilPresetParse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PartItemSlot extends Slot {
	
	public final VehicleContainerMenu menu;
	public final PartSlot data;
	
	public PartItemSlot(VehicleContainerMenu menu, int slot, PartSlot data, int x, int y) {
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
		PartInstance<?> part = UtilPresetParse.parsePartFromItem(stack);
		if (part == null) return false;
        return data.isCompatible(part);
    }
	
	@Override
	public boolean mayPickup(Player player) {
		if (data.isLocked()) return false;
		if (data.isPilotSlot()) return mayPlace(menu.getCarried());
		return true;
	}

}
