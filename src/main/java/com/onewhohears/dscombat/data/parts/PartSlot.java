package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class PartSlot {
	
	private final String name;
	private final SlotType type;
	private final Vec3 pos;
	private final int uix, uiy;
	private PartData data;
	
	protected PartSlot(String name, SlotType type, Vec3 pos, int uix, int uiy) {
		this.name = name;
		this.type = type;
		this.pos = pos;
		this.uix = uix;
		this.uiy = uiy;
	}
	
	public PartSlot(CompoundTag tag) {
		name = tag.getString("name");
		type = SlotType.values()[tag.getInt("slot_type")];
		pos = UtilParse.readVec3(tag, "slot_pos");
		uix = tag.getInt("uix");
		uiy = tag.getInt("uiy");
		boolean filled = tag.getBoolean("filled");
		if (filled) {
			CompoundTag t = tag.getCompound("data");
			PartType type = PartType.values()[tag.getInt("type")];
			switch (type) {
			case SEAT:
				data = new SeatData(t);
				break;
			case TURRENT:
				break;
			}
		}
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putString("name", name);
		tag.putInt("slot_type",type.ordinal());
		UtilParse.writeVec3(tag, pos, "slot_pos");
		tag.putInt("uix", uix);
		tag.putInt("uiy", uiy);
		tag.putBoolean("filled", filled());
		if (filled()) tag.put("data", data.write());
		return tag;
	}
	
	public PartSlot(FriendlyByteBuf buffer) {
		name = buffer.readUtf();
		type = SlotType.values()[buffer.readInt()];
		pos = DataSerializers.VEC3.read(buffer);
		uix = buffer.readInt();
		uiy = buffer.readInt();
		boolean notNull = buffer.readBoolean();
		if (notNull) data = DataSerializers.PART_DATA.read(buffer);
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(name);
		buffer.writeInt(type.ordinal());
		DataSerializers.VEC3.write(buffer, pos);
		buffer.writeInt(uix);
		buffer.writeInt(uiy);
		buffer.writeBoolean(filled());
		if (filled()) data.write(buffer);
	}
	
	public boolean filled() {
		return data != null;
	}
	
	@Nullable
	public PartData getPartData() {
		return data;
	}
	
	public void setup(EntityAbstractAircraft plane) {
		if (filled()) data.setup(plane, pos);
	}
	
	public void clientSetup(EntityAbstractAircraft plane) {
		if (filled()) data.clientSetup(plane, pos);
	}
	
	public boolean addPartData(PartData data, EntityAbstractAircraft plane) {
		if (filled()) return false;
		if (!isCompatible(data)) return false;
		this.data = data;
		if (plane == null) return true;
		if (plane.level.isClientSide) data.clientSetup(plane, pos);
		else data.setup(plane, pos);
		return true;
	}
	
	public boolean removePartData(EntityAbstractAircraft plane) {
		if (plane.level.isClientSide) data.clientRemove(plane);
		else data.remove(plane);
		if (filled()) {
			data = null;
			return true;
		}
		return false;
	}
	
	public boolean isCompatible(PartData data) {
		SlotType[] types = data.getCompatibleSlots();
		for (int i = 0; i < types.length; ++i) if (types[i] == getSlotType()) return true;
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	public SlotType getSlotType() {
		return type;
	}
	
	public int getUIX() {
		return uix;
	}
	
	public int getUIY() {
		return uiy;
	}

	public static enum SlotType {
		SEAT,
		WING,
		FRAME,
		INTERNAL
	}
	
	@Override
	public String toString() {
		return "["+name+":"+getSlotType().toString()+":"+data+"]";
	}
	
}
