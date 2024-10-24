package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.common.container.StorageBoxContainer;
import com.onewhohears.dscombat.common.container.menu.StorageBoxContainerMenu;
import com.onewhohears.dscombat.data.parts.stats.StorageStats;
import com.onewhohears.dscombat.init.ModContainers;
import com.onewhohears.onewholibs.util.UtilItem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class StorageInstance<T extends StorageStats> extends PartInstance<T> {
	
	private final StorageBoxContainer container;
	
	public StorageInstance(T stats) {
		super(stats);
		this.container = new StorageBoxContainer(this);
	}
	
	public StorageBoxContainerMenu createMenu(int id, Inventory playerInventory) {
		int rows = getRowCount();
		return new StorageBoxContainerMenu(getStorageMenuTypeByRows(rows),
				id, playerInventory, container, rows);
	}

	public static MenuType<?> getStorageMenuTypeByRows(int rows) {
		if (rows <= 0) return ModContainers.VEHICLE_STORAGE_MENU_9x0.get();
		else if (rows == 1) return ModContainers.VEHICLE_STORAGE_MENU_9x1.get();
		else if (rows == 2) return ModContainers.VEHICLE_STORAGE_MENU_9x2.get();
		else if (rows == 3) return ModContainers.VEHICLE_STORAGE_MENU_9x3.get();
		else if (rows == 4) return ModContainers.VEHICLE_STORAGE_MENU_9x4.get();
		else if (rows == 5) return ModContainers.VEHICLE_STORAGE_MENU_9x5.get();
		return ModContainers.VEHICLE_STORAGE_MENU_9x6.get();
	}

	public static StorageBoxContainerMenu getEmptyStorageMenu(int id, Inventory playerInventory) {
		return new StorageBoxContainerMenu(getStorageMenuTypeByRows(0),
				id, playerInventory, new SimpleContainer(0), 0);
	}

	public int getRowCount() {
		return (int)Math.ceil(container.getContainerSize() / 9d);
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

}
