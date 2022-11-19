package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class RadarPartData extends PartData {
	
	private String radarId;
	
	public RadarPartData(float weight, String preset, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
		this.radarId = preset;
	}
	
	public RadarPartData(float weight, String preset, String itemid, SlotType[] compatibleSlots) {
		this(weight, preset, new ResourceLocation(DSCombatMod.MODID, itemid), compatibleSlots);
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
	public void setup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		RadarData data = craft.radarSystem.get(radarId, slotId);
		if (data == null) {
			data = RadarPresets.getById(radarId);
			if (data == null) return;
			data.setSlot(slotId);
			craft.radarSystem.addRadar(data, true);
		}
	}
	
	@Override
	public boolean isSetup(String slotId, EntityAbstractAircraft craft) {
		RadarData data = craft.radarSystem.get(radarId, slotId);
		if (data == null) return false;
		return true;
	}
	
	@Override
	public void remove(String slotId) {
		super.remove(slotId);
		this.getParent().radarSystem.removeRadar(radarId, slotId, true);
	}
	
	@Override
	public void tick(String slotId) {
		super.tick(slotId);
		/*RadarData data = this.getParent().radarSystem.get(radarId, slotId);
		if (data != null) {
			
		}*/
	}
	
	@Override
	public void clientTick(String slotId) {
		super.clientTick(slotId);
		//this.tick(slotId);
	}

}
