package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

public class PartSlot {
	
	private final String name;
	private final SlotType type;
	private PartData data;
	
	protected PartSlot(String name, SlotType type) {
		this.name = name;
		this.type = type;
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
