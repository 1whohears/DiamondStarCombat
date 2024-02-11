package com.onewhohears.dscombat.common.container;

import java.util.List;

import com.onewhohears.dscombat.common.container.slot.PartItemSlot;
import com.onewhohears.dscombat.data.aircraft.AircraftClientPreset;
import com.onewhohears.dscombat.data.aircraft.AircraftClientPreset.UIPos;
import com.onewhohears.dscombat.data.aircraft.AircraftClientPresets;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModContainers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AircraftMenuContainer extends AbstractContainerMenu {
	
	private Container playerInv;
	private Container planeInv;
	private AircraftClientPreset clientData;
	
	public AircraftMenuContainer(int id, Inventory playerInv) {
		super(ModContainers.PLANE_MENU.get(), id);
		//System.out.println("AircraftMenuContainer client side "+playerInv.player.level.isClientSide);
		this.playerInv = playerInv;
		// display plane parts
		if (playerInv.player.getRootVehicle() instanceof EntityVehicle plane) {
			this.planeInv = plane.partsManager.getInventory();
			List<PartSlot> slots = plane.partsManager.getSlots();
			//System.out.println("client preset = "+plane.clientPreset);
			clientData = AircraftClientPresets.get().getPreset(plane.clientPresetId);
			//System.out.println("acp not null = "+(acp != null));
			// create plane menu container
			int x_start = 48, y_start = 15;
			int x = x_start, y = y_start;
			for (int i = 0; i < planeInv.getContainerSize(); ++i) {
				if (clientData != null) {
					UIPos uip = clientData.getSlotPos(slots.get(i).getSlotId());
					x = uip.getX();
					y = uip.getY();
				} else {
					if (i != 0 && i % 9 == 0) {
						y += 18;
						x = x_start;
					} else if (i != 0) x += 18;
				}
				//System.out.println("partsInv i = "+i+" x = "+x+" y = "+y);
				this.addSlot(new PartItemSlot(this, i, slots.get(i), x, y));
			}
		}
		// display player inventory
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				//System.out.println("playerInv i = "+i+" j = "+j);
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 48 + j * 18, 138 + i * 18));
			}
		}
		for(int i = 0; i < 9; i++) {
			//System.out.println("playerInv i = "+i);
			this.addSlot(new Slot(playerInv, i, 48 + i * 18, 196));
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public void slotsChanged(Container inventory) {
		super.slotsChanged(inventory);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		if (planeInv == null) return ItemStack.EMPTY;
		Slot slot = getSlot(index);
		if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;
		ItemStack stack1 = slot.getItem();
		ItemStack stack = stack1.copy();
		int planeSize = planeInv.getContainerSize();
		if (index < planeSize) { if (!moveItemStackTo(stack1, 
				planeSize, slots.size(), true)) {
			return ItemStack.EMPTY;
		} } else if (index >= planeSize) { if (!moveItemStackTo(stack1, 
				0, planeSize, false)) {
			return ItemStack.EMPTY;
		} }
		if (stack1.isEmpty()) slot.set(ItemStack.EMPTY);
		else slot.setChanged();
		return stack;
	}
	
	public Container getPlayerInventory() {
        return this.playerInv;
    }
	
	public Container getPlaneInventory() {
		return this.planeInv;
	}
	
	public AircraftClientPreset getClientData() {
		return clientData;
	}

}
