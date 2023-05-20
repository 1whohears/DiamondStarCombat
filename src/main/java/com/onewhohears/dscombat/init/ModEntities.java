package com.onewhohears.dscombat.init;

import com.google.common.collect.ImmutableSet;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.DefaultAircraftPresets;
import com.onewhohears.dscombat.data.aircraft.LiftKGraph;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;
import com.onewhohears.dscombat.entity.aircraft.EntityGroundVehicle;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.entity.aircraft.EntitySubmarine;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.weapon.AntiRadarMissile;
import com.onewhohears.dscombat.entity.weapon.EntityBomb;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;
import com.onewhohears.dscombat.entity.weapon.IRMissile;
import com.onewhohears.dscombat.entity.weapon.PositionMissile;
import com.onewhohears.dscombat.entity.weapon.TorpedoMissile;
import com.onewhohears.dscombat.entity.weapon.TrackEntityMissile;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
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
					DefaultAircraftPresets.DEFAULT_JAVI_PLANE,
					ModSounds.JET_1,
					6, 10, 4, 4, 
					LiftKGraph.JAVI_PLANE_GRAPH, 8f, true, 0), 
					EntityDimensions.scalable(2.45f, 2.45f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> ALEXIS_PLANE = ENTITIES.register("alexis_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					DefaultAircraftPresets.DEFAULT_ALEXIS_PLANE,
					ModSounds.JET_1,
					4, 8, 2, 3, 
					LiftKGraph.ALEXIS_PLANE_GRAPH, 8f, false, 0), 
					EntityDimensions.scalable(2.0f, 2.0f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> WOODEN_PLANE = ENTITIES.register("wooden_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					DefaultAircraftPresets.DEFAULT_WOODEN_PLANE,
					ModSounds.BIPLANE_1,
					4, 7, 3, 3, 
					LiftKGraph.WOODEN_PLANE_GRAPH, 6f, false, Mth.PI), 
					EntityDimensions.scalable(1.7f, 1.7f)));
	
	// TODO 0.2 e3sentry
	public static final RegistryObject<EntityType<EntityPlane>> E3SENTRY_PLANE = ENTITIES.register("e3sentry_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					DefaultAircraftPresets.DEFAULT_E3SENTRY_PLANE,
					ModSounds.JET_1,
					10, 12, 8, 7, 
					LiftKGraph.E3SENTRY_PLANE_GRAPH, 10f, false, Mth.PI * 0.01f), 
					EntityDimensions.scalable(4.0f, 4.0f)));
	
	// HELICOPTORS
	
	public static final RegistryObject<EntityType<EntityHelicopter>> NOAH_CHOPPER = ENTITIES.register("noah_chopper", 
			() -> createEntityTypeFar((type, level) -> new EntityHelicopter(type, level, 
					DefaultAircraftPresets.DEFAULT_NOAH_CHOPPER,
					ModSounds.HELI_1, true, 
					8, 6, 4, 4, 2.75f), 
					EntityDimensions.scalable(2.8f, 2.8f)));
	
	// TANKS
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> MRBUDGER_TANK = ENTITIES.register("mrbudger_tank", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					DefaultAircraftPresets.DEFAULT_MRBUDGER_TANK,
					ModSounds.TANK_1, true, 3), 
					EntityDimensions.scalable(3.0f, 2.5f)));
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> SMALL_ROLLER = ENTITIES.register("small_roller", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					DefaultAircraftPresets.DEFAULT_SMALL_ROLLER,
					ModSounds.TANK_1, true, 1), 
					EntityDimensions.scalable(1.5f, 0.8f)));
	
	// CARS
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> ORANGE_TESLA = ENTITIES.register("orange_tesla", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					DefaultAircraftPresets.DEFAULT_ORANGE_TESLA,
					ModSounds.ORANGE_TESLA, false, 2), 
					EntityDimensions.scalable(2.5f, 2.15f)));
	
	// TODO 0.3 axcel truck
	public static final RegistryObject<EntityType<EntityGroundVehicle>> AXCEL_TRUCK = ENTITIES.register("axcel_truck", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					null,
					ModSounds.TANK_1, false, 3), 
					EntityDimensions.scalable(2.0f, 2.15f)));
	
	// BOATS
	
	public static final RegistryObject<EntityType<EntityBoat>> NATHAN_BOAT = ENTITIES.register("nathan_boat", 
			() -> createEntityTypeFar((type, level) -> new EntityBoat(type, level, 
					DefaultAircraftPresets.DEFAULT_NATHAN_BOAT,
					ModSounds.BOAT_1, 2), 
					EntityDimensions.scalable(3.0f,1.5f)));
	
	// SUBMARINES
	
	public static final RegistryObject<EntityType<EntitySubmarine>> ANDOLF_SUB = ENTITIES.register("andolf_sub", 
			() -> createEntityTypeFar((type, level) -> new EntitySubmarine(type, level, 
					DefaultAircraftPresets.DEFAULT_ANDOLF_SUB,
					ModSounds.SUB_1, 4), 
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
	 * IDEA 9 AI for planes/turrets or an auto pilot module
	 * AI turret controllers need time and resources invested to level up
	 * it would be funny if they were villagers
	 */
	
	public static final RegistryObject<EntityType<EntitySeat>> SEAT = ENTITIES.register("seat", 
			() -> createEntityType(EntitySeat::new, EntityDimensions.scalable(0f, 0f)));
	
	public static final RegistryObject<EntityType<EntityTurret>> MINIGUN_TURRET = ENTITIES.register("minigun_turret", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 0.0, 0.8), 
					EntityDimensions.scalable(0.1f, 0.1f)));
	public static final RegistryObject<EntityType<EntityTurret>> HEAVY_TANK_TURRET = ENTITIES.register("heavy_tank_turret", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 0.0, 0.5), 
					EntityDimensions.scalable(0.1f, 0.1f)));
	public static final RegistryObject<EntityType<EntityTurret>> STEVE_UP_SMASH = ENTITIES.register("steve_up_smash", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 0.0, 3.2), 
					EntityDimensions.scalable(0.1f, 0.1f)));
	// TODO 0.4 SAM launcher
	public static final RegistryObject<EntityType<EntityTurret>> SAM_LAUNCHER = ENTITIES.register("sam_launcher", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 0.0, 3.2), 
					EntityDimensions.scalable(0.1f, 0.1f)));
	
	public static final RegistryObject<EntityType<EntityWeaponRack>> LIGHT_MISSILE_RACK = ENTITIES.register("light_missile_rack", 
			() -> createEntityType(EntityWeaponRack::new, EntityDimensions.scalable(0.1f, 0.1f)));
	public static final RegistryObject<EntityType<EntityWeaponRack>> HEAVY_MISSILE_RACK = ENTITIES.register("heavy_missile_rack", 
			() -> createEntityType(EntityWeaponRack::new, EntityDimensions.scalable(0.1f, 0.1f)));
	public static final RegistryObject<EntityType<EntityWeaponRack>> XM12 = ENTITIES.register("xm12", 
			() -> createEntityType(EntityWeaponRack::new, EntityDimensions.scalable(0.1f, 0.1f)));
	public static final RegistryObject<EntityType<EntityWeaponRack>> BOMB_RACK = ENTITIES.register("bomb_rack", 
			() -> createEntityType(EntityWeaponRack::new, EntityDimensions.scalable(0.1f, 0.1f)));
	
	public static final RegistryObject<EntityType<EntityBullet>> BULLET = ENTITIES.register("bullet", 
			() -> createEntityType(EntityBullet::new, EntityDimensions.scalable(0.15f, 0.15f)));
	public static final RegistryObject<EntityType<EntityBomb>> BOMB = ENTITIES.register("bomb", 
			() -> createEntityType(EntityBomb::new, EntityDimensions.scalable(0.6f, 0.6f)));
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
	
	public static final RegistryObject<EntityType<EntityFlare>> FLARE = ENTITIES.register("flare", 
			() -> createEntityType(EntityFlare::new, EntityDimensions.scalable(0f, 0f)));
	
	private static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, 
        		true, ImmutableSet.of(), size, 5, 3);
    }
	
	private static <T extends Entity> EntityType<T> createEntityTypeFar(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, 
        		true, ImmutableSet.of(), size, 15, 3);
    }
	
}
