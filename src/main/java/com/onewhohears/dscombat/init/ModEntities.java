package com.onewhohears.dscombat.init;

import com.google.common.collect.ImmutableSet;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.stats.TurretStats.RotBounds;
import com.onewhohears.dscombat.data.vehicle.presets.BoatPresets;
import com.onewhohears.dscombat.data.vehicle.presets.CarPresets;
import com.onewhohears.dscombat.data.vehicle.presets.NoahChopperPresets;
import com.onewhohears.dscombat.data.vehicle.presets.PlanePresets;
import com.onewhohears.dscombat.data.vehicle.presets.SubPresets;
import com.onewhohears.dscombat.entity.EntityParachute;
import com.onewhohears.dscombat.entity.parts.EntityChainHook;
import com.onewhohears.dscombat.entity.parts.EntityEngine;
import com.onewhohears.dscombat.entity.parts.EntityGimbal;
import com.onewhohears.dscombat.entity.parts.EntityRadar;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityTurret.ShootType;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.vehicle.EntityBoat;
import com.onewhohears.dscombat.entity.vehicle.EntityGroundVehicle;
import com.onewhohears.dscombat.entity.vehicle.EntityHelicopter;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;
import com.onewhohears.dscombat.entity.vehicle.EntitySubmarine;
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
	
	// VEHICLES
	
	public static final RegistryObject<EntityType<EntityPlane>> PLANE = ENTITIES.register("plane", 
			() -> createVehicleType((type, level) -> new EntityPlane(type, level, 
					PlanePresets.DEFAULT_WOODEN_PLANE.getId())));
	public static final RegistryObject<EntityType<EntityHelicopter>> HELICOPTER = ENTITIES.register("helicopter", 
			() -> createVehicleType((type, level) -> new EntityHelicopter(type, level, 
					NoahChopperPresets.DEFAULT_NOAH_CHOPPER.getId())));
	public static final RegistryObject<EntityType<EntityGroundVehicle>> CAR = ENTITIES.register("car", 
			() -> createVehicleType((type, level) -> new EntityGroundVehicle(type, level, 
					CarPresets.DEFAULT_AXCEL_TRUCK.getId())));
	public static final RegistryObject<EntityType<EntityBoat>> BOAT = ENTITIES.register("boat", 
			() -> createVehicleType((type, level) -> new EntityBoat(type, level, 
					BoatPresets.DEFAULT_NATHAN_BOAT.getId())));
	public static final RegistryObject<EntityType<EntitySubmarine>> SUBMARINE = ENTITIES.register("submarine", 
			() -> createVehicleType((type, level) -> new EntitySubmarine(type, level, 
					SubPresets.DEFAULT_ANDOLF_SUB.getId())));
	
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
	
	public static final RegistryObject<EntityType<EntityChainHook>> CHAIN_HOOK = ENTITIES.register("chain_hook", 
			() -> createEntityType(EntityChainHook::new, EntityDimensions.scalable(1.0f, 1.0f)));
	
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
	
	public static final RegistryObject<EntityType<EntityBullet<?>>> BULLET = ENTITIES.register("bullet", 
			() -> createEntityType((type, level) -> new EntityBullet<>(type, level, "20mm"), 
					EntityDimensions.scalable(0.15f, 0.15f)));
	public static final RegistryObject<EntityType<EntityBomb<?>>> BOMB = ENTITIES.register("bomb", 
			() -> createEntityType((type, level) -> new EntityBomb<>(type, level, "anm30"), 
					EntityDimensions.scalable(0.6f, 0.6f)));
	public static final RegistryObject<EntityType<EntityBunkerBuster<?>>> BUNKER_BUSTER = ENTITIES.register("bunker_buster", 
			() -> createEntityType((type, level) -> new EntityBunkerBuster<>(type, level, "gruetz_bunker_buster"), 
					EntityDimensions.scalable(0.8f, 0.8f)));
	public static final RegistryObject<EntityType<PositionMissile<?>>> POS_MISSILE = ENTITIES.register("pos_missile", 
			() -> createEntityTypeFar((type, level) -> new PositionMissile<>(type, level, "agm114k"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<IRMissile<?>>> IR_MISSILE = ENTITIES.register("ir_missile", 
			() -> createEntityTypeFar((type, level) -> new IRMissile<>(type, level, "aim9l"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile<?>>> TRACK_MISSILE = ENTITIES.register("track_missile", 
			() -> createEntityTypeFar((type, level) -> new TrackEntityMissile<>(type, level, "aim120b"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<AntiRadarMissile<?>>> ANTI_RADAR_MISSILE = ENTITIES.register("anti_radar_missile", 
			() -> createEntityTypeFar((type, level) -> new AntiRadarMissile<>(type, level, "agm88g"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TorpedoMissile<?>>> TORPEDO_MISSILE = ENTITIES.register("torpedo_missile", 
			() -> createEntityTypeFar((type, level) -> new TorpedoMissile<>(type, level, "mk13"), 
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
        		true, ImmutableSet.of(), size, 16, 3);
    }
	
	private static <T extends Entity> EntityType<T> createVehicleType(EntityType.EntityFactory<T> factory) {
		return createEntityTypeFar(factory, EntityDimensions.fixed(4, 4));
	}
	
}
