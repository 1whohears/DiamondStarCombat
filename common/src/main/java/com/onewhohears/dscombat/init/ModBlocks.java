package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.block.custom.VehicleBlock;
import com.onewhohears.dscombat.block.custom.WeaponPartsBlock;
import com.onewhohears.dscombat.block.custom.WeaponsBlock;
import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class ModBlocks {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(DSCombatMod.MODID, Registries.BLOCK);
	
    private static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> block,
                                                                       ResourceKey<CreativeModeTab> tab) {
        RegistrySupplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistrySupplier<Item> registerBlockItem(String name,
                                                                              RegistrySupplier<T> block,
                                                                              ResourceKey<CreativeModeTab> tab) {
        Supplier<Item> blockItem = () -> new BlockItem(block.get(), new Item.Properties());
        //ModItems.addTabItem(tab, blockItem);
        return ModItems.ITEMS.register(name, blockItem);
    }
	
	public static final RegistrySupplier<Block> WEAPONS_BLOCK = registerBlock("weapons_block",
			() -> new WeaponsBlock(Block.Properties.copy(Blocks.BLAST_FURNACE).strength(1.5f)
					.noOcclusion().explosionResistance(6f)), ModCMTabs.WEAPONS.getKey());
	public static final RegistrySupplier<Block> AIRCRAFT_BLOCK = registerBlock("aircraft_block",
			() -> new VehicleBlock(Block.Properties.copy(Blocks.BLAST_FURNACE).strength(1.5f)
					.noOcclusion().explosionResistance(6f)), ModCMTabs.VEHICLES.getKey());
	public static final RegistrySupplier<Block> WEAPON_PARTS_BLOCK = registerBlock("weapon_parts_block",
			() -> new WeaponPartsBlock(Block.Properties.copy(Blocks.BLAST_FURNACE).strength(1.5f)
					.noOcclusion().explosionResistance(6f)), ModCMTabs.WEAPON_PARTS.getKey());
	
	public static final RegistrySupplier<Block> ALUMINUM_BLOCK = registerBlock("aluminum_block",
			() -> new Block(Block.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(1f).explosionResistance(2f).sound(SoundType.COPPER)),
            ModCMTabs.DSC_ITEMS.getKey());
	
	public static final RegistrySupplier<LiquidBlock> OIL_LIQUID_BLOCK = BLOCKS.register("oil_block",
			() -> new ArchitecturyLiquidBlock(ModFluids.getOilFluidSource(), BlockBehaviour.Properties.copy(Blocks.WATER)));
	
	// TODO 2.3 block to put stationary turrets on

    public static void register() {
        BLOCKS.register();
    }
}
