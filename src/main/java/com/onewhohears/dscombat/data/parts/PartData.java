package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class PartData {
	
	public Vec3 relPos = Vec3.ZERO;
	
	private final SlotType[] compatibleSlots;
	private final ResourceLocation itemid;
	private final float weight;
	private EntityAbstractAircraft parent;
	
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
	
	protected PartData(float weight, String itemid, SlotType[] compatibleSlots) {
		this(weight, new ResourceLocation(DSCombatMod.MODID, itemid), compatibleSlots);
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
	
	public EntityAbstractAircraft getParent() {
		return parent;
	}
	
	public void setup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		//System.out.println("setting up part "+this+" client side "+craft.level.isClientSide+" slot "+slotId);
		parent = craft;
		relPos = pos;
	}
	
	public void clientSetup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		//System.out.println("setting up part "+this+" client side "+craft.level.isClientSide+" slot "+slotId);
		parent = craft;
		relPos = pos;
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
	
	public SlotType[] getCompatibleSlots() {
		return compatibleSlots;
	}
	
	public ItemStack getItemStack() {
		System.out.println("GETTING ITEM STACK "+itemid);
		ItemStack stack = new ItemStack(
				ForgeRegistries.ITEMS.getDelegate(itemid)
					.get()
						.get());
		stack.setTag(this.write());
		return stack;
	}
	
}
