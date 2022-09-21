package com.onewhohears.dscombat.init;

import com.google.common.collect.ImmutableSet;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.parts.EntitySeat;
import com.onewhohears.dscombat.entity.aircraft.parts.EntitySeatCamera;
import com.onewhohears.dscombat.entity.aircraft.plane.EntityTestPlane;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityRocket;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, DSCombatMod.MODID);
	
	public static final RegistryObject<EntityType<EntityTestPlane>> TEST_PLANE = ENTITIES.register("test_plane", 
			() -> createEntityType(EntityTestPlane::new, EntityDimensions.scalable(1F, 1F)));
	
	public static final RegistryObject<EntityType<EntitySeat>> SEAT = ENTITIES.register("seat", 
			() -> createEntityType(EntitySeat::new, EntityDimensions.scalable(1.1F, 1.1F)));
	
	/*
	 * this needs to be bigger that player model to stop invalid entity issue
	 * TODO find right size	
	 */
	public static final RegistryObject<EntityType<EntitySeatCamera>> CAMERA = ENTITIES.register("seat_camera", 
			() -> createEntityType(EntitySeatCamera::new, EntityDimensions.scalable(1f, 1f)));
	
	public static final RegistryObject<EntityType<EntityBullet>> BULLET = ENTITIES.register("bullet", 
			() -> createEntityType(EntityBullet::new, EntityDimensions.scalable(0.15f, 0.15f)));
	
	public static final RegistryObject<EntityType<EntityBullet>> ROCKET = ENTITIES.register("rocket", 
			() -> createEntityType(EntityRocket::new, EntityDimensions.scalable(0.25f, 0.25f)));
	
	private static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, true, ImmutableSet.of(), size, 5, 3);
    }
	
	/*public static final RegistryObject<EntityType<EntityBasicPlane>> BASIC_PLANE = ENTITIES.register("basic_plane", 
			() -> EntityType.Builder.of(EntityBasicPlane::new, MobCategory.MISC).sized(2.0f, 1.0f)
				.build(new ResourceLocation(DSCombatMod.MODID).toString()));*/
	
	public static void register(IEventBus eventBus) {
		ENTITIES.register(eventBus);
	}
}
