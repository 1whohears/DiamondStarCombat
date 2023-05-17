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
	
	public static final String PILOT_SLOT_NAME = "slotname.dscombat.pilot_seat";
	
	private final String slotId;
	private final SlotType type;
	private final Vec3 pos;
	private final int uix, uiy;
	private final float zRot;
	private PartData data;
	
	protected PartSlot(String name, SlotType type, Vec3 pos, int uix, int uiy, float zRot) {
		this.slotId = name;
		this.type = type;
		this.pos = pos;
		this.uix = uix;
		this.uiy = uiy;
		this.zRot = zRot;
	}
	
	public PartSlot(CompoundTag tag) {
		slotId = tag.getString("name");
		type = SlotType.values()[tag.getInt("slot_type")];
		pos = UtilParse.readVec3(tag, "slot_pos");
		uix = tag.getInt("uix");
		uiy = tag.getInt("uiy");
		zRot = tag.getFloat("zRot");
		if (tag.contains("data")) data = UtilParse.parsePartFromCompound(tag.getCompound("data"));
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
		SEAT("slottype.dscombat.seat"),
		WING("slottype.dscombat.wing"),
		FRAME("slottype.dscombat.frame"),
		INTERNAL("slottype.dscombat.internal"),
		ADVANCED_INTERNAL("slottype.dscombat.advanced_internal"),
		TURRET("slottype.dscombat.turret"),
		HEAVY_TURRET("slottype.dscombat.heavy_turret"),
		SPIN_ENGINE("slottype.dscombat.spin_engine"),
		PUSH_ENGINE("slottype.dscombat.push_engine"),
		HEAVY_FRAME("slottype.dscombat.heavy_frame");
		
		public static final SlotType[] SEAT_ALL = {SEAT, TURRET, HEAVY_TURRET};
		public static final SlotType[] TURRET_ALL = {TURRET, HEAVY_TURRET};
		public static final SlotType[] TURRET_HEAVY = {HEAVY_TURRET};
		
		public static final SlotType[] INTERNAL_ALL = {INTERNAL, ADVANCED_INTERNAL, PUSH_ENGINE, SPIN_ENGINE};
		public static final SlotType[] INTERNAL_ADVANCED = {ADVANCED_INTERNAL};
		public static final SlotType[] INTERNAL_ENGINE_SPIN = {SPIN_ENGINE};
		public static final SlotType[] INTERNAL_ENGINE_PUSH = {PUSH_ENGINE};
		
		public static final SlotType[] EXTERNAL_ALL = {WING, FRAME, HEAVY_FRAME};
		public static final SlotType[] EXTERNAL_HEAVY = {HEAVY_FRAME};
		
		private final String translatable;
		
		private SlotType(String translatable) {
			this.translatable = translatable;
		}
		
		public int getIconXOffset() {
			return ordinal() * 16;
		}
		
		public String getTranslatableName() {
			return translatable;
		}
		
		public String getLiteralName() {
			return name();
		}
	}
	
	@Override
	public String toString() {
		return "["+slotId+":"+getSlotType().toString()+":"+data.toString()+"]";
	}
	
	public float getZRot() {
		return zRot;
	}
	
	public boolean isPilotSlot() {
		return getName().equals(PILOT_SLOT_NAME) || getName().equals("dscombat.pilot_seat");
	}
	
}
