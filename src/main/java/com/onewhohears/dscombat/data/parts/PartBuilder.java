package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.stats.BuffStats;
import com.onewhohears.dscombat.data.parts.stats.EngineStats;

import com.onewhohears.onewholibs.data.crafting.IngredientStackBuilder;
import net.minecraft.resources.ResourceLocation;

public class PartBuilder extends IngredientStackBuilder<PartBuilder> {
	
	public static PartBuilder create(ResourceLocation item, PartType type) {
		PartBuilder builder = create(item.getNamespace(), item.getPath(), item, type);
		builder.setDisplayName("item."+item.getNamespace()+"."+item.getPath());
		return builder;
	}
	
	public static PartBuilder create(String namespace, String name, ResourceLocation item, PartType type) {
		PartBuilder builder = new PartBuilder(namespace, name, type);
		builder.setItem(item);
		builder.setDisplayName("item."+namespace+"."+name);
		return builder;
	}
	
	@Override
	public String getIngredientListName() {
		return "repair_cost";
	}
	
	protected PartBuilder(String namespace, String name, PartType type) {
		super(namespace, name, type);
	}
	
	public PartBuilder setWeight(float weight) {
		return setFloat("weight", weight);
	}
	
	protected PartBuilder setItem(ResourceLocation item) {
		return setString("item", item.toString());
	}
	
	public PartBuilder setCompatibleSlotType(SlotType type) {
		return setString("slotType", type.getSlotTypeName());
	}
	
	public PartBuilder setExternalEntityType(ResourceLocation externalEntity) {
		return setString("externalEntity", externalEntity.toString());
	}
	
	public PartBuilder setBuffStats(BuffStats.BuffType buff) {
		return setEnum("buffType", buff);
	}
	
	public PartBuilder setEngineStats(EngineStats.EngineType engineType, float thrust, float heat, float fuelRate) {
		setFloat("thrust", thrust);
		setFloat("heat", heat);
		setFloat("fuelRate", fuelRate);
		return setEnum("engineType", engineType);
	}
	
	public PartBuilder setFlareDispenserStats(int max, int age, float heat) {
		setInt("max", max);
		setInt("age", age);
		return setFloat("heat", heat);
	}
	
	public PartBuilder setFuelTankStats(float max) {
		return setFloat("max", max);
	}
	
	public PartBuilder setRadarStats(String radar) {
		return setString("radar", radar);
	}
	
	public PartBuilder setStorageStats(int size) {
		return setInt("size", size);
	}
	
	public PartBuilder setTurretStats(int maxAmmo, float maxHealth) {
		setFloat("maxHealth", maxHealth);
		return setInt("maxAmmo", maxAmmo);
	}
	
	public PartBuilder setWeaponStats(int max) {
		return setInt("max", max);
	}
	
	public PartBuilder setExternalWeaponStats(int max, float changeLaunchPitch) {
		setWeaponStats(max);
		return setFloat("changeLaunchPitch", changeLaunchPitch);
	}

}
