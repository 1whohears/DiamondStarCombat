package com.onewhohears.dscombat.data.parts.instance;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.crafting.IngredientStack;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec3;

public abstract class PartInstance<T extends PartStats> extends JsonPresetInstance<T> {
	
	public static final int PARSE_VERSION = 3;
	
	private Vec3 relPos = Vec3.ZERO;
	private EntityVehicle parent;
	private boolean damaged;
	
	public PartInstance(T stats) {
		super(stats);
	}
	
	public void setFilled(String param) {
	}
	
	public void setParamNotFilled(String param) {
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
		damaged = tag.getBoolean("damaged");
	}
	
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.putBoolean("readnbt", true);
		tag.putInt("parse_version", PARSE_VERSION);
		tag.putString("part", getStatsId());
		tag.putBoolean("damaged", damaged);
		return tag;
	}
	
	public void readBuffer(FriendlyByteBuf buffer) {
		// preset id is read in data serializer
		damaged = buffer.readBoolean();
	}
	
	public void writeBuffer(FriendlyByteBuf buffer) {
		buffer.writeUtf(getStats().getId());
		buffer.writeBoolean(damaged);
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
	
	protected void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		if (hasExternalEntity() && !isDamaged()) addEntity(craft, slotId, pos);
	}
	
	protected void clientSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		
	}
	
	public boolean canSetup() {
		return !(isDamagedPreventsSetup() && isDamaged());
	}
	
	public boolean isDamagedPreventsSetup() {
		return true;
	}
	
	public void setup(EntityVehicle craft, String slotId, Vec3 pos) {
		//System.out.println("setting up part "+this+" client side "+craft.level.isClientSide+" slot "+slotId);
		setParent(craft);
		setRelPos(pos);
		if (craft.level.isClientSide) clientSetup(craft, slotId, pos);
		else serverSetup(craft, slotId, pos);
	}
	
	protected void serverRemove(String slotId) {
		if (hasExternalEntity()) removeEntity(slotId);
	}
	
	protected void clientRemove(String slotId) {
		
	}
	
	public void remove(EntityVehicle parent, String slotId) {
		if (parent.level.isClientSide) clientRemove(slotId);
		else serverRemove(slotId);
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
	
	public void addEntity(EntityVehicle craft, String slotId, Vec3 pos) {
		if (isEntitySetup(slotId, craft)) return;
		EntityPart part = createEntity(craft, slotId);
		if (part == null) return;
		setUpPartEntity(part, craft, slotId, pos, getStats().getExternalEntityDefaultHealth());
		craft.level.addFreshEntity(part);
	}
	
	@Nullable
	protected EntityPart createEntity(EntityVehicle vehicle, String slotId) {
		return (EntityPart) getStats().getExernalEntityType().create(vehicle.level);
	}
	
	public void setUpPartEntity(EntityPart part, EntityVehicle craft, String slotId, Vec3 pos, float health) {
		part.setSlotId(slotId);
		part.setRelativePos(pos);
		part.setPos(craft.position());
		part.setHealth(health);
		part.startRiding(craft);
	}
	
	public boolean isCompatible(SlotType type) {
		return getStats().isCompatible(type);
	}
	
	public boolean isDamaged() {
		return damaged;
	}
	
	public void setDamaged(boolean damaged) {
		this.damaged = damaged;
	}
	
	public void onDamaged(EntityVehicle parent, String slotId) {
		setDamaged(true);
		remove(parent, slotId);
	}
	
	public void onRepaired(EntityVehicle parent, String slotId, Vec3 pos) {
		setDamaged(false);
		if (canSetup()) setup(parent, slotId, pos);
	}
	
	public void addToolTips(List<Component> tips, TooltipFlag isAdvanced) {
		if (isDamaged()) {
			tips.add(UtilMCText.translatable("info.dscombat.damaged").setStyle(Style.EMPTY.withColor(0xCC0000)));
			if (getStats().getRepairCost().size() > 0) {
				Style repairStyle = Style.EMPTY.withColor(0xE88888);
				MutableComponent repairCost = UtilMCText.literal("Repair Cost: ").setStyle(repairStyle);
				for (Ingredient cost: getStats().getRepairCost()) {
					int num = 1;
					if (cost instanceof IngredientStack is) num = is.cost;
					repairCost.append(cost.getItems()[0].getDisplayName()).append("("+num+")");
				}
				tips.add(repairCost);
			}
		}
		getStats().addToolTips(tips, isAdvanced);
	}
	
	public MutableComponent getItemName() {
		return getStats().getDisplayNameComponent().setStyle(Style.EMPTY.withColor(0x55FF55));
	}
	
	public boolean hasExternalEntity() {
		return getStats().hasExternalEntity();
	}
	
}
