package com.onewhohears.dscombat.common.container.menu;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxSlot;

public class StorageBoxContainerMenu extends ChestMenu {

	public StorageBoxContainerMenu(MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, Container pContainer, int pRows) {
		super(pType, pContainerId, pPlayerInventory, pContainer, pRows);
		int i = 0;
		for(int j = 0; j < getRowCount(); ++j) for(int k = 0; k < 9; ++k) 
			slots.set(i++, new ShulkerBoxSlot(pContainer, k + j * 9, 8 + k * 18, 18 + j * 18));
	}
	
	@Override
	public boolean stillValid(Player player) {
		return player.getRootVehicle() instanceof EntityVehicle;
	}

}
