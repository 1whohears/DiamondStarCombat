package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public abstract class PartData {
	
	private final float weight;
	private EntityAbstractAircraft parent;
	
	public static enum PartType {
		SEAT,
		TURRENT,
		INTERNAL_WEAPON,
		WEAPON_RACK
	}
	
	protected PartData(float weight) {
		this.weight = weight;
	}
	
	public PartData(CompoundTag tag) {
		weight = tag.getFloat("weight");
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("type", this.getType().ordinal());
		tag.putFloat("weight", weight);
		return tag;
	}
	
	public PartData(FriendlyByteBuf buffer) {
		// type int is read in DataSerializers
		weight = buffer.readFloat();
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.getType().ordinal());
		buffer.writeFloat(weight);
	}
	
	public abstract PartType getType();
	
	public EntityAbstractAircraft getParent() {
		return parent;
	}
	
	public void setup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		System.out.println("setting up part "+this+" client side "+craft.level.isClientSide+" slot "+slotId);
		parent = craft;
	}
	
	public void clientSetup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		System.out.println("setting up part "+this+" client side "+craft.level.isClientSide+" slot "+slotId);
		parent = craft;
	}
	
	public abstract boolean isSetup(String slotId, EntityAbstractAircraft craft);
	
	public void remove(String slotId) {
		
	}
	
	public void clientRemove(String slotId) {
		
	}
	
	protected void tick(String slotId) {
		
	}
	
	protected void clientTick(String slotId) {
		
	}
	
	@Override
	public String toString() {
		return "("+getType().name()+")";
	}
	
	public float getWeight() {
		return weight;
	}
	
	public abstract SlotType[] getCompatibleSlots();
	
	public abstract ItemStack getItemStack();
	
}
