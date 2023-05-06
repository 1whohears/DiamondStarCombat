package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddPart;
import com.onewhohears.dscombat.common.network.toclient.ToClientRemovePart;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class PartSlot {
	
	public static final String PILOT_SLOT_NAME = "dscombat.pilot_seat";
	
	private final String slotId, typeName;
	private final SlotType type;
	private final Vec3 pos;
	private final int uix, uiy, offsetX;
	private final float zRot;
	private PartData data;
	
	protected PartSlot(String name, SlotType type, Vec3 pos, int uix, int uiy, float zRot) {
		this.slotId = name;
		this.type = type;
		this.pos = pos;
		this.uix = uix;
		this.uiy = uiy;
		this.zRot = zRot;
		this.offsetX = getIconOffsetX(type);
		this.typeName = getTypeName(type);
	}
	
	public PartSlot(CompoundTag tag) {
		slotId = tag.getString("name");
		type = SlotType.values()[tag.getInt("slot_type")];
		pos = UtilParse.readVec3(tag, "slot_pos");
		uix = tag.getInt("uix");
		uiy = tag.getInt("uiy");
		zRot = tag.getFloat("zRot");
		if (tag.contains("data")) data = UtilParse.parsePartFromCompound(tag.getCompound("data"));
		// not saved
		offsetX = getIconOffsetX(type);
		typeName = getTypeName(type);
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putString("name", slotId);
		tag.putInt("slot_type",type.ordinal());
		UtilParse.writeVec3(tag, pos, "slot_pos");
		tag.putInt("uix", uix);
		tag.putInt("uiy", uiy);
		tag.putFloat("zRot", zRot);
		if (filled()) tag.put("data", data.write());
		return tag;
	}
	
	public PartSlot(FriendlyByteBuf buffer) {
		//System.out.println("PART SLOT BUFFER");
		slotId = buffer.readUtf();
		//System.out.println("name = "+name);
		type = SlotType.values()[buffer.readInt()];
		//System.out.println("type = "+type.name());
		pos = DataSerializers.VEC3.read(buffer);
		//System.out.println("pos = "+pos);
		uix = buffer.readInt();
		//System.out.println("uix = "+uix);
		uiy = buffer.readInt();
		//System.out.println("uiy = "+uiy);
		zRot = buffer.readFloat();
		boolean notNull = buffer.readBoolean();
		//System.out.println("notNull = "+notNull);
		if (notNull) data = DataSerializers.PART_DATA.read(buffer);
		// not in packet
		offsetX = getIconOffsetX(type);
		typeName = getTypeName(type);
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(slotId);
		buffer.writeInt(type.ordinal());
		DataSerializers.VEC3.write(buffer, pos);
		buffer.writeInt(uix);
		buffer.writeInt(uiy);
		buffer.writeFloat(zRot);
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
	
	public void serverSetup(EntityAircraft plane) {
		if (filled()) {
			data.setup(plane, slotId, pos);
			data.serverSetup(plane, slotId, pos);
		}
	}
	
	public void clientSetup(EntityAircraft plane) {
		if (filled()) {
			data.setup(plane, slotId, pos);
			data.clientSetup(plane, slotId, pos);
		}
	}
	
	protected void tick() {
		if (filled()) data.tick(slotId);
	}
	
	protected void clientTick() {
		if (filled()) data.clientTick(slotId);
	}
	
	public boolean addPartData(PartData data, EntityAircraft plane) {
		if (filled()) return false;
		if (!isCompatible(data)) return false;
		this.data = data;
		if (plane == null) return true;
		data.setup(plane, slotId, pos);
		if (plane.level.isClientSide) data.clientSetup(plane, slotId, pos);
		else {
			data.serverSetup(plane, slotId, pos);
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> plane), 
					new ToClientAddPart(plane.getId(), slotId, data));
		}
		return true;
	}
	
	public boolean removePartData(EntityAircraft plane) {
		if (filled()) {
			data.remove(slotId);
			if (plane.level.isClientSide) data.clientRemove(slotId);
			else {
				data.serverRemove(slotId);
				PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> plane), 
						new ToClientRemovePart(plane.getId(), slotId));
			}
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
		return slotId;
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
		case TURRET:
			return "dscombat.slotname_turret";
		case HEAVY_TURRET:
			return "dscombat.slotname_turret_heavy";
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
		TURRET,
		HEAVY_TURRET
	}
	
	public static final SlotType[] SEAT_ALL = new SlotType[] {SlotType.SEAT, SlotType.TURRET, SlotType.HEAVY_TURRET};
	public static final SlotType[] TURRET_ALL = new SlotType[] {SlotType.TURRET, SlotType.HEAVY_TURRET};
	public static final SlotType[] TURRET_HEAVY = new SlotType[] {SlotType.HEAVY_TURRET};
	public static final SlotType[] INTERNAL_ALL = new SlotType[] {SlotType.INTERNAL, SlotType.ADVANCED_INTERNAL};
	public static final SlotType[] ADVANCED_INTERNAL = new SlotType[] {SlotType.ADVANCED_INTERNAL};
	public static final SlotType[] EXTERNAL_ALL = new SlotType[] {SlotType.WING, SlotType.FRAME};
	
	public static int getIconOffsetX(SlotType type) {
		return type.ordinal() * 16;
	}
	
	public int getIconOffsetX() {
		return offsetX;
	}
	
	@Override
	public String toString() {
		return "["+slotId+":"+getSlotType().toString()+":"+data+"]";
	}
	
	public float getZRot() {
		return zRot;
	}
	
	public boolean isPilotSlot() {
		return getName().equals(PILOT_SLOT_NAME);
	}
	
}
