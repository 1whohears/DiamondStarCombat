package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.presets.AlexisPresets;
import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.data.aircraft.presets.CarPresets;
import com.onewhohears.dscombat.data.aircraft.presets.HeliPresets;
import com.onewhohears.dscombat.data.aircraft.presets.JaviPresets;
import com.onewhohears.dscombat.data.aircraft.presets.PlanePresets;
import com.onewhohears.dscombat.data.aircraft.presets.SubPresets;
import com.onewhohears.dscombat.data.aircraft.presets.TankPresets;
import com.onewhohears.dscombat.data.parts.BuffData.BuffType;
import com.onewhohears.dscombat.data.parts.EngineData.EngineType;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
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
import com.onewhohears.dscombat.item.ItemParachute;
import com.onewhohears.dscombat.item.ItemPart;
import com.onewhohears.dscombat.item.ItemRadarPart;
import com.onewhohears.dscombat.item.ItemRepairTool;
import com.onewhohears.dscombat.item.ItemSeat;
import com.onewhohears.dscombat.item.ItemTurret;
import com.onewhohears.dscombat.item.ItemWeaponPart;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
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
	
	// DISKS
	
	// IDEA 8.1 Jupiter Missiles and Anadyr from blowback ost
	// IDEA 8.2 disk 911?
	public static final RegistryObject<Item> MISSILE_KNOWS_WHERE_DISC = ITEMS.register("the_missile_knows_disc", 
		() -> new RecordItem(15, () -> ModSounds.MISSILE_KNOWS_WHERE.get(), 
			(new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE), 1980));
	public static final RegistryObject<Item> ORANGE_TESLA_DISC = ITEMS.register("orange_tesla_disc", 
		() -> new RecordItem(14, () -> ModSounds.ORANGE_TESLA.get(), 
			(new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE), 1080));
	
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
			() -> new Item(ItemPart.partProps(8)));
	public static final RegistryObject<Item> ADVANCED_COCKPIT = ITEMS.register("advanced_cockpit", 
			() -> new Item(ItemPart.partProps(8)));
	
	// BUFFS
	public static final RegistryObject<Item> DATA_LINK = ITEMS.register("data_link", 
			() -> new ItemBuffPart(BuffType.DATA_LINK, SlotType.INTERNAL_ADVANCED, 0f));
	public static final RegistryObject<Item> NIGHT_VISION_HUD = ITEMS.register("night_vision_hud", 
			() -> new ItemBuffPart(BuffType.NIGHT_VISION_HUD, SlotType.INTERNAL_ADVANCED, 0f));
	public static final RegistryObject<Item> RADIO = ITEMS.register("radio", 
			() -> new ItemBuffPart(BuffType.RADIO, SlotType.INTERNAL_ALL, 0f));
	public static final RegistryObject<Item> ARMOR_PIECE = ITEMS.register("armor_piece", 
			() -> new ItemBuffPart(BuffType.ARMOR, SlotType.EXTERNAL_ALL, 4f));
	
	// TOOLS
	public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", 
			() -> new ItemRepairTool(20, 5));
	public static final RegistryObject<Item> THICK_WRENCH = ITEMS.register("thick_wrench", 
			() -> new ItemRepairTool(200, 5));
	public static final RegistryObject<Item> PARACHUTE = ITEMS.register("parachute", 
			() -> new ItemParachute());
	
	// CREATIVE WANDS
	public static final RegistryObject<Item> NO_CONSUME_WAND = ITEMS.register("no_consume_wand", 
			() -> new ItemCreativeWand(new String[] {"info.dscombat.no_consume_wand_1"}) {
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
			() -> new ItemFuelTank(1f, 0f, 50f, SlotType.INTERNAL_ALL));
	public static final RegistryObject<Item> HEAVY_FUEL_TANK = ITEMS.register("heavy_fuel_tank", 
			() -> new ItemFuelTank(3f, 0f, 150f, SlotType.INTERNAL_ALL));
	
	// ENGINES
	public static final RegistryObject<Item> C6_ENGINE = ITEMS.register("c6_engine", 
			() -> new ItemEngine(EngineType.SPIN, 1.1f, 0.25f, 4.0f, 
					0.005f, false, SlotType.INTERNAL_ENGINE_SPIN));
	public static final RegistryObject<Item> C12_ENGINE = ITEMS.register("c12_engine", 
			() -> new ItemEngine(EngineType.SPIN, 2.2f, 0.45f, 8.0f, 
					0.011f, false, SlotType.INTERNAL_ENGINE_SPIN));
	public static final RegistryObject<Item> TURBOFAN_F25 = ITEMS.register("turbofan_f25", 
			() -> new ItemEngine(EngineType.PUSH, 1.2f, 0.25f, 4.0f, 
					0.005f, false, SlotType.INTERNAL_ENGINE_PUSH));
	public static final RegistryObject<Item> TURBOFAN_F145 = ITEMS.register("turbofan_f145", 
			() -> new ItemEngine(EngineType.PUSH, 2.5f, 0.46f, 8.0f, 
					0.011f, false, SlotType.INTERNAL_ENGINE_PUSH));
	public static final RegistryObject<Item> TURBOFAN_F39 = ITEMS.register("turbofan_f39", 
			() -> new ItemEngine(EngineType.PUSH, 1.5f, 0.39f, 5.0f, 
					0.007f, false, SlotType.INTERNAL_ENGINE_PUSH));
	public static final RegistryObject<Item> CFM56 = ITEMS.register("cfm56", 
			() -> new ItemEngine(EngineType.PUSH, 3.1f, 0.56f, 9.0f, 
					0.017f, true, SlotType.EXTERNAL_ALL));
	public static final RegistryObject<Item> CM_MANLY_52 = ITEMS.register("cm_manly_52", 
			() -> new ItemEngine(EngineType.PUSH, 0.8f, 0.12f, 2.0f, 
					0.004f, false, SlotType.INTERNAL_ENGINE_RADIAL));
	// TODO 2.2 engines that make sense for boats
	
	// RADARS
	public static final RegistryObject<Item> AR500 = ITEMS.register("ar500", 
			() -> new ItemRadarPart(1f, "ar500", SlotType.INTERNAL_ADVANCED));
	public static final RegistryObject<Item> AR1K = ITEMS.register("ar1k", 
			() -> new ItemRadarPart(1.5f, "ar1k", SlotType.INTERNAL_ADVANCED));
	public static final RegistryObject<Item> AR2K = ITEMS.register("ar2k", 
			() -> new ItemRadarPart(2.0f, "ar2k", SlotType.INTERNAL_ADVANCED));
	public static final RegistryObject<Item> GR200 = ITEMS.register("gr200", 
			() -> new ItemRadarPart(1f, "gr200", SlotType.INTERNAL_ADVANCED));
	public static final RegistryObject<Item> GR400 = ITEMS.register("gr400", 
			() -> new ItemRadarPart(1.5f, "gr400", SlotType.INTERNAL_ADVANCED));
	public static final RegistryObject<Item> WR400 = ITEMS.register("wr400", 
			() -> new ItemRadarPart(1f, "wr400", SlotType.INTERNAL_ADVANCED));
	public static final RegistryObject<Item> WR1K = ITEMS.register("wr1k", 
			() -> new ItemRadarPart(1.6f, "wr1k", SlotType.INTERNAL_ADVANCED));
	public static final RegistryObject<Item> GPR20 = ITEMS.register("gpr20", 
			() -> new ItemRadarPart(3f, "gpr20", SlotType.INTERNAL_ADVANCED));
	public static final RegistryObject<Item> GPR100 = ITEMS.register("gpr100", 
			() -> new ItemRadarPart(3f, "gpr100", SlotType.INTERNAL_ADVANCED));
	public static final RegistryObject<Item> AR20K = ITEMS.register("ar20k", 
			() -> new ItemRadarPart(7f, "ar20k", SlotType.EXTERNAL_HEAVY));
	public static final RegistryObject<Item> AXCEL_TRUCK_RADAR = ITEMS.register("axcel_truck_radar", 
			() -> new ItemRadarPart(3f, "axcel_truck_radar", SlotType.EXTERNAL_ADVANCED));
	// IDEA 2 passive under water sonar doesn's show RWR warning
	public static final RegistryObject<Item> AIR_SCAN_A = ITEMS.register("air_scan_a", 
			() -> new ItemRadarPart(3f, "air_scan_a", SlotType.EXTERNAL_ADVANCED, 
					ModEntities.AIR_SCAN_A.getId().toString()));
	public static final RegistryObject<Item> AIR_SCAN_B = ITEMS.register("air_scan_b", 
			() -> new ItemRadarPart(3f, "air_scan_b", SlotType.EXTERNAL_ADVANCED, 
					ModEntities.AIR_SCAN_B.getId().toString()));
	public static final RegistryObject<Item> SURVEY_ALL_A = ITEMS.register("survey_all_a", 
			() -> new ItemRadarPart(3f, "survey_all_a", SlotType.EXTERNAL_ADVANCED, 
					ModEntities.SURVEY_ALL_A.getId().toString()));
	public static final RegistryObject<Item> SURVEY_ALL_B = ITEMS.register("survey_all_b", 
			() -> new ItemRadarPart(3f, "survey_all_b", SlotType.EXTERNAL_ADVANCED, 
					ModEntities.SURVEY_ALL_B.getId().toString()));
	
	// SEATS
	public static final RegistryObject<Item> SEAT = ITEMS.register("seat", 
			() -> new ItemSeat(0.1f, SlotType.SEAT_ALL));
	
	// TURRENTS
	public static final RegistryObject<Item> MINIGUN_TURRET = ITEMS.register("minigun_turret", 
			() -> new ItemTurret(4f, SlotType.TURRET_LIGHT, 
					ModEntities.MINIGUN_TURRET.getId().toString(), "20mm",
					RotBounds.create(2.5f,50f, 50f)));
	public static final RegistryObject<Item> HEAVY_TANK_TURRET = ITEMS.register("heavy_tank_turret", 
			() -> new ItemTurret(8f, SlotType.TURRET_HEAVY, 
					ModEntities.HEAVY_TANK_TURRET.getId().toString(), "120mmhe",
					RotBounds.create(1.0f, 30f, 30f)));
	public static final RegistryObject<Item> STEVE_UP_SMASH = ITEMS.register("steve_up_smash", 
			() -> new ItemTurret(10f, SlotType.TURRET_MED, 
					ModEntities.STEVE_UP_SMASH.getId().toString(), "aim9p5",
					RotBounds.create(1.8f, 25f, 25f)));
	public static final RegistryObject<Item> SAM_LAUNCHER = ITEMS.register("sam_launcher", 
			() -> new ItemTurret(13f, SlotType.TURRET_HEAVY, 
					ModEntities.SAM_LAUNCHER.getId().toString(), "pac3",
					RotBounds.create(1.3f, 25f, 25f)));
	public static final RegistryObject<Item> MLS = ITEMS.register("mls", 
			() -> new ItemTurret(11f, SlotType.TURRET_HEAVY, 
					ModEntities.MLS.getId().toString(), "rgm84",
					RotBounds.create(1.9f, 20f, 20f)));
	public static final RegistryObject<Item> TORPEDO_TUBES = ITEMS.register("torpedo_tubes", 
			() -> new ItemTurret(10f, SlotType.TURRET_MED, 
					ModEntities.TORPEDO_TUBES.getId().toString(), "torpedo1",
					RotBounds.create(1.6f, 5f, 5f)));
	public static final RegistryObject<Item> AA_TURRET = ITEMS.register("aa_turret", 
			() -> new ItemTurret(2.5f, SlotType.TURRET_LIGHT, 
					ModEntities.AA_TURRET.getId().toString(), "10mm",
					RotBounds.create(3.0f, 30f, 30f)));
	public static final RegistryObject<Item> CIWS = ITEMS.register("ciws", 
			() -> new ItemTurret(4.5f, SlotType.TURRET_MED, 
					ModEntities.CIWS.getId().toString(), "10mm",
					RotBounds.create(2.0f, 75f, 30f)));
	public static final RegistryObject<Item> MARK7_CANNON = ITEMS.register("mark7_cannon", 
			() -> new ItemTurret(11f, SlotType.TURRET_HEAVY, 
					ModEntities.MARK7_CANNON.getId().toString(), "406mmhe",
					RotBounds.create(0.9f, 30f, 15f)));
	public static final RegistryObject<Item> MARK45_CANNON = ITEMS.register("mark45_cannon", 
			() -> new ItemTurret(6f, SlotType.TURRET_MED, 
					ModEntities.MARK45_CANNON.getId().toString(), "127mm",
					RotBounds.create(1.1f, 30f, 15f)));
	
	// FLARE DISPENSERS
	public static final RegistryObject<Item> BASIC_FLARE_DISPENSER = ITEMS.register("basic_flare_dispenser", 
			() -> new ItemFlareDispenser(0.5f, 0, 20, 20.0f, 120, SlotType.INTERNAL_ALL));
	
	// WEAPON PARTS
	public static final RegistryObject<Item> XM12 = ITEMS.register("xm12", 
			() -> new ItemWeaponPart(1f, SlotType.EXTERNAL_ALL, 0)); 
	public static final RegistryObject<Item> LIGHT_MISSILE_RACK = ITEMS.register("light_missile_rack", 
			() -> new ItemWeaponPart(2f, SlotType.EXTERNAL_ALL, 0)); 
	public static final RegistryObject<Item> HEAVY_MISSILE_RACK = ITEMS.register("heavy_missile_rack", 
			() -> new ItemWeaponPart(4f, SlotType.EXTERNAL_ALL, 0)); 
	public static final RegistryObject<Item> BOMB_RACK = ITEMS.register("bomb_rack", 
			() -> new ItemWeaponPart(5f, SlotType.EXTERNAL_ALL, 0));
	public static final RegistryObject<Item> ADL = ITEMS.register("adl", 
			() -> new ItemWeaponPart(7f, SlotType.EXTERNAL_HEAVY, 20)); 
	public static final RegistryObject<Item> VLS = ITEMS.register("vls", 
			() -> new ItemWeaponPart(7f, SlotType.EXTERNAL_HEAVY, 90)); 
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
	public static final RegistryObject<Item> GRUETZ_BUNKER_BUSTER = ITEMS.register("gruetz_bunker_buster", 
			() -> new ItemAmmo(1, "gruetz_bunker_buster")); 
	
	// PLANES
	public static final RegistryObject<Item> JAVI_PLANE = ITEMS.register("javi_plane", 
			() -> new ItemAircraft(ModEntities.JAVI_PLANE.get(), 
					JaviPresets.DEFAULT_JAVI_PLANE));
	public static final RegistryObject<Item> ALEXIS_PLANE = ITEMS.register("alexis_plane", 
			() -> new ItemAircraft(ModEntities.ALEXIS_PLANE.get(), 
					AlexisPresets.DEFAULT_ALEXIS_PLANE));
	public static final RegistryObject<Item> WOODEN_PLANE = ITEMS.register("wooden_plane", 
			() -> new ItemAircraft(ModEntities.WOODEN_PLANE.get(), 
					PlanePresets.DEFAULT_WOODEN_PLANE));
	public static final RegistryObject<Item> E3SENTRY_PLANE = ITEMS.register("e3sentry_plane", 
			() -> new ItemAircraft(ModEntities.E3SENTRY_PLANE.get(), 
					PlanePresets.DEFAULT_E3SENTRY_PLANE));
	
	// HELICOPTERS
	public static final RegistryObject<Item> NOAH_CHOPPER = ITEMS.register("noah_chopper", 
			() -> new ItemAircraft(ModEntities.NOAH_CHOPPER.get(), 
					HeliPresets.DEFAULT_NOAH_CHOPPER));
	
	// CARS
	public static final RegistryObject<Item> ORANGE_TESLA = ITEMS.register("orange_tesla", 
			() -> new ItemAircraft(ModEntities.ORANGE_TESLA.get(), 
					CarPresets.DEFAULT_ORANGE_TESLA));
	public static final RegistryObject<Item> AXCEL_TRUCK = ITEMS.register("axcel_truck", 
			() -> new ItemAircraft(ModEntities.AXCEL_TRUCK.get(), 
					CarPresets.DEFAULT_AXCEL_TRUCK));
	
	// TANKS
	public static final RegistryObject<Item> MRBUDGER_TANK = ITEMS.register("mrbudger_tank", 
			() -> new ItemAircraft(ModEntities.MRBUDGER_TANK.get(), 
					TankPresets.DEFAULT_MRBUDGER_TANK));
	public static final RegistryObject<Item> SMALL_ROLLER = ITEMS.register("small_roller", 
			() -> new ItemAircraft(ModEntities.SMALL_ROLLER.get(), 
					TankPresets.DEFAULT_SMALL_ROLLER));
	
	// BOATS
	public static final RegistryObject<Item> NATHAN_BOAT = ITEMS.register("nathan_boat", 
			() -> new ItemAircraft(ModEntities.NATHAN_BOAT.get(), 
					BoatPresets.DEFAULT_NATHAN_BOAT));
	public static final RegistryObject<Item> GRONK_BATTLESHIP = ITEMS.register("gronk_battleship", 
			() -> new ItemAircraft(ModEntities.GRONK_BATTLESHIP.get(), 
					BoatPresets.DEFAULT_GRONK_BATTLESHIP));
	public static final RegistryObject<Item> DESTROYER = ITEMS.register("destroyer", 
			() -> new ItemAircraft(ModEntities.DESTROYER.get(), 
					BoatPresets.DEFAULT_DESTROYER));
	public static final RegistryObject<Item> CRUISER = ITEMS.register("cruiser", 
			() -> new ItemAircraft(ModEntities.CRUISER.get(), 
					BoatPresets.DEFAULT_CRUISER));
	public static final RegistryObject<Item> CORVETTE = ITEMS.register("corvette", 
			() -> new ItemAircraft(ModEntities.CORVETTE.get(), 
					BoatPresets.DEFAULT_CORVETTE));
	public static final RegistryObject<Item> AIRCRAFT_CARRIER = ITEMS.register("aircraft_carrier", 
			() -> new ItemAircraft(ModEntities.AIRCRAFT_CARRIER.get(), 
					BoatPresets.DEFAULT_AIRCRAFT_CARRIER));
	
	// SUBMARINES
	public static final RegistryObject<Item> ANDOLF_SUB = ITEMS.register("andolf_sub", 
			() -> new ItemAircraft(ModEntities.ANDOLF_SUB.get(), 
					SubPresets.DEFAULT_ANDOLF_SUB));
		
}
