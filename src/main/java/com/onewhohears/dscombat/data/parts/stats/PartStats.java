package com.onewhohears.dscombat.data.parts.stats;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetStats;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.minigames.util.UtilParse;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public abstract class PartStats extends JsonPresetStats {
	
	private final float weight;
	private final String itemId, externalEntityId;
	
	private Item item;
	private EntityType<?> externalEntityType;
	
	public PartStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		this.weight = UtilParse.getFloatSafe(json, "weight", 0);
		this.itemId = UtilParse.getStringSafe(json, "item", "");
		this.externalEntityId = getExternalEntityId(json);
	}
	
	public SlotType[] getCompatibleSlots() {
		
	}
	
	public PartInstance<?> createPartInstance() {
		return (PartInstance<?>) createPresetInstance();
	}
	
	public float getWeight() {
		return weight;
	}
	
	public Item getItem() {
		if (item == null) {
			item = UtilItem.getItem(itemId);
		}
		return item;
	}
	
	public boolean hasExternalEntity() {
		return !externalEntityId.isEmpty();
	}
	
	@Nullable
	public EntityType<?> getExernalEntityType() {
		if (!hasExternalEntity()) return null;
		if (externalEntityType == null) {
			externalEntityType = UtilEntity.getEntityType(externalEntityId, getDefaultExternalEntity());
		}
		return externalEntityType;
	}
	
	@Nullable
	public EntityType<?> getDefaultExternalEntity() {
		return null;
	}
	
	protected String getExternalEntityId(JsonObject json) {
		return UtilParse.getStringSafe(json, "external_entity", "");
	}
	
	public float getExternalEntityDefaultHealth() {
		return 100000;
	}
	
	public boolean isFlareDispenser() {
		return getType().is(PartType.FLARE_DISPENSER);
	}
	
	public boolean isGimbal() {
		return getType().is(PartType.GIMBAL);
	}
	
	public boolean isChainHook() {
		return getType().is(PartType.CHAIN_HOOK);
	}
	
	public boolean isRadio() {
		return false;
	}
	
	public boolean isEngine() {
		return false;
	}
	
	public boolean isSeat() {
		return false;
	}
	
	public boolean isStorageBox() {
		return false;
	}
	
	public boolean isFuelTank() {
		return false;
	}
	
	public float getAdditionalArmor() {
		return 0;
	}
	
}
