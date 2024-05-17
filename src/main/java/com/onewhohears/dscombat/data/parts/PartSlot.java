package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddPart;
import com.onewhohears.dscombat.common.network.toclient.ToClientRemovePart;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class PartSlot {
	
	public static final String PILOT_SLOT_NAME = "pilot_seat";
	public static final String COPILOT_SLOT_NAME = "copilot_seat";
	
	private final String slotId;
	private final SlotType type;
	private final Vec3 pos;
	private final float zRot;
	private final boolean locked;
	private PartInstance<?> data;
	
	public PartSlot(CompoundTag entityNbt, @Nullable CompoundTag presetNbt) {
		slotId = entityNbt.getString("name");
		locked = entityNbt.getBoolean("locked");
		if (entityNbt.contains("data")) data = UtilParse.parsePartFromCompound(entityNbt.getCompound("data"));
		if (presetNbt == null) presetNbt = entityNbt;
		type = SlotType.getByName(presetNbt.getString("slot_type"));
		pos = UtilParse.readVec3(presetNbt, "slot_pos");
		zRot = presetNbt.getFloat("zRot");
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putString("name", slotId);
		tag.putString("slot_type",type.getSlotTypeName());
		UtilParse.writeVec3(tag, pos, "slot_pos");
		tag.putFloat("zRot", zRot);
		tag.putBoolean("locked", locked);
		if (filled()) tag.put("data", data.writeNBT());
		return tag;
	}
	
	public PartSlot(FriendlyByteBuf buffer) {
		slotId = buffer.readUtf();
		type = SlotType.getByName(buffer.readUtf());
		pos = DataSerializers.VEC3.read(buffer);
		zRot = buffer.readFloat();
		locked = buffer.readBoolean();
		boolean notNull = buffer.readBoolean();
		if (notNull) data = DataSerializers.PART_DATA.read(buffer);
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(slotId);
		buffer.writeUtf(type.getSlotTypeName());
		DataSerializers.VEC3.write(buffer, pos);
		buffer.writeFloat(zRot);
		buffer.writeBoolean(locked);
		buffer.writeBoolean(filled());
		if (filled()) DataSerializers.PART_DATA.write(buffer, data);
	}
	
	public Vec3 getRelPos() {
		return pos;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public boolean filled() {
		return data != null;
	}
	
	@Nullable
	public PartInstance<?> getPartData() {
		return data;
	}
	
	public void serverSetup(EntityVehicle plane) {
		if (filled()) {
			data.setup(plane, slotId, pos);
			data.serverSetup(plane, slotId, pos);
		}
	}
	
	public void clientSetup(EntityVehicle plane) {
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
	
	public boolean addPartData(PartInstance<?> data, EntityVehicle plane) {
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
	
	public boolean removePartData(EntityVehicle plane) {
		if (!filled()) return false;
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
	
	public boolean isSeat() {
		if (data != null) return data.getStats().isSeat();
		return false;
	}
	
	public boolean isNormalSeat() {
		if (data != null) return data.getStats().getType().is(PartType.SEAT);
		return false;
	}
	
	public boolean isCompatible(PartInstance<?> data) {
		//System.out.println("is "+data+" compatible with "+this);
		if (data == null) return false;
		// HOW 3 check for duplicates
		return data.isCompatible(getSlotType());
	}
	
	public static String getSlotId(String slotId) {
		if (slotId.startsWith("slotname.dscombat.")) return slotId.substring("slotname.dscombat.".length(), slotId.length());
		if (slotId.startsWith("dscombat.")) return slotId.substring("dscombat.".length(), slotId.length());
		return slotId;
	}
	
	public String getSlotId() {
		return getSlotId(slotId);
	}
	
	public String getTranslatableName() {
		if (slotId.startsWith("slotname.dscombat.")) return slotId;
		if (slotId.startsWith("dscombat.")) return "slotname."+slotId;
		return "slotname.dscombat."+slotId;
	}
	
	public SlotType getSlotType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "["+slotId+":"+getSlotType().toString()+":"+data+"]";
	}
	
	public float getZRot() {
		return zRot;
	}
	
	public boolean isPilotSlot() {
		return isPilotSeat(getSlotId());
	}
	
	public boolean isCoPilotSlot() {
		return isCoPilotSeat(getSlotId());
	}
	
	public static boolean isPilotSeat(String slotId) {
		return slotId.equals(PILOT_SLOT_NAME) || slotId.equals("dscombat.pilot_seat") || slotId.equals("slotname.dscombat.pilot_seat");
	}
	
	public static boolean isCoPilotSeat(String slotId) {
		return slotId.equals(COPILOT_SLOT_NAME);
	}
	
}
