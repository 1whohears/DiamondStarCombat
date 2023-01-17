package com.onewhohears.dscombat.common.container.slot;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
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
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.isEmpty()) return false;
		PartData part = UtilParse.parsePartFromCompound(tag);
		if (part == null) return false;
		if (data.getName().equals(PartSlot.PILOT_SLOT_NAME) && data.filled()) {
			if (part.getType() == PartType.SEAT) return false;
		}
		if (data.isCompatible(part)) return true;
		return false;
	}
	
	@Override
	public boolean mayPickup(Player player) {
		/*if (data.getName().equals(PartSlot.PILOT_SLOT_NAME)) {
			if (!data.filled()) return false;
			PartData pd = data.getPartData();
			if (pd.getType() == PartType.SEAT) {
				this.getItem().setCount(0);
			}
		}*/
		// TODO find way to replace pilot turrets
		return true;
	}
	
	@Override
	public void set(ItemStack stack) {
		/*System.out.println("set "+stack);
		if (data.getName().equals(PartSlot.PILOT_SLOT_NAME)) {
			if (stack.isEmpty()) {
				stack = ModItems.SEAT.get().getDefaultInstance();
				System.out.println("setting to default seat");
			} else if (data.filled()) {
				PartData pd = data.getPartData();
				if (pd.getType() == PartType.SEAT) {
					container.setItem(getSlotIndex(), ItemStack.EMPTY);
				}
			} 
		}*/
		super.set(stack);
	}

}
