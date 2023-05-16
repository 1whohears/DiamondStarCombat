package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.DefaultAircraftPresets;
import com.onewhohears.dscombat.data.parts.BuffData.BuffType;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.TurretData.RotBounds;
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
		ITEMS.register(eventBus);
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
	public static final RegistryObject<Item> NIGHT_VISION_HUD = ITEMS.register("night_vision_hud", 
			() -> new ItemBuffPart(BuffType.NIGHT_VISION_HUD, PartSlot.ADVANCED_INTERNAL));
	// TODO 9.1 radio part buff right click vehicle with music disk
	
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
			() -> new ItemFuelTank(1f, 0f, 50f, PartSlot.INTERNAL_ALL));
	public static final RegistryObject<Item> HEAVY_FUEL_TANK = ITEMS.register("heavy_fuel_tank", 
			() -> new ItemFuelTank(3f, 0f, 150f, PartSlot.INTERNAL_ALL));
	
	// ENGINES
	/**
	 * TODO 0.5 add more engine types for different vehicles so they make sense
	 * internal and external jet engines
	 * internal "rotational" engines for cars and helicopters
	 * maybe extra external engines can increase the max speed a bit
	 */
	public static final RegistryObject<Item> C6_ENGINE = ITEMS.register("c6_engine", 
			() -> new ItemEngine(1.2f, 9f*0.025f, 4.0f, 
					0.005f, false, PartSlot.INTERNAL_ALL));
	public static final RegistryObject<Item> C12_ENGINE = ITEMS.register("c12_engine", 
			() -> new ItemEngine(2.5f, 18f*0.025f, 8.0f, 
					0.012f, false, PartSlot.INTERNAL_ALL));
	
	// RADARS
	public static final RegistryObject<Item> AR500 = ITEMS.register("ar500", 
			() -> new ItemRadarPart(1f, "ar500", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> AR1K = ITEMS.register("ar1k", 
			() -> new ItemRadarPart(1.5f, "ar1k", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> AR2K = ITEMS.register("ar2k", 
			() -> new ItemRadarPart(2.0f, "ar2k", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> GR200 = ITEMS.register("gr200", 
			() -> new ItemRadarPart(1f, "gr200", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> GR400 = ITEMS.register("gr400", 
			() -> new ItemRadarPart(1.5f, "gr400", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> WR400 = ITEMS.register("wr400", 
			() -> new ItemRadarPart(1f, "wr400", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> WR1K = ITEMS.register("wr1k", 
			() -> new ItemRadarPart(1.6f, "wr1k", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> GPR20 = ITEMS.register("gpr20", 
			() -> new ItemRadarPart(3f, "gpr20", PartSlot.ADVANCED_INTERNAL));
	public static final RegistryObject<Item> AR20K = ITEMS.register("ar20k", 
			() -> new ItemRadarPart(7f, "ar20k", PartSlot.ADVANCED_INTERNAL));
	// IDEA 2 passive under water sonar doesn's show RWR warning
	
	// SEATS
	public static final RegistryObject<Item> SEAT = ITEMS.register("seat", 
			() -> new ItemSeat(0.1f, PartSlot.SEAT_ALL));
	
	// TURRENTS
	public static final RegistryObject<Item> MINIGUN_TURRET = ITEMS.register("minigun_turret", 
			() -> new ItemTurret(5f, PartSlot.TURRET_ALL, 
					ModEntities.MINIGUN_TURRET.getId().toString(), "20mm",
					new RotBounds(2f, -30f, 30f)));
	public static final RegistryObject<Item> HEAVY_TANK_TURRET = ITEMS.register("heavy_tank_turret", 
			() -> new ItemTurret(10f, PartSlot.TURRET_HEAVY, 
					ModEntities.HEAVY_TANK_TURRET.getId().toString(), "120mmhe",
					new RotBounds(1.5f, -20f, 20f)));
	public static final RegistryObject<Item> STEVE_UP_SMASH = ITEMS.register("steve_up_smash", 
			() -> new ItemTurret(15f, PartSlot.TURRET_HEAVY, 
					ModEntities.STEVE_UP_SMASH.getId().toString(), "aim9p5",
					new RotBounds(1f, -30f, 30f)));
	
	// FLARE DISPENSERS
	public static final RegistryObject<Item> BASIC_FLARE_DISPENSER = ITEMS.register("basic_flare_dispenser", 
			() -> new ItemFlareDispenser(0.5f, 0, 20, 20.0f, 120, PartSlot.INTERNAL_ALL));
	
	// WEAPON PARTS
	public static final RegistryObject<Item> XM12 = ITEMS.register("xm12", 
			() -> new ItemWeaponPart(1f, PartSlot.EXTERNAL_ALL)); 
	public static final RegistryObject<Item> LIGHT_MISSILE_RACK = ITEMS.register("light_missile_rack", 
			() -> new ItemWeaponPart(2f, PartSlot.EXTERNAL_ALL)); 
	public static final RegistryObject<Item> HEAVY_MISSILE_RACK = ITEMS.register("heavy_missile_rack", 
			() -> new ItemWeaponPart(4f, PartSlot.EXTERNAL_ALL)); 
	public static final RegistryObject<Item> BOMB_RACK = ITEMS.register("bomb_rack", 
			() -> new ItemWeaponPart(5f, PartSlot.EXTERNAL_ALL)); 
	/**
	 * TODO 2.1 radar jamming weapon
	 * causes victims radar to display random noise
	 * if your radar is strong enough and you get close enough, you stop getting jammed
	 */
	
	// AMMO
	public static final RegistryObject<Item> BULLET = ITEMS.register("bullet", 
			() -> new ItemAmmo(64, "20mm")); 
	public static final RegistryObject<Item> BOMB = ITEMS.register("bomb", 
			() -> new ItemAmmo(64, "anm57")); 
	public static final RegistryObject<Item> TRACK_AIR_MISSILE = ITEMS.register("track_air_missile", 
			() -> new ItemAmmo(8, "aim120b")); 
	public static final RegistryObject<Item> TRACK_GROUND_MISSILE = ITEMS.register("track_ground_missile", 
			() -> new ItemAmmo(8, "agm84e")); 
	public static final RegistryObject<Item> IR_MISSILE = ITEMS.register("ir_missile", 
			() -> new ItemAmmo(8, "aim9p5")); 
	public static final RegistryObject<Item> POS_MISSILE = ITEMS.register("pos_missile", 
			() -> new ItemAmmo(8, "agm114k")); 
	public static final RegistryObject<Item> TORPEDO = ITEMS.register("torpedo", 
			() -> new ItemAmmo(8, "torpedo1")); 
	public static final RegistryObject<Item> ANTIRADAR_MISSILE = ITEMS.register("antiradar_missile", 
			() -> new ItemAmmo(8, "rifel1")); 
	
	public static final RegistryObject<Item> B_20MM = ITEMS.register("20mm", 
			() -> new ItemAmmo(64, "20mm")); 
	public static final RegistryObject<Item> B_50MMHE = ITEMS.register("50mmhe", 
			() -> new ItemAmmo(64, "50mmhe")); 
	public static final RegistryObject<Item> B_120MMHE = ITEMS.register("120mmhe", 
			() -> new ItemAmmo(16, "120mmhe")); 
	public static final RegistryObject<Item> AGM65G = ITEMS.register("agm65g", 
			() -> new ItemAmmo(8, "agm65g")); 
	public static final RegistryObject<Item> AGM65L = ITEMS.register("agm65l", 
			() -> new ItemAmmo(8, "agm65l")); 
	public static final RegistryObject<Item> AGM84E = ITEMS.register("agm84e", 
			() -> new ItemAmmo(8, "agm84e")); 
	public static final RegistryObject<Item> AGM114K = ITEMS.register("agm114k", 
			() -> new ItemAmmo(16, "agm114k")); 
	public static final RegistryObject<Item> AIM7F = ITEMS.register("aim7f", 
			() -> new ItemAmmo(4, "aim7f")); 
	public static final RegistryObject<Item> AIM7MH = ITEMS.register("aim7mh", 
			() -> new ItemAmmo(4, "aim7mh")); 
	public static final RegistryObject<Item> AIM9L = ITEMS.register("aim9l", 
			() -> new ItemAmmo(8, "aim9l")); 
	public static final RegistryObject<Item> AIM9P5 = ITEMS.register("aim9p5", 
			() -> new ItemAmmo(8, "aim9p5")); 
	public static final RegistryObject<Item> AIM9X = ITEMS.register("aim9x", 
			() -> new ItemAmmo(8, "aim9x")); 
	public static final RegistryObject<Item> AIM120B = ITEMS.register("aim120b", 
			() -> new ItemAmmo(4, "aim120b")); 
	public static final RegistryObject<Item> AIM120C = ITEMS.register("aim120c", 
			() -> new ItemAmmo(4, "aim120c")); 
	public static final RegistryObject<Item> TORPEDO1 = ITEMS.register("torpedo1", 
			() -> new ItemAmmo(4, "torpedo1")); 
	public static final RegistryObject<Item> RIFEL1 = ITEMS.register("rifel1", 
			() -> new ItemAmmo(4, "rifel1")); 
	
	// PLANES
	public static final RegistryObject<Item> JAVI_PLANE = ITEMS.register("javi_plane", 
			() -> new ItemAircraft(ModEntities.JAVI_PLANE.get(), 
					DefaultAircraftPresets.DEFAULT_JAVI_PLANE));
	public static final RegistryObject<Item> ALEXIS_PLANE = ITEMS.register("alexis_plane", 
			() -> new ItemAircraft(ModEntities.ALEXIS_PLANE.get(), 
					DefaultAircraftPresets.DEFAULT_ALEXIS_PLANE));
	
	// HELICOPTERS
	public static final RegistryObject<Item> NOAH_CHOPPER = ITEMS.register("noah_chopper", 
			() -> new ItemAircraft(ModEntities.NOAH_CHOPPER.get(), 
					DefaultAircraftPresets.DEFAULT_NOAH_CHOPPER));
	
	// CARS
	public static final RegistryObject<Item> ORANGE_TESLA = ITEMS.register("orange_tesla", 
			() -> new ItemAircraft(ModEntities.ORANGE_TESLA.get(), 
					DefaultAircraftPresets.DEFAULT_ORANGE_TESLA));
	
	// TANKS
	public static final RegistryObject<Item> MRBUDGER_TANK = ITEMS.register("mrbudger_tank", 
			() -> new ItemAircraft(ModEntities.MRBUDGER_TANK.get(), 
					DefaultAircraftPresets.DEFAULT_MRBUDGER_TANK));
	public static final RegistryObject<Item> SMALL_ROLLER = ITEMS.register("small_roller", 
			() -> new ItemAircraft(ModEntities.SMALL_ROLLER.get(), 
					DefaultAircraftPresets.DEFAULT_SMALL_ROLLER));
	
	// BOATS
	public static final RegistryObject<Item> NATHAN_BOAT = ITEMS.register("nathan_boat", 
			() -> new ItemAircraft(ModEntities.NATHAN_BOAT.get(), 
					DefaultAircraftPresets.DEFAULT_NATHAN_BOAT));
	
	// SUBMARINES
	public static final RegistryObject<Item> ANDOLF_SUB = ITEMS.register("andolf_sub", 
			() -> new ItemAircraft(ModEntities.ANDOLF_SUB.get(), 
					DefaultAircraftPresets.DEFAULT_ANDOLF_SUB));
		
}
