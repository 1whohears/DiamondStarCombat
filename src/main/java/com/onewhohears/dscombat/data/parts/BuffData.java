package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class BuffData extends PartData {
	
	public static enum BuffType {
		DATA_LINK
	}
	
	private final BuffType type;
	
	public BuffData(BuffType type, ResourceLocation itemid) {
		super(0, itemid);
		this.type = type;
	}
	
	public BuffData(BuffType type, String itemid) {
		this(type, new ResourceLocation(DSCombatMod.MODID, itemid));
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
	public boolean isSetup(String slotId, EntityAbstractAircraft craft) {
		return false;
	}
	
	@Override
	public void setup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		switch (type) {
		case DATA_LINK:
			this.getParent().radarSystem.dataLink = true;
			break;
		}
	}
	
	@Override
	public void remove(String slotId) {
		super.remove(slotId);
		switch (type) {
		case DATA_LINK:
			this.getParent().radarSystem.dataLink = false;
			break;
		}
	}

	@Override
	public SlotType[] getCompatibleSlots() {
		return INTERNAL;
	}

}
