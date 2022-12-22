package com.onewhohears.dscombat.data.parts;

import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.parts.EntityTurrent;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class TurrentData extends SeatData {
	
	private final String turrentEntityKey;
	private EntityType<? extends EntityTurrent> turrentType;
	private int ammo; 
	
	public TurrentData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, String turrentEntityKey) {
		super(weight, itemid, compatibleSlots);
		this.turrentEntityKey = turrentEntityKey;
	}

	public TurrentData(CompoundTag tag) {
		super(tag);
		turrentEntityKey = tag.getString("turrentEntity");
		ammo = tag.getInt("ammo");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putString("turrentEntity", turrentEntityKey);
		tag.putInt("ammo", ammo);
		return tag;
	}

	public TurrentData(FriendlyByteBuf buffer) {
		super(buffer);
		turrentEntityKey = buffer.readUtf();
		ammo = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(turrentEntityKey);
		buffer.writeInt(ammo);
	}
	
	@Override
	public PartType getType() {
		return PartType.TURRENT;
	}
	
	@Override
	public void setup(EntityAircraft craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		EntityTurrent t = getTurrent(slotId);
		t.setAmmo(ammo);
	}
	
	@Nullable
	public EntityTurrent getTurrent(String slotId) {
		EntityAircraft craft = getParent();
		if (craft == null) return null;
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				return (EntityTurrent) part;
		EntityTurrent t = getTurrentType().create(craft.level);
		t.setSlotId(slotId);
		t.setRelativePos(getRelPos());
		t.setPos(craft.position());
		t.startRiding(craft);
		craft.level.addFreshEntity(t);
		return t;
	}
	
	@Override
	public boolean isSetup(String slotId, EntityAircraft craft) {
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				return true;
		return false;
	}
	
	@Override
	public void remove(String slotId) {
		for (EntityPart part : getParent().getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				part.discard();
	}
	
	@SuppressWarnings("unchecked")
	public EntityType<? extends EntityTurrent> getTurrentType() {
		if (turrentType == null) {
			try { turrentType = (EntityType<? extends EntityTurrent>) ForgeRegistries.ENTITY_TYPES
					.getDelegate(new ResourceLocation(turrentEntityKey)).get().get(); }
			catch(NoSuchElementException e) { turrentType = ModEntities.MINIGUN_TURRENT.get(); }
			catch(ClassCastException e) { turrentType = ModEntities.MINIGUN_TURRENT.get(); }
		}
		return turrentType;
	}
	
	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}
	
}
