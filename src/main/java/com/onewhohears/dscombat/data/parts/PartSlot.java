package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class PartSlot {
	
	public static final String PILOT_SLOT_NAME = "dscombat.pilot_seat";
	
	private final String name, typeName;
	private final SlotType type;
	private final Vec3 pos;
	private final int uix, uiy, offsetX;
	private PartData data;
	
	protected PartSlot(String name, SlotType type, Vec3 pos, int uix, int uiy) {
		this.name = name;
		this.type = type;
		this.pos = pos;
		this.uix = uix;
		this.uiy = uiy;
		this.offsetX = getIconOffsetX(type);
		this.typeName = getTypeName(type);
	}
	
	public PartSlot(CompoundTag tag) {
		name = tag.getString("name");
		type = SlotType.values()[tag.getInt("slot_type")];
		pos = UtilParse.readVec3(tag, "slot_pos");
		uix = tag.getInt("uix");
		uiy = tag.getInt("uiy");
		if (tag.contains("data")) data = UtilParse.parsePartFromCompound(tag.getCompound("data"));
		// not saved
		offsetX = getIconOffsetX(type);
		typeName = getTypeName(type);
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putString("name", name);
		tag.putInt("slot_type",type.ordinal());
		UtilParse.writeVec3(tag, pos, "slot_pos");
		tag.putInt("uix", uix);
		tag.putInt("uiy", uiy);
		if (filled()) tag.put("data", data.write());
		return tag;
	}
	
	public PartSlot(FriendlyByteBuf buffer) {
		//System.out.println("PART SLOT BUFFER");
		name = buffer.readUtf();
		//System.out.println("name = "+name);
		type = SlotType.values()[buffer.readInt()];
		//System.out.println("type = "+type.name());
		pos = DataSerializers.VEC3.read(buffer);
		//System.out.println("pos = "+pos);
		uix = buffer.readInt();
		//System.out.println("uix = "+uix);
		uiy = buffer.readInt();
		//System.out.println("uiy = "+uiy);
		boolean notNull = buffer.readBoolean();
		//System.out.println("notNull = "+notNull);
		if (notNull) data = DataSerializers.PART_DATA.read(buffer);
		// not in packet
		offsetX = getIconOffsetX(type);
		typeName = getTypeName(type);
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
		if (filled()) data.setup(plane, name, pos);
	}
	
	public void clientSetup(EntityAbstractAircraft plane) {
		if (filled()) data.clientSetup(plane, name, pos);
	}
	
	protected void tick() {
		if (filled()) data.tick(name);
	}
	
	protected void clientTick() {
		if (filled()) data.clientTick(name);
	}
	
	public boolean addPartData(PartData data, EntityAbstractAircraft plane) {
		if (filled()) return false;
		if (!isCompatible(data)) return false;
		this.data = data;
		if (plane == null) return true;
		if (plane.level.isClientSide) data.clientSetup(plane, name, pos);
		else data.setup(plane, name, pos);
		return true;
	}
	
	public boolean removePartData(EntityAbstractAircraft plane) {
		if (filled()) {
			if (plane.level.isClientSide) data.clientRemove(name);
			else data.remove(name);
			data = null;
			return true;
		}
		return false;
	}
	
	public boolean isCompatible(PartData data) {
		//System.out.println("is "+data+" compatible with "+this);
		if (data == null) return false;
		SlotType[] types = data.getCompatibleSlots();
		for (int i = 0; i < types.length; ++i) if (types[i] == getSlotType()) return true;
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public static String getTypeName(SlotType type) {
		switch (type) {
		case FRAME:
			return "dscombat.slotname_frame";
		case INTERNAL:
			return "dscombat.slotname_internal";
		case SEAT:
			return "dscombat.slotname_seat";
		case WING:
			return "dscombat.slotname_wing";
		case ADVANCED_INTERNAL:
			return "dscombat.slotname_advanced_internal";
		case TURRENT:
			return "dscombat.slotname_turrent";
		}
		return "";
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
		INTERNAL,
		ADVANCED_INTERNAL,
		TURRENT
	}
	
	public static final SlotType[] SEAT = new SlotType[] {SlotType.SEAT, SlotType.TURRENT};
	public static final SlotType[] TURRENT = new SlotType[] {SlotType.TURRENT};
	public static final SlotType[] INTERNAL = new SlotType[] {SlotType.INTERNAL, SlotType.ADVANCED_INTERNAL};
	public static final SlotType[] ADVANCED_INTERNAL = new SlotType[] {SlotType.ADVANCED_INTERNAL};
	public static final SlotType[] EXTERNAL = new SlotType[] {SlotType.WING, SlotType.FRAME};
	
	public static int getIconOffsetX(SlotType type) {
		return type.ordinal() * 16;
	}
	
	public int getIconOffsetX() {
		return offsetX;
	}
	
	@Override
	public String toString() {
		return "["+name+":"+getSlotType().toString()+":"+data+"]";
	}
	
}
