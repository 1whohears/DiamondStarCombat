package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.block.entity.VehicleBlock;
import com.onewhohears.dscombat.block.entity.WeaponPartsBlockEntity;
import com.onewhohears.dscombat.block.entity.WeaponsBlockEntity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            DSCombatMod.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
	
	public static final RegistrySupplier<BlockEntityType<WeaponsBlockEntity>> WEAPONS_BLOCK_ENTITY = BLOCK_ENTITIES.register("weapons_block_entity",
			() -> BlockEntityType.Builder.of(WeaponsBlockEntity::new, ModBlocks.WEAPONS_BLOCK.get()).build(null));
	public static final RegistrySupplier<BlockEntityType<VehicleBlock>> AIRCRAFT_BLOCK_ENTITY = BLOCK_ENTITIES.register("aircraft_block_entity",
			() -> BlockEntityType.Builder.of(VehicleBlock::new, ModBlocks.AIRCRAFT_BLOCK.get()).build(null));
	public static final RegistrySupplier<BlockEntityType<WeaponPartsBlockEntity>> WEAPON_PARTS_BLOCK_ENTITY = BLOCK_ENTITIES.register("weapon_parts_block_entity",
			() -> BlockEntityType.Builder.of(WeaponPartsBlockEntity::new, ModBlocks.WEAPON_PARTS_BLOCK.get()).build(null));

    public static void register() {
        BLOCK_ENTITIES.register();
    }
}
