package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.item.ItemAircraft;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
	
	public static final CreativeModeTab PARTS = new CreativeModeTab("parts") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.SEAT.get());
		}
	};
	
	public static final CreativeModeTab AIRCRAFT = new CreativeModeTab("aircraft") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.TEST_PLANE.get());
		}
	};
	
	public static final RegistryObject<Item> SEAT = ITEMS.register("seat", () -> new Item(new Item.Properties().tab(PARTS)));
	public static final RegistryObject<Item> TEST_PLANE = ITEMS.register("test_plane", 
			() -> new ItemAircraft(ModEntities.TEST_PLANE.get(), "test_plane"));
	public static final RegistryObject<Item> JAVI_PLANE = ITEMS.register("javi_plane", 
			() -> new ItemAircraft(ModEntities.TEST_PLANE.get(), "javi"));
	
}
