package com.onewhohears.dscombat.block.entity;

import com.onewhohears.dscombat.common.container.menu.VehicleBlockContainerMenu;
import com.onewhohears.dscombat.init.ModBlockEntities;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class VehicleBlock extends SyncedBlockEntity implements MenuProvider {
	
	private NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
	
	public VehicleBlock(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.AIRCRAFT_BLOCK_ENTITY.get(), pos, blockState);
	}

	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
		return new VehicleBlockContainerMenu(containerId, playerInventory, this);
	}
	
	public NonNullList<ItemStack> getInventory() {
		return inventory;
	}

	@Override
	public Component getDisplayName() {
		return UtilMCText.translatable("container.dscombat.aircraft_block_menu");
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		ContainerHelper.loadAllItems(tag, inventory);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		ContainerHelper.saveAllItems(tag, inventory);
		super.saveAdditional(tag);
	}
	
    public boolean stillValid(Player player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64.0;
    }
    
}
