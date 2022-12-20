package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.parts.EntityTurrent;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class TurrentData extends SeatData {
	
	public TurrentData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
	}

	public TurrentData(CompoundTag tag) {
		super(tag);
	}

	public TurrentData(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	@Override
	public PartType getType() {
		return PartType.TURRENT;
	}
	
	@Override
	public void setup(EntityAircraft craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		if (isSetup(slotId, craft)) {
			return;
		}
		EntityTurrent t = ModEntities.MINIGUN_TURRENT.get().create(craft.level);
		t.setSlotId(slotId);
		t.setRelativePos(pos);
		t.setPos(craft.position());
		t.startRiding(craft);
		craft.level.addFreshEntity(t);
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
	
}
