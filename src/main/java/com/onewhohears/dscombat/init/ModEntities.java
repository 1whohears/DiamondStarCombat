package com.onewhohears.dscombat.init;

import com.google.common.collect.ImmutableSet;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.EntityTestPlane;

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
