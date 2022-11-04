package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.parts.EntityAbstractPart;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class WeaponRackData extends WeaponPartData {

	public WeaponRackData(float weight, String preset, int ammo, int max) {
		super(weight, preset, ammo, max);
	}
	
	public WeaponRackData(CompoundTag tag) {
		super(tag);
	}

	public WeaponRackData(FriendlyByteBuf buffer) {
		super(buffer);
	}

	@Override
	public void setup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		if (!isEntitySetup(slotId, craft)) {
			EntityWeaponRack rack = new EntityWeaponRack(craft.level, slotId, pos);
			rack.setPos(craft.position());
			rack.startRiding(craft);
			craft.level.addFreshEntity(rack);
		}
	}
	
	@Override
	public boolean isSetup(String slotId, EntityAbstractAircraft craft) {
		return super.isSetup(slotId, craft);
	}
	
	public boolean isEntitySetup(String slotId, EntityAbstractAircraft craft) {
		for (EntityAbstractPart part : craft.getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				return true;
		return false;
	}
	
	@Override
	public void remove(String slotId) {
		super.remove(slotId);
		for (EntityAbstractPart part : getParent().getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				part.discard();
	}
	
	@Override
	public SlotType[] getCompatibleSlots() {
		return EXTERNAL;
	}
	
	@Override
	public PartType getType() {
		return PartType.WEAPON_RACK;
	}

}
