package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.block.entity.VehicleBlock;
import com.onewhohears.dscombat.block.entity.WeaponsBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
    }
	
	public static final RegistryObject<BlockEntityType<WeaponsBlockEntity>> WEAPONS_BLOCK_ENTITY = BLOCK_ENTITIES.register("weapons_block_entity", 
			() -> BlockEntityType.Builder.of(WeaponsBlockEntity::new, ModBlocks.WEAPONS_BLOCK.get()).build(null));
	public static final RegistryObject<BlockEntityType<VehicleBlock>> AIRCRAFT_BLOCK_ENTITY = BLOCK_ENTITIES.register("aircraft_block_entity",
			() -> BlockEntityType.Builder.of(VehicleBlock::new, ModBlocks.AIRCRAFT_BLOCK.get()).build(null));
	
}
