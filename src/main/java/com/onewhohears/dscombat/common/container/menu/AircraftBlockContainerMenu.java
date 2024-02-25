package com.onewhohears.dscombat.common.container.menu;

import com.onewhohears.dscombat.block.entity.AircraftBlockEntity;
import com.onewhohears.dscombat.init.ModContainers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AircraftBlockContainerMenu extends AbstractContainerMenu {
	
	private AircraftBlockEntity aircraftBlock;
	private BlockPos pos;
	private boolean loaded = false;
	
	public AircraftBlockContainerMenu(int windowId, Container playerInv, AircraftBlockEntity aircraftBlock) {
		super(ModContainers.AIRCRAFT_BLOCK_MENU.get(), windowId);
		this.aircraftBlock = aircraftBlock;
		this.pos = aircraftBlock.getBlockPos();
		// display player inventory
		int startX = 97;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, startX + j * 18, 157 + i * 18));
			}
		}
		for(int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInv, i, startX + i * 18, 215));
		}
		this.loaded = true;
	}
	
	@Override
	public void slotsChanged(Container inventory) {
		super.slotsChanged(inventory);
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		ItemStack stack = ItemStack.EMPTY;
		return stack;
	}

	@Override
	public boolean stillValid(Player player) {
		return aircraftBlock.stillValid(player);
	}
	
	public AircraftBlockEntity getWeaponsBlock() {
		return aircraftBlock;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public boolean isLoaded() {
    	return this.loaded;
    }

}
