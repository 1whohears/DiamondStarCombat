package com.onewhohears.dscombat.data.parts;

import java.util.NoSuchElementException;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityRadar;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ExternalRadarPartData extends RadarPartData {
	
	public final String entityTypeKey;
	
	public ExternalRadarPartData(float weight, String preset, ResourceLocation itemid, SlotType[] compatibleSlots, String entityTypeKey) {
		super(weight, preset, itemid, compatibleSlots);
		this.entityTypeKey = entityTypeKey;
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putString("entityTypeKey", entityTypeKey);
		return tag;
	}
	
	public ExternalRadarPartData(CompoundTag tag) {
		super(tag);
		entityTypeKey = tag.getString("entityTypeKey");
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(entityTypeKey);
	}
	
	public ExternalRadarPartData(FriendlyByteBuf buffer) {
		super(buffer);
		entityTypeKey = buffer.readUtf();
	}
	
	@Override
	public void serverSetup(EntityAircraft craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		if (!isEntitySetup(slotId, craft)) {
			EntityRadar radar = new EntityRadar(getEntityType(), craft.level, slotId, pos);
			radar.setPos(craft.position());
			radar.startRiding(craft);
			craft.level.addFreshEntity(radar);
		}
	}
	
	@Override
	public void serverRemove(String slotId) {
		super.serverRemove(slotId);
		removeEntity(slotId);
	}
	
	@Override
	public PartType getType() {
		return PartType.EXTERNAL_RADAR;
	}
	
	protected EntityType<?> getEntityType() {
		try {
			return ForgeRegistries.ENTITY_TYPES.getDelegate(
					new ResourceLocation(entityTypeKey)).get().get();
		} catch(NoSuchElementException e) { 
			return ModEntities.CFM56.get(); 
		}
	}

}
