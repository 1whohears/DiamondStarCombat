package com.onewhohears.dscombat.init;

import com.google.common.collect.ImmutableSet;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.presets.BoatPresets;
import com.onewhohears.dscombat.data.aircraft.presets.CarPresets;
import com.onewhohears.dscombat.data.aircraft.presets.PlanePresets;
import com.onewhohears.dscombat.data.aircraft.presets.SubPresets;
import com.onewhohears.dscombat.data.aircraft.presets.TankPresets;
import com.onewhohears.dscombat.data.parts.TurretData.RotBounds;
import com.onewhohears.dscombat.entity.EntityParachute;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;
import com.onewhohears.dscombat.entity.aircraft.EntityGroundVehicle;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.entity.aircraft.EntitySubmarine;
import com.onewhohears.dscombat.entity.aircraft.custom.AircraftCarrier;
import com.onewhohears.dscombat.entity.aircraft.custom.AlexisPlane;
import com.onewhohears.dscombat.entity.aircraft.custom.BroncoPlane;
import com.onewhohears.dscombat.entity.aircraft.custom.Corvette;
import com.onewhohears.dscombat.entity.aircraft.custom.Cruiser;
import com.onewhohears.dscombat.entity.aircraft.custom.Destroyer;
import com.onewhohears.dscombat.entity.aircraft.custom.E3SentryPlane;
import com.onewhohears.dscombat.entity.aircraft.custom.EdenPlane;
import com.onewhohears.dscombat.entity.aircraft.custom.FelixPlane;
import com.onewhohears.dscombat.entity.aircraft.custom.GronkBattleship;
import com.onewhohears.dscombat.entity.aircraft.custom.JasonPlane;
import com.onewhohears.dscombat.entity.aircraft.custom.JaviPlane;
import com.onewhohears.dscombat.entity.aircraft.custom.NoahChopper;
import com.onewhohears.dscombat.entity.parts.EntityEngine;
import com.onewhohears.dscombat.entity.parts.EntityGimbal;
import com.onewhohears.dscombat.entity.parts.EntityRadar;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityTurret.ShootType;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.weapon.AntiRadarMissile;
import com.onewhohears.dscombat.entity.weapon.EntityBomb;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityBunkerBuster;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;
import com.onewhohears.dscombat.entity.weapon.IRMissile;
import com.onewhohears.dscombat.entity.weapon.PositionMissile;
import com.onewhohears.dscombat.entity.weapon.TorpedoMissile;
import com.onewhohears.dscombat.entity.weapon.TrackEntityMissile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		ENTITIES.register(eventBus);
	}
	
	// PLANES
	
	public static final RegistryObject<EntityType<EntityPlane>> JAVI_PLANE = ENTITIES.register("javi_plane", 
			() -> createEntityTypeFar((type, level) -> new JaviPlane(type, level), 
					EntityDimensions.scalable(4.0f, 4.0f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> ALEXIS_PLANE = ENTITIES.register("alexis_plane", 
			() -> createEntityTypeFar((type, level) -> new AlexisPlane(type, level), 
					EntityDimensions.scalable(4.0f, 4.0f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> WOODEN_PLANE = ENTITIES.register("wooden_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					PlanePresets.DEFAULT_WOODEN_PLANE.getId()), 
					EntityDimensions.scalable(1.7f, 1.7f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> E3SENTRY_PLANE = ENTITIES.register("e3sentry_plane", 
			() -> createEntityTypeFar((type, level) -> new E3SentryPlane(type, level), 
					EntityDimensions.scalable(4.0f, 4.0f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> BRONCO_PLANE = ENTITIES.register("bronco_plane", 
			() -> createEntityTypeFar((type, level) -> new BroncoPlane(type, level), 
					EntityDimensions.scalable(4.0f, 4.0f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> FELIX_PLANE = ENTITIES.register("felix_plane", 
			() -> createEntityTypeFar((type, level) -> new FelixPlane(type, level), 
					EntityDimensions.scalable(4.0f, 4.0f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> JASON_PLANE = ENTITIES.register("jason_plane", 
			() -> createEntityTypeFar((type, level) -> new JasonPlane(type, level), 
					EntityDimensions.scalable(4.0f, 4.0f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> EDEN_PLANE = ENTITIES.register("eden_plane", 
			() -> createEntityTypeFar((type, level) -> new EdenPlane(type, level), 
					EntityDimensions.scalable(4.0f, 4.0f)));
	
	// HELICOPTORS
	
	public static final RegistryObject<EntityType<EntityHelicopter>> NOAH_CHOPPER = ENTITIES.register("noah_chopper", 
			() -> createEntityTypeFar((type, level) -> new NoahChopper(type, level), 
					EntityDimensions.scalable(2.8f, 2.8f)));
	
	// TANKS
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> MRBUDGER_TANK = ENTITIES.register("mrbudger_tank", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					TankPresets.DEFAULT_MRBUDGER_TANK.getId()), 
					EntityDimensions.scalable(3.0f, 2.5f)));
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> SMALL_ROLLER = ENTITIES.register("small_roller", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					TankPresets.DEFAULT_SMALL_ROLLER.getId()), 
					EntityDimensions.scalable(1.5f, 0.8f)));
	
	// CARS
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> ORANGE_TESLA = ENTITIES.register("orange_tesla", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					CarPresets.DEFAULT_ORANGE_TESLA.getId()), 
					EntityDimensions.scalable(2.5f, 2.15f)));
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> AXCEL_TRUCK = ENTITIES.register("axcel_truck", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					CarPresets.DEFAULT_AXCEL_TRUCK.getId()), 
					EntityDimensions.scalable(2.5f, 3.0f)));
	
	// TODO 2.6 fuel truck
	
	// BOATS
	
	public static final RegistryObject<EntityType<EntityBoat>> NATHAN_BOAT = ENTITIES.register("nathan_boat", 
			() -> createEntityTypeFar((type, level) -> new EntityBoat(type, level, 
					BoatPresets.DEFAULT_NATHAN_BOAT.getId()), 
					EntityDimensions.scalable(3.0f,1.5f)));
	
	public static final RegistryObject<EntityType<EntityBoat>> GRONK_BATTLESHIP = ENTITIES.register("gronk_battleship", 
			() -> createEntityTypeFar((type, level) -> new GronkBattleship(type, level), 
					EntityDimensions.scalable(14f,4f)));
	
	public static final RegistryObject<EntityType<EntityBoat>> DESTROYER = ENTITIES.register("destroyer", 
			() -> createEntityTypeFar((type, level) -> new Destroyer(type, level),
					EntityDimensions.scalable(10f,4f)));
	
	public static final RegistryObject<EntityType<EntityBoat>> CRUISER = ENTITIES.register("cruiser", 
			() -> createEntityTypeFar((type, level) -> new Cruiser(type, level),
					EntityDimensions.scalable(12f,4f)));
	
	public static final RegistryObject<EntityType<EntityBoat>> CORVETTE = ENTITIES.register("corvette", 
			() -> createEntityTypeFar((type, level) -> new Corvette(type, level),
					EntityDimensions.scalable(7f,3f)));
	
	public static final RegistryObject<EntityType<EntityBoat>> AIRCRAFT_CARRIER = ENTITIES.register("aircraft_carrier", 
			() -> createEntityTypeFar((type, level) -> new AircraftCarrier(type, level), 
					EntityDimensions.scalable(25f,6f)));
	
	// SUBMARINES
	
	public static final RegistryObject<EntityType<EntitySubmarine>> ANDOLF_SUB = ENTITIES.register("andolf_sub", 
			() -> createEntityTypeFar((type, level) -> new EntitySubmarine(type, level, 
					SubPresets.DEFAULT_ANDOLF_SUB.getId()), 
					EntityDimensions.scalable(4.5f,4.0f)));
	
	public static final RegistryObject<EntityType<EntitySubmarine>> GOOGLE_SUB = ENTITIES.register("google_sub", 
			() -> createEntityTypeFar((type, level) -> new EntitySubmarine(type, level, 
					SubPresets.DEFAULT_GOOGLE_SUB.getId()), 
					EntityDimensions.scalable(8f,8f)));
	
	/* 
	 * IDEA 5 more vehicles
	 * 
	 * PLANES
	 * wooden planes, private jet, large passenger jet
	 * Spitfire, P51 Mustang, E3 Sentry, C-5 Galaxy, Mig-29, F14, Su-27, Su-57, F22
	 * Tariku Plane (F35-B VTOL)
	 * 
	 * HELICOPTORS
	 * small unarmed heli, large cargo heli, 2 seated heli with gunner on front bottom
	 * Noah Chopper should resemble black hawk
	 * 
	 * CARS/TANKS
	 * Honda Ferrari, Joanna Bike, Mobile SAM
	 * 
	 * BOATS/SUBMARINES
	 * 
	 * OTHER
	 * Weather Balloon
	 * 
	 * IDEA 9.1 AI for planes or an auto pilot module
	 * AI turret controllers need time and resources invested to level up
	 * it would be funny if they were villagers
	 * IDEA 9.2 pillager outposts with air defense
	 * 
	 * IDEA 9.3 large physical hanger to craft vehicle/put weapons on them
	 */
	
	public static final EntityDimensions SEAT_SIZE = EntityDimensions.scalable(0.8f, 0.8f);
	
	public static final RegistryObject<EntityType<EntitySeat>> SEAT = ENTITIES.register("seat", 
			() -> createEntityType((type, level) -> new EntitySeat(type, level, 
					Vec3.ZERO), SEAT_SIZE));
	
	public static final RegistryObject<EntityType<EntityTurret>> MINIGUN_TURRET = ENTITIES.register("minigun_turret", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					Vec3.ZERO, 0.8,
					RotBounds.create(2.5f,50f, 50f)), 
					EntityDimensions.scalable(1.0f, 1.5f)));
	public static final RegistryObject<EntityType<EntityTurret>> HEAVY_TANK_TURRET = ENTITIES.register("heavy_tank_turret", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					Vec3.ZERO, 0.5,
					RotBounds.create(1.0f, 30f, 30f)), 
					EntityDimensions.scalable(2.0f, 1.0f)));
	public static final RegistryObject<EntityType<EntityTurret>> STEVE_UP_SMASH = ENTITIES.register("steve_up_smash", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					Vec3.ZERO, 3.2,
					RotBounds.create(1.8f, 25f, 25f)), 
					EntityDimensions.scalable(1.0f, 3.5f)));
	public static final RegistryObject<EntityType<EntityTurret>> SAM_LAUNCHER = ENTITIES.register("sam_launcher", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(1.2, 0.4, 0), 2.7,
					RotBounds.create(1.3f, 25f, 25f)), 
					EntityDimensions.scalable(2.0f, 3.0f)));
	public static final RegistryObject<EntityType<EntityTurret>> MLS = ENTITIES.register("mls", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(1.2, 0, 0), 1,
					RotBounds.create(1.9f, 20f, 20f)), 
					EntityDimensions.scalable(2.0f, 2.5f)));
	public static final RegistryObject<EntityType<EntityTurret>> TORPEDO_TUBES = ENTITIES.register("torpedo_tubes", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(0, 1.8, 0), 1,
					RotBounds.create(1.6f, 5f, 5f)), 
					EntityDimensions.scalable(2.5f, 2.0f)));
	public static final RegistryObject<EntityType<EntityTurret>> AA_TURRET = ENTITIES.register("aa_turret", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(0, 0.5, 0), 1.03125, 
					RotBounds.create(3.0f, 30f, 30f)), 
					EntityDimensions.scalable(1.0f, 1.25f)));
	public static final RegistryObject<EntityType<EntityTurret>> CIWS = ENTITIES.register("ciws", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(1, 0.5, 0), 0.6875,
					RotBounds.create(2.0f, 75f, 30f)), 
					EntityDimensions.scalable(1.5f, 2.5f)));
	public static final RegistryObject<EntityType<EntityTurret>> MARK7_CANNON = ENTITIES.register("mark7_cannon", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(0, 1.5, 0), 1.625,
					RotBounds.create(0.9f, 30f, 15f), ShootType.MARK7), 
					EntityDimensions.scalable(4.0f, 1.7f)));
	public static final RegistryObject<EntityType<EntityTurret>> MARK45_CANNON = ENTITIES.register("mark45_cannon", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(0, 1.5, 0), 1.5625,
					RotBounds.create(1.1f, 30f, 15f)), 
					EntityDimensions.scalable(2.0f, 1.5f)));
	
	public static final EntityDimensions TINY = EntityDimensions.scalable(0.1f, 0.1f);
	
	public static final RegistryObject<EntityType<EntityGimbal>> GIMBAL_CAMERA = ENTITIES.register("gimbal_camera", 
			() -> createEntityType(EntityGimbal::new, TINY));
	
	public static final RegistryObject<EntityType<EntityWeaponRack>> LIGHT_MISSILE_RACK = ENTITIES.register("light_missile_rack", 
			() -> createEntityType(EntityWeaponRack::new, TINY));
	public static final RegistryObject<EntityType<EntityWeaponRack>> HEAVY_MISSILE_RACK = ENTITIES.register("heavy_missile_rack", 
			() -> createEntityType(EntityWeaponRack::new, TINY));
	public static final RegistryObject<EntityType<EntityWeaponRack>> XM12 = ENTITIES.register("xm12", 
			() -> createEntityType(EntityWeaponRack::new, TINY));
	public static final RegistryObject<EntityType<EntityWeaponRack>> BOMB_RACK = ENTITIES.register("bomb_rack", 
			() -> createEntityType(EntityWeaponRack::new, TINY));
	public static final RegistryObject<EntityType<EntityWeaponRack>> ADL = ENTITIES.register("adl", 
			() -> createEntityType(EntityWeaponRack::new, TINY));
	public static final RegistryObject<EntityType<EntityWeaponRack>> VLS = ENTITIES.register("vls", 
			() -> createEntityType(EntityWeaponRack::new, TINY));
	
	public static final RegistryObject<EntityType<EntityEngine>> CFM56 = ENTITIES.register("cfm56", 
			() -> createEntityType(EntityEngine::new, EntityDimensions.scalable(0.8f, 0.8f)));
	
	public static final RegistryObject<EntityType<EntityRadar>> AIR_SCAN_A = ENTITIES.register("air_scan_a", 
			() -> createEntityType(EntityRadar::new, EntityDimensions.scalable(1.0f, 1.0f)));
	public static final RegistryObject<EntityType<EntityRadar>> AIR_SCAN_B = ENTITIES.register("air_scan_b", 
			() -> createEntityType(EntityRadar::new, EntityDimensions.scalable(1.0f, 1.0f)));
	public static final RegistryObject<EntityType<EntityRadar>> SURVEY_ALL_A = ENTITIES.register("survey_all_a", 
			() -> createEntityType(EntityRadar::new, EntityDimensions.scalable(1.0f, 0.5f)));
	public static final RegistryObject<EntityType<EntityRadar>> SURVEY_ALL_B = ENTITIES.register("survey_all_b", 
			() -> createEntityType(EntityRadar::new, EntityDimensions.scalable(1.0f, 1.0f)));
	
	public static final RegistryObject<EntityType<EntityBullet>> BULLET = ENTITIES.register("bullet", 
			() -> createEntityType((type, level) -> new EntityBullet(type, level, "20mm"), 
					EntityDimensions.scalable(0.15f, 0.15f)));
	public static final RegistryObject<EntityType<EntityBomb>> BOMB = ENTITIES.register("bomb", 
			() -> createEntityType((type, level) -> new EntityBomb(type, level, "anm30"), 
					EntityDimensions.scalable(0.6f, 0.6f)));
	public static final RegistryObject<EntityType<EntityBunkerBuster>> GRUETZ_BUNKER_BUSTER = ENTITIES.register("gruetz_bunker_buster", 
			() -> createEntityType((type, level) -> new EntityBunkerBuster(type, level, "gruetz_bunker_buster"), 
					EntityDimensions.scalable(1.5f, 2.5f)));
	
	public static final RegistryObject<EntityType<PositionMissile>> POS_MISSILE_1 = ENTITIES.register("pos_missile_1", 
			() -> createEntityTypeFar((type, level) -> new PositionMissile(type, level, "agm114k"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<IRMissile>> IR_MISSILE_1 = ENTITIES.register("ir_missile_1", 
			() -> createEntityTypeFar((type, level) -> new IRMissile(type, level, "aim9l"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> TRACK_MISSILE_1 = ENTITIES.register("track_missile_1", 
			() -> createEntityTypeFar((type, level) -> new TrackEntityMissile(type, level, "aim120b"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<AntiRadarMissile>> ANTI_RADAR_MISSILE_1 = ENTITIES.register("anti_radar_missile_1", 
			() -> createEntityTypeFar((type, level) -> new AntiRadarMissile(type, level, "agm88g"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TorpedoMissile>> TORPEDO_MISSILE_1 = ENTITIES.register("torpedo_missile_1", 
			() -> createEntityTypeFar((type, level) -> new TorpedoMissile(type, level, "mk13"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	
	public static final RegistryObject<EntityType<IRMissile>> AIM9L = ENTITIES.register("aim9l", 
			() -> createEntityTypeFar((type, level) -> new IRMissile(type, level, "aim9l"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<IRMissile>> AIM9P5 = ENTITIES.register("aim9p5", 
			() -> createEntityTypeFar((type, level) -> new IRMissile(type, level, "aim9p5"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<IRMissile>> AIM9X = ENTITIES.register("aim9x", 
			() -> createEntityTypeFar((type, level) -> new IRMissile(type, level, "aim9x"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> AIM120B = ENTITIES.register("aim120b", 
			() -> createEntityTypeFar((type, level) -> new TrackEntityMissile(type, level, "aim120b"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> AIM120C = ENTITIES.register("aim120c", 
			() -> createEntityTypeFar((type, level) -> new TrackEntityMissile(type, level, "aim120c"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> PAC3 = ENTITIES.register("pac3", 
			() -> createEntityTypeFar((type, level) -> new TrackEntityMissile(type, level, "pac3"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> AIM7F = ENTITIES.register("aim7f", 
			() -> createEntityTypeFar((type, level) -> new TrackEntityMissile(type, level, "aim7f"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> AIM7MH = ENTITIES.register("aim7mh", 
			() -> createEntityTypeFar((type, level) -> new TrackEntityMissile(type, level, "aim7mh"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<PositionMissile>> AGM114K = ENTITIES.register("agm114k", 
			() -> createEntityTypeFar((type, level) -> new PositionMissile(type, level, "agm114k"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<PositionMissile>> AGM65L = ENTITIES.register("agm65l", 
			() -> createEntityTypeFar((type, level) -> new PositionMissile(type, level, "agm65l"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> AGM84E = ENTITIES.register("agm84e", 
			() -> createEntityTypeFar((type, level) -> new TrackEntityMissile(type, level, "agm84e"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> AGM65G = ENTITIES.register("agm65g", 
			() -> createEntityTypeFar((type, level) -> new TrackEntityMissile(type, level, "agm65g"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> METEOR = ENTITIES.register("meteor", 
			() -> createEntityTypeFar((type, level) -> new TrackEntityMissile(type, level, "meteor"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TorpedoMissile>> MK13 = ENTITIES.register("mk13", 
			() -> createEntityTypeFar((type, level) -> new TorpedoMissile(type, level, "mk13"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<AntiRadarMissile>> AGM88G = ENTITIES.register("agm88g", 
			() -> createEntityTypeFar((type, level) -> new AntiRadarMissile(type, level, "agm88g"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	
	public static final RegistryObject<EntityType<EntityFlare>> FLARE = ENTITIES.register("flare", 
			() -> createEntityType(EntityFlare::new, EntityDimensions.scalable(0f, 0f)));
	public static final RegistryObject<EntityType<EntityParachute>> PARACHUTE = ENTITIES.register("parachute", 
			() -> createEntityType(EntityParachute::new, EntityDimensions.scalable(0.625f, 0.125f)));
	
	private static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, 
        		true, ImmutableSet.of(), size, 5, 3);
    }
	
	private static <T extends Entity> EntityType<T> createEntityTypeFar(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, 
        		true, ImmutableSet.of(), size, 15, 3);
    }
	
}
