package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityVehiclePart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;

public class RadarPartData extends PartData {
	
	private String radarId;
	
	public RadarPartData(float weight, String preset, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
		this.radarId = preset;
	}
	
	protected RadarPartData(float weight, String preset, ResourceLocation itemid, SlotType[] compatibleSlots,
			String modelId, EntityDimensions size) {
		super(weight, itemid, compatibleSlots, modelId, size);
		this.radarId = preset;
	}
	
	public RadarPartData(CompoundTag tag) {
		super(tag);
		radarId = tag.getString("radarId");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putString("radarId", radarId);
		return tag;
	}

	public RadarPartData(FriendlyByteBuf buffer) {
		super(buffer);
		radarId = buffer.readUtf();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(radarId);
	}

	@Override
	public PartType getType() {
		return PartType.INTERNAL_RADAR;
	}
	
	@Override
	public void setup(EntityAircraft craft, String slotId, Vec3 pos, float zRot) {
		super.setup(craft, slotId, pos, zRot);
		RadarData data = craft.radarSystem.get(radarId, slotId);
		if (data == null) {
			data = RadarPresets.get().getPreset(radarId);
			if (data == null) return;
			data.setSlot(slotId);
			data.setPos(pos);
			craft.radarSystem.addRadar(data);
		}
	}
	
	@Override
	public void remove() {
		super.serverRemove();
		getParent().radarSystem.removeRadar(radarId, getSlotId());
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
