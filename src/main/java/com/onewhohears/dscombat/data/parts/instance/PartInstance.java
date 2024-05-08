package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public abstract class PartInstance<T extends PartStats> extends JsonPresetInstance<T> {
	
	public static final int PARSE_VERSION = 3;
	
	private Vec3 relPos = Vec3.ZERO;
	private EntityVehicle parent;
	
	public PartInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
	}
	
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.putBoolean("readnbt", true);
		tag.putInt("parse_version", PARSE_VERSION);
		return tag;
	}
	
	public void readBuffer(FriendlyByteBuf buffer) {
		// preset id is read in data serializer
	}
	
	public void writeBuffer(FriendlyByteBuf buffer) {
		buffer.writeUtf(getStats().getId());
	}
	
	public int getFlares() {
		return 0;
	}
	
	public float getWeight() {
		return getStats().getWeight();
	}
	
	public EntityVehicle getParent() {
		return parent;
	}
	
	public Vec3 getRelPos() {
		return relPos;
	}
	
	protected void setParent(EntityVehicle parent) {
		this.parent = parent;
	}
	
	protected void setRelPos(Vec3 pos) {
		this.relPos = pos;
	}
	
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		if (getStats().hasExternalEntity() && !isEntitySetup(slotId, craft)) {
			EntityPart part = (EntityPart) getStats().getExernalEntityType().create(craft.level);
			setUpPartEntity(part, craft, slotId, pos, getStats().getExternalEntityDefaultHealth());
			craft.level.addFreshEntity(part);
		}
	}
	
	public void clientSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		
	}
	
	public void setup(EntityVehicle craft, String slotId, Vec3 pos) {
		//System.out.println("setting up part "+this+" client side "+craft.level.isClientSide+" slot "+slotId);
		setParent(craft);
		setRelPos(pos);
	}
	
	public abstract boolean isSetup(String slotId, EntityVehicle craft);
	
	public void serverRemove(String slotId) {
		if (getStats().hasExternalEntity()) {
			removeEntity(slotId);
		}
	}
	
	public void clientRemove(String slotId) {
		
	}
	
	public void remove(String slotId) {
		
	}
	
	public void tick(String slotId) {
		
	}
	
	public void clientTick(String slotId) {
		
	}
	
	public ItemStack getNewItemStack() {
		ItemStack s = getStats().getItem().getDefaultInstance();
		s.setTag(writeNBT());
		return s;
	}
	
	public boolean isEntitySetup(String slotId, EntityVehicle craft) {
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getPartType().is(getStats().getType()) && part.getSlotId().equals(slotId)) 
				return true;
		return false;
	}
	
	public void removeEntity(String slotId) {
		if (getParent() == null) return;
		for (EntityPart part : getParent().getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				part.discard();
	}
	
	public void setUpPartEntity(EntityPart part, EntityVehicle craft, String slotId, Vec3 pos, float health) {
		part.setSlotId(slotId);
		part.setRelativePos(pos);
		part.setPos(craft.position());
		part.setHealth(health);
		part.startRiding(craft);
	}

}
