package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;

public class StorageBoxData extends PartData {
	
	private final int size;
	private final SimpleContainer container;
	
	public StorageBoxData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, int size) {
		super(weight, itemid, compatibleSlots);
		this.size = size;
		this.container = new SimpleContainer(size);
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
	
	public void read(FriendlyByteBuf buffer) {
		super.read(buffer);
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		
	}
	
	public int getSize() {
		return size;
	}
	
	public SimpleContainer getItems() {
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
