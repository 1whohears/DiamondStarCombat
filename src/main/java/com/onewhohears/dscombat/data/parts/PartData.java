package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public abstract class PartData {
	
	private final String id;
	private final String itemId;
	private EntityAbstractAircraft parent;
	
	public static enum PartType {
		SEAT,
		TURRENT
	}
	
	protected PartData(String id, String itemId) {
		this.id = id;
		this.itemId = itemId;
	}
	
	public PartData(CompoundTag tag) {
		id = tag.getString("id");
		itemId = tag.getString("itemId");
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("type", this.getType().ordinal());
		tag.putString("id", id);
		tag.putString("itemId", itemId);
		return tag;
	}
	
	public PartData(FriendlyByteBuf buffer) {
		// type int is read in DataSerializers
		id = buffer.readUtf();
		itemId = buffer.readUtf();
		//pos = DataSerializers.VEC3.read(buffer);
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.getType().ordinal());
		buffer.writeUtf(id);
		buffer.writeUtf(itemId);
		//DataSerializers.VEC3.write(buffer, pos);
	}
	
	public abstract PartType getType();
	
	public String getId() {
		return id;
	}
	
	public String getItemId() {
		return itemId;
	}
	
	public EntityAbstractAircraft getParent() {
		return parent;
	}
	
	public void setup(EntityAbstractAircraft craft, Vec3 pos) {
		System.out.println("setting up part "+getId()+" client side "+craft.level.isClientSide);
		parent = craft;
	}
	
	public void clientSetup(EntityAbstractAircraft craft, Vec3 pos) {
		System.out.println("setting up part "+getId()+" client side "+craft.level.isClientSide);
		parent = craft;
	}
	
	public void remove(EntityAbstractAircraft craft) {
		
	}
	
	public void clientRemove(EntityAbstractAircraft craft) {
		
	}
	
	@Override
	public String toString() {
		return "("+getType().name()+"@"+getId()+")";
	}
	
	public abstract float getWeight();
	
	public abstract SlotType[] getCompatibleSlots();
	
	public abstract ItemStack getItemStack();
	
}
