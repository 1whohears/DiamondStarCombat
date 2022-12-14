package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.BuffData.BuffType;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.TurretData.RotBounds;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.item.ItemAircraft;
import com.onewhohears.dscombat.item.ItemAmmo;
import com.onewhohears.dscombat.item.ItemBuffPart;
import com.onewhohears.dscombat.item.ItemCreativeWand;
import com.onewhohears.dscombat.item.ItemEngine;
import com.onewhohears.dscombat.item.ItemFlareDispenser;
import com.onewhohears.dscombat.item.ItemFuelTank;
import com.onewhohears.dscombat.item.ItemGasCan;
import com.onewhohears.dscombat.item.ItemPart;
import com.onewhohears.dscombat.item.ItemRadarPart;
import com.onewhohears.dscombat.item.ItemRepairTool;
import com.onewhohears.dscombat.item.ItemSeat;
import com.onewhohears.dscombat.item.ItemTurret;
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
			WeaponData data = WeaponPresets.weapons.get(i);
			final int size;
			if (data.getMaxAmmo() < 64) size = data.getMaxAmmo();
			else size = 64;
			ITEMS.register(data.getId(), () -> new ItemAmmo(size));
		}
	}
	
	public static final CreativeModeTab PARTS = new CreativeModeTab("parts") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.SEAT.get());
		}
	};
	
	public static final CreativeModeTab WEAPONS = new CreativeModeTab("weapons") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.XM12.get());
		}
	};
	
	public static final CreativeModeTab AIRCRAFT = new CreativeModeTab("aircraft") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.NOAH_CHOPPER.get());
		}
	};
	
	// PARTS
	public static final RegistryObject<Item> TI83 = ITEMS.register("ti83", 
			() -> new Item(ItemPart.partProps(64)));
	public static final RegistryObject<Item> INTEL_PENTIUM = ITEMS.register("intel_pentium", 
			() -> new Item(ItemPart.partProps(64)));
	public static final RegistryObject<Item> INTEL_CORE_I9X = ITEMS.register("intel_core_i9x", 
			() -> new Item(ItemPart.partProps(64)));
	public static final RegistryObject<Item> FUSELAGE = ITEMS.register("fuselage", 
			() -> new Item(ItemPart.partProps(32)));
	public static final RegistryObject<Item> LARGE_FUSELAGE = ITEMS.register("large_fuselage", 
			() -> new Item(ItemPart.partProps(16)));
	public static final RegistryObject<Item> WING = ITEMS.register("wing", 
			() -> new Item(ItemPart.partProps(32)));
	public static final RegistryObject<Item> LARGE_WING = ITEMS.register("large_wing", 
			() -> new Item(ItemPart.partProps(16)));
	public static final RegistryObject<Item> PROPELLER = ITEMS.register("propeller", 
			() -> new Item(ItemPart.partProps(32)));
	public static final RegistryObject<Item> LARGE_PROPELLER = ITEMS.register("large_propeller", 
			() -> new Item(ItemPart.partProps(16)));
	public static final RegistryObject<Item> COCKPIT = ITEMS.register("cockpit", 
			() -> new Item(ItemPart.partProps(4)));
	public static final RegistryObject<Item> ADVANCED_COCKPIT = ITEMS.register("advanced_cockpit", 
			() -> new Item(ItemPart.partProps(1)));
	
	// BUFFS
	public static final RegistryObject<Item> DATA_LINK = ITEMS.register("data_link", 
			() -> new ItemBuffPart(BuffType.DATA_LINK, PartSlot.ADVANCED_INTERNAL));
	
	// TOOLS
	public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", 
			() -> new ItemRepairTool(20, 5));
	public static final RegistryObject<Item> THICK_WRENCH = ITEMS.register("thick_wrench", 
			() -> new ItemRepairTool(200, 5));
	
	// CREATIVE WANDS
	public static final RegistryObject<Item> NO_CONSUME_WAND = ITEMS.register("no_consume_wand", 
			() -> new ItemCreativeWand(new String[] {"dscombat.no_consume_wand_1"}) {
				@Override
				public boolean modifyAircraft(EntityAircraft plane) {
					plane.setNoConsume(true);
					return true;
				}
			});
	
	// GAS CANS
	public static final RegistryObject<Item> GAS_CAN = ITEMS.register("gas_can", 
			() -> new ItemGasCan(25));
	public static final RegistryObject<Item> BIG_GAS_CAN = ITEMS.register("big_gas_can", 
			() -> new ItemGasCan(75));
	public static final RegistryObject<Item> BIG_ASS_CAN = ITEMS.register("big_ass_can", 
			() -> new ItemGasCan(300));
	
	// FUEL TANKS
	public static final RegistryObject<Item> LIGHT_FUEL_TANK = ITEMS.register("light_fuel_tank", 
			() -> new ItemFuelTank(0.003f, 0f, 50f, PartSlot.INTERNAL));
	public static final RegistryObject<Item> HEAVY_FUEL_TANK = ITEMS.register("heavy_fuel_tank", 
			() -> new ItemFuelTank(0.010f, 0f, 150f, PartSlot.INTERNAL));
	
	// ENGINES
	public static final RegistryObject<Item> C6_ENGINE = ITEMS.register("c6_engine", 
			() -> new ItemEngine(0.010f, 0.040f, 4.0f, 0.005f, false, PartSlot.INTERNAL));
	public static final RegistryObject<Item> C12_ENGINE = ITEMS.register("c12_engine", 
			() -> new ItemEngine(0.016f, 0.080f, 8.0f, 0.015f, false, PartSlot.INTERNAL));
	
	// RADARS
	public static final RegistryObject<Item> AR500 = ITEMS.register("ar500", 
			() -> new ItemRadarPart(0.002f, "ar500", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> AR1K = ITEMS.register("ar1k", 
			() -> new ItemRadarPart(0.005f, "ar1k", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> AR2K = ITEMS.register("ar2k", 
			() -> new ItemRadarPart(0.010f, "ar2k", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> GR200 = ITEMS.register("gr200", 
			() -> new ItemRadarPart(0.004f, "gr200", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> GR400 = ITEMS.register("gr400", 
			() -> new ItemRadarPart(0.008f, "gr400", PartSlot.ADVANCED_INTERNAL));
	
	// SEATS
	public static final RegistryObject<Item> SEAT = ITEMS.register("seat", 
			() -> new ItemSeat(0.001f, PartSlot.SEAT));
	
	// TURRENTS
	public static final RegistryObject<Item> MINIGUN_TURRET = ITEMS.register("minigun_turret", 
			() -> new ItemTurret(0.019f, PartSlot.SEAT, 
					ModEntities.MINIGUN_TURRET.getId().toString(), "20mm",
					new RotBounds(2f, -30f, 30f)));
	public static final RegistryObject<Item> HEAVY_TANK_TURRET = ITEMS.register("heavy_tank_turret", 
			() -> new ItemTurret(0.027f, PartSlot.SEAT, 
					ModEntities.HEAVY_TANK_TURRET.getId().toString(), "120mmhe",
					new RotBounds(1.5f, -20f, 20f)));
	public static final RegistryObject<Item> STEVE_UP_SMASH = ITEMS.register("steve_up_smash", 
			() -> new ItemTurret(0.035f, PartSlot.SEAT, 
					ModEntities.STEVE_UP_SMASH.getId().toString(), "aim9p5",
					new RotBounds(1f, -30f, 30f)));
	
	// FLARE DISPENSERS
	public static final RegistryObject<Item> BASIC_FLARE_DISPENSER = ITEMS.register("basic_flare_dispenser", 
			() -> new ItemFlareDispenser(0.010f, 0, 20, 5.0f, 100, PartSlot.INTERNAL));
	
	// WEAPON PARTS
	public static final RegistryObject<Item> XM12 = ITEMS.register("xm12", 
			() -> new ItemWeaponPart(0.003f, "xm12", PartSlot.EXTERNAL)); 
	public static final RegistryObject<Item> LIGHT_MISSILE_RACK = ITEMS.register("light_missile_rack", 
			() -> new ItemWeaponPart(0.004f, "light_missile_rack", PartSlot.EXTERNAL)); 
	public static final RegistryObject<Item> HEAVY_MISSILE_RACK = ITEMS.register("heavy_missile_rack", 
			() -> new ItemWeaponPart(0.008f, "heavy_missile_rack", PartSlot.EXTERNAL)); 
	
	// PLANES
	public static final RegistryObject<Item> JAVI_PLANE = ITEMS.register("javi_plane", 
			() -> new ItemAircraft(ModEntities.JAVI_PLANE.get(), "javi_plane"));
	public static final RegistryObject<Item> ALEXIS_PLANE = ITEMS.register("alexis_plane", 
			() -> new ItemAircraft(ModEntities.ALEXIS_PLANE.get(), "alexis_plane"));
	public static final RegistryObject<Item> NOAH_CHOPPER = ITEMS.register("noah_chopper", 
			() -> new ItemAircraft(ModEntities.NOAH_CHOPPER.get(), "noah_chopper"));
	public static final RegistryObject<Item> MRBUDGER_TANK = ITEMS.register("mrbudger_tank", 
			() -> new ItemAircraft(ModEntities.MRBUDGER_TANK.get(), "mrbudger_tank"));
	public static final RegistryObject<Item> SMALL_ROLLER = ITEMS.register("small_roller", 
			() -> new ItemAircraft(ModEntities.SMALL_ROLLER.get(), "small_roller"));
	
}
