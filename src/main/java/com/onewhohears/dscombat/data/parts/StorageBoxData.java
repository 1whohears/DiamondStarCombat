package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.common.container.StorageBoxContainer;
import com.onewhohears.dscombat.common.container.menu.StorageBoxContainerMenu;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class StorageBoxData extends PartData {
	
	private final int size;
	private final StorageBoxContainer container;
	
	public StorageBoxData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, int size) {
		super(weight, itemid, compatibleSlots);
		this.size = size;
		this.container = new StorageBoxContainer(size);
	}
	
	public AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
		int rows = (int)Math.ceil(container.getContainerSize() / 9d);
		return new StorageBoxContainerMenu(getChestMenuTypeByRows(rows), id, 
				playerInventory, container, rows);
	}
	
	public static MenuType<?> getChestMenuTypeByRows(int rows) {
		if (rows == 1) return MenuType.GENERIC_9x1;
		else if (rows == 2) return MenuType.GENERIC_9x2;
		else if (rows == 3) return MenuType.GENERIC_9x3;
		else if (rows == 4) return MenuType.GENERIC_9x4;
		else if (rows == 5) return MenuType.GENERIC_9x5;
		else if (rows == 6) return MenuType.GENERIC_9x6;
		return MenuType.GENERIC_9x6;
	}
	
	public void read(CompoundTag tag) {
		super.read(tag);
		container.fromTag(tag.getList("items", 10));
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.put("items", container.createTag());
		return tag;
	}
	
	public int getSize() {
		return size;
	}
	
	public StorageBoxContainer getItems() {
		return container;
	}

	@Override
	public PartType getType() {
		return PartType.STORAGE_BOX;
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}

}
