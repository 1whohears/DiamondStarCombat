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
import dev.architectury.core.item.ArchitecturyBucketItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;

import java.util.function.Supplier;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            DSCombatMod.MODID, Registries.ITEM);

    public static <R extends Item> RegistrySupplier<R> registerItem(String id, Supplier<? extends R> item) {
        return ITEMS.register(id, item);
    }

	// DISCS
	// IDEA 8.1 Jupiter Missiles and Anadyr from blowback ost
	// IDEA 8.2 disk 911?
	public static final RegistrySupplier<Item> MISSILE_KNOWS_WHERE_DISC = registerItem(
            "the_missile_knows_disc",
            () -> new RecordItem(15, ModSounds.MISSILE_KNOWS_WHERE,
                    (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE)
                            .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES), 1980));
	public static final RegistrySupplier<Item> ORANGE_TESLA_DISC = registerItem(
            "orange_tesla_disc",
            () -> new RecordItem(14, ModSounds.ORANGE_TESLA,
                    (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE)
                            .arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES), 1080));
	
	// MATERIALS
	public static final RegistrySupplier<Item> RAW_ALUMINUM = registerItem("raw_aluminum",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> ALUMINUM_INGOT = registerItem("aluminum_ingot",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> COMPRESSED_FOSSIL = registerItem("compressed_fossil",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> OIL_BUCKET = registerItem("oil_bucket",
			() -> new ArchitecturyBucketItem(ModFluids.getOilFluidSource(),
					ItemPart.itemProps(1).craftRemainder(Items.BUCKET)));
	
	// PARTS
	public static final RegistrySupplier<Item> TI83 = registerItem("ti83",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> INTEL_PENTIUM = registerItem("intel_pentium",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> INTEL_CORE_I9X = registerItem("intel_core_i9x",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> WHEEL = registerItem("wheel",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_WHEEL = registerItem("large_wheel",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> TANK_TRACK = registerItem("tank_track",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> FUSELAGE = registerItem("fuselage",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_FUSELAGE = registerItem("large_fuselage",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> WING = registerItem("wing",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_WING = registerItem("large_wing",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> PROPELLER = registerItem("propeller",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_PROPELLER = registerItem("large_propeller",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> COCKPIT = registerItem("cockpit",
			() -> new Item(ItemPart.itemProps(16)));
	public static final RegistrySupplier<Item> ADVANCED_COCKPIT = registerItem("advanced_cockpit",
			() -> new Item(ItemPart.itemProps(16)));
	
	// TOOLS
	public static final RegistrySupplier<Item> WRENCH = registerItem("wrench",
			() -> new ItemRepairTool(20, 5));
	public static final RegistrySupplier<Item> THICK_WRENCH = registerItem("thick_wrench",
			() -> new ItemRepairTool(200, 5));
	public static final RegistrySupplier<Item> PARACHUTE = registerItem("parachute",
            ItemParachute::new);
	public static final RegistrySupplier<Item> SPRAYCAN = registerItem("spraycan",
			() -> new Item(new Item.Properties().stacksTo(1).arch$tab(ModCMTabs.DSC_ITEMS)));
	public static final RegistrySupplier<Item> TICKET_BOOK = registerItem("ticket_book",
            ItemTicketBook::new);
	
	// CREATIVE WANDS
	public static final RegistrySupplier<Item> NO_CONSUME_WAND = registerItem("no_consume_wand",
			() -> new ItemCreativeWand(vehicle -> vehicle.setNoConsume(true), 
					"info.dscombat.no_consume_wand_1"));
	public static final RegistrySupplier<Item> INSTANT_REPAIR_WAND = registerItem("instant_repair_wand",
			() -> new ItemCreativeWand(EntityVehicle::repairAll,
					"info.dscombat.instant_repair_wand_1"));
	public static final RegistrySupplier<Item> REFILL_WEAPONS_WAND = registerItem("refill_weapons_wand",
			() -> new ItemCreativeWand(EntityVehicle::refillAllWeapons,
					"info.dscombat.refill_weapons_wand_1"));
	public static final RegistrySupplier<Item> REFILL_FUEL_WAND = registerItem("refill_fuel_wand",
			() -> new ItemCreativeWand(EntityVehicle::refillFuel,
					"info.dscombat.refill_fuel_wand_1"));
	public static final RegistrySupplier<Item> REFILL_ALL_WAND = registerItem("refill_all_wand",
			() -> new ItemCreativeWand(EntityVehicle::refillAll,
					"info.dscombat.refill_all_wand_1"));
	
	// GAS CANS
	public static final RegistrySupplier<Item> GAS_CAN = registerItem("gas_can",
			() -> new ItemGasCan(50));
	public static final RegistrySupplier<Item> BIG_GAS_CAN = registerItem("big_gas_can",
			() -> new ItemGasCan(150));
	public static final RegistrySupplier<Item> BIG_ASS_CAN = registerItem("big_ass_can",
			() -> new ItemGasCan(600));
	
	// BUFFS
	public static final RegistrySupplier<ItemPart> DATA_LINK = registerItem("data_link",
			() -> new ItemPart(64));
	public static final RegistrySupplier<ItemPart> NIGHT_VISION_HUD = registerItem("night_vision_hud",
			() -> new ItemPart(64));
	public static final RegistrySupplier<ItemPart> RADIO = registerItem("radio",
			() -> new ItemPart(64));
	public static final RegistrySupplier<ItemPart> ARMOR_PIECE = registerItem("armor_piece",
			() -> new ItemPart(64));
	
	// GIMBALS
	public static final RegistrySupplier<ItemPart> GIMBAL_CAMERA = registerItem("gimbal_camera",
			() -> new ItemPart(16));
	
	// CHAIN HOOK
	public static final RegistrySupplier<ItemPart> CHAIN_HOOK = registerItem("chain_hook",
			() -> ItemExternalPart.create(16, "chain_hook"));
	
	// STORAGE BOXES
	public static final RegistrySupplier<ItemPart> SMALL_STORAGE_BOX = registerItem("small_storage_box",
			() -> new ItemStorageBox(1)); 
	public static final RegistrySupplier<ItemPart> MED_STORAGE_BOX = registerItem("medium_storage_box",
			() -> new ItemStorageBox(1));
	public static final RegistrySupplier<ItemPart> LARGE_STORAGE_BOX = registerItem("large_storage_box",
			() -> new ItemStorageBox(1));
	
	// FUEL TANKS
	public static final RegistrySupplier<ItemPart> LIGHT_FUEL_TANK = registerItem("light_fuel_tank",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> HEAVY_FUEL_TANK = registerItem("heavy_fuel_tank",
			() -> new ItemPart(16));
    public static final RegistrySupplier<ItemPart> LIGHT_EXTERNAL_FUEL_TANK = registerItem("light_external_fuel_tank",
            () -> ItemExternalPart.create(16, "light_external_fuel_tank"));
    public static final RegistrySupplier<ItemPart> HEAVY_EXTERNAL_FUEL_TANK = registerItem("heavy_external_fuel_tank",
            () -> ItemExternalPart.create(16, "heavy_external_fuel_tank"));


    // ENGINES
	public static final RegistrySupplier<ItemPart> C6_ENGINE = registerItem("c6_engine",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> C12_ENGINE = registerItem("c12_engine",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> TURBOFAN_F25 = registerItem("turbofan_f25",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> TURBOFAN_F145 = registerItem("turbofan_f145",
			() -> ItemExternalPart.create(16, "turbofan_f145"));
	public static final RegistrySupplier<ItemPart> TURBOFAN_F39 = registerItem("turbofan_f39",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> KLIMOV_RD33 = registerItem("klimov_rd33",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> CM_MANLY_52 = registerItem("cm_manly_52",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> ALLISON_V_1710 = registerItem("allison_v_1710",
			() -> ItemExternalPart.create(16, "allison_v_1710"));
	public static final RegistrySupplier<ItemPart> COMPOUND_TURBINE = registerItem("compound_turbine",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> CFM56 = registerItem("cfm56",
			() -> new ItemPart(16));
	
	// RADARS
	public static final RegistrySupplier<ItemPart> AR500 = registerItem("ar500",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AR1K = registerItem("ar1k",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AR2K = registerItem("ar2k",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GR200 = registerItem("gr200",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GR400 = registerItem("gr400",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> WR400 = registerItem("wr400",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> WR1K = registerItem("wr1k",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GPR20 = registerItem("gpr20",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GPR100 = registerItem("gpr100",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AR20K = registerItem("ar20k",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AXCEL_TRUCK_RADAR = registerItem("axcel_truck_radar",
			() -> new ItemPart(16));
	// IDEA 2 passive under water sonar doesn's show RWR warning
	public static final RegistrySupplier<ItemPart> AIR_SCAN_A = registerItem("air_scan_a",
			() -> ItemExternalPart.create(16, "air_scan_a"));
	public static final RegistrySupplier<ItemPart> AIR_SCAN_B = registerItem("air_scan_b",
			() -> ItemExternalPart.create(16, "air_scan_b"));
	public static final RegistrySupplier<ItemPart> SURVEY_ALL_A = registerItem("survey_all_a",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> SURVEY_ALL_B = registerItem("survey_all_b",
			() -> ItemExternalPart.create(16, "survey_all_b"));
	
	// SEATS
	public static final RegistrySupplier<ItemPart> SEAT = registerItem("seat",
			() -> new ItemSeat(64));
	
	// TURRETS
	public static final RegistrySupplier<ItemPart> TURRET = registerItem("turret",
			() -> ItemTurret.create(16, "aa_turret"));
	public static final RegistrySupplier<ItemPart> AA_TURRET = registerItem("aa_turret",
			() -> ItemTurret.create(16, "aa_turret"));
	public static final RegistrySupplier<ItemPart> MINIGUN_TURRET = registerItem("minigun_turret",
			() -> ItemTurret.create(16, "minigun_turret"));
	public static final RegistrySupplier<ItemPart> CIWS = registerItem("ciws",
			() -> ItemTurret.create(16, "ciws"));
	public static final RegistrySupplier<ItemPart> MARK45_CANNON = registerItem("mark45_cannon",
			() -> ItemTurret.create(16, "mark45_cannon"));
	public static final RegistrySupplier<ItemPart> HEAVY_TANK_TURRET = registerItem("heavy_tank_turret",
			() -> ItemTurret.create(16, "heavy_tank_turret"));
	public static final RegistrySupplier<ItemPart> MARK7_CANNON = registerItem("mark7_cannon",
			() -> ItemTurret.create(16, "mark7_cannon"));
	public static final RegistrySupplier<ItemPart> STEVE_UP_SMASH = registerItem("steve_up_smash",
			() -> ItemTurret.create(16, "steve_up_smash"));
	public static final RegistrySupplier<ItemPart> SAM_LAUNCHER = registerItem("sam_launcher",
			() -> ItemTurret.create(16, "sam_launcher"));
	public static final RegistrySupplier<ItemPart> TORPEDO_TUBES = registerItem("torpedo_tubes",
			() -> ItemTurret.create(16, "torpedo_tubes"));
	public static final RegistrySupplier<ItemPart> MLS = registerItem("mls",
			() -> ItemTurret.create(16, "mls"));
	public static final RegistrySupplier<ItemPart> MLRS = registerItem("mlrs",
			() -> ItemTurret.create(16, "mlrs"));
	public static final RegistrySupplier<ItemPart> ARTILLERY_CANNON = registerItem("artillery_cannon",
			() -> ItemTurret.create(16, "artillery_cannon"));
	
	// FLARE DISPENSERS
	public static final RegistrySupplier<ItemPart> BASIC_FLARE_DISPENSER = registerItem("basic_flare_dispenser",
			() -> new ItemPart(16));
	
	// WEAPON PARTS
	public static final RegistrySupplier<ItemPart> EXTERNAL_WEAPON_PART = registerItem("external_weapon_part",
			() -> ItemWeaponPart.create(16, "xm12"));
	public static final RegistrySupplier<ItemPart> XM12 = registerItem("xm12",
			() -> ItemWeaponPart.create(16, "xm12"));
	public static final RegistrySupplier<ItemPart> INTERNAL_GUN = registerItem("internal_gun",
			() -> ItemWeaponPart.create(16, "internal_gun"));
	public static final RegistrySupplier<ItemPart> LIGHT_MISSILE_RACK = registerItem("light_missile_rack",
			() -> ItemWeaponPart.create(16, "light_missile_rack"));
	public static final RegistrySupplier<ItemPart> HEAVY_MISSILE_RACK = registerItem("heavy_missile_rack",
			() -> ItemWeaponPart.create(16, "heavy_missile_rack"));
	public static final RegistrySupplier<ItemPart> BOMB_RACK = registerItem("bomb_rack",
			() -> ItemWeaponPart.create(16, "bomb_rack"));
	public static final RegistrySupplier<ItemPart> ADL = registerItem("adl",
			() -> ItemWeaponPart.create(16, "adl"));
	public static final RegistrySupplier<ItemPart> VLS = registerItem("vls",
			() -> ItemWeaponPart.create(16, "vls"));
	/**
	 * TODO 2.1 radar jamming weapon
	 * causes victims radar to display random noise
	 * if your radar is strong enough and you get close enough, you stop getting jammed
	 */
	
	// AMMO
	public static final RegistrySupplier<Item> AMMO = registerItem("ammo",
			() -> ItemAmmo.create(64, "20mm"));
	public static final RegistrySupplier<Item> BULLET = registerItem("bullet",
			() -> ItemAmmo.create(64, "20mm"));
	public static final RegistrySupplier<Item> BOMB = registerItem("bomb",
			() -> ItemAmmo.create(64, "anm57"));
	public static final RegistrySupplier<Item> MISSILE = registerItem("missile",
			() -> ItemAmmo.create(16, "agm114k"));
	public static final RegistrySupplier<Item> TRACK_AIR_MISSILE = registerItem("track_air_missile",
			() -> ItemAmmo.create(16, "aim120b"));
	public static final RegistrySupplier<Item> TRACK_GROUND_MISSILE = registerItem("track_ground_missile",
			() -> ItemAmmo.create(16, "agm84e"));
	public static final RegistrySupplier<Item> IR_MISSILE = registerItem("ir_missile",
			() -> ItemAmmo.create(16, "aim9p5"));
	public static final RegistrySupplier<Item> POS_MISSILE = registerItem("pos_missile",
			() -> ItemAmmo.create(16, "agm114k"));
	public static final RegistrySupplier<Item> TORPEDO = registerItem("torpedo",
			() -> ItemAmmo.create(16, "mk13"));
	public static final RegistrySupplier<Item> ANTIRADAR_MISSILE = registerItem("antiradar_missile",
			() -> ItemAmmo.create(16, "agm88g"));
	
	public static final RegistrySupplier<Item> B_20MM = registerItem("20mm",
			() -> ItemAmmo.create(64, "20mm"));
	public static final RegistrySupplier<Item> B_50MMHE = registerItem("50mmhe",
			() -> ItemAmmo.create(64, "50mmhe"));
	public static final RegistrySupplier<Item> B_120MMHE = registerItem("120mmhe",
			() -> ItemAmmo.create(16, "120mmhe"));
	public static final RegistrySupplier<Item> AGM65G = registerItem("agm65g",
			() -> ItemAmmo.create(16, "agm65g"));
	public static final RegistrySupplier<Item> AGM65L = registerItem("agm65l",
			() -> ItemAmmo.create(16, "agm65l"));
	public static final RegistrySupplier<Item> AGM84E = registerItem("agm84e",
			() -> ItemAmmo.create(16, "agm84e"));
	public static final RegistrySupplier<Item> AGM114K = registerItem("agm114k",
			() -> ItemAmmo.create(16, "agm114k"));
	public static final RegistrySupplier<Item> AIM7F = registerItem("aim7f",
			() -> ItemAmmo.create(16, "aim7f"));
	public static final RegistrySupplier<Item> AIM7MH = registerItem("aim7mh",
			() -> ItemAmmo.create(16, "aim7mh"));
	public static final RegistrySupplier<Item> AIM9L = registerItem("aim9l",
			() -> ItemAmmo.create(16, "aim9l"));
	public static final RegistrySupplier<Item> AIM9P5 = registerItem("aim9p5",
			() -> ItemAmmo.create(16, "aim9p5"));
	public static final RegistrySupplier<Item> AIM9X = registerItem("aim9x",
			() -> ItemAmmo.create(16, "aim9x"));
	public static final RegistrySupplier<Item> AIM120B = registerItem("aim120b",
			() -> ItemAmmo.create(16, "aim120b"));
	public static final RegistrySupplier<Item> AIM120C = registerItem("aim120c",
			() -> ItemAmmo.create(16, "aim120c"));
	public static final RegistrySupplier<Item> TORPEDO1 = registerItem("torpedo1",
			() -> ItemAmmo.create(16, "mk13"));
	public static final RegistrySupplier<Item> RIFEL1 = registerItem("rifel1",
			() -> ItemAmmo.create(16, "agm88g"));
	public static final RegistrySupplier<Item> GRUETZ_BUNKER_BUSTER = registerItem("gruetz_bunker_buster",
			() -> ItemAmmo.create(16, "gruetz_bunker_buster"));
	public static final RegistrySupplier<Item> MK13 = registerItem("mk13",
			() -> ItemAmmo.create(16, "mk13"));
	public static final RegistrySupplier<Item> AGM88G = registerItem("agm88g",
			() -> ItemAmmo.create(16, "agm88g"));

	// VEHICLE
	public static final RegistrySupplier<Item> VEHICLE = registerItem("vehicle",
			() -> ItemVehicle.create(TankPresets.UNARMED_SMALL_ROLLER.getId()));

	// PLANES
	public static final RegistrySupplier<Item> JAVI_PLANE = registerItem("javi_plane",
			() -> ItemVehicle.create(JaviPresets.DEFAULT_JAVI_PLANE.getId()));
	public static final RegistrySupplier<Item> ALEXIS_PLANE = registerItem("alexis_plane",
			() -> ItemVehicle.create(AlexisPresets.DEFAULT_ALEXIS_PLANE.getId()));
	public static final RegistrySupplier<Item> WOODEN_PLANE = registerItem("wooden_plane",
			() -> ItemVehicle.create(PlanePresets.DEFAULT_WOODEN_PLANE.getId()));
	public static final RegistrySupplier<Item> E3SENTRY_PLANE = registerItem("e3sentry_plane",
			() -> ItemVehicle.create(PlanePresets.DEFAULT_E3SENTRY_PLANE.getId()));
	public static final RegistrySupplier<Item> BRONCO_PLANE = registerItem("bronco_plane",
			() -> ItemVehicle.create(BroncoPresets.DEFAULT_BRONCO_PLANE.getId()));
	public static final RegistrySupplier<Item> FELIX_PLANE = registerItem("felix_plane",
			() -> ItemVehicle.create(FelixPresets.DEFAULT_FELIX_PLANE.getId()));
	public static final RegistrySupplier<Item> JASON_PLANE = registerItem("jason_plane",
			() -> ItemVehicle.create(JasonPresets.DEFAULT_JASON_PLANE.getId()));
	public static final RegistrySupplier<Item> EDEN_PLANE = registerItem("eden_plane",
			() -> ItemVehicle.create(EdenPresets.DEFAULT_EDEN_PLANE.getId()));
	public static final RegistrySupplier<Item> JAMES_WOODEN_PLANE = registerItem("james_wooden_plane",
			() -> ItemVehicle.create(JamesPresets.DEFAULT_JAMES_PLANE.getId()));
	
	// HELICOPTERS
	public static final RegistrySupplier<Item> NOAH_CHOPPER = registerItem("noah_chopper",
			() -> ItemVehicle.create(NoahChopperPresets.DEFAULT_NOAH_CHOPPER.getId()));
	public static final RegistrySupplier<Item> KRAIT_CHOPPER = registerItem("krait_chopper",
			() -> ItemVehicle.create(KraitChopperPresets.DEFAULT_KRAIT_CHOPPER.getId()));
	
	// CARS
	public static final RegistrySupplier<Item> ORANGE_TESLA = registerItem("orange_tesla",
			() -> ItemVehicle.create(CarPresets.DEFAULT_ORANGE_TESLA.getId()));
	public static final RegistrySupplier<Item> AXCEL_TRUCK = registerItem("axcel_truck",
			() -> ItemVehicle.create(CarPresets.DEFAULT_AXCEL_TRUCK.getId()));
	public static final RegistrySupplier<Item> ERIC_TRUCK = registerItem("eric_truck",
			() -> ItemVehicle.create(CarPresets.DEFAULT_ERIC_TRUCK.getId()));
	
	// TANKS
	public static final RegistrySupplier<Item> MRBUDGER_TANK = registerItem("mrbudger_tank",
			() -> ItemVehicle.create(TankPresets.DEFAULT_MRBUDGER_TANK.getId()));
	public static final RegistrySupplier<Item> SMALL_ROLLER = registerItem("small_roller",
			() -> ItemVehicle.create(TankPresets.DEFAULT_SMALL_ROLLER.getId()));
	
	// BOATS
	public static final RegistrySupplier<Item> NATHAN_BOAT = registerItem("nathan_boat",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_NATHAN_BOAT.getId()));
	public static final RegistrySupplier<Item> GRONK_BATTLESHIP = registerItem("gronk_battleship",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_GRONK_BATTLESHIP.getId()));
	public static final RegistrySupplier<Item> DESTROYER = registerItem("destroyer",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_DESTROYER.getId()));
	public static final RegistrySupplier<Item> CRUISER = registerItem("cruiser",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_CRUISER.getId()));
	public static final RegistrySupplier<Item> CORVETTE = registerItem("corvette",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_CORVETTE.getId()));
	public static final RegistrySupplier<Item> AIRCRAFT_CARRIER = registerItem("aircraft_carrier",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_AIRCRAFT_CARRIER.getId()));
	
	// SUBMARINES
	public static final RegistrySupplier<Item> ANDOLF_SUB = registerItem("andolf_sub",
			() -> ItemVehicle.create(SubPresets.DEFAULT_ANDOLF_SUB.getId()));
	public static final RegistrySupplier<Item> GOOGLE_SUB = registerItem("google_sub",
			() -> ItemVehicle.create(SubPresets.DEFAULT_GOOGLE_SUB.getId()));

	// STATIONARY
	public static final RegistrySupplier<Item> EWR4000 = registerItem("ewr4000",
			() -> ItemVehicle.create(StationaryPresets.EWR4000.getId()));

    public static void register() {
        ITEMS.register();
    }
}
