package com.onewhohears.dscombat.common.container;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.ModContainers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

public class AircraftMenuContainer extends AbstractContainerMenu {
	
	private ContainerData planeParts;
	
	// server constructor
	public AircraftMenuContainer(int id, Inventory playerInv, EntityAbstractAircraft plane) {
		this(id, playerInv);
		planeParts = plane.partsManager.getContainerData();
	}
	
	// client constructor
	public AircraftMenuContainer(int id, Inventory playerInv) {
		super(ModContainers.PLANE_MENU.get(), id);
		// TODO create plane menu container
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
