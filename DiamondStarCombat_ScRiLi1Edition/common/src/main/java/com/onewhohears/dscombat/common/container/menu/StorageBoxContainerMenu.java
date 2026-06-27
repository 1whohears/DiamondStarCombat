package com.onewhohears.dscombat.common.container.menu;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class StorageBoxContainerMenu extends ChestMenu {

	int slotNum = 0;

	public StorageBoxContainerMenu(MenuType<?> type, int containerId, Inventory playerInventory, Container container, int rows) {
		super(type, containerId, playerInventory, container, rows);
	}

	@Override
	public boolean stillValid(Player player) {
		return player.getRootVehicle() instanceof EntityVehicle;
	}

	@Override
	protected @NotNull Slot addSlot(@NotNull Slot slot) {
		if (slotNum < getRowCount() * 9) {
			slot = new ShulkerBoxSlot(getContainer(), slotNum, slot.x, slot.y);
			++slotNum;
		}
		return super.addSlot(slot);
	}
}
