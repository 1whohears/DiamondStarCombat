package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.vehicle.presets.boat.BoatPresets;
import com.onewhohears.dscombat.data.vehicle.presets.ground_vehicle.CarPresets;
import com.onewhohears.dscombat.data.vehicle.presets.ground_vehicle.StationaryPresets;
import com.onewhohears.dscombat.data.vehicle.presets.ground_vehicle.TankPresets;
import com.onewhohears.dscombat.data.vehicle.presets.helicopter.KraitChopperPresets;
import com.onewhohears.dscombat.data.vehicle.presets.helicopter.NoahChopperPresets;
import com.onewhohears.dscombat.data.vehicle.presets.plane.*;
import com.onewhohears.dscombat.data.vehicle.presets.submarine.SubPresets;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.item.*;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import org.jetbrains.annotations.NotNull;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            DSCombatMod.MODID, Registry.ITEM_REGISTRY);
	
	public static final CreativeModeTab DSC_ITEMS = new CreativeModeTab(-1, "items") {
		@Override
		public @NotNull ItemStack makeIcon() {
			return new ItemStack(ModItems.WRENCH.get());
		}
	};
	
	public static final CreativeModeTab PARTS = new CreativeModeTab(-1, "parts") {
		@Override
		public @NotNull ItemStack makeIcon() {
			return new ItemStack(ModItems.C12_ENGINE.get());
		}
	};
	
	public static final CreativeModeTab WEAPONS = new CreativeModeTab(-1, "weapons") {
		@Override
		public @NotNull ItemStack makeIcon() {
			return new ItemStack(ModItems.AIM9X.get());
		}
	};
	
	public static final CreativeModeTab WEAPON_PARTS = new CreativeModeTab(-1, "weapon_parts") {
		@Override
		public @NotNull ItemStack makeIcon() {
			return new ItemStack(ModItems.CIWS.get());
		}
	};
	
	public static final CreativeModeTab VEHICLES = new CreativeModeTab(-1, "vehicle") {
		@Override
		public @NotNull ItemStack makeIcon() {
			return new ItemStack(ModItems.JAVI_PLANE.get());
		}
	};
	
	// DISKS
	// IDEA 8.1 Jupiter Missiles and Anadyr from blowback ost
	// IDEA 8.2 disk 911?
	public static final RegistrySupplier<Item> MISSILE_KNOWS_WHERE_DISC = ITEMS.register("the_missile_knows_disc", 
		() -> new RecordItem(15, ModSounds.MISSILE_KNOWS_WHERE,
			(new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE), 1980));
	public static final RegistrySupplier<Item> ORANGE_TESLA_DISC = ITEMS.register("orange_tesla_disc", 
		() -> new RecordItem(14, ModSounds.ORANGE_TESLA,
			(new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE), 1080));
	
	// MATERIALS
	public static final RegistrySupplier<Item> RAW_ALUMINUM = ITEMS.register("raw_aluminum", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> ALUMINUM_INGOT = ITEMS.register("aluminum_ingot", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> COMPRESSED_FOSSIL = ITEMS.register("compressed_fossil", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> OIL_BUCKET = ITEMS.register("oil_bucket", 
			() -> new BucketItem(ModFluids.OIL_FLUID_SOURCE.get(),
					ItemPart.itemProps(1).craftRemainder(Items.BUCKET)));
	
	// PARTS
	public static final RegistrySupplier<Item> TI83 = ITEMS.register("ti83", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> INTEL_PENTIUM = ITEMS.register("intel_pentium", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> INTEL_CORE_I9X = ITEMS.register("intel_core_i9x", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> WHEEL = ITEMS.register("wheel", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_WHEEL = ITEMS.register("large_wheel", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> TANK_TRACK = ITEMS.register("tank_track", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> FUSELAGE = ITEMS.register("fuselage", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_FUSELAGE = ITEMS.register("large_fuselage", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> WING = ITEMS.register("wing", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_WING = ITEMS.register("large_wing", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> PROPELLER = ITEMS.register("propeller", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_PROPELLER = ITEMS.register("large_propeller", 
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> COCKPIT = ITEMS.register("cockpit", 
			() -> new Item(ItemPart.itemProps(16)));
	public static final RegistrySupplier<Item> ADVANCED_COCKPIT = ITEMS.register("advanced_cockpit", 
			() -> new Item(ItemPart.itemProps(16)));
	
	// TOOLS
	public static final RegistrySupplier<Item> WRENCH = ITEMS.register("wrench", 
			() -> new ItemRepairTool(20, 5));
	public static final RegistrySupplier<Item> THICK_WRENCH = ITEMS.register("thick_wrench", 
			() -> new ItemRepairTool(200, 5));
	public static final RegistrySupplier<Item> PARACHUTE = ITEMS.register("parachute",
            ItemParachute::new);
	public static final RegistrySupplier<Item> SPRAYCAN = ITEMS.register("spraycan", 
			() -> new Item(new Item.Properties().tab(ModItems.DSC_ITEMS).stacksTo(1)));
	public static final RegistrySupplier<Item> TICKET_BOOK = ITEMS.register("ticket_book",
            ItemTicketBook::new);
	
	// CREATIVE WANDS
	public static final RegistrySupplier<Item> NO_CONSUME_WAND = ITEMS.register("no_consume_wand", 
			() -> new ItemCreativeWand(vehicle -> vehicle.setNoConsume(true), 
					"info.dscombat.no_consume_wand_1"));
	public static final RegistrySupplier<Item> INSTANT_REPAIR_WAND = ITEMS.register("instant_repair_wand", 
			() -> new ItemCreativeWand(EntityVehicle::repairAll,
					"info.dscombat.instant_repair_wand_1"));
	public static final RegistrySupplier<Item> REFILL_WEAPONS_WAND = ITEMS.register("refill_weapons_wand", 
			() -> new ItemCreativeWand(EntityVehicle::refillAllWeapons,
					"info.dscombat.refill_weapons_wand_1"));
	public static final RegistrySupplier<Item> REFILL_FUEL_WAND = ITEMS.register("refill_fuel_wand", 
			() -> new ItemCreativeWand(EntityVehicle::refillFuel,
					"info.dscombat.refill_fuel_wand_1"));
	public static final RegistrySupplier<Item> REFILL_ALL_WAND = ITEMS.register("refill_all_wand", 
			() -> new ItemCreativeWand(EntityVehicle::refillAll,
					"info.dscombat.refill_all_wand_1"));
	
	// GAS CANS
	public static final RegistrySupplier<Item> GAS_CAN = ITEMS.register("gas_can", 
			() -> new ItemGasCan(50));
	public static final RegistrySupplier<Item> BIG_GAS_CAN = ITEMS.register("big_gas_can", 
			() -> new ItemGasCan(150));
	public static final RegistrySupplier<Item> BIG_ASS_CAN = ITEMS.register("big_ass_can", 
			() -> new ItemGasCan(600));
	
	// BUFFS
	public static final RegistrySupplier<ItemPart> DATA_LINK = ITEMS.register("data_link", 
			() -> new ItemPart(64));
	public static final RegistrySupplier<ItemPart> NIGHT_VISION_HUD = ITEMS.register("night_vision_hud", 
			() -> new ItemPart(64));
	public static final RegistrySupplier<ItemPart> RADIO = ITEMS.register("radio", 
			() -> new ItemPart(64));
	public static final RegistrySupplier<ItemPart> ARMOR_PIECE = ITEMS.register("armor_piece", 
			() -> new ItemPart(64));
	
	// GIMBALS
	public static final RegistrySupplier<ItemPart> GIMBAL_CAMERA = ITEMS.register("gimbal_camera", 
			() -> new ItemPart(16));
	
	// CHAIN HOOK
	public static final RegistrySupplier<ItemPart> CHAIN_HOOK = ITEMS.register("chain_hook", 
			() -> new ItemPart(16));
	
	// STORAGE BOXES
	public static final RegistrySupplier<ItemPart> SMALL_STORAGE_BOX = ITEMS.register("small_storage_box", 
			() -> new ItemStorageBox(1)); 
	public static final RegistrySupplier<ItemPart> MED_STORAGE_BOX = ITEMS.register("medium_storage_box", 
			() -> new ItemStorageBox(1));
	public static final RegistrySupplier<ItemPart> LARGE_STORAGE_BOX = ITEMS.register("large_storage_box", 
			() -> new ItemStorageBox(1));
	
	// FUEL TANKS
	public static final RegistrySupplier<ItemPart> LIGHT_FUEL_TANK = ITEMS.register("light_fuel_tank", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> HEAVY_FUEL_TANK = ITEMS.register("heavy_fuel_tank", 
			() -> new ItemPart(16));
	
	// ENGINES
	public static final RegistrySupplier<ItemPart> C6_ENGINE = ITEMS.register("c6_engine", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> C12_ENGINE = ITEMS.register("c12_engine", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> TURBOFAN_F25 = ITEMS.register("turbofan_f25", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> TURBOFAN_F145 = ITEMS.register("turbofan_f145", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> TURBOFAN_F39 = ITEMS.register("turbofan_f39", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> KLIMOV_RD33 = ITEMS.register("klimov_rd33", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> CM_MANLY_52 = ITEMS.register("cm_manly_52", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> ALLISON_V_1710 = ITEMS.register("allison_v_1710", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> COMPOUND_TURBINE = ITEMS.register("compound_turbine", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> CFM56 = ITEMS.register("cfm56", 
			() -> new ItemPart(16));
	
	// RADARS
	public static final RegistrySupplier<ItemPart> AR500 = ITEMS.register("ar500", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AR1K = ITEMS.register("ar1k", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AR2K = ITEMS.register("ar2k", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GR200 = ITEMS.register("gr200", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GR400 = ITEMS.register("gr400", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> WR400 = ITEMS.register("wr400", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> WR1K = ITEMS.register("wr1k", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GPR20 = ITEMS.register("gpr20", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GPR100 = ITEMS.register("gpr100", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AR20K = ITEMS.register("ar20k", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AXCEL_TRUCK_RADAR = ITEMS.register("axcel_truck_radar", 
			() -> new ItemPart(16));
	// IDEA 2 passive under water sonar doesn's show RWR warning
	public static final RegistrySupplier<ItemPart> AIR_SCAN_A = ITEMS.register("air_scan_a", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AIR_SCAN_B = ITEMS.register("air_scan_b", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> SURVEY_ALL_A = ITEMS.register("survey_all_a", 
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> SURVEY_ALL_B = ITEMS.register("survey_all_b", 
			() -> new ItemPart(16));
	
	// SEATS
	public static final RegistrySupplier<ItemPart> SEAT = ITEMS.register("seat", 
			() -> new ItemSeat(64));
	
	// TURRETS
	public static final RegistrySupplier<ItemPart> TURRET = ITEMS.register("turret",
			() -> new ItemTurret(16, "aa_turret"));
	public static final RegistrySupplier<ItemPart> AA_TURRET = ITEMS.register("aa_turret", 
			() -> new ItemTurret(16, "aa_turret"));
	public static final RegistrySupplier<ItemPart> MINIGUN_TURRET = ITEMS.register("minigun_turret", 
			() -> new ItemTurret(16, "minigun_turret"));
	public static final RegistrySupplier<ItemPart> CIWS = ITEMS.register("ciws", 
			() -> new ItemTurret(16, "ciws"));
	public static final RegistrySupplier<ItemPart> MARK45_CANNON = ITEMS.register("mark45_cannon", 
			() -> new ItemTurret(16, "mark45_cannon"));
	public static final RegistrySupplier<ItemPart> HEAVY_TANK_TURRET = ITEMS.register("heavy_tank_turret", 
			() -> new ItemTurret(16, "heavy_tank_turret"));
	public static final RegistrySupplier<ItemPart> MARK7_CANNON = ITEMS.register("mark7_cannon", 
			() -> new ItemTurret(16, "mark7_cannon"));
	public static final RegistrySupplier<ItemPart> STEVE_UP_SMASH = ITEMS.register("steve_up_smash", 
			() -> new ItemTurret(16, "steve_up_smash"));
	public static final RegistrySupplier<ItemPart> SAM_LAUNCHER = ITEMS.register("sam_launcher", 
			() -> new ItemTurret(16, "sam_launcher"));
	public static final RegistrySupplier<ItemPart> TORPEDO_TUBES = ITEMS.register("torpedo_tubes", 
			() -> new ItemTurret(16, "torpedo_tubes"));
	public static final RegistrySupplier<ItemPart> MLS = ITEMS.register("mls", 
			() -> new ItemTurret(16, "mls"));
	public static final RegistrySupplier<ItemPart> MLRS = ITEMS.register("mlrs",
			() -> new ItemTurret(16, "mlrs"));
	public static final RegistrySupplier<ItemPart> ARTILLERY_CANNON = ITEMS.register("artillery_cannon",
			() -> new ItemTurret(16, "artillery_cannon"));
	
	// FLARE DISPENSERS
	public static final RegistrySupplier<ItemPart> BASIC_FLARE_DISPENSER = ITEMS.register("basic_flare_dispenser", 
			() -> new ItemPart(16));
	
	// WEAPON PARTS
	public static final RegistrySupplier<ItemPart> EXTERNAL_WEAPON_PART = ITEMS.register("external_weapon_part",
			() -> new ItemWeaponPart(16, "xm12"));
	public static final RegistrySupplier<ItemPart> XM12 = ITEMS.register("xm12", 
			() -> new ItemWeaponPart(16, "xm12"));
	public static final RegistrySupplier<ItemPart> INTERNAL_GUN = ITEMS.register("internal_gun", 
			() -> new ItemWeaponPart(16, "internal_gun"));
	public static final RegistrySupplier<ItemPart> LIGHT_MISSILE_RACK = ITEMS.register("light_missile_rack", 
			() -> new ItemWeaponPart(16, "light_missile_rack"));
	public static final RegistrySupplier<ItemPart> HEAVY_MISSILE_RACK = ITEMS.register("heavy_missile_rack", 
			() -> new ItemWeaponPart(16, "heavy_missile_rack"));
	public static final RegistrySupplier<ItemPart> BOMB_RACK = ITEMS.register("bomb_rack", 
			() -> new ItemWeaponPart(16, "bomb_rack"));
	public static final RegistrySupplier<ItemPart> ADL = ITEMS.register("adl", 
			() -> new ItemWeaponPart(16, "adl"));
	public static final RegistrySupplier<ItemPart> VLS = ITEMS.register("vls", 
			() -> new ItemWeaponPart(16, "vls"));
	/**
	 * TODO 2.1 radar jamming weapon
	 * causes victims radar to display random noise
	 * if your radar is strong enough and you get close enough, you stop getting jammed
	 */
	
	// AMMO
	public static final RegistrySupplier<Item> AMMO = ITEMS.register("ammo",
			() -> new ItemAmmo(64, "20mm"));
	public static final RegistrySupplier<Item> BULLET = ITEMS.register("bullet", 
			() -> new ItemAmmo(64, "20mm")); 
	public static final RegistrySupplier<Item> BOMB = ITEMS.register("bomb", 
			() -> new ItemAmmo(64, "anm57"));
	public static final RegistrySupplier<Item> MISSILE = ITEMS.register("missile",
			() -> new ItemAmmo(16, "agm114k"));
	public static final RegistrySupplier<Item> TRACK_AIR_MISSILE = ITEMS.register("track_air_missile", 
			() -> new ItemAmmo(16, "aim120b")); 
	public static final RegistrySupplier<Item> TRACK_GROUND_MISSILE = ITEMS.register("track_ground_missile", 
			() -> new ItemAmmo(16, "agm84e")); 
	public static final RegistrySupplier<Item> IR_MISSILE = ITEMS.register("ir_missile", 
			() -> new ItemAmmo(16, "aim9p5")); 
	public static final RegistrySupplier<Item> POS_MISSILE = ITEMS.register("pos_missile", 
			() -> new ItemAmmo(16, "agm114k")); 
	public static final RegistrySupplier<Item> TORPEDO = ITEMS.register("torpedo", 
			() -> new ItemAmmo(16, "mk13")); 
	public static final RegistrySupplier<Item> ANTIRADAR_MISSILE = ITEMS.register("antiradar_missile", 
			() -> new ItemAmmo(16, "agm88g")); 
	
	public static final RegistrySupplier<Item> B_20MM = ITEMS.register("20mm", 
			() -> new ItemAmmo(64, "20mm")); 
	public static final RegistrySupplier<Item> B_50MMHE = ITEMS.register("50mmhe", 
			() -> new ItemAmmo(64, "50mmhe")); 
	public static final RegistrySupplier<Item> B_120MMHE = ITEMS.register("120mmhe", 
			() -> new ItemAmmo(16, "120mmhe")); 
	public static final RegistrySupplier<Item> AGM65G = ITEMS.register("agm65g", 
			() -> new ItemAmmo(16, "agm65g")); 
	public static final RegistrySupplier<Item> AGM65L = ITEMS.register("agm65l", 
			() -> new ItemAmmo(16, "agm65l")); 
	public static final RegistrySupplier<Item> AGM84E = ITEMS.register("agm84e", 
			() -> new ItemAmmo(16, "agm84e")); 
	public static final RegistrySupplier<Item> AGM114K = ITEMS.register("agm114k", 
			() -> new ItemAmmo(16, "agm114k")); 
	public static final RegistrySupplier<Item> AIM7F = ITEMS.register("aim7f", 
			() -> new ItemAmmo(16, "aim7f")); 
	public static final RegistrySupplier<Item> AIM7MH = ITEMS.register("aim7mh", 
			() -> new ItemAmmo(16, "aim7mh")); 
	public static final RegistrySupplier<Item> AIM9L = ITEMS.register("aim9l", 
			() -> new ItemAmmo(16, "aim9l")); 
	public static final RegistrySupplier<Item> AIM9P5 = ITEMS.register("aim9p5", 
			() -> new ItemAmmo(16, "aim9p5")); 
	public static final RegistrySupplier<Item> AIM9X = ITEMS.register("aim9x", 
			() -> new ItemAmmo(16, "aim9x")); 
	public static final RegistrySupplier<Item> AIM120B = ITEMS.register("aim120b", 
			() -> new ItemAmmo(16, "aim120b")); 
	public static final RegistrySupplier<Item> AIM120C = ITEMS.register("aim120c", 
			() -> new ItemAmmo(16, "aim120c")); 
	public static final RegistrySupplier<Item> TORPEDO1 = ITEMS.register("torpedo1", 
			() -> new ItemAmmo(16, "mk13")); 
	public static final RegistrySupplier<Item> RIFEL1 = ITEMS.register("rifel1", 
			() -> new ItemAmmo(16, "agm88g")); 
	public static final RegistrySupplier<Item> GRUETZ_BUNKER_BUSTER = ITEMS.register("gruetz_bunker_buster", 
			() -> new ItemAmmo(16, "gruetz_bunker_buster")); 
	public static final RegistrySupplier<Item> MK13 = ITEMS.register("mk13", 
			() -> new ItemAmmo(16, "mk13")); 
	public static final RegistrySupplier<Item> AGM88G = ITEMS.register("agm88g", 
			() -> new ItemAmmo(16, "agm88g")); 

	// VEHICLE
	public static final RegistrySupplier<Item> VEHICLE = ITEMS.register("vehicle",
			() -> new ItemVehicle(TankPresets.UNARMED_SMALL_ROLLER.getId()));

	// PLANES
	public static final RegistrySupplier<Item> JAVI_PLANE = ITEMS.register("javi_plane", 
			() -> new ItemVehicle(JaviPresets.DEFAULT_JAVI_PLANE.getId()));
	public static final RegistrySupplier<Item> ALEXIS_PLANE = ITEMS.register("alexis_plane", 
			() -> new ItemVehicle(AlexisPresets.DEFAULT_ALEXIS_PLANE.getId()));
	public static final RegistrySupplier<Item> WOODEN_PLANE = ITEMS.register("wooden_plane", 
			() -> new ItemVehicle(PlanePresets.DEFAULT_WOODEN_PLANE.getId()));
	public static final RegistrySupplier<Item> E3SENTRY_PLANE = ITEMS.register("e3sentry_plane", 
			() -> new ItemVehicle(PlanePresets.DEFAULT_E3SENTRY_PLANE.getId()));
	public static final RegistrySupplier<Item> BRONCO_PLANE = ITEMS.register("bronco_plane", 
			() -> new ItemVehicle(BroncoPresets.DEFAULT_BRONCO_PLANE.getId()));
	public static final RegistrySupplier<Item> FELIX_PLANE = ITEMS.register("felix_plane", 
			() -> new ItemVehicle(FelixPresets.DEFAULT_FELIX_PLANE.getId()));
	public static final RegistrySupplier<Item> JASON_PLANE = ITEMS.register("jason_plane", 
			() -> new ItemVehicle(JasonPresets.DEFAULT_JASON_PLANE.getId()));
	public static final RegistrySupplier<Item> EDEN_PLANE = ITEMS.register("eden_plane", 
			() -> new ItemVehicle(EdenPresets.DEFAULT_EDEN_PLANE.getId()));
	public static final RegistrySupplier<Item> JAMES_WOODEN_PLANE = ITEMS.register("james_wooden_plane",
			() -> new ItemVehicle(JamesPresets.DEFAULT_JAMES_PLANE.getId()));
	
	// HELICOPTERS
	public static final RegistrySupplier<Item> NOAH_CHOPPER = ITEMS.register("noah_chopper", 
			() -> new ItemVehicle(NoahChopperPresets.DEFAULT_NOAH_CHOPPER.getId()));
	public static final RegistrySupplier<Item> KRAIT_CHOPPER = ITEMS.register("krait_chopper",
			() -> new ItemVehicle(KraitChopperPresets.DEFAULT_KRAIT_CHOPPER.getId()));
	
	// CARS
	public static final RegistrySupplier<Item> ORANGE_TESLA = ITEMS.register("orange_tesla", 
			() -> new ItemVehicle(CarPresets.DEFAULT_ORANGE_TESLA.getId()));
	public static final RegistrySupplier<Item> AXCEL_TRUCK = ITEMS.register("axcel_truck", 
			() -> new ItemVehicle(CarPresets.DEFAULT_AXCEL_TRUCK.getId()));
	public static final RegistrySupplier<Item> ERIC_TRUCK = ITEMS.register("eric_truck",
			() -> new ItemVehicle(CarPresets.DEFAULT_ERIC_TRUCK.getId()));
	
	// TANKS
	public static final RegistrySupplier<Item> MRBUDGER_TANK = ITEMS.register("mrbudger_tank", 
			() -> new ItemVehicle(TankPresets.DEFAULT_MRBUDGER_TANK.getId()));
	public static final RegistrySupplier<Item> SMALL_ROLLER = ITEMS.register("small_roller", 
			() -> new ItemVehicle(TankPresets.DEFAULT_SMALL_ROLLER.getId()));
	
	// BOATS
	public static final RegistrySupplier<Item> NATHAN_BOAT = ITEMS.register("nathan_boat", 
			() -> new ItemVehicle(BoatPresets.DEFAULT_NATHAN_BOAT.getId()));
	public static final RegistrySupplier<Item> GRONK_BATTLESHIP = ITEMS.register("gronk_battleship", 
			() -> new ItemVehicle(BoatPresets.DEFAULT_GRONK_BATTLESHIP.getId()));
	public static final RegistrySupplier<Item> DESTROYER = ITEMS.register("destroyer", 
			() -> new ItemVehicle(BoatPresets.DEFAULT_DESTROYER.getId()));
	public static final RegistrySupplier<Item> CRUISER = ITEMS.register("cruiser", 
			() -> new ItemVehicle(BoatPresets.DEFAULT_CRUISER.getId()));
	public static final RegistrySupplier<Item> CORVETTE = ITEMS.register("corvette", 
			() -> new ItemVehicle(BoatPresets.DEFAULT_CORVETTE.getId()));
	public static final RegistrySupplier<Item> AIRCRAFT_CARRIER = ITEMS.register("aircraft_carrier", 
			() -> new ItemVehicle(BoatPresets.DEFAULT_AIRCRAFT_CARRIER.getId()));
	
	// SUBMARINES
	public static final RegistrySupplier<Item> ANDOLF_SUB = ITEMS.register("andolf_sub", 
			() -> new ItemVehicle(SubPresets.DEFAULT_ANDOLF_SUB.getId()));
	public static final RegistrySupplier<Item> GOOGLE_SUB = ITEMS.register("google_sub", 
			() -> new ItemVehicle(SubPresets.DEFAULT_GOOGLE_SUB.getId()));

	// STATIONARY
	public static final RegistrySupplier<Item> EWR4000 = ITEMS.register("ewr4000",
			() -> new ItemVehicle(StationaryPresets.EWR4000.getId()));

    public static void register() {
        ITEMS.register();
    }
}
