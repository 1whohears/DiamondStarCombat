package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class FuelTankData extends PartData {
	
	private final float max;
	private float fuel;
	
	public FuelTankData(float weight, float fuel, float max, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
		this.fuel = fuel;
		this.max = max;
	}
	
	public void read(CompoundTag tag) {
		super.read(tag);
		fuel = tag.getFloat("fuel");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putFloat("fuel", fuel);
		return tag;
	}
	
	public void read(FriendlyByteBuf buffer) {
		super.read(buffer);
		fuel = buffer.readFloat();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeFloat(fuel);
	}
	
	@Override
	public PartType getType() {
		return PartType.FUEL_TANK;
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}
	
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
