package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class FlareDispenserData extends PartData {
	
	private final int max;
	private final float heat;
	private final int age;
	private int flares;
	
	public FlareDispenserData(float weight, int flares, int max, float heat, int age, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
		this.flares = flares;
		this.heat = heat;
		this.age = age;
		this.max = max;
	}
	
	public FlareDispenserData(CompoundTag tag) {
		super(tag);
		flares = tag.getInt("flares");
		heat = tag.getFloat("heat");
		age = tag.getInt("age");
		max = tag.getInt("max");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putInt("flares", flares);
		tag.putFloat("heat", heat);
		tag.putInt("age", age);
		tag.putInt("max", max);
		return tag;
	}
	
	public FlareDispenserData(FriendlyByteBuf buffer) {
		super(buffer);
		flares = buffer.readInt();
		heat = buffer.readFloat();
		age = buffer.readInt();
		max = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeInt(flares);
		buffer.writeFloat(heat);
		buffer.writeInt(age);
		buffer.writeInt(max);
	}
	
	@Override
	public PartType getType() {
		return PartType.FLARE_DISPENSER;
	}

	@Override
	public boolean isSetup(String slotId, EntityAircraft craft) {
		return false;
	}
	
	/**
	 * @param flares
	 * @return remainder
	 */
	public int addFlares(int flares) {
		this.flares += flares;
		if (this.flares < 0) {
			int r = this.flares;
			this.flares = 0;
			return r;
		}
		else if (this.flares > max) {
			int r = this.flares - this.max;
			this.flares = max;
			return r;
		}
		return 0;
	}
	
	public void setFlares(int flares) {
		if (flares > max) flares = max;
		else if (flares < 0) flares = 0;
		this.flares = flares;
	}
	
	public int getFlares() {
		return flares;
	}
	
	public int getMaxFlares() {
		return max;
	}
	
	@Override
	public float getWeight() {
		return super.getWeight();
	}
	
	public boolean flare(boolean consume) {
		if (getParent() == null) return false;
		if (getFlares() <= 0) return false;
		Level level = getParent().level;
		EntityFlare flare = new EntityFlare(level, heat, age, 3);
		flare.setPos(getParent().position().add(relPos));
		level.addFreshEntity(flare);
		if (consume) addFlares(-1);
		return true;
	}

}
