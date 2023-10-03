package com.onewhohears.dscombat.data.parts;

import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityVehiclePart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class PartData {
	
	private final SlotType[] compatibleSlots;
	private final ResourceLocation itemid;
	private final float weight;
	private final String modelId;
	private final EntityDimensions size;
	private EntityAircraft parent;
	private String slotId;
	private Vec3 relPos = Vec3.ZERO;
	private float zRot = 0f;
	
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
		EXTERNAL_RADAR;
		
		public boolean isSeat() {
			return this == SEAT || this == TURRENT;
		}
		
		public boolean isTurret() {
			return this == TURRENT;
		}
	}
	
	protected PartData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, 
			String modelId, EntityDimensions size) {
		this.weight = weight;
		this.itemid = itemid;
		this.compatibleSlots = compatibleSlots;
		this.modelId = modelId;
		if (size == null) size = EntityDimensions.scalable(0.1f, 0.1f);
		this.size = size;
	}
	
	protected PartData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots) {
		this(weight, itemid, compatibleSlots, "", EntityDimensions.scalable(0.1f, 0.1f));
	}
	
	public PartData(CompoundTag tag) {
		weight = tag.getFloat("weight");
		itemid = new ResourceLocation(tag.getString("itemid"));
		int[] cs = tag.getIntArray("compatibleSlots");
		compatibleSlots = new SlotType[cs.length];
		for (int i = 0; i < cs.length; ++i) compatibleSlots[i] = SlotType.values()[cs[i]];
		modelId = tag.getString("modelId");
		float w = tag.getFloat("entity_width");
		float h = tag.getFloat("entity_height");
		size = EntityDimensions.scalable(w, h);
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("type", this.getType().ordinal());
		tag.putFloat("weight", weight);
		tag.putString("itemid", itemid.toString());
		int[] cs = new int[compatibleSlots.length];
		for (int i = 0; i < compatibleSlots.length; ++i) cs[i] = compatibleSlots[i].ordinal();
		tag.putIntArray("compatibleSlots", cs);
		tag.putString("modelId", modelId);
		tag.putFloat("entity_width", size.width);
		tag.putFloat("entity_height", size.width);
		return tag;
	}
	
	public PartData(FriendlyByteBuf buffer) {
		// type int is read in DataSerializers
		weight = buffer.readFloat();
		itemid = new ResourceLocation(buffer.readUtf());
		modelId = buffer.readUtf();
		float w = buffer.readFloat();
		float h = buffer.readFloat();
		size = EntityDimensions.scalable(w, h);
		int[] cs = buffer.readVarIntArray();
		compatibleSlots = new SlotType[cs.length];
		for (int i = 0; i < cs.length; ++i) compatibleSlots[i] = SlotType.values()[cs[i]];
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.getType().ordinal());
		buffer.writeFloat(weight);
		buffer.writeUtf(itemid.toString());
		buffer.writeUtf(modelId);
		buffer.writeFloat(size.width);
		buffer.writeFloat(size.height);
		int[] cs = new int[compatibleSlots.length];
		for (int i = 0; i < compatibleSlots.length; ++i) cs[i] = compatibleSlots[i].ordinal();
		buffer.writeVarIntArray(cs);
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
	
	public boolean isRadio() {
		return false;
	}
	
	public float getAdditionalArmor() {
		return 0;
	}
	
	public String getModelId() {
		return modelId;
	}
	
	public EntityDimensions getExternalEntitySize() {
		return size;
	}
	
	public EntityAircraft getParent() {
		return parent;
	}
	
	public Vec3 getRelPos() {
		return relPos;
	}
	
	public String getSlotId() {
		return slotId;
	}
	
	public float getZRot() {
		return zRot;
	}
	
	protected void setParent(EntityAircraft parent) {
		this.parent = parent;
	}
	
	protected void setRelPos(Vec3 pos) {
		this.relPos = pos;
	}
	
	protected void setSlotId(String slotId) {
		this.slotId = slotId;
	}
	
	protected void setZRot(float zRot) {
		this.zRot = zRot;
	}
	
	public void setup(EntityAircraft craft, String slotId, Vec3 pos, float zRot) {
		//System.out.println("setting up part "+this+" client side "+craft.level.isClientSide+" slot "+slotId);
		setParent(craft);
		setRelPos(pos);
		setSlotId(slotId);
		setZRot(zRot);
		setupPartEntity();
	}
	
	public void serverSetup() {
	}
	
	public void clientSetup() {
	}
	
	public abstract boolean hasExternalPartEntity();
	
	@Nullable
	public abstract EntityVehiclePart getPartEntity();
	
	public void setupPartEntity() {
		if (!hasExternalPartEntity()) return;
		EntityVehiclePart part = getPartEntity();
		if (part == null) return;
		getParent().addPartEntity(part);
	}
	
	public void removePartEntity() {
		if (!hasExternalPartEntity()) return;
		getParent().removePartEntity(getSlotId());
	}
	
	public void remove() {
		removePartEntity();
	}
	
	public void serverRemove() {
	}
	
	public void clientRemove() {
	}
	
	protected void tick() {
	}
	
	protected void clientTick() {
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
	
}
