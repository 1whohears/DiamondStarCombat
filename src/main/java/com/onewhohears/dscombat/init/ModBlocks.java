package com.onewhohears.dscombat.init;

import java.util.function.Supplier;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.block.custom.AircraftBlock;
import com.onewhohears.dscombat.block.custom.WeaponsBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
    }
	
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
	
	public static final RegistryObject<Block> WEAPONS_BLOCK = registerBlock("weapons_block", 
			() -> new WeaponsBlock(Block.Properties.of(Material.METAL)
                    .strength(1.5f).noOcclusion().explosionResistance(6f)), ModItems.WEAPONS);
	public static final RegistryObject<Block> AIRCRAFT_BLOCK = registerBlock("aircraft_block", 
			() -> new AircraftBlock(Block.Properties.of(Material.METAL)
                    .strength(1.5f).noOcclusion().explosionResistance(6f)), ModItems.AIRCRAFT);
	
	// TODO 2.3 block to put stationary turrets on
	
}
