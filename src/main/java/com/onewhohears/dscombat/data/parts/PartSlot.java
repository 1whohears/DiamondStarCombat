package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class PartSlot {
	
	private final String name;
	private final SlotType type;
	private PartData data;
	
	protected PartSlot(String name, SlotType type) {
		this.name = name;
		this.type = type;
	}
	
	public PartSlot(CompoundTag tag) {
		name = tag.getString("name");
		type = SlotType.values()[tag.getInt("type")];
		PartType type = PartType.values()[tag.getInt("type")];
		switch (type) {
		case SEAT:
			data = new SeatData(tag);
			break;
		case TURRENT:
			break;
		}
		
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		
		return tag;
	}
	
	public PartSlot(FriendlyByteBuf buffer) {
		name = buffer.readUtf();
		type = SlotType.values()[buffer.readInt()];
		boolean notNull = buffer.readBoolean();
		if (notNull) data = DataSerializers.PART_DATA.read(buffer);
	}
	
	public void write(FriendlyByteBuf buffer) {
		
	}
	
	public boolean isEmpty() {
		return data == null;
	}
	
	@Nullable
	public PartData getPartData() {
		return data;
	}
	
	protected boolean setPartData(PartData data) {
		if (!isCompatible(data)) return false;
		this.data = data;
		return true;
	}
	
	public boolean isCompatible(PartData data) {
		SlotType[] types = data.getCompatibleSlots();
		for (int i = 0; i < types.length; ++i) if (types[i] == getSlotType()) return true;
		return false;
	}
	
	public String getString() {
		return name;
	}
	
	public SlotType getSlotType() {
		return type;
	}
	
	public static enum SlotType {
		SEAT,
		WING,
		FRAME,
		INTERNAL
	}
	
}
