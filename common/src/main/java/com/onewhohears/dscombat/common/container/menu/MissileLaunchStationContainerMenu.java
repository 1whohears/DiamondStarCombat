package com.onewhohears.dscombat.common.container.menu;

import com.onewhohears.dscombat.block.entity.MissileLaunchStationBlockEntity;
import com.onewhohears.dscombat.init.ModContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MissileLaunchStationContainerMenu extends AbstractContainerMenu {
	
	private final MissileLaunchStationBlockEntity station;
	private final BlockPos pos;
	private final ContainerData data;
	
	// Client-side constructor
	public MissileLaunchStationContainerMenu(int windowId, Inventory playerInv) {
		this(windowId, playerInv, null, new SimpleContainerData(8));
	}
	
	// Server-side constructor
	public MissileLaunchStationContainerMenu(int windowId, Inventory playerInv, 
											 MissileLaunchStationBlockEntity station, ContainerData data) {
		super(ModContainers.MISSILE_LAUNCH_STATION_MENU.get(), windowId);
		this.station = station;
		this.pos = station != null ? station.getBlockPos() : BlockPos.ZERO;
		this.data = data;
		
		addDataSlots(data);
		
		Container inventory = station != null ? new InventoryWrapper(station.getInventory()) : new InventoryWrapper(null);
		
		// Missile slot (1 slot, positioned left of ARM button) - only accepts missiles
		int missileX = 8; // Left side of the interface
		int missileY = 20; // Same height as ARM button
		addSlot(new com.onewhohears.dscombat.common.container.slot.MissileSlot(
			inventory, 0, missileX, missileY));
		
		// Player inventory (3 rows) - adjusted for new layout
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 108 + i * 18));
			}
		}
		
		// Player hotbar - adjusted for new layout
		for (int i = 0; i < 9; i++) {
			addSlot(new Slot(playerInv, i, 8 + i * 18, 166));
		}
	}

	@Override
	public @NotNull ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		
		if (slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			itemstack = slotStack.copy();
			
			// From station to player inventory
			if (index < 1) {
				if (!this.moveItemStackTo(slotStack, 1, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			}
			// From player inventory to station
			else {
				if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			if (slotStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		
		return itemstack;
	}

	@Override
	public boolean stillValid(Player player) {
		return station == null || station.stillValid(player);
	}
	
	public MissileLaunchStationBlockEntity getStation() {
		return station;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	// Data getters for client
	public double getTargetX() {
		return data.get(0);
	}
	
	public double getTargetY() {
		return data.get(1);
	}
	
	public double getTargetZ() {
		return data.get(2);
	}
	
	public boolean isArmed() {
		return data.get(3) != 0;
	}
	
	public int getLaunchCooldown() {
		return data.get(4);
	}
	
	public int getSelectedMissileSlot() {
		return data.get(5);
	}

	public int getEstimatedDistance() {
		return data.get(6);
	}

	public int getEstimatedFlightTicks() {
		return data.get(7);
	}
	
	// Simple wrapper to adapt NonNullList to Container interface
	private static class InventoryWrapper implements Container {
		private final java.util.List<ItemStack> items;
		
		public InventoryWrapper(java.util.List<ItemStack> items) {
			this.items = items != null ? items : java.util.Collections.emptyList();
		}
		
		@Override
		public int getContainerSize() {
			return items.size();
		}
		
		@Override
		public boolean isEmpty() {
			return items.stream().allMatch(ItemStack::isEmpty);
		}
		
		@Override
		public ItemStack getItem(int slot) {
			return slot >= 0 && slot < items.size() ? items.get(slot) : ItemStack.EMPTY;
		}
		
		@Override
		public ItemStack removeItem(int slot, int amount) {
			if (slot >= 0 && slot < items.size()) {
				return items.get(slot).split(amount);
			}
			return ItemStack.EMPTY;
		}
		
		@Override
		public ItemStack removeItemNoUpdate(int slot) {
			if (slot >= 0 && slot < items.size()) {
				ItemStack stack = items.get(slot);
				items.set(slot, ItemStack.EMPTY);
				return stack;
			}
			return ItemStack.EMPTY;
		}
		
		@Override
		public void setItem(int slot, ItemStack stack) {
			if (slot >= 0 && slot < items.size()) {
				items.set(slot, stack);
			}
		}
		
		@Override
		public void setChanged() {
		}
		
		@Override
		public boolean stillValid(Player player) {
			return true;
		}
		
		@Override
		public void clearContent() {
			items.clear();
		}
	}
}
