package com.onewhohears.dscombat.common.container;

import java.util.List;

import com.onewhohears.dscombat.common.container.slot.PartItemSlot;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.ModContainers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AircraftMenuContainer extends AbstractContainerMenu {
	
	private Container playerInv;
	private PartsManager pm;
	private boolean loaded = false;
	
	public AircraftMenuContainer(int id, Inventory playerInv) {
		super(ModContainers.PLANE_MENU.get(), id);
		System.out.println("AircraftMenuContainer client side "+playerInv.player.level.isClientSide);
		this.playerInv = playerInv;
		// display plane parts
		if (playerInv.player.getRootVehicle() instanceof EntityAbstractAircraft plane) {
			this.pm = plane.partsManager;
			Container partsInv = pm.getContainer(this);
			List<PartSlot> slots = plane.partsManager.getSlots();
			// create plane menu container
			for (int i = 0; i < partsInv.getContainerSize(); ++i) {
				//System.out.println("partsInv i = "+i);
				this.addSlot(new PartItemSlot(partsInv, i, slots.get(i)));
			}
		}
		// TODO display plane ammo
		
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
		this.loaded = true;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public void slotsChanged(Container inventory) {
		//System.out.println("SLOTS CHANGED "+inventory);
		if (this.loaded) pm.readContainer(inventory);
		super.slotsChanged(inventory);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		return stack;
	}
	
	public Container getPlayerInventory() {
        return this.playerInv;
    }

    public PartsManager getPartsInventory() {
        return this.pm;
    }
    
    public boolean isLoaded() {
    	return this.loaded;
    }

}
