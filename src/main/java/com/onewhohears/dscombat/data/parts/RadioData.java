package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class RadioData extends PartData {
	
	private String disk;
	
	public RadioData(ResourceLocation itemid, String disk) {
		super(0.1f, itemid, SlotType.INTERNAL_ALL);
		this.disk = disk;
	}

	public RadioData(CompoundTag tag) {
		super(tag);
		disk = tag.getString("disk");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putString("disk", disk);
		return tag;
	}

	public RadioData(FriendlyByteBuf buffer) {
		super(buffer);
		disk = buffer.readUtf();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(disk);
	}
	
	@Override
	public PartType getType() {
		return PartType.RADIO;
	}

	@Override
	public boolean isSetup(String slotId, EntityAircraft craft) {
		return false;
	}
	
	@Override
	protected void clientTick(String slotId) {
		if (disk.isEmpty()) return;
		// TODO 9.2 play music on client side
	}
	
	/**
	 * use from server side
	 * @param disk music disk item id
	 */
	public void setDisk(String disk) {
		// TODO 9.3 set disk packet
		this.disk = disk;
	}
	
	/**
	 * use from server side
	 */
	public void removeDisk() {
		// TODO 9.4 remove disk packet
		this.disk = "";
	}

}
