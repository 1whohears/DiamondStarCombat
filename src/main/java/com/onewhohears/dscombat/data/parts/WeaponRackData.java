package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.parts.EntityVehiclePart;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;

public class WeaponRackData extends WeaponPartData {
	
	public final float changeLaunchPitch;
	
	public WeaponRackData(float weight, String preset, String[] compatible, ResourceLocation itemid, SlotType[] compatibleSlots, float launchPitch) {
		super(weight, preset, compatible, itemid, compatibleSlots);
		this.changeLaunchPitch = launchPitch;
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putFloat("changeLaunchPitch", changeLaunchPitch);
		return tag;
	}
	
	public WeaponRackData(CompoundTag tag) {
		super(tag);
		changeLaunchPitch = tag.getFloat("changeLaunchPitch");
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeFloat(changeLaunchPitch);
	}
	
	public WeaponRackData(FriendlyByteBuf buffer) {
		super(buffer);
		changeLaunchPitch = buffer.readFloat();
	}
	
	@Override
	public EntityVehiclePart getPartEntity() {
		WeaponData data = getParent().weaponSystem.get(weaponId, getSlotId());
		if (data == null) return null;
		data.setChangeLaunchPitch(changeLaunchPitch);
		return new EntityWeaponRack(getParent(), data.getRackModelId(), 
				EntityDimensions.scalable(0.1f, 0.1f), 
				getSlotId(), getRelPos(), getZRot());
	}
	
	@Override
	public boolean hasExternalPartEntity() {
		return true;
	}
	
	@Override
	public PartType getType() {
		return PartType.WEAPON_RACK;
	}

}
