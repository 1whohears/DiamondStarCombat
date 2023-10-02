package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.parts.EntityVehiclePart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

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
	
	public BuffData(CompoundTag tag) {
		super(tag);
		type = BuffType.values()[tag.getInt("bufftype")];
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putInt("bufftype", type.ordinal());
		return tag;
	}
	
	public BuffData(FriendlyByteBuf buffer) {
		super(buffer);
		type = BuffType.values()[buffer.readInt()];
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeInt(type.ordinal());
	}

	@Override
	public PartType getType() {
		return PartType.BUFF_DATA;
	}
	
	@Override
	public void serverSetup() {
		super.serverSetup();
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
	public void serverRemove() {
		super.serverRemove();
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
	
	@Nullable
	@Override
	public EntityVehiclePart getPartEntity() {
		return null;
	}

	@Override
	public boolean hasExternalPartEntity() {
		return false;
	}

}
