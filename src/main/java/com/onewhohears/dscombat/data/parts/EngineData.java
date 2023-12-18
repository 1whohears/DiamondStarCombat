package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class EngineData extends PartData {
	
	private final EngineType engineType;
	private final float thrust;
	private final float heat;
	private final float fuelRate;
	
	public EngineData(EngineType engineType, float weight, float thrust, float heat, float fuelRate, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
		this.engineType = engineType;
		this.thrust = thrust;
		this.heat = heat;
		this.fuelRate = fuelRate;
	}
	
	public EngineData(CompoundTag tag) {
		super(tag);
		engineType = EngineType.values()[tag.getInt("engineType")];
		thrust = tag.getFloat("thrust");
		heat = tag.getFloat("heat");
		fuelRate = tag.getFloat("fuelRate");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putInt("engineType", engineType.ordinal());
		tag.putFloat("thrust", thrust);
		tag.putFloat("heat", heat);
		tag.putFloat("fuelRate", fuelRate);
		return tag;
	}
	
	public EngineData(FriendlyByteBuf buffer) {
		super(buffer);
		engineType = EngineType.values()[buffer.readInt()];
		thrust = buffer.readFloat();
		heat = buffer.readFloat();
		fuelRate = buffer.readFloat();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeInt(engineType.ordinal());
		buffer.writeFloat(thrust);
		buffer.writeFloat(heat);
		buffer.writeFloat(fuelRate);
	}

	@Override
	public PartType getType() {
		return PartType.ENGINE;
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}
	
	public EngineType getEngineType() {
		return engineType;
	}
	
	public float getThrust() {
		return thrust;
	}
	
	public float getHeat() {
		return heat;
	}
	
	public float getFuelPerTick() {
		return fuelRate;
	}
	
	public static enum EngineType {
		SPIN,
		PUSH
	}

}
