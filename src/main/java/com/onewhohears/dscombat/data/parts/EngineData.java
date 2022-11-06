package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class EngineData extends PartData {
	
	private final float thrust;
	private final float heat;
	private final float fuelRate;
	
	protected EngineData(float weight, float thrust, float heat, float fuelRate) {
		super(weight);
		this.thrust = thrust;
		this.heat = heat;
		this.fuelRate = fuelRate;
	}
	
	public EngineData(CompoundTag tag) {
		super(tag);
		thrust = tag.getFloat("thrust");
		heat = tag.getFloat("heat");
		fuelRate = tag.getFloat("fuelRate");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putFloat("thrust", thrust);
		tag.putFloat("heat", heat);
		tag.putFloat("fuelRate", fuelRate);
		return tag;
	}
	
	public EngineData(FriendlyByteBuf buffer) {
		super(buffer);
		thrust = buffer.readFloat();
		heat = buffer.readFloat();
		fuelRate = buffer.readFloat();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeFloat(thrust);
		buffer.writeFloat(heat);
		buffer.writeFloat(fuelRate);
	}

	@Override
	public PartType getType() {
		return PartType.ENGINE;
	}

	@Override
	public boolean isSetup(String slotId, EntityAbstractAircraft craft) {
		return true;
	}

	@Override
	public SlotType[] getCompatibleSlots() {
		return EXTERNAL;
	}

	@Override
	public ItemStack getItemStack() {
		// TODO Auto-generated method stub
		return null;
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

}
