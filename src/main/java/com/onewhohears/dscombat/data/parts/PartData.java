package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public abstract class PartData {
	
	private EntityAbstractAircraft parent;
	
	public static enum PartType {
		SEAT,
		TURRENT
	}
	
	protected PartData() {
	}
	
	public PartData(CompoundTag tag) {
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("type", this.getType().ordinal());
		return tag;
	}
	
	public PartData(FriendlyByteBuf buffer) {
		// type int is read in DataSerializers
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.getType().ordinal());
	}
	
	public abstract PartType getType();
	
	/*public String getId() {
		return id;
	}*/
	
	public EntityAbstractAircraft getParent() {
		return parent;
	}
	
	public void setup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		System.out.println("setting up part "+this+" client side "+craft.level.isClientSide);
		parent = craft;
	}
	
	public void clientSetup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		System.out.println("setting up part "+this+" client side "+craft.level.isClientSide);
		parent = craft;
	}
	
	public abstract boolean isSetup(String slotId);
	
	public void remove(String slotId) {
		
	}
	
	public void clientRemove(String slotId) {
		
	}
	
	@Override
	public String toString() {
		return "("+getType().name()+")";
	}
	
	public abstract float getWeight();
	
	public abstract SlotType[] getCompatibleSlots();
	
	public abstract ItemStack getItemStack();
	
}
