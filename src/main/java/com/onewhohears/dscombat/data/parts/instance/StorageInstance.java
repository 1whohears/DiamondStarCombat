package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.common.container.StorageBoxContainer;
import com.onewhohears.dscombat.common.container.menu.StorageBoxContainerMenu;
import com.onewhohears.dscombat.data.parts.stats.StorageStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilItem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class StorageInstance<T extends StorageStats> extends PartInstance<T> {
	
	private final StorageBoxContainer container;
	
	public StorageInstance(T stats) {
		super(stats);
		this.container = new StorageBoxContainer(stats.getSize());
	}
	
	public AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
		int rows = (int)Math.ceil(container.getContainerSize() / 9d);
		return new StorageBoxContainerMenu(UtilItem.getChestMenuTypeByRows(rows), id, 
				playerInventory, container, rows);
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
		container.fromTag(tag.getList("items", 10));
	}
	
	@Override
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.put("items", container.createTag());
		return tag;
	}
	
	public StorageBoxContainer getItems() {
		return container;
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}

}
