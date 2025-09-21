package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.block.custom.VehicleBlock;
import com.onewhohears.dscombat.block.custom.WeaponPartsBlock;
import com.onewhohears.dscombat.block.custom.WeaponsBlock;
import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class ModBlocks {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(DSCombatMod.MODID, Registry.BLOCK_REGISTRY);
	
    private static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> block,
                                                                       CreativeModeTab tab) {
        RegistrySupplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistrySupplier<Item> registerBlockItem(String name,
                                                                              RegistrySupplier<T> block,
                                                                              CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
	
	public static final RegistrySupplier<Block> WEAPONS_BLOCK = registerBlock("weapons_block",
			() -> new WeaponsBlock(Block.Properties.of(Material.METAL).strength(1.5f)
					.noOcclusion().explosionResistance(6f)), ModItems.WEAPONS);
	public static final RegistrySupplier<Block> AIRCRAFT_BLOCK = registerBlock("aircraft_block",
			() -> new VehicleBlock(Block.Properties.of(Material.METAL).strength(1.5f)
					.noOcclusion().explosionResistance(6f)), ModItems.VEHICLES);
	public static final RegistrySupplier<Block> WEAPON_PARTS_BLOCK = registerBlock("weapon_parts_block",
			() -> new WeaponPartsBlock(Block.Properties.of(Material.METAL).strength(1.5f)
					.noOcclusion().explosionResistance(6f)), ModItems.WEAPON_PARTS);
	
	public static final RegistrySupplier<Block> ALUMINUM_BLOCK = registerBlock("aluminum_block",
			() -> new Block(Block.Properties.of(Material.METAL)
                    .strength(1f).explosionResistance(2f).sound(SoundType.COPPER)), ModItems.DSC_ITEMS);
	
	public static final RegistrySupplier<LiquidBlock> OIL_LIQUID_BLOCK = BLOCKS.register("oil_block",
			() -> new ArchitecturyLiquidBlock(ModFluids.getOilFluidFlowing(), BlockBehaviour.Properties.copy(Blocks.WATER)));
	
	// TODO 2.3 block to put stationary turrets on

    public static void register() {
        BLOCKS.register();
    }
}
