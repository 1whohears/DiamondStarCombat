package com.onewhohears.dscombat.init;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntitySeatCamera;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		ENTITIES.register(eventBus);
	}
	
	@Nullable
	public static RegistryObject<EntityType<?>> getObjectByKey(String key) {
		for (RegistryObject<EntityType<?>> type : ENTITIES.getEntries()) 
			if(type.getId().toString().equals(key)) return type;
		return null;
	}
	
	public static final RegistryObject<EntityType<EntityPlane>> TEST_PLANE = ENTITIES.register("test_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					new ResourceLocation(DSCombatMod.MODID, "textures/entities/basic_plane.png"),
					ModSounds.BIPLANE_1, ModItems.TEST_PLANE), 
					EntityDimensions.scalable(1.5f, 1.5f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> JAVI_PLANE = ENTITIES.register("javi_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					new ResourceLocation(DSCombatMod.MODID, "textures/entities/javi_plane.png"),
					ModSounds.BIPLANE_1, ModItems.JAVI_PLANE), 
					EntityDimensions.scalable(1.5f, 1.5f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> ALEXIS_PLANE = ENTITIES.register("alexis_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					new ResourceLocation(DSCombatMod.MODID, "textures/entities/alexis_plane.png"),
					ModSounds.BIPLANE_1, ModItems.ALEXIS_PLANE), 
					EntityDimensions.scalable(1.5f, 1.5f)));
	
	public static final RegistryObject<EntityType<EntityHelicopter>> NOAH_CHOPPER = ENTITIES.register("noah_chopper", 
			() -> createEntityTypeFar((type, level) -> new EntityHelicopter(type, level, 
					new ResourceLocation(DSCombatMod.MODID, "textures/entities/noah_chopper.png"),
					ModSounds.HELI_1, ModItems.NOAH_CHOPPER, true), 
					EntityDimensions.scalable(2.8f, 2.8f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> F16 = ENTITIES.register("f16", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					new ResourceLocation(DSCombatMod.MODID, "textures/entities/f16.png"),
					ModSounds.BIPLANE_1, ModItems.F16), 
					EntityDimensions.scalable(1.5f, 1.5f)));
	
	public static final RegistryObject<EntityType<EntitySeat>> SEAT = ENTITIES.register("seat", 
			() -> createEntityType(EntitySeat::new, EntityDimensions.scalable(0f, 0f)));
	
	public static final RegistryObject<EntityType<EntitySeatCamera>> CAMERA = ENTITIES.register("seat_camera", 
			() -> createEntityType(EntitySeatCamera::new, EntityDimensions.scalable(0f, 0f)));
	
	public static final RegistryObject<EntityType<EntityWeaponRack>> WEAPON_RACK = ENTITIES.register("weapon_rack", 
			() -> createEntityType(EntityWeaponRack::new, EntityDimensions.scalable(0f, 0f)));
	
	public static final RegistryObject<EntityType<?>> BULLET = ENTITIES.register("bullet", 
			() -> createEntityType(EntityBullet::new, EntityDimensions.scalable(0.15f, 0.15f)));
	
	public static final RegistryObject<EntityType<?>> MISSILE1 = ENTITIES.register("missile1", 
			() -> createEntityTypeFar(EntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	
	public static final RegistryObject<EntityType<?>> MISSILE2 = ENTITIES.register("missile2", 
			() -> createEntityTypeFar(EntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	
	public static final RegistryObject<EntityType<?>> MISSILE3 = ENTITIES.register("missile3", 
			() -> createEntityTypeFar(EntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	
	public static final RegistryObject<EntityType<EntityFlare>> FLARE = ENTITIES.register("flare", 
			() -> createEntityType(EntityFlare::new, EntityDimensions.scalable(0f, 0f)));
	
	private static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, true, ImmutableSet.of(), size, 5, 3);
    }
	
	private static <T extends Entity> EntityType<T> createEntityTypeFar(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, true, ImmutableSet.of(), size, 10, 3);
    }
	
}
