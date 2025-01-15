package com.onewhohears.dscombat.data.parts.stats;

import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.data.crafting.IngredientStackBuilder;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetStats;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.SlotType;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilItem;
import com.onewhohears.onewholibs.util.UtilMCText;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class PartStats extends JsonPresetStats {
	
	private final float weight, hitbox_width, hitbox_height;
	private final String itemId, externalEntityId;
	private final SlotType compatibleSlotType;
	
	private Item item;
	private EntityType<?> externalEntityType;
	private NonNullList<Ingredient> repair_cost;
	private EntityDimensions dimensions;
	private NonNullList<Ingredient> ingredients;

	public PartStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		this.weight = UtilParse.getFloatSafe(json, "weight", 0);
		this.itemId = UtilParse.getStringSafe(json, "item", "");
		this.externalEntityId = getExternalEntityId(json);
		this.compatibleSlotType = SlotType.getByName(UtilParse.getStringSafe(json, "slotType", ""));
		this.hitbox_width = UtilParse.getFloatSafe(json, "hitbox_width", 0.8f);
		this.hitbox_height = UtilParse.getFloatSafe(json, "hitbox_height", 0.8f);
	}
	
	public boolean isCompatible(SlotType type) {
		if (type == null) return false;
		return compatibleSlotType.isCompatible(type);
	}
	
	public PartInstance<?> createPartInstance() {
		return (PartInstance<?>) createPresetInstance();
	}
	
	public PartInstance<?> createFilledPartInstance(String param) {
		PartInstance<?> filled = (PartInstance<?>) createPresetInstance();
		filled.setFilled(param);
		return filled;
	}
	
	public float getWeight() {
		return weight;
	}
	
	public String getItemId() {
		return itemId;
	}
	
	public Item getItem() {
		if (item == null) item = UtilItem.getItem(itemId);
		return item;
	}
	
	public NonNullList<Ingredient> getRepairCost() {
		if (repair_cost == null) repair_cost = IngredientStackBuilder.getIngredients(
				getJsonData(), "repair_cost");
		return repair_cost;
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
		return UtilParse.getStringSafe(json, "externalEntity", "");
	}
	
	public void addToolTips(List<Component> tips, TooltipFlag isAdvanced) {
		tips.add(UtilMCText.translatable("info.dscombat.compatible")
				.append(": ").setStyle(Style.EMPTY.withColor(0xFFFF55))
				.append(UtilMCText.translatable(compatibleSlotType.getTranslatableName())));
		tips.add(UtilMCText.translatable("info.dscombat.mass").append(": "+weight)
				.setStyle(Style.EMPTY.withColor(0xAAAAAA)));
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

	public EntityDimensions getEntityDimensions() {
		if (dimensions == null) dimensions = EntityDimensions.fixed(hitbox_width, hitbox_height);
		return dimensions;
	}

	public NonNullList<Ingredient> getIngredients() {
		if (ingredients == null) {
			ingredients = IngredientStackBuilder.getIngredients(getJsonData());
		}
		return ingredients;
	}

	public boolean isCraftableWeaponPart() {
		return false;
	}
	
}
