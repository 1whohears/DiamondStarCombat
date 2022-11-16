package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class FuelTankData extends PartData {
	
	private final float max;
	private float fuel;
	
	public FuelTankData(float weight, float fuel, float max, ResourceLocation itemid) {
		super(weight, itemid);
		this.fuel = fuel;
		this.max = max;
	}
	
	public FuelTankData(float weight, float fuel, float max, String itemid) {
		this(weight, fuel, max, new ResourceLocation(DSCombatMod.MODID, itemid));
	}
	
	public FuelTankData(CompoundTag tag) {
		super(tag);
		fuel = tag.getFloat("fuel");
		max = tag.getFloat("max");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putFloat("fuel", fuel);
		tag.putFloat("max", max);
		return tag;
	}
	
	public FuelTankData(FriendlyByteBuf buffer) {
		super(buffer);
		fuel = buffer.readFloat();
		max = buffer.readFloat();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeFloat(fuel);
		buffer.writeFloat(max);
	}
	
	@Override
	public PartType getType() {
		return PartType.FUEL_TANK;
	}

	@Override
	public boolean isSetup(String slotId, EntityAbstractAircraft craft) {
		return false;
	}

	@Override
	public SlotType[] getCompatibleSlots() {
		return INTERNAL;
	}

	/*@Override
	public ItemStack getItemStack() {
		return null;
	}*/
	
	/**
	 * @param fuel
	 * @return remainder
	 */
	public float addFuel(float fuel) {
		this.fuel += fuel;
		if (this.fuel < 0) {
			float r = this.fuel;
			this.fuel = 0;
			return r;
		}
		else if (this.fuel > max) {
			float r = this.fuel - this.max;
			this.fuel = max;
			return r;
		}
		return 0;
	}
	
	public void setFuel(float fuel) {
		if (fuel > max) fuel = max;
		else if (fuel < 0) fuel = 0;
		this.fuel = fuel;
	}
	
	public float getFuel() {
		return fuel;
	}
	
	public float getMaxFuel() {
		return max;
	}
	
	@Override
	public float getWeight() {
		float w = super.getWeight();
		return w * fuel / max;
	}

}
