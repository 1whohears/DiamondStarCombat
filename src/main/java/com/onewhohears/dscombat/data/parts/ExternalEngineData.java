package com.onewhohears.dscombat.data.parts;

import java.util.NoSuchElementException;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityEngine;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ExternalEngineData extends EngineData {
	
	private final String entityTypeKey;
	
	public ExternalEngineData(EngineType engineType, float weight, float thrust, float heat, float fuelRate, 
			ResourceLocation itemid, SlotType[] compatibleSlots, String entityTypeKey) {
		super(engineType, weight, thrust, heat, fuelRate, itemid, compatibleSlots);
		this.entityTypeKey = entityTypeKey;
	}
	
	public ExternalEngineData(CompoundTag tag) {
		super(tag);
		entityTypeKey = tag.getString("entityTypeKey");
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putString("entityTypeKey", entityTypeKey);
		return tag;
	}
	
	public ExternalEngineData(FriendlyByteBuf buffer) {
		super(buffer);
		entityTypeKey = buffer.readUtf();
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(entityTypeKey);
	}
	
	@Override
	public PartType getType() {
		return PartType.EXTERNAL_ENGINE;
	}
	
	protected EntityType<?> getEntityType() {
		try {
			return ForgeRegistries.ENTITY_TYPES.getDelegate(
					new ResourceLocation(entityTypeKey)).get().get();
		} catch(NoSuchElementException e) { 
			return ModEntities.CFM56.get(); 
		}
	}
	
	@Override
	public void serverSetup(EntityAircraft craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		if (!isEntitySetup(slotId, craft)) {
			EntityEngine engine = new EntityEngine(getEntityType(), craft.level, slotId, pos);
			engine.setPos(craft.position());
			engine.startRiding(craft);
			craft.level.addFreshEntity(engine);
		}
	}
	
	
	public boolean isEntitySetup(String slotId, EntityAircraft craft) {
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getPartType() == getType() && part.getSlotId().equals(slotId)) 
				return true;
		return false;
	}
	
	@Override
	public void serverRemove(String slotId) {
		super.serverRemove(slotId);
		for (EntityPart part : getParent().getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				part.discard();
	}

}
