package com.onewhohears.dscombat.data.parts;

import java.util.NoSuchElementException;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class PartData {
	
	public Vec3 relPos = Vec3.ZERO;
	
	private final SlotType[] compatibleSlots;
	private final ResourceLocation itemid;
	private final float weight;
	private EntityAircraft parent;
	
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
		BUFF_DATA
	}
	
	protected PartData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots) {
		this.weight = weight;
		this.itemid = itemid;
		this.compatibleSlots = compatibleSlots;
	}
	
	public PartData(CompoundTag tag) {
		weight = tag.getFloat("weight");
		itemid = new ResourceLocation(tag.getString("itemid"));
		int[] cs = tag.getIntArray("compatibleSlots");
		compatibleSlots = new SlotType[cs.length];
		for (int i = 0; i < cs.length; ++i) compatibleSlots[i] = SlotType.values()[cs[i]];
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("type", this.getType().ordinal());
		tag.putFloat("weight", weight);
		tag.putString("itemid", itemid.toString());
		int[] cs = new int[compatibleSlots.length];
		for (int i = 0; i < compatibleSlots.length; ++i) cs[i] = compatibleSlots[i].ordinal();
		tag.putIntArray("compatibleSlots", cs);
		return tag;
	}
	
	public PartData(FriendlyByteBuf buffer) {
		// type int is read in DataSerializers
		weight = buffer.readFloat();
		itemid = new ResourceLocation(buffer.readUtf());
		int[] cs = buffer.readVarIntArray();
		compatibleSlots = new SlotType[cs.length];
		for (int i = 0; i < cs.length; ++i) compatibleSlots[i] = SlotType.values()[cs[i]];
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.getType().ordinal());
		buffer.writeFloat(weight);
		buffer.writeUtf(itemid.toString());
		int[] cs = new int[compatibleSlots.length];
		for (int i = 0; i < compatibleSlots.length; ++i) cs[i] = compatibleSlots[i].ordinal();
		buffer.writeVarIntArray(cs);
	}
	
	public abstract PartType getType();
	
	public EntityAircraft getParent() {
		return parent;
	}
	
	public Vec3 getRelPos() {
		return relPos;
	}
	
	protected void setParent(EntityAircraft parent) {
		this.parent = parent;
	}
	
	protected void setRelPos(Vec3 pos) {
		this.relPos = pos;
	}
	
	public void setup(EntityAircraft craft, String slotId, Vec3 pos) {
		//System.out.println("setting up part "+this+" client side "+craft.level.isClientSide+" slot "+slotId);
		setParent(craft);
		setRelPos(pos);
	}
	
	public void clientSetup(EntityAircraft craft, String slotId, Vec3 pos) {
		//System.out.println("setting up part "+this+" client side "+craft.level.isClientSide+" slot "+slotId);
		parent = craft;
		relPos = pos;
	}
	
	public abstract boolean isSetup(String slotId, EntityAircraft craft);
	
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
	
	public SlotType[] getCompatibleSlots() {
		return compatibleSlots;
	}
	
	private ItemStack stack;
	
	public ItemStack getNewItemStack() {
		System.out.println("GETTING ITEM STACK "+itemid);
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
	
}
