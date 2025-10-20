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
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            DSCombatMod.MODID, Registries.ITEM);

    public static final Map<ResourceKey<CreativeModeTab>, List<Supplier<? extends Item>>> CREATIVE_TAB_MAP = new HashMap<>();

    @ExpectPlatform
    public static ResourceKey<CreativeModeTab> createTab(String name, Supplier<RegistrySupplier<? extends Item>> displayItem) {
        throw new AssertionError();
    }
	
	public static final ResourceKey<CreativeModeTab> DSC_ITEMS = createTab("items", () -> ModItems.WRENCH);
    public static final ResourceKey<CreativeModeTab> PARTS = createTab("parts", () -> ModItems.C12_ENGINE);
    public static final ResourceKey<CreativeModeTab> WEAPONS = createTab("weapons", () -> ModItems.AIM9X);
    public static final ResourceKey<CreativeModeTab> WEAPON_PARTS = createTab("weapon_parts", () -> ModItems.CIWS);
    public static final ResourceKey<CreativeModeTab> VEHICLES = createTab("vehicle", () -> ModItems.JAVI_PLANE);

    public static <R extends Item> RegistrySupplier<R> registerItem(String id, Supplier<? extends R> item,
                                                                    ResourceKey<CreativeModeTab> tab) {
        addTabItem(tab, item);
        return ITEMS.register(id, item);
    }

    public static <R extends Item> void addTabItem(ResourceKey<CreativeModeTab> tab, Supplier<? extends R> item) {
        if (CREATIVE_TAB_MAP.containsKey(tab)) {
            CREATIVE_TAB_MAP.get(tab).add(item);
            return;
        }
        List<Supplier<? extends Item>> list = new ArrayList<>();
        list.add(item);
        CREATIVE_TAB_MAP.put(tab, list);
    }

    public static <R extends Item> RegistrySupplier<R> registerDSCItem(String id, Supplier<? extends R> item) {
        return registerItem(id, item, DSC_ITEMS);
    }

    public static <R extends Item> RegistrySupplier<R> registerPart(String id, Supplier<? extends R> item) {
        return registerItem(id, item, PARTS);
    }

    public static <R extends Item> RegistrySupplier<R> registerWeapon(String id, Supplier<? extends R> item) {
        return registerItem(id, item, WEAPONS);
    }

    public static <R extends Item> RegistrySupplier<R> registerWeaponPart(String id, Supplier<? extends R> item) {
        return registerItem(id, item, WEAPON_PARTS);
    }

    public static <R extends Item> RegistrySupplier<R> registerVehicle(String id, Supplier<? extends R> item) {
        return registerItem(id, item, VEHICLES);
    }

	// DISCS
	// IDEA 8.1 Jupiter Missiles and Anadyr from blowback ost
	// IDEA 8.2 disk 911?
	public static final RegistrySupplier<Item> MISSILE_KNOWS_WHERE_DISC = registerItem("the_missile_knows_disc",
		() -> new RecordItem(15, ModSounds.MISSILE_KNOWS_WHERE,
			(new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 1980),
            CreativeModeTabs.OP_BLOCKS);
	public static final RegistrySupplier<Item> ORANGE_TESLA_DISC = registerItem(
            "orange_tesla_disc",
            () -> new RecordItem(14, ModSounds.ORANGE_TESLA,
                    (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 1080),
            CreativeModeTabs.OP_BLOCKS);
	
	// MATERIALS
	public static final RegistrySupplier<Item> RAW_ALUMINUM = registerDSCItem("raw_aluminum",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> ALUMINUM_INGOT = registerDSCItem("aluminum_ingot",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> COMPRESSED_FOSSIL = registerDSCItem("compressed_fossil",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> OIL_BUCKET = registerDSCItem("oil_bucket",
			() -> new ArchitecturyBucketItem(ModFluids.getOilFluidSource(),
					ItemPart.itemProps(1).craftRemainder(Items.BUCKET)));
	
	// PARTS
	public static final RegistrySupplier<Item> TI83 = registerDSCItem("ti83",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> INTEL_PENTIUM = registerDSCItem("intel_pentium",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> INTEL_CORE_I9X = registerDSCItem("intel_core_i9x",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> WHEEL = registerDSCItem("wheel",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_WHEEL = registerDSCItem("large_wheel",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> TANK_TRACK = registerDSCItem("tank_track",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> FUSELAGE = registerDSCItem("fuselage",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_FUSELAGE = registerDSCItem("large_fuselage",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> WING = registerDSCItem("wing",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_WING = registerDSCItem("large_wing",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> PROPELLER = registerDSCItem("propeller",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> LARGE_PROPELLER = registerDSCItem("large_propeller",
			() -> new Item(ItemPart.itemProps(64)));
	public static final RegistrySupplier<Item> COCKPIT = registerDSCItem("cockpit",
			() -> new Item(ItemPart.itemProps(16)));
	public static final RegistrySupplier<Item> ADVANCED_COCKPIT = registerDSCItem("advanced_cockpit",
			() -> new Item(ItemPart.itemProps(16)));
	
	// TOOLS
	public static final RegistrySupplier<Item> WRENCH = registerDSCItem("wrench",
			() -> new ItemRepairTool(20, 5));
	public static final RegistrySupplier<Item> THICK_WRENCH = registerDSCItem("thick_wrench",
			() -> new ItemRepairTool(200, 5));
	public static final RegistrySupplier<Item> PARACHUTE = registerDSCItem("parachute",
            ItemParachute::new);
	public static final RegistrySupplier<Item> SPRAYCAN = registerDSCItem("spraycan",
			() -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistrySupplier<Item> TICKET_BOOK = registerDSCItem("ticket_book",
            ItemTicketBook::new);
	
	// CREATIVE WANDS
	public static final RegistrySupplier<Item> NO_CONSUME_WAND = registerDSCItem("no_consume_wand",
			() -> new ItemCreativeWand(vehicle -> vehicle.setNoConsume(true), 
					"info.dscombat.no_consume_wand_1"));
	public static final RegistrySupplier<Item> INSTANT_REPAIR_WAND = registerDSCItem("instant_repair_wand",
			() -> new ItemCreativeWand(EntityVehicle::repairAll,
					"info.dscombat.instant_repair_wand_1"));
	public static final RegistrySupplier<Item> REFILL_WEAPONS_WAND = registerDSCItem("refill_weapons_wand",
			() -> new ItemCreativeWand(EntityVehicle::refillAllWeapons,
					"info.dscombat.refill_weapons_wand_1"));
	public static final RegistrySupplier<Item> REFILL_FUEL_WAND = registerDSCItem("refill_fuel_wand",
			() -> new ItemCreativeWand(EntityVehicle::refillFuel,
					"info.dscombat.refill_fuel_wand_1"));
	public static final RegistrySupplier<Item> REFILL_ALL_WAND = registerDSCItem("refill_all_wand",
			() -> new ItemCreativeWand(EntityVehicle::refillAll,
					"info.dscombat.refill_all_wand_1"));
	
	// GAS CANS
	public static final RegistrySupplier<Item> GAS_CAN = registerDSCItem("gas_can",
			() -> new ItemGasCan(50));
	public static final RegistrySupplier<Item> BIG_GAS_CAN = registerDSCItem("big_gas_can",
			() -> new ItemGasCan(150));
	public static final RegistrySupplier<Item> BIG_ASS_CAN = registerDSCItem("big_ass_can",
			() -> new ItemGasCan(600));
	
	// BUFFS
	public static final RegistrySupplier<ItemPart> DATA_LINK = registerPart("data_link",
			() -> new ItemPart(64));
	public static final RegistrySupplier<ItemPart> NIGHT_VISION_HUD = registerPart("night_vision_hud",
			() -> new ItemPart(64));
	public static final RegistrySupplier<ItemPart> RADIO = registerPart("radio",
			() -> new ItemPart(64));
	public static final RegistrySupplier<ItemPart> ARMOR_PIECE = registerPart("armor_piece",
			() -> new ItemPart(64));
	
	// GIMBALS
	public static final RegistrySupplier<ItemPart> GIMBAL_CAMERA = registerPart("gimbal_camera",
			() -> new ItemPart(16));
	
	// CHAIN HOOK
	public static final RegistrySupplier<ItemPart> CHAIN_HOOK = registerPart("chain_hook",
			() -> ItemExternalPart.create(16, "chain_hook"));
	
	// STORAGE BOXES
	public static final RegistrySupplier<ItemPart> SMALL_STORAGE_BOX = registerPart("small_storage_box",
			() -> new ItemStorageBox(1)); 
	public static final RegistrySupplier<ItemPart> MED_STORAGE_BOX = registerPart("medium_storage_box",
			() -> new ItemStorageBox(1));
	public static final RegistrySupplier<ItemPart> LARGE_STORAGE_BOX = registerPart("large_storage_box",
			() -> new ItemStorageBox(1));
	
	// FUEL TANKS
	public static final RegistrySupplier<ItemPart> LIGHT_FUEL_TANK = registerPart("light_fuel_tank",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> HEAVY_FUEL_TANK = registerPart("heavy_fuel_tank",
			() -> new ItemPart(16));
    public static final RegistrySupplier<ItemPart> LIGHT_EXTERNAL_FUEL_TANK = registerPart("light_external_fuel_tank",
            () -> ItemExternalPart.create(16, "light_external_fuel_tank"));
    public static final RegistrySupplier<ItemPart> HEAVY_EXTERNAL_FUEL_TANK = registerPart("heavy_external_fuel_tank",
            () -> ItemExternalPart.create(16, "heavy_external_fuel_tank"));


    // ENGINES
	public static final RegistrySupplier<ItemPart> C6_ENGINE = registerPart("c6_engine",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> C12_ENGINE = registerPart("c12_engine",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> TURBOFAN_F25 = registerPart("turbofan_f25",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> TURBOFAN_F145 = registerPart("turbofan_f145",
			() -> ItemExternalPart.create(16, "turbofan_f145"));
	public static final RegistrySupplier<ItemPart> TURBOFAN_F39 = registerPart("turbofan_f39",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> KLIMOV_RD33 = registerPart("klimov_rd33",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> CM_MANLY_52 = registerPart("cm_manly_52",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> ALLISON_V_1710 = registerPart("allison_v_1710",
			() -> ItemExternalPart.create(16, "allison_v_1710"));
	public static final RegistrySupplier<ItemPart> COMPOUND_TURBINE = registerPart("compound_turbine",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> CFM56 = registerPart("cfm56",
			() -> new ItemPart(16));
	
	// RADARS
	public static final RegistrySupplier<ItemPart> AR500 = registerPart("ar500",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AR1K = registerPart("ar1k",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AR2K = registerPart("ar2k",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GR200 = registerPart("gr200",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GR400 = registerPart("gr400",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> WR400 = registerPart("wr400",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> WR1K = registerPart("wr1k",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GPR20 = registerPart("gpr20",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> GPR100 = registerPart("gpr100",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AR20K = registerPart("ar20k",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> AXCEL_TRUCK_RADAR = registerPart("axcel_truck_radar",
			() -> new ItemPart(16));
	// IDEA 2 passive under water sonar doesn's show RWR warning
	public static final RegistrySupplier<ItemPart> AIR_SCAN_A = registerPart("air_scan_a",
			() -> ItemExternalPart.create(16, "air_scan_a"));
	public static final RegistrySupplier<ItemPart> AIR_SCAN_B = registerPart("air_scan_b",
			() -> ItemExternalPart.create(16, "air_scan_b"));
	public static final RegistrySupplier<ItemPart> SURVEY_ALL_A = registerPart("survey_all_a",
			() -> new ItemPart(16));
	public static final RegistrySupplier<ItemPart> SURVEY_ALL_B = registerPart("survey_all_b",
			() -> ItemExternalPart.create(16, "survey_all_b"));
	
	// SEATS
	public static final RegistrySupplier<ItemPart> SEAT = registerPart("seat",
			() -> new ItemSeat(64));
	
	// TURRETS
	public static final RegistrySupplier<ItemPart> TURRET = registerWeaponPart("turret",
			() -> ItemTurret.create(16, "aa_turret"));
	public static final RegistrySupplier<ItemPart> AA_TURRET = registerWeaponPart("aa_turret",
			() -> ItemTurret.create(16, "aa_turret"));
	public static final RegistrySupplier<ItemPart> MINIGUN_TURRET = registerWeaponPart("minigun_turret",
			() -> ItemTurret.create(16, "minigun_turret"));
	public static final RegistrySupplier<ItemPart> CIWS = registerWeaponPart("ciws",
			() -> ItemTurret.create(16, "ciws"));
	public static final RegistrySupplier<ItemPart> MARK45_CANNON = registerWeaponPart("mark45_cannon",
			() -> ItemTurret.create(16, "mark45_cannon"));
	public static final RegistrySupplier<ItemPart> HEAVY_TANK_TURRET = registerWeaponPart("heavy_tank_turret",
			() -> ItemTurret.create(16, "heavy_tank_turret"));
	public static final RegistrySupplier<ItemPart> MARK7_CANNON = registerWeaponPart("mark7_cannon",
			() -> ItemTurret.create(16, "mark7_cannon"));
	public static final RegistrySupplier<ItemPart> STEVE_UP_SMASH = registerWeaponPart("steve_up_smash",
			() -> ItemTurret.create(16, "steve_up_smash"));
	public static final RegistrySupplier<ItemPart> SAM_LAUNCHER = registerWeaponPart("sam_launcher",
			() -> ItemTurret.create(16, "sam_launcher"));
	public static final RegistrySupplier<ItemPart> TORPEDO_TUBES = registerWeaponPart("torpedo_tubes",
			() -> ItemTurret.create(16, "torpedo_tubes"));
	public static final RegistrySupplier<ItemPart> MLS = registerWeaponPart("mls",
			() -> ItemTurret.create(16, "mls"));
	public static final RegistrySupplier<ItemPart> MLRS = registerWeaponPart("mlrs",
			() -> ItemTurret.create(16, "mlrs"));
	public static final RegistrySupplier<ItemPart> ARTILLERY_CANNON = registerWeaponPart("artillery_cannon",
			() -> ItemTurret.create(16, "artillery_cannon"));
	
	// FLARE DISPENSERS
	public static final RegistrySupplier<ItemPart> BASIC_FLARE_DISPENSER = registerWeaponPart("basic_flare_dispenser",
			() -> new ItemPart(16));
	
	// WEAPON PARTS
	public static final RegistrySupplier<ItemPart> EXTERNAL_WEAPON_PART = registerWeaponPart("external_weapon_part",
			() -> ItemWeaponPart.create(16, "xm12"));
	public static final RegistrySupplier<ItemPart> XM12 = registerWeaponPart("xm12",
			() -> ItemWeaponPart.create(16, "xm12"));
	public static final RegistrySupplier<ItemPart> INTERNAL_GUN = registerWeaponPart("internal_gun",
			() -> ItemWeaponPart.create(16, "internal_gun"));
	public static final RegistrySupplier<ItemPart> LIGHT_MISSILE_RACK = registerWeaponPart("light_missile_rack",
			() -> ItemWeaponPart.create(16, "light_missile_rack"));
	public static final RegistrySupplier<ItemPart> HEAVY_MISSILE_RACK = registerWeaponPart("heavy_missile_rack",
			() -> ItemWeaponPart.create(16, "heavy_missile_rack"));
	public static final RegistrySupplier<ItemPart> BOMB_RACK = registerWeaponPart("bomb_rack",
			() -> ItemWeaponPart.create(16, "bomb_rack"));
	public static final RegistrySupplier<ItemPart> ADL = registerWeaponPart("adl",
			() -> ItemWeaponPart.create(16, "adl"));
	public static final RegistrySupplier<ItemPart> VLS = registerWeaponPart("vls",
			() -> ItemWeaponPart.create(16, "vls"));
	/**
	 * TODO 2.1 radar jamming weapon
	 * causes victims radar to display random noise
	 * if your radar is strong enough and you get close enough, you stop getting jammed
	 */
	
	// AMMO
	public static final RegistrySupplier<Item> AMMO = registerWeapon("ammo",
			() -> ItemAmmo.create(64, "20mm"));
	public static final RegistrySupplier<Item> BULLET = registerWeapon("bullet",
			() -> ItemAmmo.create(64, "20mm"));
	public static final RegistrySupplier<Item> BOMB = registerWeapon("bomb",
			() -> ItemAmmo.create(64, "anm57"));
	public static final RegistrySupplier<Item> MISSILE = registerWeapon("missile",
			() -> ItemAmmo.create(16, "agm114k"));
	public static final RegistrySupplier<Item> TRACK_AIR_MISSILE = registerWeapon("track_air_missile",
			() -> ItemAmmo.create(16, "aim120b"));
	public static final RegistrySupplier<Item> TRACK_GROUND_MISSILE = registerWeapon("track_ground_missile",
			() -> ItemAmmo.create(16, "agm84e"));
	public static final RegistrySupplier<Item> IR_MISSILE = registerWeapon("ir_missile",
			() -> ItemAmmo.create(16, "aim9p5"));
	public static final RegistrySupplier<Item> POS_MISSILE = registerWeapon("pos_missile",
			() -> ItemAmmo.create(16, "agm114k"));
	public static final RegistrySupplier<Item> TORPEDO = registerWeapon("torpedo",
			() -> ItemAmmo.create(16, "mk13"));
	public static final RegistrySupplier<Item> ANTIRADAR_MISSILE = registerWeapon("antiradar_missile",
			() -> ItemAmmo.create(16, "agm88g"));
	
	public static final RegistrySupplier<Item> B_20MM = registerWeapon("20mm",
			() -> ItemAmmo.create(64, "20mm"));
	public static final RegistrySupplier<Item> B_50MMHE = registerWeapon("50mmhe",
			() -> ItemAmmo.create(64, "50mmhe"));
	public static final RegistrySupplier<Item> B_120MMHE = registerWeapon("120mmhe",
			() -> ItemAmmo.create(16, "120mmhe"));
	public static final RegistrySupplier<Item> AGM65G = registerWeapon("agm65g",
			() -> ItemAmmo.create(16, "agm65g"));
	public static final RegistrySupplier<Item> AGM65L = registerWeapon("agm65l",
			() -> ItemAmmo.create(16, "agm65l"));
	public static final RegistrySupplier<Item> AGM84E = registerWeapon("agm84e",
			() -> ItemAmmo.create(16, "agm84e"));
	public static final RegistrySupplier<Item> AGM114K = registerWeapon("agm114k",
			() -> ItemAmmo.create(16, "agm114k"));
	public static final RegistrySupplier<Item> AIM7F = registerWeapon("aim7f",
			() -> ItemAmmo.create(16, "aim7f"));
	public static final RegistrySupplier<Item> AIM7MH = registerWeapon("aim7mh",
			() -> ItemAmmo.create(16, "aim7mh"));
	public static final RegistrySupplier<Item> AIM9L = registerWeapon("aim9l",
			() -> ItemAmmo.create(16, "aim9l"));
	public static final RegistrySupplier<Item> AIM9P5 = registerWeapon("aim9p5",
			() -> ItemAmmo.create(16, "aim9p5"));
	public static final RegistrySupplier<Item> AIM9X = registerWeapon("aim9x",
			() -> ItemAmmo.create(16, "aim9x"));
	public static final RegistrySupplier<Item> AIM120B = registerWeapon("aim120b",
			() -> ItemAmmo.create(16, "aim120b"));
	public static final RegistrySupplier<Item> AIM120C = registerWeapon("aim120c",
			() -> ItemAmmo.create(16, "aim120c"));
	public static final RegistrySupplier<Item> TORPEDO1 = registerWeapon("torpedo1",
			() -> ItemAmmo.create(16, "mk13"));
	public static final RegistrySupplier<Item> RIFEL1 = registerWeapon("rifel1",
			() -> ItemAmmo.create(16, "agm88g"));
	public static final RegistrySupplier<Item> GRUETZ_BUNKER_BUSTER = registerWeapon("gruetz_bunker_buster",
			() -> ItemAmmo.create(16, "gruetz_bunker_buster"));
	public static final RegistrySupplier<Item> MK13 = registerWeapon("mk13",
			() -> ItemAmmo.create(16, "mk13"));
	public static final RegistrySupplier<Item> AGM88G = registerWeapon("agm88g",
			() -> ItemAmmo.create(16, "agm88g"));

	// VEHICLE
	public static final RegistrySupplier<Item> VEHICLE = registerVehicle("vehicle",
			() -> ItemVehicle.create(TankPresets.UNARMED_SMALL_ROLLER.getId()));

	// PLANES
	public static final RegistrySupplier<Item> JAVI_PLANE = registerVehicle("javi_plane",
			() -> ItemVehicle.create(JaviPresets.DEFAULT_JAVI_PLANE.getId()));
	public static final RegistrySupplier<Item> ALEXIS_PLANE = registerVehicle("alexis_plane",
			() -> ItemVehicle.create(AlexisPresets.DEFAULT_ALEXIS_PLANE.getId()));
	public static final RegistrySupplier<Item> WOODEN_PLANE = registerVehicle("wooden_plane",
			() -> ItemVehicle.create(PlanePresets.DEFAULT_WOODEN_PLANE.getId()));
	public static final RegistrySupplier<Item> E3SENTRY_PLANE = registerVehicle("e3sentry_plane",
			() -> ItemVehicle.create(PlanePresets.DEFAULT_E3SENTRY_PLANE.getId()));
	public static final RegistrySupplier<Item> BRONCO_PLANE = registerVehicle("bronco_plane",
			() -> ItemVehicle.create(BroncoPresets.DEFAULT_BRONCO_PLANE.getId()));
	public static final RegistrySupplier<Item> FELIX_PLANE = registerVehicle("felix_plane",
			() -> ItemVehicle.create(FelixPresets.DEFAULT_FELIX_PLANE.getId()));
	public static final RegistrySupplier<Item> JASON_PLANE = registerVehicle("jason_plane",
			() -> ItemVehicle.create(JasonPresets.DEFAULT_JASON_PLANE.getId()));
	public static final RegistrySupplier<Item> EDEN_PLANE = registerVehicle("eden_plane",
			() -> ItemVehicle.create(EdenPresets.DEFAULT_EDEN_PLANE.getId()));
	public static final RegistrySupplier<Item> JAMES_WOODEN_PLANE = registerVehicle("james_wooden_plane",
			() -> ItemVehicle.create(JamesPresets.DEFAULT_JAMES_PLANE.getId()));
	
	// HELICOPTERS
	public static final RegistrySupplier<Item> NOAH_CHOPPER = registerVehicle("noah_chopper",
			() -> ItemVehicle.create(NoahChopperPresets.DEFAULT_NOAH_CHOPPER.getId()));
	public static final RegistrySupplier<Item> KRAIT_CHOPPER = registerVehicle("krait_chopper",
			() -> ItemVehicle.create(KraitChopperPresets.DEFAULT_KRAIT_CHOPPER.getId()));
	
	// CARS
	public static final RegistrySupplier<Item> ORANGE_TESLA = registerVehicle("orange_tesla",
			() -> ItemVehicle.create(CarPresets.DEFAULT_ORANGE_TESLA.getId()));
	public static final RegistrySupplier<Item> AXCEL_TRUCK = registerVehicle("axcel_truck",
			() -> ItemVehicle.create(CarPresets.DEFAULT_AXCEL_TRUCK.getId()));
	public static final RegistrySupplier<Item> ERIC_TRUCK = registerVehicle("eric_truck",
			() -> ItemVehicle.create(CarPresets.DEFAULT_ERIC_TRUCK.getId()));
	
	// TANKS
	public static final RegistrySupplier<Item> MRBUDGER_TANK = registerVehicle("mrbudger_tank",
			() -> ItemVehicle.create(TankPresets.DEFAULT_MRBUDGER_TANK.getId()));
	public static final RegistrySupplier<Item> SMALL_ROLLER = registerVehicle("small_roller",
			() -> ItemVehicle.create(TankPresets.DEFAULT_SMALL_ROLLER.getId()));
	
	// BOATS
	public static final RegistrySupplier<Item> NATHAN_BOAT = registerVehicle("nathan_boat",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_NATHAN_BOAT.getId()));
	public static final RegistrySupplier<Item> GRONK_BATTLESHIP = registerVehicle("gronk_battleship",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_GRONK_BATTLESHIP.getId()));
	public static final RegistrySupplier<Item> DESTROYER = registerVehicle("destroyer",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_DESTROYER.getId()));
	public static final RegistrySupplier<Item> CRUISER = registerVehicle("cruiser",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_CRUISER.getId()));
	public static final RegistrySupplier<Item> CORVETTE = registerVehicle("corvette",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_CORVETTE.getId()));
	public static final RegistrySupplier<Item> AIRCRAFT_CARRIER = registerVehicle("aircraft_carrier",
			() -> ItemVehicle.create(BoatPresets.DEFAULT_AIRCRAFT_CARRIER.getId()));
	
	// SUBMARINES
	public static final RegistrySupplier<Item> ANDOLF_SUB = registerVehicle("andolf_sub",
			() -> ItemVehicle.create(SubPresets.DEFAULT_ANDOLF_SUB.getId()));
	public static final RegistrySupplier<Item> GOOGLE_SUB = registerVehicle("google_sub",
			() -> ItemVehicle.create(SubPresets.DEFAULT_GOOGLE_SUB.getId()));

	// STATIONARY
	public static final RegistrySupplier<Item> EWR4000 = registerVehicle("ewr4000",
			() -> ItemVehicle.create(StationaryPresets.EWR4000.getId()));

    public static void register() {
        ITEMS.register();
    }
}
