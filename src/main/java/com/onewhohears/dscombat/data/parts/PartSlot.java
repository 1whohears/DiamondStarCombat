package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddPart;
import com.onewhohears.dscombat.common.network.toclient.ToClientRemovePart;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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
	private PartData data;
	
	public PartSlot(CompoundTag entityNbt, @Nullable CompoundTag presetNbt) {
		slotId = entityNbt.getString("name");
		locked = entityNbt.getBoolean("locked");
		if (entityNbt.contains("data")) data = UtilParse.parsePartFromCompound(entityNbt.getCompound("data"));
		if (presetNbt == null) presetNbt = entityNbt;
		if (presetNbt.getTagType("slot_type") == CompoundTag.TAG_INT) {
			type = SlotType.getByOldOrdinal(presetNbt.getInt("slot_type"));
		} else type = SlotType.getByName(presetNbt.getString("slot_type"));
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
		if (filled()) tag.put("data", data.write());
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
		if (filled()) data.write(buffer);
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
	public PartData getPartData() {
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
	
	public boolean addPartData(PartData data, EntityVehicle plane) {
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
		if (data != null) return data.isSeat();
		return false;
	}
	
	public boolean isNormalSeat() {
		if (data != null) return data.getType() == PartType.SEAT;
		return false;
	}
	
	public boolean isCompatible(PartData data) {
		//System.out.println("is "+data+" compatible with "+this);
		if (data == null) return false;
		// HOW 3 check for duplicates
		SlotType[] types = data.getCompatibleSlots();
		for (int i = 0; i < types.length; ++i) if (types[i] == getSlotType()) return true;
		return false;
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
	
	public static enum SlotType {
		SEAT("seat"),
		LIGHT_TURRET("light_turret"),
		MED_TURRET("med_turret"),
		HEAVY_TURRET("heavy_turret"),
		
		WING("wing"),
		FRAME("frame"),
		HEAVY_FRAME("heavy_frame"),
		ADVANCED_FRAME("advanced_frame"),
		
		INTERNAL("internal"),
		ADVANCED_INTERNAL("advanced_internal"),
		
		SPIN_ENGINE("spin_engine"),
		PUSH_ENGINE("push_engine"),
		RADIAL_ENGINE("radial_engine");
		
		public static final SlotType[] SEAT_ALL = {SEAT, LIGHT_TURRET, MED_TURRET, HEAVY_TURRET};
		public static final SlotType[] TURRET_LIGHT = {LIGHT_TURRET, MED_TURRET, HEAVY_TURRET};
		public static final SlotType[] TURRET_MED = {MED_TURRET, HEAVY_TURRET};
		public static final SlotType[] TURRET_HEAVY = {HEAVY_TURRET};
		
		public static final SlotType[] INTERNAL_ALL = {INTERNAL, ADVANCED_INTERNAL, PUSH_ENGINE, SPIN_ENGINE};
		public static final SlotType[] INTERNAL_ADVANCED = {ADVANCED_INTERNAL};
		public static final SlotType[] INTERNAL_ENGINE_SPIN = {SPIN_ENGINE};
		public static final SlotType[] INTERNAL_ENGINE_PUSH = {PUSH_ENGINE};
		public static final SlotType[] INTERNAL_ENGINE_RADIAL = {RADIAL_ENGINE};
		
		public static final SlotType[] EXTERNAL_ALL = {WING, FRAME, HEAVY_FRAME, ADVANCED_FRAME};
		public static final SlotType[] EXTERNAL_HEAVY = {HEAVY_FRAME, HEAVY_TURRET};
		public static final SlotType[] EXTERNAL_ADVANCED = {ADVANCED_FRAME};
		
		@Nullable
		public static SlotType getByName(String name) {
			for (int i = 0; i < values().length; ++i)
				if (values()[i].getSlotTypeName().equals(name)) 
					return values()[i];
			return null;
		}
		
		@Nullable
		public static SlotType getByOldOrdinal(int o) {
			switch (o) {
			case 0: return SEAT;
			case 1: return WING;
			case 2: return FRAME;
			case 3: return INTERNAL;
			case 4: return ADVANCED_INTERNAL;
			case 5: return MED_TURRET;
			case 6: return HEAVY_TURRET;
			case 7: return SPIN_ENGINE;
			case 8: return PUSH_ENGINE;
			case 9: return HEAVY_FRAME;
			case 10: return RADIAL_ENGINE;
			}
			return null;
		}
		
		private final String slotTypeName;
		private final ResourceLocation bg_texture;
		
		private SlotType(String slotTypeName) {
			this.slotTypeName = slotTypeName;
			this.bg_texture = new ResourceLocation("dscombat:textures/ui/slots/"+slotTypeName+".png");
		}
		
		public String getSlotTypeName() {
			return slotTypeName;
		}
		
		@Override
		public String toString() {
			return getSlotTypeName();
		}
		
		public ResourceLocation getBgTexture() {
			return bg_texture;
		}
		
		public String getTranslatableName() {
			return "slottype.dscombat."+slotTypeName;
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
