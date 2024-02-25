package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class BuffData extends PartData {
	
	public static enum BuffType {
		DATA_LINK,
		NIGHT_VISION_HUD,
		RADIO,
		ARMOR
	}
	
	private final BuffType type;
	
	public BuffData(BuffType type, ResourceLocation itemid, SlotType[] compatibleSlots, float weight) {
		super(weight, itemid, compatibleSlots);
		this.type = type;
	}

	@Override
	public PartType getType() {
		return PartType.BUFF_DATA;
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}
	
	@Override
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		switch (type) {
		case DATA_LINK:
			getParent().radarSystem.dataLink = true;
			break;
		case NIGHT_VISION_HUD:
			getParent().nightVisionHud = true;
			break;
		case RADIO:
			getParent().hasRadio = true;
			break;
		case ARMOR:
			break;
		}
	}
	
	@Override
	public void serverRemove(String slotId) {
		super.serverRemove(slotId);
		if (getParent() == null) return;
		switch (type) {
		case DATA_LINK:
			getParent().radarSystem.dataLink = false;
			break;
		case NIGHT_VISION_HUD:
			getParent().nightVisionHud = false;
			break;
		case RADIO:
			getParent().hasRadio = false;
			break;
		case ARMOR:
			break;
		}
	}
	
	@Override
	public boolean isRadio() {
		return type == BuffType.RADIO;
	}
	
	@Override
	public float getAdditionalArmor() {
		if (type == BuffType.ARMOR) return 4f;
		return 0f;
	}

}
