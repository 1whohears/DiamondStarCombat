package com.onewhohears.dscombat.common.container;

import com.onewhohears.dscombat.common.container.slot.PartItemSlot;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.ModContainers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AircraftMenuContainer extends AbstractContainerMenu {
	
	private Container playerInv;
	private Container partsInv = new SimpleContainer(0);;
	
	/*public AircraftMenuContainer(int id, Inventory playerInv) {
		this(id, playerInv, new PartsManager());
		System.out.println("AircraftMenuContainer CLIENT");
	}*/
	
	public AircraftMenuContainer(int id, Inventory playerInv) {
		super(ModContainers.PLANE_MENU.get(), id);
		System.out.println("AircraftMenuContainer client side "+playerInv.player.level.isClientSide);
		this.playerInv = playerInv;
		if (playerInv.player.getRootVehicle() instanceof EntityAbstractAircraft plane) {
			this.partsInv = plane.partsManager.getContainer();
		}
		// create plane menu container
		for (int i = 0; i < partsInv.getContainerSize(); ++i) {
			//System.out.println("partsInv i = "+i);
			this.addSlot(new PartItemSlot(partsInv, i, 8 + i * 9, 50));
		}
		// display player inventory
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				//System.out.println("playerInv i = "+i+" j = "+j);
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
			}
		}
		for(int i = 0; i < 9; i++) {
			//System.out.println("playerInv i = "+i);
			this.addSlot(new Slot(playerInv, i, 8 + i * 18, 160));
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
