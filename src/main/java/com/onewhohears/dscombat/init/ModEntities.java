package com.onewhohears.dscombat.init;

import com.google.common.collect.ImmutableSet;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.renderer.texture.EntityScreenTypes;
import com.onewhohears.dscombat.data.aircraft.ImmutableVehicleData;
import com.onewhohears.dscombat.entity.EntityParachute;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;
import com.onewhohears.dscombat.entity.aircraft.EntityGroundVehicle;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.entity.aircraft.EntitySubmarine;
import com.onewhohears.dscombat.entity.aircraft.RotableHitbox;
import com.onewhohears.dscombat.entity.aircraft.EntityScreenData;
import com.onewhohears.dscombat.entity.parts.EntityEngine;
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
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					ImmutableVehicleData.JAVI_PLANE_DATA), 
					EntityDimensions.scalable(2.45f, 2.45f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> ALEXIS_PLANE = ENTITIES.register("alexis_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					ImmutableVehicleData.ALEXIS_PLANE_DATA) {
						@Override
						public void addVehicleScreens() {
							screens = new EntityScreenData[1];
							screens[0] = new EntityScreenData(EntityScreenTypes.RADAR_SCREEN, 
									new Vec3(0.25, 1.2, 7.22), 
									0.4f, 0.4f, 
									40.2f, 0f, 0f);
						}
					}, 
					EntityDimensions.scalable(4.0f, 4.0f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> WOODEN_PLANE = ENTITIES.register("wooden_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					ImmutableVehicleData.WOODEN_PLANE_DATA), 
					EntityDimensions.scalable(1.7f, 1.7f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> E3SENTRY_PLANE = ENTITIES.register("e3sentry_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					ImmutableVehicleData.E3SENTRY_PLANE_DATA), 
					EntityDimensions.scalable(4.0f, 4.0f)));
	
	// HELICOPTORS
	
	public static final RegistryObject<EntityType<EntityHelicopter>> NOAH_CHOPPER = ENTITIES.register("noah_chopper", 
			() -> createEntityTypeFar((type, level) -> new EntityHelicopter(type, level, 
					ImmutableVehicleData.NOAH_CHOPPER_DATA), 
					EntityDimensions.scalable(2.8f, 2.8f)));
	
	// TANKS
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> MRBUDGER_TANK = ENTITIES.register("mrbudger_tank", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					ImmutableVehicleData.MRBUDGER_TANK_DATA), 
					EntityDimensions.scalable(3.0f, 2.5f)));
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> SMALL_ROLLER = ENTITIES.register("small_roller", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					ImmutableVehicleData.SMALL_ROLLER_DATA), 
					EntityDimensions.scalable(1.5f, 0.8f)));
	
	// CARS
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> ORANGE_TESLA = ENTITIES.register("orange_tesla", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					ImmutableVehicleData.ORANGE_TESLA_DATA), 
					EntityDimensions.scalable(2.5f, 2.15f)));
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> AXCEL_TRUCK = ENTITIES.register("axcel_truck", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					ImmutableVehicleData.AXCEL_TRUCK_DATA), 
					EntityDimensions.scalable(2.5f, 3.0f)));
	
	// TODO 2.6 fuel truck
	
	// BOATS
	
	public static final RegistryObject<EntityType<EntityBoat>> NATHAN_BOAT = ENTITIES.register("nathan_boat", 
			() -> createEntityTypeFar((type, level) -> new EntityBoat(type, level, 
					ImmutableVehicleData.NATHA_BOAT_DATA), 
					EntityDimensions.scalable(3.0f,1.5f)));
	
	public static final RegistryObject<EntityType<EntityBoat>> GRONK_BATTLESHIP = ENTITIES.register("gronk_battleship", 
			() -> createEntityTypeFar((type, level) -> new EntityBoat(type, level, 
					ImmutableVehicleData.GRONK_BATTLESHIP_DATA) {
						@Override
						public void addHitboxes() {
							hitboxes = new RotableHitbox[5];
							hitboxes[0] = new RotableHitbox(this, "plat0", 
									new Vector3f(14, 4.02f, 34), 
									new Vec3(0, 2, -0.5));
							hitboxes[1] = new RotableHitbox(this, "plat1", 
									new Vector3f(9, 1.02f, 30), 
									new Vec3(0, 4.5, -0.5));
							hitboxes[2] = new RotableHitbox(this, "plat1", 
									new Vector3f(6, 1.02f, 18), 
									new Vec3(0, 5.5, -0.5));
							hitboxes[3] = new RotableHitbox(this, "front", 
									new Vector3f(8, 2.02f, 8), 
									new Vec3(0, 3, 20.5));
							hitboxes[4] = new RotableHitbox(this, "back", 
									new Vector3f(8, 2.02f, 8), 
									new Vec3(0, 3, -21.5));
						}
					}, EntityDimensions.scalable(14f,4f)));
	
	public static final RegistryObject<EntityType<EntityBoat>> DESTROYER = ENTITIES.register("destroyer", 
			() -> createEntityTypeFar((type, level) -> new EntityBoat(type, level, 
					ImmutableVehicleData.DESTROYER_DATA) {
						@Override
						public void addHitboxes() {
							hitboxes = new RotableHitbox[1];
							hitboxes[0] = new RotableHitbox(this, "plat0", 
								new Vector3f(10, 4.02f, 27.6f), 
								new Vec3(0, 2, 0));
						}
					}, EntityDimensions.scalable(10f,4f)));
	
	public static final RegistryObject<EntityType<EntityBoat>> CRUISER = ENTITIES.register("cruiser", 
			() -> createEntityTypeFar((type, level) -> new EntityBoat(type, level, 
					ImmutableVehicleData.CRUISER_DATA) {
						@Override
						public void addHitboxes() {
							hitboxes = new RotableHitbox[2];
							hitboxes[0] = new RotableHitbox(this, "plat0", 
									new Vector3f(12, 4.02f, 35.6f), 
									new Vec3(0, 2, -2));
							hitboxes[1] = new RotableHitbox(this, "plat1", 
									new Vector3f(7, 1.02f, 21), 
									new Vec3(0, 4.5, 0.8));
						}
					}, EntityDimensions.scalable(12f,4f)));
	
	public static final RegistryObject<EntityType<EntityBoat>> CORVETTE = ENTITIES.register("corvette", 
			() -> createEntityTypeFar((type, level) -> new EntityBoat(type, level, 
					ImmutableVehicleData.CORVETTE_DATA) {
						@Override
						public void addHitboxes() {
							hitboxes = new RotableHitbox[1];
							hitboxes[0] = new RotableHitbox(this, "plat0", 
									new Vector3f(7, 3.02f, 16), 
									new Vec3(0, 1.5, -2));
						}
					}, EntityDimensions.scalable(7f,3f)));
	
	public static final RegistryObject<EntityType<EntityBoat>> AIRCRAFT_CARRIER = ENTITIES.register("aircraft_carrier", 
			() -> createEntityTypeFar((type, level) -> new EntityBoat(type, level, 
					ImmutableVehicleData.AIRCRAFT_CARRIER_DATA) {
						@Override
						public void addHitboxes() {
							hitboxes = new RotableHitbox[2];
							hitboxes[0] = new RotableHitbox(this, "runway", 
									new Vector3f(16, 6.02f, 50), 
									new Vec3(0, 3, 0));
							hitboxes[1] = new RotableHitbox(this, "side_plat", 
									new Vector3f(25, 3.02f, 24), 
									new Vec3(0, 4.5, 0));
						}
					}, EntityDimensions.scalable(25f,6f)));
	
	// SUBMARINES
	
	public static final RegistryObject<EntityType<EntitySubmarine>> ANDOLF_SUB = ENTITIES.register("andolf_sub", 
			() -> createEntityTypeFar((type, level) -> new EntitySubmarine(type, level, 
					ImmutableVehicleData.ANDOLF_SUB_DATA), 
					EntityDimensions.scalable(4.5f,4.0f)));
	
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
					Vec3.ZERO, 0.8), 
					EntityDimensions.scalable(1.0f, 1.5f)));
	public static final RegistryObject<EntityType<EntityTurret>> HEAVY_TANK_TURRET = ENTITIES.register("heavy_tank_turret", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					Vec3.ZERO, 0.5), 
					EntityDimensions.scalable(2.0f, 1.0f)));
	public static final RegistryObject<EntityType<EntityTurret>> STEVE_UP_SMASH = ENTITIES.register("steve_up_smash", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					Vec3.ZERO, 3.2), 
					EntityDimensions.scalable(1.0f, 3.5f)));
	public static final RegistryObject<EntityType<EntityTurret>> SAM_LAUNCHER = ENTITIES.register("sam_launcher", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(1.2, 0.4, 0), 2.7), 
					EntityDimensions.scalable(2.0f, 3.0f)));
	public static final RegistryObject<EntityType<EntityTurret>> MLS = ENTITIES.register("mls", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(1.2, 0, 0), 1), 
					EntityDimensions.scalable(2.0f, 2.5f)));
	public static final RegistryObject<EntityType<EntityTurret>> TORPEDO_TUBES = ENTITIES.register("torpedo_tubes", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(0, 1.8, 0), 1), 
					EntityDimensions.scalable(2.5f, 2.0f)));
	public static final RegistryObject<EntityType<EntityTurret>> AA_TURRET = ENTITIES.register("aa_turret", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(0, 0.5, 0), 1.03125), 
					EntityDimensions.scalable(1.0f, 1.25f)));
	public static final RegistryObject<EntityType<EntityTurret>> CIWS = ENTITIES.register("ciws", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(1, 0.5, 0), 0.6875), 
					EntityDimensions.scalable(1.5f, 2.5f)));
	public static final RegistryObject<EntityType<EntityTurret>> MARK7_CANNON = ENTITIES.register("mark7_cannon", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(0, 1.5, 0), 1.625, ShootType.MARK7), 
					EntityDimensions.scalable(4.0f, 1.7f)));
	public static final RegistryObject<EntityType<EntityTurret>> MARK45_CANNON = ENTITIES.register("mark45_cannon", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 
					new Vec3(0, 1.5, 0), 1.5625), 
					EntityDimensions.scalable(2.0f, 1.5f)));
	
	public static final EntityDimensions TINY = EntityDimensions.scalable(0.1f, 0.1f);
	
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
			() -> createEntityType(EntityBullet::new, EntityDimensions.scalable(0.15f, 0.15f)));
	public static final RegistryObject<EntityType<EntityBomb>> BOMB = ENTITIES.register("bomb", 
			() -> createEntityType(EntityBomb::new, EntityDimensions.scalable(0.6f, 0.6f)));
	public static final RegistryObject<EntityType<EntityBunkerBuster>> GRUETZ_BUNKER_BUSTER = ENTITIES.register("gruetz_bunker_buster", 
			() -> createEntityType(EntityBunkerBuster::new, EntityDimensions.scalable(1.5f, 2.5f)));
	
	public static final RegistryObject<EntityType<PositionMissile>> POS_MISSILE_1 = ENTITIES.register("pos_missile_1", 
			() -> createEntityTypeFar(PositionMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<IRMissile>> IR_MISSILE_1 = ENTITIES.register("ir_missile_1", 
			() -> createEntityTypeFar(IRMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> TRACK_MISSILE_1 = ENTITIES.register("track_missile_1", 
			() -> createEntityTypeFar(TrackEntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<AntiRadarMissile>> ANTI_RADAR_MISSILE_1 = ENTITIES.register("anti_radar_missile_1", 
			() -> createEntityTypeFar(AntiRadarMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TorpedoMissile>> TORPEDO_MISSILE_1 = ENTITIES.register("torpedo_missile_1", 
			() -> createEntityTypeFar(TorpedoMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	
	public static final RegistryObject<EntityType<IRMissile>> AIM9L = ENTITIES.register("aim9l", 
			() -> createEntityTypeFar(IRMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<IRMissile>> AIM9P5 = ENTITIES.register("aim9p5", 
			() -> createEntityTypeFar(IRMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<IRMissile>> AIM9X = ENTITIES.register("aim9x", 
			() -> createEntityTypeFar(IRMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> AIM120B = ENTITIES.register("aim120b", 
			() -> createEntityTypeFar(TrackEntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> AIM120C = ENTITIES.register("aim120c", 
			() -> createEntityTypeFar(TrackEntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> PAC3 = ENTITIES.register("pac3", 
			() -> createEntityTypeFar(TrackEntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> AIM7F = ENTITIES.register("aim7f", 
			() -> createEntityTypeFar(TrackEntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> AIM7MH = ENTITIES.register("aim7mh", 
			() -> createEntityTypeFar(TrackEntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<PositionMissile>> AGM114K = ENTITIES.register("agm114k", 
			() -> createEntityTypeFar(PositionMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<PositionMissile>> AGM84E = ENTITIES.register("agm84e", 
			() -> createEntityTypeFar(PositionMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<PositionMissile>> AGM65L = ENTITIES.register("agm65l", 
			() -> createEntityTypeFar(PositionMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> AGM65G = ENTITIES.register("agm65g", 
			() -> createEntityTypeFar(TrackEntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> METEOR = ENTITIES.register("meteor", 
			() -> createEntityTypeFar(TrackEntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	
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
