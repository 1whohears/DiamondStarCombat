package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.item.ItemAircraft;
import com.onewhohears.dscombat.item.ItemAmmo;
import com.onewhohears.dscombat.item.ItemEngine;
import com.onewhohears.dscombat.item.ItemFuelTank;
import com.onewhohears.dscombat.item.ItemSeat;
import com.onewhohears.dscombat.item.ItemWeaponPart;

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
		registerWeaponPresets();
		ITEMS.register(eventBus);
	}
	
	public static void registerWeaponPresets() {
		for (int i = 0; i < WeaponPresets.weapons.size(); ++i) {
			final WeaponData data = WeaponPresets.weapons.get(i);
			ITEMS.register(data.getId(), () -> new ItemAmmo(data.getMaxAmmo()));
		}
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
	
	public static final RegistryObject<Item> SEAT = ITEMS.register("seat", 
			() -> new ItemSeat());
	
	public static final RegistryObject<Item> TEST_MISSILE_RACK = ITEMS.register("test_missile_rack", 
			() -> new ItemWeaponPart(0)); 
	public static final RegistryObject<Item> TEST_BIG_GUN = ITEMS.register("test_big_gun", 
			() -> new ItemWeaponPart(1)); 
	
	public static final RegistryObject<Item> TEST_TANK = ITEMS.register("test_fuel_tank", 
			() -> new ItemFuelTank(0));
	public static final RegistryObject<Item> TEST_TANK_2 = ITEMS.register("test_fuel_tank_2", 
			() -> new ItemFuelTank(1));
	
	public static final RegistryObject<Item> TEST_ENGINE = ITEMS.register("test_engine", 
			() -> new ItemEngine(0));
	
	public static final RegistryObject<Item> TEST_PLANE = ITEMS.register("test_plane", 
			() -> new ItemAircraft(ModEntities.TEST_PLANE.get(), "test_plane"));
	public static final RegistryObject<Item> JAVI_PLANE = ITEMS.register("javi_plane", 
			() -> new ItemAircraft(ModEntities.JAVI_PLANE.get(), "javi"));
	public static final RegistryObject<Item> ALEXIS_PLANE = ITEMS.register("alexis_plane", 
			() -> new ItemAircraft(ModEntities.ALEXIS_PLANE.get(), "alexis"));
	public static final RegistryObject<Item> F16 = ITEMS.register("f16", 
			() -> new ItemAircraft(ModEntities.F16.get(), "javi"));
	
}
