package com.onewhohears.dscombat.data.parts;

import java.util.NoSuchElementException;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityPart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class PartData {
	
	public static final int PARSE_VERSION = 2;
	
	public Vec3 relPos = Vec3.ZERO;
	
	private final SlotType[] compatibleSlots;
	private final ResourceLocation itemid;
	private final float weight;
	private EntityVehicle parent;
	
	public static enum PartType {
		SEAT,
		TURRENT,
		INTERNAL_WEAPON,
		WEAPON_RACK,
		ENGINE,
		FUEL_TANK,
		INTERNAL_RADAR,
		FLARE_DISPENSER,
		EXTERNAL_ENGINE,
		BUFF_DATA,
		EXTERNAL_RADAR,
		GIMBAL,
		STORAGE_BOX;
		
		public boolean isSeat() {
			return this == SEAT || this == TURRENT;
		}
	}
	
	protected PartData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots) {
		this.weight = weight;
		this.itemid = itemid;
		this.compatibleSlots = compatibleSlots;
	}
	
	public void read(CompoundTag tag) {
		
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putString("itemid", itemid.toString());
		tag.putBoolean("readnbt", true);
		tag.putInt("parse_version", PARSE_VERSION);
		return tag;
	}
	
	public void read(FriendlyByteBuf buffer) {
		// item id is read in data serializer
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(itemid.toString());
	}
	
	public abstract PartType getType();
	
	public boolean isEngine() {
		return getType() == PartType.ENGINE || getType() == PartType.EXTERNAL_ENGINE;
	}
	
	public boolean isFuelTank() {
		return getType() == PartType.FUEL_TANK;
	}
	
	public boolean isFlareDispenser() {
		return getType() == PartType.FLARE_DISPENSER;
	}
	
	public boolean isSeat() {
		return getType().isSeat();
	}
	
	public boolean isGimbal() {
		return getType() == PartType.GIMBAL;
	}
	
	public boolean isRadio() {
		return false;
	}
	
	public float getAdditionalArmor() {
		return 0;
	}
	
	public int getFlares() {
		return 0;
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
		
	}
	
	public void clientRemove(String slotId) {
		
	}
	
	public void remove(String slotId) {
		
	}
	
	protected void tick(String slotId) {
		
	}
	
	protected void clientTick(String slotId) {
		
	}
	
	@Override
	public String toString() {
		return "("+getType().name()+","+itemid.toString()+")";
	}
	
	public float getWeight() {
		return weight;
	}
	
	public SlotType[] getCompatibleSlots() {
		return compatibleSlots;
	}
	
	private ItemStack stack;
	
	public ItemStack getNewItemStack() {
		//System.out.println("GETTING ITEM STACK "+itemid);
		if (stack == null) {
			try {
				Item i = ForgeRegistries.ITEMS.getDelegate(itemid).get().get();
				stack = new ItemStack(i);
			} catch(NoSuchElementException e) {
				stack = ItemStack.EMPTY;
			}
		}
		ItemStack s = stack.copy();
		s.setTag(write());
		return s;
	}
	
	public boolean isEntitySetup(String slotId, EntityVehicle craft) {
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getPartType() == getType() && part.getSlotId().equals(slotId)) 
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
