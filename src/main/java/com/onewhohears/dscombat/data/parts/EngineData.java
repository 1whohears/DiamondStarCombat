package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class EngineData extends PartData {
	
	private final float thrust;
	private final float heat;
	private final float fuelRate;
	
	public EngineData(float weight, float thrust, float heat, float fuelRate, ResourceLocation itemid) {
		super(weight, itemid);
		this.thrust = thrust;
		this.heat = heat;
		this.fuelRate = fuelRate;
	}
	
	public EngineData(float weight, float thrust, float heat, float fuelRate, String itemid) {
		this(weight, thrust, heat, fuelRate, new ResourceLocation(DSCombatMod.MODID, itemid));
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
		return false;
	}

	@Override
	public SlotType[] getCompatibleSlots() {
		return INTERNAL;
	}

	/*@Override
	public ItemStack getItemStack() {
		
	}*/
	
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
