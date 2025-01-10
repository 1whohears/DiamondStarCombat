package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.stats.BuffStats;
import com.onewhohears.dscombat.data.parts.stats.EngineStats;

import com.onewhohears.dscombat.data.parts.stats.TurretStats;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.onewholibs.data.crafting.IngredientStackBuilder;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

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

	public PartBuilder setEntityHitboxSize(float width, float height) {
		setFloat("hitbox_width", width);
		return setFloat("hitbox_height", height);
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
	
	public PartBuilder setTurretStats(int maxAmmo, float maxHealth, Vec3 passengerOffset, double weaponOffset,
									  TurretStats.RotBounds rotBounds, EntityTurret.ShootType shootType, float width, float height) {
		setFloat("maxHealth", maxHealth);
		setEntityHitboxSize(width, height);
		setExternalEntityType(ModEntities.TURRET.getId());
		UtilParse.writeVec3(getData(), "passenger_offset", passengerOffset);
		setFloat("weaponOffset", (float)weaponOffset);
		rotBounds.writeToJson(getData());
		UtilParse.writeEnum(getData(), "shootType", shootType);
		return setInt("maxAmmo", maxAmmo);
	}

	public PartBuilder setTurretStats(int maxAmmo, float maxHealth, Vec3 passengerOffset, double weaponOffset,
									  TurretStats.RotBounds rotBounds, float width, float height) {
		return setTurretStats(maxAmmo, maxHealth, passengerOffset, weaponOffset, rotBounds, EntityTurret.ShootType.NORMAL, width, height);
	}
	
	public PartBuilder setWeaponStats(int max) {
		return setInt("max", max);
	}
	
	public PartBuilder setExternalWeaponStats(int max, float changeLaunchPitch) {
		setWeaponStats(max);
		setEntityHitboxSize(0.1f, 0.1f);
		setExternalEntityType(ModEntities.EXTERNAL_WEAPON_PART.getId());
		return setFloat("changeLaunchPitch", changeLaunchPitch);
	}

}
