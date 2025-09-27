package com.onewhohears.dscombat.common.container.menu;

import com.onewhohears.dscombat.block.entity.WeaponsBlockEntity;
import com.onewhohears.dscombat.common.container.slot.DisplaySlot;
import com.onewhohears.dscombat.init.ModContainers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WeaponsBlockContainerMenu extends AbstractContainerMenu {
	
	public final Container recipeSlots = new SimpleContainer(16);
	private WeaponsBlockEntity weaponsBlock;
	private BlockPos pos;
	private boolean loaded = false;
	
	public WeaponsBlockContainerMenu(int windowId, Container playerInv, WeaponsBlockEntity weaponsBlock) {
		super(ModContainers.WEAPONS_BLOCK_MENU.get(), windowId);
		this.weaponsBlock = weaponsBlock;
		this.pos = weaponsBlock.getBlockPos();
		int startX = 130, startY = 46;
		for (int i = 0; i < 4; ++i) for(int j = 0; j < 4; j++) 
			addSlot(new DisplaySlot(recipeSlots, j + i * 4, startX + j * 18, startY + i * 18));
		// display player inventory
		for(int i = 0; i < 3; i++) for(int j = 0; j < 9; j++) 
			addSlot(new Slot(playerInv, j + i * 9 + 9, 48 + j * 18, 138 + i * 18));
		for(int i = 0; i < 9; i++) 
			addSlot(new Slot(playerInv, i, 48 + i * 18, 196));
		this.loaded = true;
	}
	
	@Override
	public void slotsChanged(Container inventory) {
		//System.out.println("SLOTS CHANGED "+inventory);
		if (this.loaded) {
			
		}
		super.slotsChanged(inventory);
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		ItemStack stack = ItemStack.EMPTY;
		return stack;
	}

	@Override
	public boolean stillValid(Player player) {
		return weaponsBlock.stillValid(player);
	}
	
	public WeaponsBlockEntity getWeaponsBlock() {
		return weaponsBlock;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public boolean isLoaded() {
    	return this.loaded;
    }

}
