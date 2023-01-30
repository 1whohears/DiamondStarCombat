package com.onewhohears.dscombat.common.container;

import com.onewhohears.dscombat.block.entity.AircraftBlockEntity;
import com.onewhohears.dscombat.init.ModContainers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AircraftBlockMenuContainer extends AbstractContainerMenu {
	
	private AircraftBlockEntity aircraftBlock;
	private BlockPos pos;
	private boolean loaded = false;
	
	public AircraftBlockMenuContainer(int windowId, Container playerInv, AircraftBlockEntity aircraftBlock) {
		super(ModContainers.AIRCRAFT_BLOCK_MENU.get(), windowId);
		this.aircraftBlock = aircraftBlock;
		this.pos = aircraftBlock.getBlockPos();
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
