package com.onewhohears.dscombat.common.container;

import com.onewhohears.dscombat.common.container.slot.PartItemSlot;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.init.ModContainers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AircraftMenuContainer extends AbstractContainerMenu {
	
	private Container playerInv;
	private Container partsInv;
	
	public AircraftMenuContainer(int id, Inventory playerInv) {
		this(id, playerInv, new PartsManager());
		System.out.println("AircraftMenuContainer CLIENT");
	}
	
	public AircraftMenuContainer(int id, Inventory playerInv, PartsManager pm) {
		super(ModContainers.PLANE_MENU.get(), id);
		System.out.println("AircraftMenuContainer SERVER");
		this.playerInv = playerInv;
		partsInv = pm.getContainer();
		// create plane menu container
		for (int i = 0; i < partsInv.getContainerSize(); ++i) {
			this.addSlot(new PartItemSlot(partsInv, i, i*9, 0));
		}
		// display player inventory
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
			}
		}
		for(int i = 0; i < 9; i++) {
			if(i == playerInv.selected) {
				this.addSlot(new Slot(playerInv, i, 8 + i * 18, 160) {
					@Override
					public boolean mayPickup(Player playerIn) {
						return false;
					}
				});
			} else {
				this.addSlot(new Slot(playerInv, i, 8 + i * 18, 160));
			}
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return null;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

}
